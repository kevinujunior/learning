# System Design Notes — SDE-2 Interview Preparation

> Target: SDE-2 (3–6 years experience) | All 16 systems | Strict structure per system

---

# Table of Contents

1. [URL Shortener (like Bit.ly)](#1-url-shortener-like-bitly)
2. [Rate Limiter](#2-rate-limiter)
3. [Key-Value Store (like Redis)](#3-key-value-store-like-redis)
4. [Distributed Cache](#4-distributed-cache)
5. [Twitter / Instagram Feed](#5-twitter--instagram-feed)
6. [WhatsApp / Messenger](#6-whatsapp--messenger)
7. [YouTube / Netflix](#7-youtube--netflix)
8. [Uber / Lyft (Ride Sharing)](#8-uber--lyft-ride-sharing)
9. [Google Drive / Dropbox](#9-google-drive--dropbox)
10. [Notification System](#10-notification-system)
11. [Google Search / Typeahead / Autocomplete](#11-google-search--typeahead--autocomplete)
12. [Web Crawler](#12-web-crawler)
13. [Metrics / Monitoring System (like Datadog)](#13-metrics--monitoring-system-like-datadog)
14. [Task Queue / Job Scheduler](#14-task-queue--job-scheduler)
15. [API Gateway](#15-api-gateway)
16. [Distributed Message Queue (like Kafka)](#16-distributed-message-queue-like-kafka)

---

# 1. URL Shortener (like Bit.ly)

## Problem Overview

- Convert long URLs into short aliases (e.g., `bit.ly/abc123`)
- Redirect users from short URL to original URL
- Used in marketing campaigns, social media, analytics tracking

## Functional Requirements

- Given a long URL, generate a short URL
- Redirect short URL to original URL
- Custom aliases (optional)
- URL expiration (optional)
- Click analytics (optional)

## Non-Functional Requirements

- 100:1 read/write ratio (redirects >> shortening)
- Latency: redirect < 10ms (with cache), creation < 100ms
- High availability: 99.99% uptime
- Eventual consistency acceptable for analytics
- Short codes must be globally unique

## Capacity Estimation

- 100M new URLs/day = ~1160 writes/sec
- 10B redirects/day = ~116K reads/sec
- Avg URL = 500 bytes → 100M * 500B = 50GB/day storage
- Short code: 7 chars from [a-zA-Z0-9] = 62^7 ≈ 3.5 trillion combinations

## High-Level Architecture

```
Client
  |
Load Balancer (L7)
  |
URL Shortener Service (stateless, horizontally scaled)
  |         |
Cache    Database (NoSQL: Cassandra or DynamoDB)
(Redis)
  |
Analytics Service (async via Kafka)
```

## Data Model & Storage

**Table: urls**

| Column     | Type      | Notes                        |
|------------|-----------|------------------------------|
| short_code | VARCHAR   | Primary Key, indexed         |
| long_url   | TEXT      |                              |
| user_id    | VARCHAR   | nullable                     |
| created_at | TIMESTAMP |                              |
| expires_at | TIMESTAMP | nullable                     |

**Storage choice: NoSQL (Cassandra/DynamoDB)**
- Write-heavy at scale, key-value access pattern fits perfectly
- No joins needed
- Horizontal scaling via consistent hashing on `short_code`

**Sharding:** Shard on `short_code` — natural key, evenly distributed

## Core Algorithms & Techniques

### Short Code Generation

**Option 1: Base62 Encoding of Auto-increment ID**
```java
public String encode(long id) {
    String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder();
    while (id > 0) {
        sb.append(chars.charAt((int)(id % 62)));
        id /= 62;
    }
    return sb.reverse().toString();
}
```

- Problem: requires a central counter → single point of failure
- Fix: Use distributed ID generators (Twitter Snowflake, or range-based ID allocation per server)

**Option 2: MD5/SHA256 + Take First 7 chars**
```java
public String generateCode(String longUrl) {
    String hash = DigestUtils.md5Hex(longUrl + System.nanoTime());
    return hash.substring(0, 7);
}
```
- Collision possible → check DB and retry

**Option 3: Pre-generated Code Pool (Recommended)**
- Background job pre-generates unused codes and stores in a `code_pool` table
- Shortener service picks one atomically
- Eliminates collision check at request time

### Redirect
- HTTP 301 (permanent, browser caches) vs HTTP 302 (temporary, server always hit)
- Use 302 for analytics tracking

## Scaling Strategy

- All short URL lookups go through Redis first (TTL = expiry time or 24h)
- Cache hit rate expected > 99% for popular URLs
- Read replicas for DB
- CDN for popular short URLs (edge cache the redirect response)
- Consistent hashing across DB shards

## Failure Scenarios & Reliability

- **Cache miss storm**: Populate cache on write; use read-through cache
- **DB node failure**: Replicas take over; eventual consistency acceptable
- **Code collision**: Retry with different nano-seed; or use pre-generated pool
- **Service crash**: Stateless service, LB retries on healthy instance

## Bottlenecks & Optimizations

- Redirect latency: Solve with Redis + CDN edge caching
- DB write bottleneck: Async writes for analytics; pre-generated code pool for short URL creation
- Hot short codes: Cache with high TTL, replicate to edge

## SDE-2 Deep Dive

### Distributed ID Generation with Range-Based Allocation

Instead of a central counter, allocate ID ranges to each server:
- Server A gets IDs 1–1000, Server B gets 1001–2000, etc.
- Each server increments locally until range exhausted, then fetches next range
- One DB call per 1000 IDs → minimal contention

```java
class IdGenerator {
    private long current;
    private long max;

    public synchronized long next() {
        if (current >= max) fetchNewRange();
        return current++;
    }

    private void fetchNewRange() {
        // Atomic DB update: UPDATE counter SET val = val + 1000 RETURNING val
        long base = db.fetchAndIncrementBy(1000);
        current = base;
        max = base + 1000;
    }
}
```

### Redis Cache Design

```
Key: "url:abc123"
Value: "https://very-long-original-url.com/path"
TTL: set to expiry time, or 86400s if no expiry
```

Use Redis Cluster for horizontal scaling. Consistent hashing distributes keys.

## Interview Talking Points

- Why NoSQL over SQL? → Key-value access pattern, no joins, easy sharding
- Why 302 over 301? → Need click tracking; 301 caches at browser and bypasses server
- How to handle custom aliases? → Check availability, store in same table with flag
- How to handle URL expiration? → TTL in Redis + background job to clean DB
- Bottleneck in ID generation? → Range-based allocation or Snowflake IDs
- Mistakes to avoid: Don't use UUID as short code (too long), don't forget cache invalidation on delete

---


[Back to Top](#table-of-contents)

---

# 2. Rate Limiter

## Problem Overview

- Control the rate of requests from a client to prevent abuse, ensure fair use, protect downstream services
- Used in API gateways, login endpoints, payment systems

## Functional Requirements

- Limit requests per user/IP per time window
- Return HTTP 429 when limit exceeded
- Support different limits per endpoint or user tier
- Distributed enforcement (multiple servers must share state)

## Non-Functional Requirements

- Latency overhead: < 1ms per check
- High availability: rate limiter failure should fail open (not block all traffic)
- Consistency: slight inconsistency acceptable (soft limits)
- Scalability: millions of unique clients

## Capacity Estimation

- 100K requests/sec across all clients
- Each check = Redis read + conditional write
- Redis can handle 500K ops/sec per node → 1 node sufficient for most cases

## High-Level Architecture

```
Client
  |
API Gateway / Middleware (Rate Limiter Layer)
  |
Redis Cluster (shared counter store)
  |
Backend Service
```

## Data Model & Storage

**Redis Key Schema:**
```
rate:{user_id}:{window}  → counter (integer)
TTL set to window duration
```

**Storage choice: Redis**
- In-memory, sub-millisecond latency
- Atomic increment operations (INCR is atomic)
- TTL support built-in

## Core Algorithms & Techniques

### 1. Token Bucket (Recommended)

- Bucket holds max N tokens; tokens refill at rate R/sec
- Each request consumes 1 token; reject if empty
- Allows bursting up to bucket size

```java
// Using Redis with Lua script for atomicity
String luaScript =
    "local tokens = redis.call('get', KEYS[1]) " +
    "if tokens == false then tokens = ARGV[2] end " +
    "tokens = tonumber(tokens) " +
    "if tokens >= 1 then " +
    "  redis.call('set', KEYS[1], tokens - 1) " +
    "  redis.call('expire', KEYS[1], ARGV[1]) " +
    "  return 1 " +  // allowed
    "else return 0 end";  // rejected
```

### 2. Sliding Window Counter

- Track request count in current + previous window weighted by overlap
- More accurate than fixed window

```java
long now = System.currentTimeMillis() / 1000;
long currentWindow = now / windowSize;
long prevWindow = currentWindow - 1;

long prevCount = redis.get("rate:" + userId + ":" + prevWindow);
long currCount = redis.get("rate:" + userId + ":" + currentWindow);

double overlap = (windowSize - (now % windowSize)) / (double) windowSize;
double estimated = prevCount * overlap + currCount;

if (estimated < limit) {
    redis.incr("rate:" + userId + ":" + currentWindow);
    redis.expire("rate:" + userId + ":" + currentWindow, windowSize * 2);
    return true; // allowed
}
return false; // rejected
```

### 3. Fixed Window Counter (Simple but edge-case prone)

- Reset counter every window
- Problem: burst at window boundary (2x limit in 1 second)

### 4. Leaky Bucket

- Queue requests; process at fixed rate
- Good for smoothing; adds latency

**Recommendation: Token Bucket for APIs, Sliding Window for accuracy-critical systems**

## Scaling Strategy

- Use Redis Cluster; shard by `user_id`
- Local in-memory cache per server for approximate limiting (reduces Redis calls by 90%)
  - Each server tracks count locally; sync to Redis every 100ms
  - Trade-off: may allow slight over-limit during sync interval
- Consistent hashing ensures same user always hits same Redis shard

## Failure Scenarios & Reliability

- **Redis down**: Fail open (allow requests) to avoid blocking legitimate traffic; alert ops
- **Network partition**: Local fallback counter; re-sync when Redis recovers
- **Clock skew**: Use server-side timestamps for window calculation

## Bottlenecks & Optimizations

- Hot user IDs (high-traffic clients): Pre-warm cache; local counter with periodic Redis sync
- Lua scripts for atomicity avoid race conditions without distributed locks
- Use pipeline/batch Redis calls when checking multiple rules per request

## SDE-2 Deep Dive

### Distributed Rate Limiting with Local + Redis Hybrid

```
Per Server:
  localCounter[userId] += 1
  if localCounter[userId] % SYNC_THRESHOLD == 0:
    delta = SYNC_THRESHOLD
    globalCount = redis.incrBy("rate:" + userId, delta)
    if globalCount > limit:
      return REJECT
  else:
    if localCounter[userId] > limit / numServers:
      // soft local limit exceeded, check Redis
      globalCount = redis.get("rate:" + userId)
      if globalCount > limit: return REJECT
return ALLOW
```

This reduces Redis calls dramatically while keeping accuracy within acceptable bounds.

### Lua Script for Atomic Token Bucket

Lua scripts in Redis execute atomically — no race condition possible.

```lua
local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

local bucket = redis.call('hmget', key, 'tokens', 'last_refill')
local tokens = tonumber(bucket[1]) or capacity
local last_refill = tonumber(bucket[2]) or now

local elapsed = now - last_refill
local new_tokens = math.min(capacity, tokens + elapsed * refill_rate)

if new_tokens >= 1 then
    redis.call('hmset', key, 'tokens', new_tokens - 1, 'last_refill', now)
    redis.call('expire', key, 3600)
    return 1
else
    return 0
end
```

## Interview Talking Points

- Fixed vs sliding window: Fixed is simpler; sliding is more accurate, prevents boundary bursting
- Where to enforce: Client-side (easily bypassed), server-side middleware, API gateway (best for cross-service)
- How to handle distributed state: Redis with Lua for atomicity
- Consistency vs availability: Fail open on Redis failure; slight over-limit is acceptable
- Mistakes to avoid: Using DB instead of Redis (too slow), not using atomic operations (race conditions)

---


[Back to Top](#table-of-contents)

---

# 3. Key-Value Store (like Redis)

## Problem Overview

- Store and retrieve data by key with sub-millisecond latency
- Used as cache, session store, feature flags, leaderboard, pub/sub
- Core primitive for distributed systems

## Functional Requirements

- PUT(key, value), GET(key), DELETE(key)
- Optional: TTL per key, atomic increment, compare-and-swap
- Optional: Pub/Sub, sorted sets, hashes

## Non-Functional Requirements

- Latency: < 1ms for GET/PUT
- Throughput: 100K–1M ops/sec
- Durability: configurable (in-memory only, or with persistence)
- Availability: 99.99%
- Partition tolerance required for distributed mode

## Capacity Estimation

- Single node: 8–64GB RAM, ~500K ops/sec
- Cluster: horizontally shard across N nodes
- Key size: avg 100 bytes, value: avg 1KB → 1M keys = ~1GB

## High-Level Architecture

```
Client
  |
Client Library (hashing, connection pool)
  |
Redis Cluster Node 1  |  Node 2  |  Node 3  (each with primary + replica)
```

## Data Model & Storage

**Internal Data Structures:**

| Data Type    | Internal Structure         | Use Case                   |
|--------------|---------------------------|----------------------------|
| String       | SDS (simple dynamic string)| Cache, counters            |
| Hash         | HashMap / ZipList          | Object fields              |
| List         | LinkedList / QuickList     | Queues, timelines          |
| Set          | HashTable / IntSet         | Tags, unique members       |
| Sorted Set   | SkipList + HashTable       | Leaderboards, ranking      |

**Persistence Options:**
- RDB (snapshot): Periodic dump to disk. Fast recovery; may lose recent writes
- AOF (append-only file): Log every write. Durable; slower recovery
- Hybrid: RDB for base + AOF for recent changes

## Core Algorithms & Techniques

### Hash Table with Open Addressing

Internal dict uses two hash tables (ht[0] and ht[1]) for incremental rehashing:

```java
// Simplified hash lookup
int hash = key.hashCode() & (tableSize - 1);
while (table[hash] != null && !table[hash].key.equals(key)) {
    hash = (hash + 1) % tableSize; // linear probing
}
```

### Skip List for Sorted Sets

- O(log n) insert, delete, range query
- Simpler to implement than balanced BST; good cache locality

```
Level 3: 1 ---------> 50 ----------> null
Level 2: 1 --> 10 --> 50 --> 80 ---> null
Level 1: 1 -> 5 -> 10 -> 20 -> 50 -> 80 -> null
```

### LRU Eviction (Approximate)

Redis uses approximate LRU: sample N random keys, evict least recently used among them. Saves memory vs true LRU linked list.

```java
// Approximate LRU
List<Entry> sample = randomSample(pool, N=5);
Entry victim = sample.stream().min(Comparator.comparingLong(e -> e.lastAccess)).get();
evict(victim);
```

### Consistent Hashing for Cluster

- Cluster uses 16384 hash slots
- Each node owns a range of slots
- `HASH_SLOT = CRC16(key) % 16384`

## Scaling Strategy

- Redis Cluster: automatic sharding across nodes via hash slots
- Each shard has 1 primary + 1–2 replicas
- Reads can go to replicas (with `READONLY` command) for read scaling
- Sentinel for failover in non-cluster mode

## Failure Scenarios & Reliability

- **Primary failure**: Sentinel or Cluster detects via heartbeat; promotes replica (< 30s)
- **Network partition**: Split-brain possible; use majority-quorum for leader election
- **OOM**: Eviction policy triggers (allkeys-lru, volatile-lru, etc.)
- **Data loss on crash**: AOF fsync=always guarantees zero data loss at cost of throughput

## Bottlenecks & Optimizations

- Single-threaded event loop: commands must be non-blocking; avoid O(n) commands like KEYS *
- Use SCAN instead of KEYS; use pipelining to batch commands
- Memory fragmentation: use jemalloc; restart with BGSAVE + BGREWRITEAOF periodically

## SDE-2 Deep Dive

### Incremental Rehashing

When load factor exceeds threshold, Redis allocates a new hash table (ht[1]) double the size. During rehashing:
- New writes go to ht[1]
- Old data migrated incrementally: one bucket per command
- Reads check both ht[0] and ht[1]
- When ht[0] empty, swap pointers

```java
void rehashStep() {
    int idx = rehashIdx;
    while (ht0.table[idx] == null) idx++; // skip empty buckets
    Entry e = ht0.table[idx];
    while (e != null) {
        Entry next = e.next;
        int newIdx = hash(e.key) & ht1.mask;
        e.next = ht1.table[newIdx];
        ht1.table[newIdx] = e;
        e = next;
    }
    ht0.table[idx] = null;
    ht0.used--;
    rehashIdx = idx + 1;
    if (ht0.used == 0) { ht0 = ht1; ht1 = null; }
}
```

This avoids O(n) pause that would occur if rehashing happened all at once.

### AOF Rewrite

As AOF grows, Redis rewrites it in background: fork child, child writes minimal set of commands to reconstruct current state. Parent continues writing to old AOF + buffer. After child completes, buffer appended to new AOF, then renamed atomically.

## Interview Talking Points

- Why single-threaded? Avoids locking overhead; I/O is the bottleneck, not CPU. Redis 6+ uses I/O threading for network while command execution stays single-threaded
- CAP theorem position: AP (availability + partition tolerance); eventual consistency on replica reads
- Eviction policies and when to use each
- Why skip list over balanced BST for sorted sets: easier range queries, simpler concurrent access
- Mistakes to avoid: Using KEYS * in production, not setting eviction policy, TTL on every cached key

---


[Back to Top](#table-of-contents)

---

# 4. Distributed Cache

## Problem Overview

- Reduce latency and DB load by storing frequently accessed data in fast memory
- Sits between application and database
- Critical for scaling read-heavy systems

## Functional Requirements

- Read-through, write-through, or write-behind caching
- TTL-based expiration
- Cache invalidation on data updates
- Distributed across multiple nodes

## Non-Functional Requirements

- Latency: < 1ms reads
- Cache hit rate: > 95%
- Scalability: horizontally add nodes
- Consistency: eventual (stale reads acceptable for short TTL)
- Availability: cache miss should not bring down the system

## Capacity Estimation

- 1TB total hot data across cluster
- 16GB per node → 64 nodes for storage
- But cache typically holds 20% of data that serves 80% of traffic
- 200GB hot data → 13 nodes (with headroom)

## High-Level Architecture

```
Application Server
       |
Cache Client Library (consistent hashing, connection pool)
       |           |           |
  Cache Node 1  Node 2  ... Node N (Redis/Memcached)
       |
Database (fallback on cache miss)
```

## Data Model & Storage

- Key: composite identifier (e.g., `user:123:profile`)
- Value: serialized object (JSON, Protobuf, MessagePack)
- TTL: per-key expiration
- No secondary indexes; pure key-value lookups

## Core Algorithms & Techniques

### Consistent Hashing

Maps keys to nodes such that adding/removing a node only remaps K/N keys (vs K keys in naive modulo hashing).

```java
class ConsistentHashRing {
    TreeMap<Integer, String> ring = new TreeMap<>();
    int virtualNodes = 150;

    void addNode(String node) {
        for (int i = 0; i < virtualNodes; i++) {
            int hash = hash(node + "#" + i);
            ring.put(hash, node);
        }
    }

    String getNode(String key) {
        int hash = hash(key);
        Map.Entry<Integer, String> entry = ring.ceilingEntry(hash);
        if (entry == null) entry = ring.firstEntry();
        return entry.getValue();
    }
}
```

Virtual nodes ensure even distribution even with heterogeneous node sizes.

### Cache Eviction Policies

- LRU: Evict least recently used. Best for temporal locality
- LFU: Evict least frequently used. Better for long-running caches
- FIFO: Simple but poor hit rate
- TTL-based: Proactive expiration regardless of access

### Cache Invalidation Strategies

1. TTL (Time-to-Live): Simple, eventual consistency, stale data acceptable
2. Write-through: Update cache on every write. Always fresh, higher write latency
3. Cache-aside (Lazy loading): Read from DB on miss, populate cache. Simpler but thundering herd on cold start
4. Write-behind (Write-back): Write to cache, async write to DB. Low write latency, risk of data loss

## Scaling Strategy

- Add nodes dynamically; consistent hashing minimizes key remapping
- Replication: each cache node has a replica (hot standby)
- Read from primary only; replica promoted on failure
- Layered caching: L1 local cache (per server, small) → L2 distributed cache → DB

## Failure Scenarios & Reliability

- **Cache stampede (thundering herd)**: Many requests hit DB simultaneously on cache miss
  - Mitigation: Mutex lock on cache miss (only one request populates), or probabilistic early expiration
- **Cache node failure**: Consistent hashing redirects to next node; it's a cache miss (not data loss)
- **Cold start**: Pre-warm cache on startup using background loader

### Probabilistic Early Expiration (PER)
```java
boolean shouldRefresh(long expiresAt, long beta, long fetchTime) {
    double prob = -fetchTime * beta * Math.log(Math.random());
    return System.currentTimeMillis() + prob >= expiresAt;
}
```
Proactively refreshes cache before expiry under high load.

## Bottlenecks & Optimizations

- Hot keys: Replicate popular keys across multiple nodes; use local L1 cache for top-N hot keys
- Large values: Compress with snappy/gzip; split large objects
- Connection pooling: Maintain persistent connections to cache nodes

## SDE-2 Deep Dive

### Thundering Herd with Mutex/Semaphore

```java
String cacheKey = "user:" + userId;
String value = cache.get(cacheKey);
if (value != null) return value;

// Acquire distributed lock
String lockKey = "lock:" + cacheKey;
boolean locked = cache.setNX(lockKey, "1", 5); // 5s TTL

if (locked) {
    try {
        value = database.fetch(userId);
        cache.set(cacheKey, value, TTL);
    } finally {
        cache.delete(lockKey);
    }
} else {
    // Wait and retry
    Thread.sleep(50);
    value = cache.get(cacheKey);
    if (value == null) value = database.fetch(userId); // fallback
}
return value;
```

## Interview Talking Points

- Cache-aside vs write-through: Cache-aside is simpler and lazy; write-through keeps cache fresh but adds write latency
- Consistent hashing trade-off: Virtual nodes add memory overhead but ensure even distribution
- How to handle cache invalidation (the hardest problem in CS): Use TTL + event-driven invalidation via Kafka
- When NOT to cache: Highly unique query patterns, write-heavy data, data requiring strong consistency
- Mistakes to avoid: Not setting TTL (memory leak), caching null values without TTL, no cache warming strategy

---


[Back to Top](#table-of-contents)

---

# 5. Twitter / Instagram Feed

## Problem Overview

- Show a personalized feed of posts from followed users
- Core product feature for social platforms
- Challenge: fan-out to millions of followers at write or read time

## Functional Requirements

- Post a tweet/photo
- Follow/unfollow users
- View home timeline (posts from followed users, reverse chronological)
- Like, comment (basic)

## Non-Functional Requirements

- 300M daily active users, 500M tweets/day
- Timeline load: < 200ms
- High availability (feeds must work even under partial failure)
- Eventual consistency acceptable (feed may be slightly stale)

## Capacity Estimation

- 5800 tweets/sec write; 580K timeline reads/sec (100:1 ratio)
- Each tweet: ~1KB
- 500M tweets/day * 1KB = 500GB/day storage
- Fan-out: avg 200 followers → 500M * 200 = 100B timeline writes/day

## High-Level Architecture

```
Client
  |
API Gateway + Load Balancer
  |              |
Tweet Service   Timeline Service
  |                    |
Kafka (fan-out)    Redis (timeline cache per user)
  |
Fan-out Workers
  |
User Timeline Cache (Redis: sorted set per user)
  |
Object Store (S3) for media
```

## Data Model & Storage

**Tweets Table (Cassandra)**

| Column      | Type      |
|-------------|-----------|
| tweet_id    | UUID (PK) |
| user_id     | UUID      |
| content     | TEXT      |
| media_url   | TEXT      |
| created_at  | TIMESTAMP |

**User Timeline Cache (Redis Sorted Set)**
```
Key: timeline:{user_id}
Value: Set of tweet_ids scored by timestamp
ZADD timeline:123 1700000000 "tweet_456"
ZREVRANGE timeline:123 0 49  // Get latest 50 tweets
```

**Follow Graph (Graph DB or Cassandra)**

| follower_id | followee_id | created_at |
|-------------|-------------|------------|

## Core Algorithms & Techniques

### Fan-out on Write (Push Model)

When user A posts, push tweet_id to all followers' timeline caches.

```java
void onTweetCreate(Tweet tweet) {
    List<Long> followers = followService.getFollowers(tweet.userId);
    for (Long followerId : followers) {
        redisPipeline.zadd("timeline:" + followerId, tweet.createdAt, tweet.id);
        redisPipeline.zremrangeByRank("timeline:" + followerId, 0, -801); // keep 800
    }
    redisPipeline.sync();
}
```

- Pro: Read is O(1) from cache
- Con: Slow write for users with millions of followers (celebrities)

### Fan-out on Read (Pull Model)

When user opens feed, merge timelines of all followees.

```java
List<Tweet> getTimeline(long userId) {
    List<Long> followees = followService.getFollowees(userId);
    // Merge sorted streams from each followee
    PriorityQueue<Tweet> pq = new PriorityQueue<>(Comparator.comparingLong(t -> -t.createdAt));
    for (Long followeeId : followees) {
        List<Tweet> tweets = tweetCache.getRecentTweets(followeeId, 20);
        pq.addAll(tweets);
    }
    return pq.stream().limit(50).collect(Collectors.toList());
}
```

- Pro: Simple writes; handles celebrities well
- Con: Slow reads for users following many accounts

### Hybrid Approach (Recommended)

- Regular users (< 10K followers): Fan-out on write
- Celebrities (> 10K followers): Fan-out on read (merge at read time)
- User timeline = precomputed cache + lazy merge of celebrity posts

## Scaling Strategy

- Redis Cluster: shard timelines by user_id
- Kafka for async fan-out: tweet event published, fan-out workers consume in parallel
- CDN for media (images, videos)
- Sharded Cassandra for tweet storage, partitioned by tweet_id

## Failure Scenarios & Reliability

- **Fan-out worker lag**: Timeline may be stale; acceptable (eventual consistency)
- **Redis node failure**: Rebuild timeline from DB; acceptable for cache (not source of truth)
- **Kafka consumer lag**: Monitor consumer group offset; add worker instances to catch up

## Bottlenecks & Optimizations

- Celebrity post fan-out: Hybrid model prevents write amplification
- Timeline read: Precomputed Redis sorted set; O(log n) per insert, O(1) for range read
- Media: S3 + CloudFront CDN, adaptive bitrate streaming for video

## SDE-2 Deep Dive

### Redis Sorted Set for Timeline

```
ZADD timeline:userId timestamp tweetId
ZREVRANGEBYSCORE timeline:userId +inf -inf LIMIT 0 50
ZREMRANGEBYRANK timeline:userId 0 -801  // trim to 800 entries
```

Sorted set scores = Unix timestamp. Range queries are O(log n + k) where k = result count. Each user timeline in Redis uses ~80KB (800 tweets * 100 bytes).

Total memory: 300M users * 80KB = 24TB → too large for single cluster; only cache timelines for active users with LRU eviction at user level.

## Interview Talking Points

- Fan-out on write vs read: Write optimizes read latency; read optimizes write amplification
- Why Redis sorted set: O(log n) insert, O(log n + k) range query, TTL at key level
- Handling celebrities: Hybrid approach is the standard answer; mention follower count threshold
- CAP theorem: AP system; eventual consistency is fine for feeds
- Mistakes to avoid: Storing full tweet content in timeline cache (wastes memory), not trimming timeline size

---


[Back to Top](#table-of-contents)

---

# 6. WhatsApp / Messenger

## Problem Overview

- Real-time 1:1 and group messaging
- Message delivery guarantees: at-least-once; exactly-once preferred
- Offline message delivery when recipient reconnects

## Functional Requirements

- Send and receive messages (1:1 and groups)
- Message delivery receipts (sent, delivered, read)
- Online/offline presence
- Message history
- Media sharing

## Non-Functional Requirements

- 2B users, 100B messages/day = ~1.16M messages/sec
- Latency: < 100ms for message delivery
- High availability: 99.999%
- Messages must not be lost
- End-to-end encryption

## Capacity Estimation

- 100B messages/day / 86400s ≈ 1.16M msgs/sec
- Avg message: 100 bytes → 10TB/day
- Storage: compress + archive older messages to cold storage

## High-Level Architecture

```
Client App
  |
WebSocket / XMPP Connection
  |
Connection Manager (maintains user → socket mapping)
  |
Message Service
  |           |
Message DB   Kafka (for fan-out, delivery tracking)
(Cassandra)
  |
Notification Service (APNs/FCM for offline users)
  |
Presence Service (Redis)
```

## Data Model & Storage

**Messages Table (Cassandra)**

| Column      | Type      | Notes                          |
|-------------|-----------|--------------------------------|
| chat_id     | UUID      | Partition key                  |
| message_id  | TIMEUUID  | Clustering key (time-ordered)  |
| sender_id   | UUID      |                                |
| content     | TEXT      | Encrypted                      |
| status      | ENUM      | sent/delivered/read            |
| created_at  | TIMESTAMP |                                |

Cassandra chosen: write-heavy, time-series data, no joins needed, wide-row per chat_id.

**Presence Store (Redis)**
```
Key: presence:{user_id}
Value: {"status": "online", "last_seen": 1700000000}
TTL: 30s (refreshed by heartbeat)
```

## Core Algorithms & Techniques

### WebSocket Connection Management

Each Connection Manager maintains an in-memory map of `userId → WebSocket`. When message arrives for userId:

```java
void deliverMessage(Message msg) {
    WebSocket ws = connectionMap.get(msg.recipientId);
    if (ws != null && ws.isOpen()) {
        ws.send(serialize(msg));
        updateDeliveryStatus(msg.id, DELIVERED);
    } else {
        // User offline: store in DB, trigger push notification
        messageQueue.push(msg);
        pushNotificationService.notify(msg.recipientId, msg);
    }
}
```

### Message ID Generation (Monotonic, Globally Ordered per Chat)

Use Snowflake-style IDs: timestamp (41 bits) + machine ID (10 bits) + sequence (12 bits)

```java
long generateId() {
    long ts = System.currentTimeMillis() - EPOCH;
    return (ts << 22) | (machineId << 12) | (sequence++ & 0xFFF);
}
```

IDs are monotonically increasing within a machine, ensuring message ordering per chat.

### Delivery Guarantees

- At-least-once: Client retries until ACK received with exponential backoff
- Deduplication: Server checks message_id before storing; idempotent inserts in Cassandra (IF NOT EXISTS or use LWT)

## Scaling Strategy

- Connection Managers scaled horizontally; service discovery via ZooKeeper/etcd maps userId → connection manager host
- Message routing: Kafka topic per user shard; fan-out consumers deliver to connected managers
- Group messages: Kafka fanout to all group members' queues
- Media: Upload to S3, send URL in message

## Failure Scenarios & Reliability

- **Connection Manager crash**: Client reconnects to another server; undelivered messages in Kafka are reprocessed
- **Message loss**: Kafka retention + client-side retry ensures no loss
- **DB failure**: Cassandra multi-datacenter replication; quorum writes

## Bottlenecks & Optimizations

- 1.16M msgs/sec: Kafka partitioned by chat_id; parallel consumers
- Message ordering: Cassandra clustering key on TIMEUUID guarantees order per chat
- Read history: Paginated queries with TIMEUUID cursor

## SDE-2 Deep Dive

### Exactly-Once Message Delivery

True exactly-once requires:
1. Idempotent producer: Client assigns UUID to message; server deduplicates
2. Transactional consumer: Kafka consumer commits offset only after successful DB write
3. Status update propagation: Separate event stream for delivery receipts

```java
// Producer (client)
Message msg = new Message(UUID.randomUUID(), content, chatId);
while (!acked) {
    send(msg); // same UUID on retry
    wait(ACK_TIMEOUT);
}

// Consumer (server)
@KafkaListener
void handleMessage(Message msg) {
    try {
        db.insertIfNotExists(msg); // idempotent
        ws.send(msg.recipientId, msg);
        kafka.commitOffset(); // only after delivery
    } catch (Exception e) {
        // don't commit offset; message reprocessed
    }
}
```

## Interview Talking Points

- WebSocket vs polling: WebSocket for real-time; long polling as fallback
- How to route message to correct Connection Manager: Service registry (ZooKeeper/Redis) maps userId → server; message service looks up and forwards
- Group messaging at scale: Fan-out via Kafka; each member has independent delivery queue
- End-to-end encryption: Signal Protocol; server stores only ciphertext
- Mistakes to avoid: Blocking on message delivery, not handling offline scenarios, losing messages on server crash

---


[Back to Top](#table-of-contents)

---

# 7. YouTube / Netflix

## Problem Overview

- Upload, store, process, and stream video content to millions of concurrent viewers
- Core challenges: video transcoding, adaptive bitrate streaming, global CDN delivery

## Functional Requirements

- Upload video
- Transcode to multiple resolutions/formats
- Stream video with adaptive bitrate
- Search and browse content
- View counts, recommendations (basic)

## Non-Functional Requirements

- 500 hours of video uploaded/minute (YouTube scale)
- 1B+ hours watched/day
- Streaming latency: < 2s buffer start
- 99.99% availability for streaming
- Global distribution

## Capacity Estimation

- 500 hrs/min uploaded → 720K hrs/day of new content
- 1 hour of 1080p video ≈ 8GB raw → 4GB after encoding (multiple resolutions ≈ 10GB total)
- 720K * 10GB = 7.2PB/day new storage
- CDN egress: 1B hours * avg 2Mbps = 250PB/day

## High-Level Architecture

```
                   Upload API
                      |
                 Object Store (S3)
                      |
            Transcoding Service (workers)
            (FFmpeg, Zencoder, or MediaConvert)
                      |
            Transcoded files → S3/CDN Origin
                      |
             CDN (CloudFront/Akamai) Edge Nodes
                      |
                   Video Player
                (HLS/DASH adaptive streaming)
```

## Data Model & Storage

**Videos Table (SQL - PostgreSQL)**

| Column      | Type      |
|-------------|-----------|
| video_id    | UUID PK   |
| title       | TEXT      |
| uploader_id | UUID      |
| status      | ENUM      |
| s3_path     | TEXT      |
| duration    | INT       |
| created_at  | TIMESTAMP |

**Video Segments in S3:**
```
/videos/{video_id}/360p/segment_001.ts
/videos/{video_id}/720p/segment_001.ts
/videos/{video_id}/1080p/segment_001.ts
/videos/{video_id}/manifest.m3u8  (HLS manifest)
```

**View Counts (Redis + periodic flush to DB)**
```
INCR views:video_id
// Flush to DB every minute via cron
```

## Core Algorithms & Techniques

### Adaptive Bitrate Streaming (ABR)

HLS (HTTP Live Streaming) splits video into 2–10 second segments. Client downloads manifest, picks bitrate based on bandwidth:

```
#EXTM3U
#EXT-X-STREAM-INF:BANDWIDTH=800000,RESOLUTION=640x360
360p/index.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=3000000,RESOLUTION=1280x720
720p/index.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=8000000,RESOLUTION=1920x1080
1080p/index.m3u8
```

Player monitors download speed; switches to lower bitrate if speed drops.

### Video Transcoding Pipeline

```java
// Pseudocode for transcoding job
class TranscodingJob {
    void execute(String s3SourcePath, String videoId) {
        // Download from S3
        File input = s3.download(s3SourcePath);

        // Transcode to multiple resolutions
        for (Resolution res : List.of(R360, R720, R1080)) {
            File output = ffmpeg.transcode(input, res);
            s3.upload(output, "/videos/" + videoId + "/" + res.name + "/");
            generateHlsSegments(output, videoId, res);
        }

        // Update video status in DB
        db.update(videoId, Status.READY);

        // Invalidate CDN if re-transcoding
        cdn.invalidate("/videos/" + videoId + "/*");
    }
}
```

### Chunked Upload for Large Files

```java
// Client uploads in 5MB chunks
void uploadVideo(File file) {
    int chunkSize = 5 * 1024 * 1024;
    String uploadId = initiateMultipartUpload();
    for (int i = 0; i * chunkSize < file.size(); i++) {
        byte[] chunk = file.read(i * chunkSize, chunkSize);
        uploadChunk(uploadId, i, chunk);
    }
    completeUpload(uploadId);
}
```

S3 multipart upload supports pause/resume; each chunk has its own checksum.

## Scaling Strategy

- Transcoding: Horizontal scaling of worker fleet; message queue (SQS/Kafka) for job distribution
- CDN: Global edge nodes cache video segments; 99%+ cache hit rate for popular content
- Long-tail content: Infrequently watched videos fetched from origin S3 on first request, then cached at edge
- Database: Read replicas for metadata queries; search via Elasticsearch

## Failure Scenarios & Reliability

- **Transcoding job failure**: Dead letter queue + retry with exponential backoff; alert if retries exhausted
- **CDN node failure**: Anycast routing redirects to nearest healthy edge node
- **Upload interrupted**: Multipart upload allows resuming from last successful chunk
- **Origin S3 unreachable**: CDN serves stale content from cache during S3 outage

## Bottlenecks & Optimizations

- Video encoding is CPU-intensive: Use GPU instances (NVIDIA A10) for 10x speedup
- Popular video startup latency: Pre-warm CDN for scheduled releases (new movie, live event)
- Storage costs: Tiered storage — hot (frequent access) → warm → cold (Glacier for old content)

## SDE-2 Deep Dive

### CDN Cache Strategy

Segments are immutable (content-addressed by timestamp); cache indefinitely with `Cache-Control: max-age=31536000`. Only manifests need short TTL (60s) since they point to current segment list.

For live streaming, segments are generated every 2–6 seconds. CDN must not cache segments too aggressively; use `Cache-Control: max-age=5` for live manifests.

Geo-routing: DNS-based CDN routing directs users to nearest PoP. If PoP has cache miss, it fetches from origin and caches for subsequent users in that region.

### Transcoding at Scale

Each video triggers a Kafka event; transcoding workers consume and execute independently. Workers are auto-scaled based on queue depth. Use spot/preemptible instances (70% cost savings); handle preemption by checkpointing progress per segment.

## Interview Talking Points

- HLS vs DASH: Both are adaptive streaming; HLS is Apple-native; DASH is open standard; both work similarly
- Why CDN is critical: Video traffic is egress-heavy; origin cannot serve 1B hours/day
- Transcoding queue vs sync: Always async; transcoding takes minutes; user gets a "processing" state
- Storage tiering: Archive old/unpopular content to S3 Glacier ($0.004/GB/month vs $0.023 for S3 Standard)
- Mistakes to avoid: Serving video directly from S3 (no ABR, no geo-distribution), blocking upload API during transcoding

---


[Back to Top](#table-of-contents)

---

# 8. Uber / Lyft (Ride Sharing)

## Problem Overview

- Match riders with nearby drivers in real time
- Track driver locations continuously
- Calculate ETAs and pricing dynamically
- Handle the complete ride lifecycle

## Functional Requirements

- Driver sends location updates every 4 seconds
- Rider requests a ride; matched with nearest available driver
- Real-time ETA and trip tracking
- Fare calculation
- Trip history

## Non-Functional Requirements

- 1M active drivers worldwide
- Location updates: 250K writes/sec (1M drivers / 4s)
- Matching latency: < 5s end-to-end
- Availability: 99.99%
- Geo-spatial queries at scale

## Capacity Estimation

- 250K location updates/sec
- 10M ride requests/day = ~116 requests/sec
- Location data: driver_id (8B) + lat (8B) + lon (8B) + timestamp (8B) = 32 bytes
- 250K * 32B = 8MB/sec write throughput

## High-Level Architecture

```
Driver App ─── Location Service ── Redis Geo
                                      |
Rider App ──── Matching Service ──────┘
                    |
              Trip Management Service
                    |
              Notification Service (FCM/APNs)
                    |
Kafka (all events) ─── Analytics / Surge Pricing
```

## Data Model & Storage

**Driver Location (Redis GEO)**
```
GEOADD drivers_online longitude latitude driver_id
GEORADIUS drivers_online lng lat 5 km ASC COUNT 10
```

Redis GEO uses a sorted set with geohash as score. O(n+log(m)) for radius query.

**Trips Table (PostgreSQL)**

| Column         | Type      |
|----------------|-----------|
| trip_id        | UUID PK   |
| rider_id       | UUID      |
| driver_id      | UUID      |
| status         | ENUM      |
| pickup_lat/lon | FLOAT     |
| dropoff_lat/lon| FLOAT     |
| fare           | DECIMAL   |
| created_at     | TIMESTAMP |

**Driver State Machine (Redis)**
```
Key: driver:state:{driver_id}
Value: AVAILABLE | MATCHED | ON_TRIP | OFFLINE
```

## Core Algorithms & Techniques

### Geo-Spatial Indexing with Geohash

Geohash divides world into a grid of cells. Each cell has a string prefix.

```
Precision 6 geohash cell ≈ 1.2km x 0.6km
Precision 7 geohash cell ≈ 153m x 153m
```

```java
String geohash = GeoHash.encode(lat, lon, precision=7);
// Store: HSET geohash:dp3wjz driver_ids "d1,d2,d3"
// Query: look up cell + 8 neighbors
```

**Why 8 neighbors?** A point near the edge of a cell is close to drivers in adjacent cells.

### Driver Matching Algorithm

```java
List<String> findNearbyDrivers(double lat, double lon) {
    // Query Redis GEO for drivers within 5km
    List<GeoResult> results = redis.georadius("drivers_online", lon, lat, 5, KM, ASC, COUNT, 10);

    return results.stream()
        .filter(r -> driverState.get(r.driverId).equals("AVAILABLE"))
        .map(GeoResult::getDriverId)
        .collect(Collectors.toList());
}

String matchDriver(String riderId, double riderLat, double riderLon) {
    List<String> candidates = findNearbyDrivers(riderLat, riderLon);
    for (String driverId : candidates) {
        // Optimistic locking: try to claim driver atomically
        boolean claimed = redis.compareAndSet("driver:state:" + driverId, "AVAILABLE", "MATCHED");
        if (claimed) {
            notifyDriver(driverId, riderId);
            return driverId;
        }
    }
    return null; // no driver available; retry
}
```

### ETA Calculation

- Road network as weighted directed graph (OpenStreetMap data)
- Dijkstra / A* for shortest path
- Real-time traffic: adjust edge weights based on aggregated driver speed data in that segment
- Pre-compute H3 hexagon-level traffic averages; apply to path segments

## Scaling Strategy

- Location updates via UDP (fire-and-forget for speed); server-side deduplication
- Redis Cluster sharded by driver_id
- Matching Service: stateless, horizontally scaled; reads from Redis
- Kafka for all events: location updates, trip events, used by analytics and surge pricing

## Failure Scenarios & Reliability

- **Driver location stale**: TTL on Redis geo entry; remove driver from pool if no update in 10s
- **Matching Service crash**: Stateless; LB retries; Redis holds state
- **Driver accepts then crashes**: Trip state machine in DB; timeout + re-match if driver doesn't confirm in 30s

## Bottlenecks & Optimizations

- 250K location writes/sec: Batch updates in Kafka; write to Redis from consumers
- GEORADIUS query latency: O(N+log(M)); limit radius and count; < 5ms at normal load
- Surge pricing: Separate service reads demand/supply ratio from Kafka streams; updates surge multiplier in Redis

## SDE-2 Deep Dive

### H3 Hexagonal Grid for Supply/Demand

Uber uses H3 (Uber's hexagonal grid system). Hexagons tile the plane uniformly (no edge/corner ambiguity like squares). Each hexagon at resolution 9 ≈ 0.1 km².

```java
// H3 library usage
String h3Index = h3.geoToH3(lat, lon, resolution=9);
// Count available drivers in cell
long supply = redis.hget("h3_supply", h3Index);
long demand = redis.hget("h3_demand", h3Index);
double surgeMultiplier = calculateSurge(supply, demand);
```

Hexagons are hierarchical: resolution 7 contains ~7 resolution 9 cells. Aggregate demand up the hierarchy for broader surge zones.

## Interview Talking Points

- Why Redis GEO over PostGIS: Sub-millisecond reads; PostGIS is durable but 10-100ms; use PostGIS for historical data
- Matching fairness: Nearest driver first; could use ETA-based matching for better reliability
- CAP theorem: CP for trip state (can't double-book a driver), AP for location tracking
- Driver state atomicity: Compare-and-swap in Redis prevents two riders claiming same driver
- Mistakes to avoid: Using HTTP polling for location updates (too slow), not handling driver app crash during matching

---


[Back to Top](#table-of-contents)

---

# 9. Google Drive / Dropbox

## Problem Overview

- Cloud file storage with sync across devices
- Real-time collaboration and version history
- Reliable upload/download with large file support

## Functional Requirements

- Upload/download files and folders
- File sync across devices
- Share files/folders with other users
- Version history and restore
- Search by filename

## Non-Functional Requirements

- 1B users, 50B files
- File sizes: 1KB to 5GB
- Upload/download throughput: maximize
- Durability: 99.999999999% (eleven nines) — like S3
- Availability: 99.99%

## Capacity Estimation

- Avg file size: 500KB → 50B * 500KB = 25PB storage
- 1% of files modified/day → 500M changes/day
- Delta sync reduces bandwidth: only changed blocks uploaded

## High-Level Architecture

```
Client (Desktop/Mobile Sync Agent)
  |
Upload Service (chunked multipart upload)
  |
Block Storage (S3 or custom object store)
  |
Metadata Service ── PostgreSQL (file tree, versions)
  |
Sync Service (Kafka) ── notify other devices
  |
CDN (fast downloads, edge caching)
```

## Data Model & Storage

**Files Table (PostgreSQL)**

| Column      | Type      | Notes                             |
|-------------|-----------|-----------------------------------|
| file_id     | UUID PK   |                                   |
| parent_id   | UUID FK   | Folder hierarchy                  |
| owner_id    | UUID      |                                   |
| name        | TEXT      |                                   |
| size        | BIGINT    |                                   |
| mime_type   | TEXT      |                                   |
| checksum    | TEXT      | SHA-256 of file content           |
| is_deleted  | BOOLEAN   | Soft delete                       |
| created_at  | TIMESTAMP |                                   |

**File Blocks Table**

| Column   | Type  | Notes                         |
|----------|-------|-------------------------------|
| block_id | TEXT  | SHA-256 of block content (PK) |
| s3_path  | TEXT  |                               |
| size     | INT   |                               |

**File Versions Table**

| Column     | Type      |
|------------|-----------|
| version_id | UUID PK   |
| file_id    | UUID FK   |
| block_ids  | TEXT[]    | Ordered list of block IDs     |
| created_at | TIMESTAMP |                               |

## Core Algorithms & Techniques

### Content-Defined Chunking (CDC)

Split files into variable-size chunks using Rabin fingerprinting. Chunk boundaries shift with content; only changed chunks need re-upload.

```java
List<byte[]> chunk(byte[] file) {
    List<byte[]> chunks = new ArrayList<>();
    int start = 0;
    long hash = 0;
    for (int i = 0; i < file.length; i++) {
        hash = (hash << 1) ^ file[i] ^ POLY;
        if ((hash & MASK) == PATTERN || i - start > MAX_CHUNK) {
            chunks.add(Arrays.copyOfRange(file, start, i + 1));
            start = i + 1;
            hash = 0;
        }
    }
    if (start < file.length) chunks.add(Arrays.copyOfRange(file, start, file.length));
    return chunks;
}
```

Content-addressable storage: `block_id = SHA256(block_content)`. Identical blocks across users stored once (deduplication).

### Delta Sync Protocol

```java
void sync(File localFile) {
    List<Block> localBlocks = chunk(localFile);
    List<String> localBlockIds = localBlocks.stream().map(b -> sha256(b)).collect(toList());

    // Ask server which blocks it already has
    Set<String> serverHas = metadataService.checkBlocks(localBlockIds);

    // Upload only missing blocks
    for (Block block : localBlocks) {
        if (!serverHas.contains(sha256(block))) {
            uploadBlock(block);
        }
    }

    // Update file metadata with new block list
    metadataService.updateVersion(fileId, localBlockIds);
}
```

### Conflict Resolution

When two clients modify same file simultaneously:
- Last-write-wins (simple but loses data)
- Both versions preserved; user prompted to resolve (Dropbox approach)
- Operational Transform / CRDT for real-time collaborative editing (Google Docs approach)

## Scaling Strategy

- Block storage: S3 with versioning disabled (versions managed in our DB); cross-region replication
- Metadata: PostgreSQL with read replicas; shard by owner_id at scale
- Sync notifications: Kafka → WebSocket push to connected clients
- Upload acceleration: Multipart parallel upload; direct-to-S3 presigned URLs (bypasses app servers)

## Failure Scenarios & Reliability

- **Partial upload**: Resumable uploads with block-level checksums; resume from last successful block
- **Sync conflict**: Detect via vector clocks or last-modified timestamp; create conflict copy
- **S3 outage**: Cross-region replication; failover to secondary region
- **Metadata DB failure**: Read replicas serve reads; writes queued in Kafka until primary recovers

## Bottlenecks & Optimizations

- Large file upload: Parallel chunk upload to S3 (up to 10 parallel streams)
- Deduplication: SHA-256 content addressing eliminates redundant storage; typical 30-40% savings
- Bandwidth: Delta sync uploads only changed blocks; typical 90% bandwidth reduction for modifications

## SDE-2 Deep Dive

### Presigned URL Upload Flow

```
Client → Upload Service: "I want to upload file.pdf"
Upload Service → S3: generate presigned PUT URL (valid 1 hour)
Upload Service → Client: presigned URL
Client → S3: PUT file directly (bypasses app servers)
S3 → SNS/SQS: upload complete event
Metadata Service → consumes event: update DB
```

This eliminates app server as a bottleneck for large uploads. S3 handles throughput directly.

### Block-Level Deduplication

Block_id = SHA-256(content). Before uploading:
```java
List<String> missing = metadataService.getMissingBlocks(blockIds);
// Only upload blocks not in `missing`
// Server-side: INSERT INTO blocks (block_id, s3_path) ON CONFLICT DO NOTHING
```

At 1B users * avg 25GB, deduplication can reduce storage from 25EB to 2–5EB (especially for common documents, OS files, etc.).

## Interview Talking Points

- CDC vs fixed-size chunking: CDC adapts to content; insert in middle shifts boundaries for fixed-size but not CDC
- Deduplication trade-off: SHA-256 collision risk is negligible (2^256 space); storage savings are significant
- Why presigned URLs: Security (S3 access without exposing credentials) + performance (direct upload)
- Soft delete + recycle bin: Set is_deleted = true; cron job permanently deletes after 30 days
- Mistakes to avoid: Storing files in DB (BLOBs don't scale), not implementing chunked uploads, re-uploading unchanged file

---


[Back to Top](#table-of-contents)

---

# 10. Notification System

## Problem Overview

- Deliver messages to users via multiple channels: push (APNs/FCM), SMS, email
- High-throughput, reliable delivery with deduplication
- Support scheduled and triggered notifications

## Functional Requirements

- Send notifications to users via push, SMS, email
- Template-based notifications
- Deduplication (no duplicate notifications)
- User preferences (opt-in/out per channel)
- Delivery tracking

## Non-Functional Requirements

- 10M notifications/day = ~115/sec average; up to 1M/sec during marketing blasts
- Delivery latency: push < 1s, email < 5s, SMS < 10s
- Exactly-once delivery preferred; at-least-once acceptable
- High availability: 99.99%

## Capacity Estimation

- 10M push + 1M SMS + 5M email / day
- Each notification record: ~1KB
- Storage: 16M * 1KB = 16GB/day
- Third-party API calls: FCM (Google), APNs (Apple), Twilio (SMS), SendGrid (email)

## High-Level Architecture

```
Triggering Service (User action, cron job, campaign service)
  |
Notification Service (API)
  |
Kafka (notification events, partitioned by user_id)
  |           |           |
Push Worker  SMS Worker  Email Worker
  |           |           |
FCM/APNs    Twilio    SendGrid/SES
  |
Delivery DB (MySQL) — track status
  |
Retry Queue (Kafka DLQ)
```

## Data Model & Storage

**Notifications Table (MySQL)**

| Column      | Type      | Notes                          |
|-------------|-----------|--------------------------------|
| notif_id    | UUID PK   |                                |
| user_id     | UUID      | Indexed                        |
| type        | ENUM      | push/sms/email                 |
| template_id | VARCHAR   |                                |
| payload     | JSON      |                                |
| status      | ENUM      | pending/sent/delivered/failed  |
| idempotency_key | VARCHAR | Unique constraint             |
| created_at  | TIMESTAMP |                                |

**User Preferences Table**

| Column        | Type    |
|---------------|---------|
| user_id       | UUID PK |
| push_enabled  | BOOLEAN |
| sms_enabled   | BOOLEAN |
| email_enabled | BOOLEAN |
| quiet_hours   | JSON    |

## Core Algorithms & Techniques

### Idempotent Notification Delivery

```java
void sendNotification(Notification notif) {
    // Check if already sent using idempotency key
    if (db.existsByIdempotencyKey(notif.idempotencyKey)) {
        log.info("Duplicate notification, skipping: {}", notif.idempotencyKey);
        return;
    }

    // Insert with unique constraint on idempotency_key
    try {
        db.insert(notif); // throws on duplicate key
    } catch (DuplicateKeyException e) {
        return; // race condition, already inserted
    }

    // Send via appropriate channel
    boolean success = channelRouter.send(notif);
    db.updateStatus(notif.id, success ? SENT : FAILED);
}
```

### Rate Limiting per User per Channel

Prevent notification spam:
```java
boolean canSend(String userId, String channel) {
    String key = "notif_rate:" + userId + ":" + channel + ":" + (System.currentTimeMillis() / 3600000);
    long count = redis.incr(key);
    if (count == 1) redis.expire(key, 3600);
    return count <= LIMIT_PER_HOUR.get(channel);
}
```

### Batching for Email/SMS

```java
void processBatch(List<Notification> batch) {
    // Group by recipient for SMS aggregation
    Map<String, List<Notification>> byUser = batch.stream().collect(groupingBy(n -> n.userId));
    byUser.forEach((userId, notifs) -> {
        if (notifs.size() > 3) {
            // Aggregate: "You have 5 new notifications"
            sendAggregated(userId, notifs);
        } else {
            notifs.forEach(this::sendIndividual);
        }
    });
}
```

## Scaling Strategy

- Kafka partitioned by user_id: same user's notifications processed in order
- Worker pools per channel: push workers separate from SMS/email (different rate limits)
- Priority queues: Transactional notifications (OTP, order confirmation) > Marketing
- Scale workers independently based on queue depth

## Failure Scenarios & Reliability

- **Third-party API failure (FCM/Twilio)**: Retry with exponential backoff; dead letter queue after N retries; alert ops
- **Worker crash**: Kafka offset not committed; messages reprocessed (idempotency prevents duplicate sends)
- **DB failure**: Write to Kafka first (WAL); DB consumer catches up on recovery

## Bottlenecks & Optimizations

- Marketing blasts (send to 100M users at once): Batch in chunks of 10K; rate-limit to avoid overwhelming downstream APIs
- Token management: Device push tokens expire; handle `InvalidRegistration` responses from FCM by deleting stale tokens
- Email deliverability: Dedicated IPs, SPF/DKIM/DMARC, warm-up IP reputation for new campaigns

## SDE-2 Deep Dive

### Priority Queue with Kafka

Use separate Kafka topics for priority tiers:
```
notifications-high    (OTPs, security alerts) → consumed first
notifications-medium  (order updates, messages) → consumed normally
notifications-low     (marketing, promotions) → consumed if no high/medium lag
```

Workers poll high-priority topic first; fall back to medium/low if empty. This ensures critical notifications are delivered even during load spikes.

### Handling FCM Rate Limits

FCM imposes per-token and per-project rate limits. Use token bucket on sender side:
```java
RateLimiter fcmLimiter = RateLimiter.create(1000); // 1000/sec

void sendFcm(FcmMessage msg) {
    fcmLimiter.acquire(); // blocks if over limit
    fcmClient.send(msg);
}
```

Also handle FCM errors: `MessageRateExceeded` → back off 60s; `InvalidRegistration` → delete token from DB.

## Interview Talking Points

- Push vs pull for notifications: Push (FCM/APNs) is real-time; SMS as fallback when app not installed
- Deduplication strategy: Idempotency key = hash(user_id + template_id + trigger_event_id); unique DB constraint
- Handling opt-out at scale: Check preferences table per user; cache preferences in Redis (TTL 1h)
- Quiet hours: Store user timezone; check local time before sending non-urgent notifications
- Mistakes to avoid: Not batching/aggregating notifications (spam), not handling third-party rate limits, no retry logic

---


[Back to Top](#table-of-contents)

---

# 11. Google Search / Typeahead / Autocomplete

## Problem Overview

- **Search**: Given a query, return ranked list of relevant web pages
- **Typeahead**: As user types, suggest completions in real time (< 100ms)
- Foundational to almost every search product

## Functional Requirements

- Typeahead: Return top 5–10 suggestions as user types each character
- Suggestions ranked by popularity/frequency
- Personalized suggestions (optional)
- Handle billions of possible prefix queries

## Non-Functional Requirements

- 5B searches/day on Google = ~58K searches/sec
- Typeahead latency: < 50ms (ideally < 10ms)
- High availability: 99.99%
- Suggestions must be updated within 1 hour of trending topics

## Capacity Estimation

- 5B searches/day; avg query = 4 words = 20 chars
- User types each char → 5B * 20 chars = 100B typeahead requests/day ≈ 1.16M/sec
- With client-side debouncing: reduce by 90% → ~116K requests/sec
- Trie storage: ~100M unique queries, avg 20 chars = ~2GB raw; compressed significantly

## High-Level Architecture

```
User Types
  |
Client (debounce 100ms)
  |
Typeahead Service (stateless, CDN-cached)
  |
Trie Service (in-memory distributed trie)
  |
Query Log Aggregator (Kafka → MapReduce → update trie daily)
  |
Personalization Layer (optional, user query history)
```

## Data Model & Storage

### Trie Data Structure

```
root
 ├── 'a' (freq: 0)
 │    └── 'p' (freq: 0)
 │         ├── 'p' (freq: 1000)  → "app"
 │         └── 'l' (freq: 800)   → "apple"
 └── 'g' (freq: 0)
      └── 'o' (freq: 0)
           └── 'o' (freq: 5000) → "google"
```

Each node stores: character, frequency, top-k suggestions list (cached to avoid traversal).

**Storage**: Serialize trie to disk; shard by first 2 characters.

## Core Algorithms & Techniques

### Trie with Cached Top-K at Each Node

```java
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    int frequency;
    List<String> topSuggestions; // top-5 cached at each node

    void updateTopSuggestions(String query) {
        topSuggestions.add(query);
        topSuggestions.sort((a, b) -> freqMap.get(b) - freqMap.get(a));
        if (topSuggestions.size() > 5) topSuggestions.remove(5);
    }
}

List<String> search(String prefix) {
    TrieNode node = root;
    for (char c : prefix.toCharArray()) {
        if (!node.children.containsKey(c)) return Collections.emptyList();
        node = node.children.get(c);
    }
    return node.topSuggestions; // O(prefix.length) lookup
}
```

This makes lookup O(L) where L = prefix length; no need to traverse subtree.

### Trie Update Strategy (Offline)

```
Daily:
1. Aggregate query logs from Kafka (MapReduce)
2. Compute frequency of each query
3. Rebuild trie offline
4. Blue-green swap: update new trie instance, switch traffic
```

For trending topics (real-time):
- Maintain a separate fast-path for "trending queries" (last 1hr top-100) using a simple sorted set
- Merge trending results with trie results

### Alternative: Prefix Hash + Suggestion Cache

```
Redis:
Key: "suggest:go"
Value: ["google", "google maps", "golang", "google drive", "go game"]
```

Pre-compute suggestions for all prefixes up to length 10. Cache in Redis.

- Storage: 1M unique prefixes * avg 500B per suggestion list = 500GB (manageable with Redis Cluster)
- Update: Recompute and update Redis entries for affected prefixes when frequencies change

## Scaling Strategy

- Shard trie by first 2 chars: 26^2 = 676 shards; each small enough for single server
- CDN cache: suggestions for common prefixes cached at edge (high hit rate)
- Stateless service: Multiple replicas; consistent hashing routes same prefix to same shard

## Failure Scenarios & Reliability

- **Trie node failure**: Replica takes over; slight stale data acceptable
- **Trie update failure**: Keep old trie serving; rollback update job
- **Cache miss**: Fall back to trie service; acceptable latency increase

## Bottlenecks & Optimizations

- Client-side debounce (100ms) reduces requests by 5-10x
- CDN caching of common prefixes: top 10K prefixes cover 90% of traffic
- Trie compression: Merge nodes with single children (Patricia trie) reduces memory by 60%

## SDE-2 Deep Dive

### Personalization Layer

User-specific suggestions based on query history:
```java
List<String> getPersonalizedSuggestions(String prefix, String userId) {
    List<String> global = trieService.get(prefix);
    List<String> personal = redis.zrevrange("user_queries:" + userId + ":" + prefix, 0, 2);

    // Merge: personal suggestions first, then global
    List<String> result = new ArrayList<>(personal);
    for (String s : global) {
        if (!result.contains(s) && result.size() < 5) result.add(s);
    }
    return result;
}
```

Store user query history as `ZADD user_queries:{userId}:{prefix} timestamp query` with max 100 entries per prefix.

## Interview Talking Points

- Why trie over inverted index for autocomplete: Trie is prefix-optimized; inverted index is word-based
- How often to update: Batch daily for stability; real-time cache for trending
- How to handle typos: Fuzzy matching using BK-tree or Levenshtein distance on candidates
- Why cache top-k at each node: Avoids O(n) subtree traversal on every lookup
- Mistakes to avoid: Not caching at CDN (every character causes server hit), not debouncing client-side

---


[Back to Top](#table-of-contents)

---

# 12. Web Crawler

## Problem Overview

- Systematically browse the web to discover and index pages
- Foundation for search engines (Google), price aggregators, news aggregators
- Must handle scale, politeness, and deduplication

## Functional Requirements

- Start from seed URLs; follow links to discover new pages
- Download and store page content
- Respect robots.txt
- Avoid crawling same URL twice
- Prioritize important URLs

## Non-Functional Requirements

- Crawl 1B pages/month = ~385 pages/sec
- Average page: 100KB HTML
- Storage: 1B * 100KB = 100TB/month
- Distributed: single server cannot crawl fast enough
- Politeness: max 1 request/sec per domain

## Capacity Estimation

- 385 pages/sec * 100KB = 38.5 MB/sec bandwidth
- DNS lookups: 385/sec (cache domain → IP)
- URL frontier: 10B URLs in queue at any time

## High-Level Architecture

```
Seed URLs
  |
URL Frontier (priority queue, per-domain rate limiting)
  |
Fetcher Pool (100s of worker machines)
  |           |
DNS Cache   HTML Content → Object Store (S3)
  |
Link Extractor
  |
URL Filter (Bloom Filter dedup + robots.txt check)
  |
Back to URL Frontier (if new URL)
  |
Indexer (downstream consumer)
```

## Data Model & Storage

**URL Frontier (distributed priority queue)**
- Priority based on: PageRank estimate, freshness, crawl date
- Per-domain queue to enforce politeness
- Implemented with Kafka + per-domain scheduling

**Crawled URL Store (Bloom Filter in Redis + DB)**
```
Redis Bloom Filter: membership check (probabilistic, O(1))
PostgreSQL: exact record of crawled URLs, last crawl time, checksum
```

**Page Content Store (S3)**
```
s3://crawler-pages/{domain}/{url_hash}.html.gz
```

## Core Algorithms & Techniques

### Bloom Filter for URL Deduplication

```java
BloomFilter<String> seenUrls = BloomFilter.create(
    Funnels.stringFunnel(Charset.UTF_8),
    1_000_000_000, // 1B elements
    0.001          // 1% false positive rate
);
// Memory: ~1.2GB for 1B elements at 0.1% FPR

boolean isNew(String url) {
    if (seenUrls.mightContain(url)) {
        // Might be false positive; check DB for confirmation
        return !db.exists(url);
    }
    return true;
}

void markCrawled(String url) {
    seenUrls.put(url);
    db.insert(url);
}
```

### URL Frontier with Politeness

```java
class URLFrontier {
    Map<String, Queue<String>> domainQueues = new HashMap<>();
    Map<String, Long> domainLastCrawl = new HashMap<>();

    String next() {
        for (String domain : domainQueues.keySet()) {
            long lastCrawl = domainLastCrawl.getOrDefault(domain, 0L);
            if (System.currentTimeMillis() - lastCrawl > 1000) { // 1s per domain
                String url = domainQueues.get(domain).poll();
                if (url != null) {
                    domainLastCrawl.put(domain, System.currentTimeMillis());
                    return url;
                }
            }
        }
        return null; // all domains at capacity
    }
}
```

### robots.txt Parsing

```java
Map<String, Boolean> robotsCache = new LRUCache<>(100_000);

boolean isAllowed(String url) {
    String domain = extractDomain(url);
    String path = extractPath(url);

    RobotsTxt robots = robotsCache.computeIfAbsent(domain,
        d -> fetch("http://" + d + "/robots.txt"));

    return robots.isAllowed("*", path);
}
```

### Content Deduplication (SimHash)

Two pages with similar content (duplicate articles, mirrored sites) should not both be indexed.

```java
long simhash(String content) {
    int[] v = new int[64];
    for (String token : tokenize(content)) {
        long h = murmurhash(token);
        for (int i = 0; i < 64; i++) {
            if ((h >> i & 1) == 1) v[i]++;
            else v[i]--;
        }
    }
    long fingerprint = 0;
    for (int i = 0; i < 64; i++) {
        if (v[i] > 0) fingerprint |= (1L << i);
    }
    return fingerprint;
}

int hammingDistance(long a, long b) {
    return Long.bitCount(a ^ b);
}
// hammingDistance < 3 → near-duplicate
```

## Scaling Strategy

- Horizontal scaling of fetcher workers (100–1000 machines)
- Distributed URL frontier: Kafka topics per domain priority tier
- DNS caching: local DNS cache per worker; TTL-based refresh
- Content storage: S3 with intelligent tiering for older crawls

## Failure Scenarios & Reliability

- **Fetcher crash**: URLs re-queued in Kafka (offset not committed)
- **Blocked by target site**: Rate limiting, IP rotation (within legal bounds), respect robots.txt
- **URL frontier overload**: Cap queue size per domain; drop low-priority overflow

## Bottlenecks & Optimizations

- DNS lookup is bottleneck: Maintain in-memory DNS cache; warm on startup
- Politeness throttles throughput: Maximize domain diversity in frontier
- Duplicate content: SimHash deduplication before indexing reduces storage and index size by 30-40%

## SDE-2 Deep Dive

### Distributed URL Frontier Architecture

```
URL Router Service: receives extracted URLs, hashes domain → assigns to partition
Kafka (partitioned by domain hash): each partition is a crawler's queue
Crawler Worker: consumes from assigned partitions, respects per-domain rate limit
Priority: Kafka consumer pulls from high-priority topic first
```

This ensures:
- Same domain always processed by same worker (consistent rate limiting)
- Horizontal scaling: add workers → add partitions
- No central lock on frontier

### PageRank-Based Prioritization

Assign crawl priority based on estimated PageRank:
- Pages with many inbound links get higher priority score
- Bootstrap from DMOZ/known high-authority domains
- Re-crawl high-PageRank pages more frequently

```java
double priority(String url) {
    double inlinkCount = linkGraph.getInlinkCount(url); // from previous crawl
    double freshness = 1.0 / (daysSinceLastCrawl(url) + 1);
    return 0.7 * Math.log(inlinkCount + 1) + 0.3 * freshness;
}
```

## Interview Talking Points

- How to avoid crawling trap (infinite URLs): URL normalization, max depth limit, query param dedup
- robots.txt respect: Legal requirement in many jurisdictions; cache per domain
- Why Bloom filter: 1B URL Bloom filter uses ~1.2GB vs 100GB for hash table
- How to handle JS-rendered pages: Headless browser (Puppeteer/Playwright) for important domains; expensive so selective
- Mistakes to avoid: No politeness (gets IP banned), no content dedup (wasted storage), not normalizing URLs (crawl same page multiple times)

---


[Back to Top](#table-of-contents)

---

# 13. Metrics / Monitoring System (like Datadog)

## Problem Overview

- Collect metrics (CPU, memory, custom business metrics) from thousands of services
- Store time-series data efficiently
- Query and visualize; alert on anomalies
- Essential for operational observability

## Functional Requirements

- Ingest metrics from agents on all hosts
- Store time-series data (metric_name, tags, timestamp, value)
- Query: range query, aggregation (avg, p99, sum) over time windows
- Alerting based on thresholds
- Dashboards

## Non-Functional Requirements

- 10K hosts, each emitting 1000 metrics/min = 10M data points/min ≈ 167K/sec
- Query latency: < 1s for 1-hour range, < 5s for 1-week range
- Storage: 10M points/min * 8 bytes = 80MB/min = ~115GB/day
- 1 year retention with downsampling
- Availability: 99.9% (monitoring can tolerate brief outages)

## Capacity Estimation

- Raw: 115GB/day, 42TB/year (1-second granularity)
- With downsampling: 5-minute aggregates for data > 24h → 20x compression → ~2TB/year
- With columnar compression: further 10x → ~200GB/year

## High-Level Architecture

```
Host Agents (StatsD / Prometheus Node Exporter)
  |
Collection API (UDP preferred for metrics, TCP for logs)
  |
Kafka (metrics stream, partitioned by metric_name)
  |
Ingestion Workers
  |           |
Time-Series DB   Meta DB
(InfluxDB/       (MySQL: alert rules,
 Prometheus      dashboard configs)
 Thanos)
  |
Query Service + Alert Evaluator
  |
Dashboard UI
```

## Data Model & Storage

### Time-Series Data Point
```
metric_name: "api.request.latency"
tags: {"service": "payment", "region": "us-east-1", "host": "web-01"}
timestamp: 1700000000
value: 42.5
```

**Tag-based indexing**: Store tag combinations as inverted index for fast filtering.

**Time-Series DB Internals (InfluxDB/Prometheus TSDB)**

Data organized in:
- Time-ordered chunks (2h blocks in Prometheus)
- Columnar storage within each chunk (all values for same series together)
- Delta-of-delta encoding + XOR compression

```
Series: api.request.latency{service=payment}
Timestamps: [1000, 1060, 1120, 1180, ...] → delta: [1000, 60, 60, 60] → delta-of-delta: [1000, 60, 0, 0] → RLE
Values: [42.5, 43.1, 41.8, ...] → XOR float compression (Gorilla encoding)
```

Gorilla compression achieves ~1.5 bytes per data point vs 16 bytes raw.

## Core Algorithms & Techniques

### Gorilla Float Compression (Facebook/Prometheus)

```java
// XOR current with previous; most metrics change little
long xorValue = Float.floatToIntBits(current) ^ Float.floatToIntBits(prev);
if (xorValue == 0) {
    // Same as previous, write 0-bit
} else {
    // Write leading zeros, meaningful bits length, meaningful bits
    int leading = Long.numberOfLeadingZeros(xorValue);
    int trailing = Long.numberOfTrailingZeros(xorValue);
    // ...encode efficiently
}
```

### Downsampling for Long-Term Storage

```java
void downsample(String metric, long windowStart, long windowEnd, int targetPoints) {
    List<DataPoint> raw = tsdb.query(metric, windowStart, windowEnd);
    int bucketSize = raw.size() / targetPoints;
    for (int i = 0; i < raw.size(); i += bucketSize) {
        List<DataPoint> bucket = raw.subList(i, Math.min(i + bucketSize, raw.size()));
        double avg = bucket.stream().mapToDouble(d -> d.value).average().orElse(0);
        double p99 = percentile(bucket, 0.99);
        double max = bucket.stream().mapToDouble(d -> d.value).max().orElse(0);
        lowResStore.write(metric, bucket.get(0).timestamp, avg, p99, max);
    }
}
```

Store avg, p99, max, min for each downsampled bucket — never recompute from raw after downsampling.

### Alerting with Sliding Window

```java
boolean shouldAlert(AlertRule rule) {
    List<DataPoint> recent = tsdb.query(rule.metric, now() - rule.window, now());
    double value = aggregate(recent, rule.aggregation); // avg, max, p99
    return rule.comparator == GT ? value > rule.threshold : value < rule.threshold;
}

// Run every 30 seconds per alert rule
scheduler.scheduleAtFixedRate(() -> rules.forEach(this::evaluateAlert), 0, 30, SECONDS);
```

Alert deduplication: Fire once; re-fire only if resolved and triggered again.

## Scaling Strategy

- Kafka partitioned by metric_name for ordering within series
- TSDB sharding: shard by metric_name + tags (using consistent hashing)
- Query fan-out: query routes to appropriate shard; aggregation service merges results
- Long-term storage: Thanos/Cortex federate Prometheus across regions; store raw in S3

## Failure Scenarios & Reliability

- **Ingestion lag**: Kafka provides buffering; consumers catch up; metrics delayed but not lost
- **TSDB node failure**: Replication factor 2; replica serves reads during primary recovery
- **Alert evaluator crash**: Stateless; restart and re-evaluate from TSDB

## Bottlenecks & Optimizations

- High cardinality tags: Each unique tag combination = unique time series; limit tag values (avoid user_id as tag)
- Query performance: Pre-aggregate rollups at ingest time; separate hot (24h) and cold (archive) storage
- Write path: Batch writes (1000 points per batch); WAL for durability before memtable

## SDE-2 Deep Dive

### TSDB Write Path (Prometheus-style)

```
Incoming data point
  |
WAL (Write-Ahead Log) - durability guarantee
  |
In-memory Chunks (per series, 2h window)
  |
Periodic Compaction (every 2h) → immutable block written to disk
  |
Longer-range compaction → merge blocks, apply downsampling
```

Each series maintains a current "head chunk" in memory. On flush, chunk compressed with XOR encoding, written to block. Blocks are immutable; deleted by retention policy.

High-cardinality problem: 1M unique label combinations = 1M separate time series. Each needs its own chunk in memory. 1M series * 1KB chunk = 1GB just for head chunks. Solutions: limit cardinality at ingestion, use tag value hashing for high-cardinality fields.

## Interview Talking Points

- Why UDP for metric ingestion: Loss-tolerant; no connection overhead; metrics are high-frequency and losing 0.01% is acceptable
- Push vs pull model: Prometheus pulls (simpler service discovery); Datadog pushes (works behind NAT); both are valid
- High-cardinality challenge: Don't use user_id/request_id as tags; hash to fixed buckets
- Long-term retention: S3 with Parquet/ORC columnar format; query via Athena/Presto
- Mistakes to avoid: Storing raw metrics forever (costs explode), using RDBMS for time-series (poor compression, slow range queries)

---


[Back to Top](#table-of-contents)

---

# 14. Task Queue / Job Scheduler

## Problem Overview

- Execute background tasks asynchronously (email sending, report generation, video transcoding)
- Schedule tasks for future execution (cron-like)
- Retry failed tasks; ensure at-least-once execution

## Functional Requirements

- Enqueue task with payload
- Workers dequeue and execute tasks
- Retry on failure with backoff
- Scheduled/delayed tasks (run at specific time)
- Priority queues
- Dead letter queue for permanently failed tasks

## Non-Functional Requirements

- Throughput: 100K tasks/sec enqueue; 10K tasks/sec execution
- Latency: task picked up within 1s of enqueue
- Exactly-once execution preferred; at-least-once guaranteed
- Scalability: horizontally scale workers
- Durability: tasks must not be lost

## Capacity Estimation

- 100K enqueue/sec → Kafka is the right choice for throughput
- Task payloads: avg 1KB → 100MB/sec write to broker
- 10K workers, each executing 1 task/sec = 10K tasks/sec execution rate
- With 10s avg task duration: 100K in-flight tasks at any time

## High-Level Architecture

```
Producer (API Server)
  |
Task API Service
  |          |
Kafka       Delay Store (Redis Sorted Set — for scheduled tasks)
  |
Dispatcher (reads Kafka, pops due tasks from Redis)
  |
Worker Pool (horizontally scaled, pulls tasks)
  |
Result Store (Redis / DB)
  |
DLQ (failed tasks after N retries)
```

## Data Model & Storage

**Tasks Table (PostgreSQL for durability)**

| Column      | Type      | Notes                            |
|-------------|-----------|----------------------------------|
| task_id     | UUID PK   |                                  |
| queue_name  | VARCHAR   |                                  |
| payload     | JSONB     |                                  |
| status      | ENUM      | pending/running/done/failed/dead |
| retries     | INT       |                                  |
| run_at      | TIMESTAMP | for scheduled tasks              |
| created_at  | TIMESTAMP |                                  |
| locked_by   | VARCHAR   | Worker ID holding lock           |
| locked_at   | TIMESTAMP |                                  |

**Delayed Task Queue (Redis Sorted Set)**
```
ZADD scheduled_tasks run_at_unix task_id
ZRANGEBYSCORE scheduled_tasks -inf now LIMIT 0 100  // tasks due now
```

## Core Algorithms & Techniques

### Task Locking (At-Least-Once with Visibility Timeout)

```java
// Worker fetches task with optimistic lock
Task fetchTask(String queue) {
    return db.executeInTransaction(() -> {
        Task task = db.queryOne(
            "SELECT * FROM tasks WHERE queue=? AND status='pending' " +
            "AND run_at <= now() ORDER BY priority DESC, created_at ASC " +
            "LIMIT 1 FOR UPDATE SKIP LOCKED",
            queue
        );
        if (task != null) {
            db.update("UPDATE tasks SET status='running', locked_by=?, locked_at=now() WHERE task_id=?",
                workerId, task.id);
        }
        return task;
    });
}
```

`FOR UPDATE SKIP LOCKED` is a PostgreSQL feature that allows concurrent workers to each grab different tasks without contention.

### Visibility Timeout (Heartbeat)

```java
// Worker sends heartbeat every 30s while task is running
scheduler.scheduleAtFixedRate(() -> {
    if (currentTask != null) {
        db.update("UPDATE tasks SET locked_at=now() WHERE task_id=? AND locked_by=?",
            currentTask.id, workerId);
    }
}, 0, 30, SECONDS);

// Reaper thread reclaims stale tasks
scheduler.scheduleAtFixedRate(() -> {
    db.update("UPDATE tasks SET status='pending', locked_by=null WHERE status='running' " +
              "AND locked_at < now() - interval '90 seconds'");
}, 0, 60, SECONDS);
```

### Retry with Exponential Backoff

```java
void onTaskFailure(Task task, Exception e) {
    if (task.retries >= MAX_RETRIES) {
        db.update("UPDATE tasks SET status='dead' WHERE task_id=?", task.id);
        dlq.send(task); // dead letter queue for inspection
        return;
    }
    long delay = (long) Math.pow(2, task.retries) * 1000; // 1s, 2s, 4s, 8s...
    delay += random.nextInt(1000); // jitter to prevent thundering herd
    db.update("UPDATE tasks SET status='pending', retries=retries+1, run_at=now()+interval '? ms' WHERE task_id=?",
        delay, task.id);
}
```

### Scheduled Tasks (Delay Queue)

```java
// Enqueue delayed task
void scheduleTask(Task task, long runAtUnix) {
    db.insert(task);
    redis.zadd("scheduled:" + task.queue, runAtUnix, task.id);
}

// Dispatcher loop
void dispatchScheduledTasks() {
    long now = System.currentTimeMillis() / 1000;
    List<String> dueTaskIds = redis.zrangebyscore("scheduled:*", "-inf", now, 0, 100);
    dueTaskIds.forEach(id -> {
        db.update("UPDATE tasks SET status='pending' WHERE task_id=?", id);
        redis.zrem("scheduled:*", id);
    });
}
```

## Scaling Strategy

- Workers: Stateless, scale based on queue depth (autoscaling)
- Queue broker: Kafka for high-throughput; Redis/SQS for simpler deployments
- Partitioning: Partition by queue_name so related tasks process in order
- Priority: Separate Kafka topics (or queue tables) per priority tier

## Failure Scenarios & Reliability

- **Worker crash mid-task**: Visibility timeout + reaper reclaims task; idempotency in task handler prevents double-execution side effects
- **Kafka partition unavailable**: Replicated partitions; consumer reassignment
- **Scheduled task dispatcher crash**: Tasks remain in Redis sorted set and DB; new dispatcher instance picks up

## Bottlenecks & Optimizations

- `FOR UPDATE SKIP LOCKED` contention: Shard task table by queue_name; separate workers per queue
- Scheduled task polling: Polling Redis every second is fine for millions of tasks
- Large payloads: Store payload in S3, pass reference in task (max task size in Kafka is 1MB by default)

## SDE-2 Deep Dive

### Exactly-Once Execution

True exactly-once requires idempotent task handlers:

```java
@TaskHandler("send_welcome_email")
void sendWelcomeEmail(Task task) {
    String userId = task.payload.get("user_id");
    String idempotencyKey = "welcome_email:" + userId;

    // Check if already sent
    if (redis.exists(idempotencyKey)) {
        log.info("Already sent welcome email to {}", userId);
        return;
    }

    emailService.send(userId, "Welcome!");

    // Mark as sent with TTL longer than max retry period
    redis.setex(idempotencyKey, 86400 * 7, "sent");
}
```

The task framework guarantees at-least-once execution. Idempotent handlers guarantee at-most-once side effects. Combined = effectively-once.

## Interview Talking Points

- At-least-once vs exactly-once: Exactly-once requires idempotent handlers + deduplication; framework provides at-least-once
- FOR UPDATE SKIP LOCKED: PostgreSQL-specific; alternative in MySQL is `SELECT ... WHERE ... FOR UPDATE` with application-level skip
- When to use Kafka vs Redis Queue vs DB queue: Kafka for high-throughput; Redis for simple delay queues; DB for transactional enqueue (task enqueued atomically with business operation)
- DLQ purpose: Inspect failed tasks, fix bug, re-enqueue
- Mistakes to avoid: No visibility timeout (crashed workers hold tasks forever), no idempotency in handlers, no dead letter queue

---


[Back to Top](#table-of-contents)

---

# 15. API Gateway

## Problem Overview

- Single entry point for all client requests to microservices
- Handles cross-cutting concerns: authentication, rate limiting, routing, SSL termination
- Eliminates the need for each service to implement these independently

## Functional Requirements

- Route requests to appropriate backend service
- Authentication and authorization (JWT/OAuth)
- Rate limiting per client
- Request/response transformation
- SSL termination
- Load balancing
- Caching for GET responses

## Non-Functional Requirements

- Throughput: 500K requests/sec
- Latency overhead: < 5ms added by gateway
- High availability: 99.999% (it's the entry point)
- Horizontal scaling without state

## Capacity Estimation

- 500K req/sec with < 5ms overhead
- Each request adds: auth check (~1ms Redis), rate limit check (~0.5ms Redis), routing (~0.1ms), logging (~0.1ms)
- Total: ~2ms overhead achievable

## High-Level Architecture

```
Internet
  |
DNS + Global Load Balancer (Anycast)
  |
API Gateway Cluster (stateless, N instances)
  |     |    |    |    |
Auth  Rate  Route Cache  Log
 |    Limit   |          |
Redis Redis  Service   Kafka
             Registry
```

## Data Model & Storage

**Route Table (in-memory, loaded from config DB)**
```json
{
  "/api/v1/users/*": "user-service:8080",
  "/api/v1/orders/*": "order-service:8081",
  "/api/v1/payments/*": "payment-service:8082"
}
```

**JWT Validation (stateless — no DB lookup)**
```
JWT Header.Payload.Signature
Signature = HMAC(Header + "." + Payload, secret)
Validate: recompute signature, compare; extract claims from payload
```

For token revocation: maintain a Redis blocklist of revoked JWTs (TTL = token expiry time).

## Core Algorithms & Techniques

### Request Pipeline (Chain of Responsibility)

```java
class ApiGateway {
    List<Filter> pipeline = List.of(
        new SSLTerminator(),
        new RateLimiter(),          // Redis check
        new AuthenticationFilter(), // JWT validation
        new AuthorizationFilter(),  // RBAC check
        new RequestTransformer(),   // Header injection, body transform
        new CacheFilter(),          // Check cache for GET
        new RouterFilter(),         // Route to backend
        new ResponseTransformer(),  // Response mapping
        new LoggingFilter()         // Async to Kafka
    );

    Response handle(Request req) {
        for (Filter f : pipeline) {
            Response earlyResponse = f.process(req);
            if (earlyResponse != null) return earlyResponse; // short-circuit
        }
        return proxyToBackend(req);
    }
}
```

### Service Discovery Integration

```java
// Gateway queries service registry (Consul/etcd/Kubernetes) for backend addresses
String resolveBackend(String serviceName) {
    List<ServiceInstance> instances = serviceRegistry.getInstances(serviceName);
    // Round-robin or weighted load balancing
    return loadBalancer.choose(instances).getUri();
}
```

### Circuit Breaker

```java
class CircuitBreaker {
    int failures = 0;
    State state = CLOSED;
    long openTime;

    boolean allowRequest() {
        if (state == OPEN) {
            if (System.currentTimeMillis() - openTime > TIMEOUT) {
                state = HALF_OPEN; // try one request
                return true;
            }
            return false; // fast fail
        }
        return true;
    }

    void recordFailure() {
        failures++;
        if (failures > THRESHOLD) {
            state = OPEN;
            openTime = System.currentTimeMillis();
        }
    }

    void recordSuccess() {
        failures = 0;
        state = CLOSED;
    }
}
```

## Scaling Strategy

- Stateless gateway: All state in Redis (rate limits, token blocklist); any instance handles any request
- Horizontal scaling behind global load balancer
- L4 LB for connection distribution; L7 LB for content-based routing if needed before gateway
- Geographic deployment: API gateway instances in each region; Route53 latency-based routing

## Failure Scenarios & Reliability

- **Gateway instance crash**: LB removes from pool; traffic rerouted
- **Redis unavailable**: Rate limiter fails open; auth falls back to local JWT validation (no revocation check)
- **Backend service down**: Circuit breaker opens; return 503 immediately; retry routing to healthy replicas

## Bottlenecks & Optimizations

- Auth latency: Cache validated JWT payload locally (LRU with TTL matching token TTL)
- Redis round-trip: Pipeline rate-limit + auth checks in single Redis round-trip
- Connection pooling: Maintain persistent connection pools to each backend service

## SDE-2 Deep Dive

### L4 vs L7 Load Balancing

L4 (TCP/IP level):
- Routes based on IP + port
- Very fast (~100ns overhead)
- Cannot inspect HTTP headers or path
- Used for initial ingress (distributing TCP connections)

L7 (Application level):
- Routes based on URL path, headers, cookies
- Higher overhead (~1ms)
- Enables sticky sessions, content-based routing, A/B testing
- API Gateway operates at L7

Architecture: L4 LB (hardware or Maglev) → L7 LB (NGINX/Envoy) → API Gateway → Backend

### JWT vs Session Tokens

JWT: Stateless; payload contains claims; validated locally; can't revoke without blocklist.
Session: Stateful; stored in Redis; revocable; requires DB lookup per request.

For API Gateway: JWT preferred (no DB per request); revocation via short TTL + Redis blocklist for compromised tokens.

## Interview Talking Points

- Why central API gateway: Avoid duplicating auth, rate limiting in every service
- Circuit breaker pattern: Fail fast when downstream is unhealthy; prevent cascade failure
- JWT revocation: Short TTL (15min) + blocklist for explicit revocation; trade-off: blocklist grows with revocations
- Rate limiting in gateway: Per-client, per-endpoint; Redis with Lua script for atomicity
- Mistakes to avoid: Making gateway do business logic (it should be infrastructure), synchronous auth service call on every request (use JWT), no circuit breakers

---


[Back to Top](#table-of-contents)

---

# 16. Distributed Message Queue (like Kafka)

## Problem Overview

- Durable, high-throughput, ordered message streaming between producers and consumers
- Decouples systems; enables async processing, event sourcing, log aggregation
- Core infrastructure for modern distributed systems

## Functional Requirements

- Producers publish messages to topics
- Consumers subscribe and read messages in order
- Message durability: messages persisted on disk
- At-least-once delivery (exactly-once with transactions)
- Consumer offset management (track where each consumer group is)
- Topic partitioning for parallelism

## Non-Functional Requirements

- Throughput: 1M messages/sec per broker
- Latency: < 10ms end-to-end (producer to consumer)
- Durability: replicated across N brokers; survive broker failure
- Retention: configurable (7 days default, or by size)
- Horizontal scaling: add partitions/brokers without downtime

## Capacity Estimation

- 1M msgs/sec per broker * 10 brokers = 10M msgs/sec cluster throughput
- Avg message: 1KB → 10GB/sec write throughput
- 7-day retention * 10GB/sec = ~6PB storage (with replication factor 3: ~18PB)
- Compression (LZ4): ~5x → ~3.5PB actual disk

## High-Level Architecture

```
Producer App
  |
Kafka Client (batching, compression, acks)
  |
Kafka Broker Cluster (Leader + Followers per partition)
  |
ZooKeeper / KRaft (cluster metadata, leader election)
  |
Consumer App (Consumer Group, offset tracking)
```

## Data Model & Storage

### Topic → Partitions → Segments

```
Topic: "orders"
  Partition 0: [msg0, msg1, msg2, ...] (on Broker 1, replicated on Brokers 2,3)
  Partition 1: [msg0, msg1, msg2, ...] (on Broker 2, replicated on Brokers 1,3)
  Partition 2: [msg0, msg1, msg2, ...] (on Broker 3, replicated on Brokers 1,2)
```

Each partition is an append-only ordered log on disk.

### Log Segment Files

```
/kafka/data/orders-0/
  00000000000000000000.log   (messages)
  00000000000000000000.index (offset → file position)
  00000000000000000000.timeindex (timestamp → offset)
  00000000000000001000.log   (next segment after 1000 messages)
```

Index file enables binary search for a given offset without reading all messages.

### Consumer Offset Storage

```
__consumer_offsets topic (internal Kafka topic)
Key: group_id + topic + partition
Value: committed offset
```

## Core Algorithms & Techniques

### Zero-Copy I/O (sendfile)

Traditional file read: disk → OS buffer → user space → socket buffer → network
Zero-copy (sendfile): disk → OS buffer → network (skips user space copy)

Kafka uses `FileChannel.transferTo()` which maps to `sendfile(2)` syscall — 2x throughput improvement.

```java
// Kafka's file send path (simplified)
void sendToSocket(FileChannel file, SocketChannel socket, long offset, long count) {
    file.transferTo(offset, count, socket); // zero-copy
}
```

### Leader Election per Partition (ISR-based)

In-Sync Replicas (ISR): Set of replicas fully caught up with leader.

```
Leader: Broker 1
ISR: [Broker 1, Broker 2, Broker 3]
```

On leader failure:
1. ZooKeeper/KRaft detects leader loss
2. Controller selects new leader from ISR
3. Remaining ISR members recognize new leader
4. Producers/consumers redirect to new leader

```java
// Producer acks configuration
props.put("acks", "all");   // wait for all ISR replicas
props.put("acks", "1");     // wait for leader only (faster, less durable)
props.put("acks", "0");     // fire and forget (highest throughput)
```

### Exactly-Once Semantics (Transactions)

```java
// Producer
producer.initTransactions();
producer.beginTransaction();
try {
    producer.send(new ProducerRecord<>("topic-A", key, value));
    producer.send(new ProducerRecord<>("topic-B", key, value));
    // Atomic: either both sent or neither
    producer.sendOffsetsToTransaction(offsets, consumerGroup);
    producer.commitTransaction();
} catch (Exception e) {
    producer.abortTransaction();
}
```

Internally uses 2-phase commit coordinated by a Transaction Coordinator broker.

### Consumer Group Rebalancing

When consumer joins or leaves, partitions redistributed:

```
Before: ConsumerA → Partition[0,1,2], ConsumerB → Partition[3,4,5]
ConsumerC joins
After: ConsumerA → [0,1], ConsumerB → [2,3], ConsumerC → [4,5]
```

Rebalance protocol:
1. Consumers send JoinGroup request to Group Coordinator
2. Coordinator elects group leader
3. Leader runs partition assignment algorithm (RangeAssignor, RoundRobinAssignor)
4. Leader sends assignment back via SyncGroup
5. All consumers begin consuming assigned partitions

### Log Compaction

For event sourcing use cases: keep only last value per key.

```
Before compaction:
key=user1 val=A
key=user2 val=X
key=user1 val=B  (later update)
key=user2 val=Y  (later update)

After compaction:
key=user1 val=B  (only latest)
key=user2 val=Y  (only latest)
```

Log cleaner thread runs in background; merges segments and removes superseded keys.

## Scaling Strategy

- Add partitions: More parallelism for producers and consumers; can't reduce partition count
- Add brokers: Kafka automatically rebalances partition leaders; use `kafka-reassign-partitions`
- Consumer parallelism: Max parallelism = number of partitions; add consumers up to partition count
- Compression: LZ4 at producer side; 3-5x reduction in network and disk I/O
- Batching: Producer batches messages (linger.ms, batch.size); larger batches = higher throughput, higher latency

## Failure Scenarios & Reliability

- **Broker failure**: Leader partitions fail over to ISR replicas (30–60s in ZooKeeper era; faster with KRaft)
- **Network partition (split brain)**: Controller ensures only one leader per partition; non-ISR replicas cannot be elected
- **Consumer lag**: Monitor consumer group lag; scale out consumers; increase partition count
- **Disk full**: Log retention by time/size prevents unbounded growth; monitor disk and alert

## Bottlenecks & Optimizations

- Producer throughput: Enable batching and compression; use async send
- Consumer throughput: Increase `fetch.min.bytes` and `fetch.max.wait.ms` for larger batches
- Replication lag: Tune `replica.fetch.max.bytes`; ensure broker-to-broker bandwidth sufficient
- ZooKeeper bottleneck: Migrate to KRaft mode (Kafka 3.x+) — removes ZooKeeper dependency

## SDE-2 Deep Dive

### Write Path: How a Message Gets Persisted

```
1. Producer sends ProducerRecord to leader broker
2. Broker writes to OS page cache (not fsync yet)
3. Broker appends to partition log file
4. Followers fetch from leader (replication)
5. Once ISR replicas acknowledge, leader marks message as committed
6. Committed offset returned to producer
7. OS page cache eventually flushed to disk (fsync on interval or segment roll)
```

Kafka relies on OS page cache for read performance: recently written messages served from page cache (RAM) without disk read. This is why Kafka is fast even without explicit caching layer.

### Partition Key Selection Strategy

Messages with same key → same partition (ordering guarantee per key).

```java
int partition = Math.abs(key.hashCode()) % numPartitions;
```

Choose partition key based on ordering requirement:
- Order events: partition by order_id (all events for one order go to same partition, in order)
- User activity: partition by user_id
- No ordering needed: null key → round-robin for maximum throughput

Poor key selection → hot partitions → uneven load. Use key hashing with virtual nodes if keys are skewed.

### KRaft Mode (Kafka without ZooKeeper)

KRaft uses Raft consensus within Kafka itself:
- A subset of brokers act as controllers (voters)
- Metadata stored in an internal `__cluster_metadata` topic
- Leader election via Raft: majority quorum required
- Eliminates ZooKeeper operational overhead and split-brain between ZK and Kafka

Benefits: Faster controller failover (< 1s vs minutes with ZK), simpler deployment, higher metadata throughput.

## Interview Talking Points

- Why Kafka over RabbitMQ: Kafka is a log; consumers read at their own pace; messages retained for N days; supports replay. RabbitMQ is a traditional queue; messages deleted after consumption; better for task queues
- Exactly-once semantics: Only with idempotent producer + transactional API; consumer must also be idempotent
- Partition count decision: More partitions = more parallelism but higher overhead; rule of thumb: 10-100 partitions per topic; can increase later
- Consumer group vs broadcast: Consumer group = load balanced (each message processed by one consumer); topic subscription without group = each consumer gets all messages
- CAP theorem: Kafka is CP — partitions tolerated with ISR; consistency ensured by not electing out-of-sync replicas (may lose availability briefly)
- Mistakes to avoid: Too few partitions (bottleneck), using same consumer group for different use cases (they share offset), not monitoring consumer lag, using Kafka as a database (it's not optimized for random reads)

---

[Back to Top](#table-of-contents)

# Quick Reference: Key Trade-offs Summary

| Topic | Decision | Trade-off |
|-------|----------|-----------|
| URL Shortener | Base62 vs MD5 | Base62 needs counter; MD5 needs collision check |
| Rate Limiter | Token bucket vs sliding window | Token burst-friendly; sliding more accurate |
| Cache | Write-through vs cache-aside | Write-through fresh; cache-aside lazy |
| Feed | Fan-out write vs read | Write fast reads; read avoids write amplification |
| Messaging | WebSocket vs polling | WS real-time; polling simpler |
| Video | HLS vs DASH | HLS Apple-native; DASH open standard |
| Storage | SQL vs NoSQL | SQL for relations; NoSQL for scale/flexibility |
| Kafka | acks=all vs acks=1 | all=durable; 1=faster |
| Crawler | Bloom filter vs hash set | Bloom: 1.2GB for 1B URLs; hash: 100GB |
| TSDB | Push vs pull | Push works behind NAT; pull simpler service discovery |

---

*End of System Design Notes — All 16 Systems*
