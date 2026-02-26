# AWS Developer Notes — SDE-2 Level

## Table of Contents
1. AWS Fundamentals
2. Identity & Security (IAM)
3. Networking — VPC Deep Dive
4. Compute Services
5. Storage Services
6. Databases
7. Messaging & Event-Driven Systems
8. Load Balancing & Scalability
9. CDN & DNS
10. Observability & Monitoring
11. Infrastructure as Code
12. Security Deep Dive
13. Cost Optimization
14. Migration & Real-World Architecture

---

# 1. AWS Fundamentals

## What Is Cloud Computing?

Before AWS existed, companies had to buy physical servers, store them in their own data centers, hire people to maintain them, and plan capacity years in advance. If they bought too few servers, they couldn't handle traffic spikes. If they bought too many, those servers sat idle, wasting money.

Cloud computing flips this model. Instead of owning hardware, you rent computing resources from a provider (like AWS) and pay only for what you use. Think of it like electricity — you don't build a power plant; you plug into the grid and pay per kilowatt-hour.

AWS is the largest cloud provider in the world. It offers hundreds of services covering compute, storage, databases, networking, machine learning, and more — all accessible via APIs.

**Why companies use AWS:**
- No upfront hardware cost
- Scale up or down instantly based on demand
- Global infrastructure available immediately
- Pay-as-you-go pricing
- Managed services reduce operational burden (AWS handles OS patches, hardware failures, etc.)

## AWS Global Infrastructure

### Regions

A **Region** is a geographic area where AWS has built data centers. Examples: us-east-1 (Northern Virginia), eu-west-1 (Ireland), ap-southeast-1 (Singapore).

Each Region is completely independent — data stored in one Region does not automatically replicate to another. This is important for data sovereignty (e.g., GDPR requires EU user data to stay in the EU).

When you choose a Region, consider:
- **Latency**: Pick the Region closest to your users
- **Compliance**: Some data must stay in specific geographies
- **Service availability**: Not all AWS services are in all Regions
- **Cost**: Prices vary slightly by Region

### Availability Zones (AZs)

Within each Region, AWS has multiple **Availability Zones** (usually 3–6). Each AZ is one or more physically separate data centers with independent power, cooling, and networking.

AZs within a Region are connected by low-latency, high-throughput private fiber. They are far enough apart that a single disaster (flood, fire) won't take out more than one AZ.

**Why this matters**: If you deploy your application in a single AZ and that AZ has a power outage, your app goes down. If you deploy across multiple AZs, your app stays up even when one AZ fails. This is called **high availability**.

```
Region: us-east-1
├── AZ: us-east-1a  (Data Center cluster A)
├── AZ: us-east-1b  (Data Center cluster B)
└── AZ: us-east-1c  (Data Center cluster C)
```

### Edge Locations

**Edge Locations** are smaller AWS infrastructure points spread across 400+ cities globally. They are used by services like CloudFront (CDN) and Route 53 (DNS).

When a user in Mumbai requests your website hosted in us-east-1, the content doesn't travel all the way to Virginia. Instead, CloudFront serves it from the nearest edge location (maybe in Mumbai itself), dramatically reducing latency.

```
User in Mumbai
     |
     v
Edge Location (Mumbai)  <-- serves cached content instantly
     |
     v  (only on cache miss)
Origin: us-east-1 (your actual servers)
```

## Shared Responsibility Model

This is one of the most important concepts in AWS security. It defines who is responsible for what.

**AWS is responsible for "security OF the cloud":**
- Physical data center security
- Hardware and underlying infrastructure
- Hypervisor (virtualization layer)
- Managed service software (e.g., AWS patches the RDS database engine)

**You are responsible for "security IN the cloud":**
- Your application code
- Operating system configuration on EC2 instances
- Network and firewall settings (Security Groups, NACLs)
- Data encryption (you choose whether to encrypt your S3 data)
- Identity and access management (who has access to what)
- Patching your EC2 operating systems

**Example:**
If you run a web server on EC2 and fail to apply OS security patches, and an attacker exploits that vulnerability — that's your responsibility, not AWS's. AWS gave you a secure hypervisor; you didn't secure your OS.

---

# 2. Identity & Security (IAM)

## Why IAM Exists

Every AWS API call must be authenticated (who are you?) and authorized (are you allowed to do this?). IAM (Identity and Access Management) is the system AWS uses to handle this.

Without IAM, anyone could call any AWS API and do anything — delete your databases, read your S3 files, spin up thousands of expensive EC2 instances.

## IAM Components

### Root User

When you create an AWS account, you get a **root user** — it has unlimited access to everything. Think of it as the "god mode" account.

**Best practice**: Never use the root user for daily work. Create IAM users and use those instead. Enable MFA on the root account immediately and lock the credentials away.

The only things that require root user: closing the account, changing the support plan, enabling some billing features.

### IAM Users

An **IAM User** represents a person or application that needs long-term access to AWS. Users have credentials (username/password for console, or access key/secret for API).

**Problem with users**: If you're building an EC2 instance that needs to read from S3, you might be tempted to create an IAM user, generate access keys, and hardcode them in your application. This is bad — keys can be leaked in code repositories, are long-lived, and hard to rotate.

### IAM Groups

A **Group** is a collection of IAM users. You attach policies to groups, and all users in the group inherit those permissions. This makes managing permissions for teams much easier.

Example: Create a "Developers" group with permissions to deploy Lambda functions and read CloudWatch logs. Add all developers to that group.

### IAM Roles

A **Role** is the key concept for secure, temporary access — especially for AWS services.

A Role is an IAM identity with permissions attached, but unlike a user, it has no permanent credentials. Instead, when something "assumes" a role, AWS issues temporary security credentials (valid for 15 minutes to 12 hours).

**Key use cases:**
- An EC2 instance assumes a role to get temporary credentials to access S3 (no hardcoded keys needed)
- A Lambda function assumes a role to write to DynamoDB
- A developer assumes a role to access a production environment (with MFA required)
- Cross-account access: Role in Account B that Account A users can assume

```
EC2 Instance
    |
    | "I need to read from S3"
    v
IAM Role (attached to EC2)
    |
    | Issues temporary credentials
    v
STS (Security Token Service)
    |
    v
EC2 gets: AccessKeyId, SecretAccessKey, SessionToken (expires in 1 hour)
    |
    v
S3 API Call succeeds
```

## IAM Policies

A **Policy** is a JSON document that defines permissions. It specifies what actions are allowed or denied on what resources.

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:GetObject", "s3:PutObject"],
      "Resource": "arn:aws:s3:::my-bucket/*"
    }
  ]
}
```

### Identity-Based Policies

Attached to a user, group, or role. Defines what that identity can do.

- **AWS Managed Policies**: Pre-built by AWS (e.g., `AmazonS3ReadOnlyAccess`). Easy to use but broad.
- **Customer Managed Policies**: You create them. More specific, reusable across identities.
- **Inline Policies**: Embedded directly in a single user/role. Not reusable.

### Resource-Based Policies

Attached to a resource (like an S3 bucket or SQS queue). Defines who can access that resource and what they can do.

Example: An S3 bucket policy that says "allow the CloudFront service to read any object in this bucket."

### Policy Evaluation Logic

When an API call is made, AWS evaluates all applicable policies:

1. If there's an explicit **Deny** anywhere → DENY (Deny always wins)
2. If there's an explicit **Allow** → ALLOW
3. If neither → DENY (default deny)

This means: no policy = no access. You must explicitly grant everything.

## Security Best Practices

**Least Privilege**: Grant only the permissions needed to do the job. Start with nothing and add permissions as needed. If a Lambda only reads from one DynamoDB table, give it exactly that — not `dynamodb:*` on all tables.

**MFA (Multi-Factor Authentication)**: Require a second factor (like a phone app) for human logins. Critical for root and admin accounts.

**Temporary Credentials**: Always prefer roles over long-term access keys. If you must use access keys, rotate them regularly.

**No hardcoded credentials**: Never put access keys in code, config files, or environment variables. Use IAM roles for EC2/Lambda, and Secrets Manager for third-party credentials.

## Real Use Cases

**EC2 accessing S3 securely:**
1. Create an IAM Role with a policy allowing s3:GetObject on your bucket
2. Attach the role to the EC2 instance at launch (instance profile)
3. Your application calls S3 — the AWS SDK automatically fetches temporary credentials from the instance metadata endpoint
4. No keys ever touch your code

**Lambda accessing DynamoDB:**
1. Create an IAM Role with dynamodb:GetItem, dynamodb:PutItem permissions
2. Assign this role as the Lambda's "execution role"
3. Lambda automatically gets credentials when invoked

---

# 3. Networking — VPC Deep Dive

## What Is a VPC?

A **Virtual Private Cloud (VPC)** is your own isolated network within AWS. Think of it as buying a private plot of land in a city — AWS is the city, and your VPC is your fenced plot. You control what goes in and out.

When you create an AWS account, AWS creates a "default VPC" in each Region. But for production systems, you always create custom VPCs with deliberate network design.

**Without a VPC**, all your resources would be on a shared public network — anyone could potentially reach them. A VPC creates isolation.

## CIDR and IP Addressing

**CIDR (Classless Inter-Domain Routing)** notation defines a range of IP addresses.

Format: `10.0.0.0/16`
- `10.0.0.0` is the starting IP
- `/16` means the first 16 bits are fixed; the remaining 16 bits are variable
- This gives you 2^16 = 65,536 IP addresses (10.0.0.0 through 10.0.255.255)

Common VPC CIDR choices:
- `10.0.0.0/16` — 65,536 addresses (recommended for large systems)
- `172.16.0.0/16` — another private range
- `192.168.0.0/16` — smaller deployments

**Rule**: Use private IP ranges (10.x, 172.16-31.x, 192.168.x) for VPCs — these are not routable on the public internet.

## Subnets

A **Subnet** is a subdivision of your VPC — a smaller range of IPs within your VPC's range. Each subnet lives in exactly one AZ.

### Public Subnets

A subnet is "public" when it has a route to the Internet Gateway. Resources here can receive traffic from the internet (if they have a public IP).

Use for: Load balancers, NAT gateways, bastion hosts

### Private Subnets

A subnet is "private" when it has NO route to the Internet Gateway. Resources here cannot be directly accessed from the internet.

Use for: Application servers, databases

Private subnets can still reach the internet (for software updates, calling external APIs) via a NAT Gateway.

### Isolated Subnets

No inbound or outbound internet access at all. Even stricter than private.

Use for: Highly sensitive databases, compliance-sensitive workloads

```
VPC: 10.0.0.0/16
├── Public Subnet: 10.0.1.0/24 (AZ-a)   — has Internet Gateway route
├── Public Subnet: 10.0.2.0/24 (AZ-b)   — has Internet Gateway route
├── Private Subnet: 10.0.3.0/24 (AZ-a)  — routes internet via NAT
├── Private Subnet: 10.0.4.0/24 (AZ-b)  — routes internet via NAT
├── Isolated Subnet: 10.0.5.0/24 (AZ-a) — no internet at all
└── Isolated Subnet: 10.0.6.0/24 (AZ-b) — no internet at all
```

## Route Tables

A **Route Table** is a set of rules that determine where network traffic is directed. Every subnet is associated with exactly one route table.

Example route table for a public subnet:
```
Destination       Target
10.0.0.0/16      local          (traffic within VPC stays local)
0.0.0.0/0        igw-xxxxxxx    (everything else goes to Internet Gateway)
```

Example route table for a private subnet:
```
Destination       Target
10.0.0.0/16      local
0.0.0.0/0        nat-xxxxxxx    (internet traffic goes through NAT Gateway)
```

## Internet Gateway (IGW)

An **Internet Gateway** is an AWS-managed component attached to your VPC that allows communication between your VPC and the internet. It's horizontally scaled, redundant, and highly available — you don't manage it.

For a resource to be reachable from the internet, it needs:
1. An Internet Gateway attached to the VPC
2. A route in its subnet's route table pointing to the IGW
3. A public IP address (Elastic IP or auto-assigned public IP)
4. Security Group/NACL allowing the traffic

## NAT Gateway

A **NAT (Network Address Translation) Gateway** allows resources in private subnets to initiate outbound connections to the internet (e.g., download packages, call external APIs) while preventing the internet from initiating connections back to them.

The NAT Gateway lives in a public subnet and has a public IP. Private resources send traffic to NAT, which forwards it to the internet. Responses come back to NAT, which forwards them to the originating private resource.

**Cost note**: NAT Gateways are charged per hour AND per GB of data processed. In high-traffic systems, this can be significant.

## Security Groups vs. NACLs

These are two different firewalls in AWS. Both control traffic, but they work differently.

| Feature | Security Group | NACL (Network ACL) |
|---------|---------------|---------------------|
| Level | Instance/ENI level | Subnet level |
| State | Stateful | Stateless |
| Rules | Allow rules only | Allow and Deny rules |
| Evaluation | All rules evaluated | Rules evaluated in order (lowest number first) |
| Default | Deny all inbound, allow all outbound | Allow all traffic |

**Stateful vs Stateless explained:**
- Security Groups are stateful: if you allow inbound traffic on port 80, the response is automatically allowed back out — you don't need a separate outbound rule.
- NACLs are stateless: you must explicitly allow both the inbound request AND the outbound response.

**Practical usage:**
- Use Security Groups for most traffic control (they're simpler and more intuitive)
- Use NACLs as an additional layer, especially to block specific IP ranges at the subnet level

## VPC Peering

**VPC Peering** connects two VPCs so resources in them can communicate using private IPs, as if they were in the same network. Works across accounts and regions.

Limitations:
- No transitive routing: If A peers with B, and B peers with C, A cannot reach C through B
- CIDR ranges must not overlap

## VPC Endpoints

By default, if your EC2 in a private subnet calls S3, the traffic goes through the NAT Gateway, out to the internet, and back. This costs money for NAT data processing and means your traffic leaves AWS's network.

**VPC Endpoints** let your VPC connect to AWS services privately, without the traffic leaving AWS's network.

Two types:
- **Gateway Endpoints**: For S3 and DynamoDB. Free. You add a route in your route table.
- **Interface Endpoints** (PrivateLink): For most other AWS services. Powered by ENIs in your subnet. Small hourly cost.

## Common Real-World VPC Architecture

```
Internet
    |
Internet Gateway
    |
+-- Public Subnet (10.0.1.0/24) ----+
|   Load Balancer                    |
|   NAT Gateway                      |
+-----------------------------------+
    |
+-- Private Subnet (10.0.2.0/24) ---+
|   Application Servers (EC2/ECS)    |
+-----------------------------------+
    |
+-- Isolated Subnet (10.0.3.0/24) --+
|   RDS Database                     |
+-----------------------------------+
```

---

# 4. Compute Services

## EC2 — Elastic Compute Cloud

EC2 provides virtual servers (instances) in the cloud. You choose the OS, CPU, memory, storage, and networking. EC2 is the foundation of most AWS architectures.

### Instance Types

Instance types define the hardware profile. They follow a naming convention: `m5.xlarge`
- `m`: Instance family (m=general purpose, c=compute optimized, r=memory optimized, etc.)
- `5`: Generation (higher = newer hardware)
- `xlarge`: Size (nano, micro, small, medium, large, xlarge, 2xlarge, ...)

Common families:
- **t3/t4g**: Burstable general purpose. Cheap. Good for development, low-traffic apps.
- **m6i**: Balanced compute/memory. Good for most web applications.
- **c6i**: Compute optimized. Good for CPU-intensive tasks (video encoding, batch processing).
- **r6i**: Memory optimized. Good for large databases, caching.
- **p3/g4**: GPU instances. Good for machine learning training.

### Storage Options

**EBS (Elastic Block Store)**: Network-attached storage. Persists after instance termination. Can be detached and reattached. Like a USB drive you plug into your server.

EBS volume types:
- **gp3**: General purpose SSD. Default choice. Good for most workloads.
- **io2**: Provisioned IOPS SSD. For databases needing consistent low latency.
- **st1**: Throughput-optimized HDD. For big data, log processing.

**Instance Store**: Physical disks attached to the host machine. Much faster than EBS (NVMe). But ephemeral — data is lost when the instance stops or terminates. Use for temp files, caching, buffers.

### AMIs (Amazon Machine Images)

An AMI is a snapshot of an EC2 instance's OS, configuration, and software. It's the template used to launch instances.

You can use:
- AWS-provided AMIs (Amazon Linux, Ubuntu, Windows)
- Marketplace AMIs (pre-configured application stacks)
- Custom AMIs you build yourself (your app pre-installed)

**Key practice**: Build custom AMIs with your application pre-installed. This makes Auto Scaling faster (no bootstrapping time) and ensures consistency across instances.

### Auto Scaling

**Auto Scaling Groups (ASGs)** automatically add or remove EC2 instances based on demand.

You define:
- Minimum instances (e.g., 2)
- Maximum instances (e.g., 20)
- Desired instances (e.g., 4)
- Scaling policies (when to scale)

Scaling triggers:
- **Target Tracking**: "Keep average CPU at 60%" — ASG automatically adds/removes instances to maintain that
- **Step Scaling**: "If CPU > 80%, add 2 instances. If CPU > 90%, add 5 instances."
- **Scheduled Scaling**: "Add 10 instances every Monday at 8 AM"

## Lambda

Lambda is AWS's serverless compute service. You provide a function (code), and AWS runs it in response to events. You don't manage servers, OS, or scaling.

### How Lambda Works Internally

1. You upload code (or write it in the console)
2. You define a trigger (API Gateway, S3 event, SQS message, etc.)
3. When a trigger fires, AWS finds (or creates) a container to run your code
4. Your function executes
5. AWS bills you for execution duration (measured in milliseconds) and number of invocations

### Cold Starts

The first time a Lambda function is invoked (or after a period of inactivity), AWS must:
1. Allocate a container
2. Download and unpack your code
3. Initialize the runtime (e.g., start the JVM for Java)
4. Run your initialization code (imports, connections setup)

This takes time — called a **cold start**. Can be 100ms for Python/Node.js, up to 2–5 seconds for Java.

Subsequent invocations reuse the warm container — much faster.

**Mitigation strategies:**
- Use lightweight runtimes (Python, Node.js) when latency matters
- Keep deployment packages small
- Use Provisioned Concurrency (AWS pre-warms containers, costs extra)
- Keep Lambda "warm" with scheduled pings (hacky, not recommended for production)

### Lambda Limits

- Maximum execution time: 15 minutes
- Memory: 128 MB to 10 GB
- Deployment package: 50 MB zipped, 250 MB unzipped
- Concurrency: 1000 concurrent executions per Region (soft limit, can be increased)
- Ephemeral storage (/tmp): 512 MB to 10 GB

### When to Use Lambda

**Good fit:**
- Event-driven processing (process S3 uploads, SQS messages)
- APIs with variable traffic
- Scheduled jobs (cron-like tasks)
- Lightweight microservices

**Poor fit:**
- Long-running processes (> 15 minutes)
- Heavy CPU workloads (limited to 6 vCPUs)
- Workloads needing persistent connections (databases — mitigate with RDS Proxy)
- Applications where cold starts are unacceptable

## ECS, EKS, and Fargate

### Containers — A Simple Explanation

A container packages your application and all its dependencies (libraries, runtime, config) into a single portable unit. Unlike a VM, it shares the host OS kernel — making it lighter and faster to start.

Docker is the most popular container technology. You define a `Dockerfile`, build an image, and run containers from that image.

### ECS (Elastic Container Service)

AWS's own container orchestration service. You define "Task Definitions" (what container to run, how much CPU/memory) and "Services" (how many copies to run, how to update them).

ECS manages scheduling containers across a cluster of EC2 instances.

### EKS (Elastic Kubernetes Service)

Managed Kubernetes. If your team already uses Kubernetes or needs its advanced features, EKS runs a managed control plane for you.

Kubernetes has a steeper learning curve but is more flexible and has a larger ecosystem.

### Fargate

Fargate is a "serverless" compute engine for containers. Instead of managing EC2 instances in your cluster, you simply define your container tasks and AWS runs them on invisible infrastructure.

With Fargate, you pay per task (based on CPU/memory allocated) rather than per EC2 instance.

**Choosing between them:**

| Scenario | Recommendation |
|---------|---------------|
| New containerized app, want simplicity | ECS + Fargate |
| Need fine-grained control, use existing Kubernetes configs | EKS |
| High-throughput, cost-sensitive (EC2 cheaper than Fargate at scale) | ECS + EC2 |
| Serverless everything | ECS/EKS + Fargate |

### Monolith to Microservices Evolution

A **monolith** is a single application that does everything — auth, product catalog, orders, payments — all in one codebase.

Problem: A bug in the payment module can take down the whole app. Scaling one feature (search) requires scaling the entire app.

**Microservices** split these into independent services that communicate via APIs or messages. Each service can be scaled, deployed, and updated independently.

AWS pattern for this evolution:
1. Start with EC2 monolith
2. Containerize the monolith (same code, just in Docker)
3. Split the monolith into services, run each in ECS/EKS
4. Highly independent services (event-driven) use Lambda

---

# 5. Storage Services

## S3 — Simple Storage Service

S3 is AWS's object storage service. Think of it as an infinitely scalable file system, but not exactly — it stores objects (files with metadata), not files in a traditional directory hierarchy.

### Buckets and Objects

A **Bucket** is a container for objects. Bucket names are globally unique across all AWS accounts. You create buckets in a specific region.

An **Object** is a file plus metadata. Maximum object size: 5 TB. Objects are identified by a key (think: the path).

`s3://my-company-assets/images/profile/user123.jpg`
- Bucket: `my-company-assets`
- Key: `images/profile/user123.jpg`

### Versioning

When enabled, S3 keeps every version of every object. Overwriting or deleting a file doesn't destroy the old version — it just adds a new version (or a "delete marker").

Use versioning when:
- You need to recover from accidental deletions
- Compliance requires audit trail of file changes
- You're storing code artifacts or backups

### Encryption

S3 supports several encryption options:

**SSE-S3 (Server-Side Encryption with S3-Managed Keys)**: AWS manages the keys. Simplest option. No extra cost.

**SSE-KMS (with AWS KMS)**: AWS KMS manages the keys. You control key policies, rotation, and auditing. Costs per API call to KMS.

**SSE-C (Customer-Provided Keys)**: You provide the key on every request. AWS uses it to encrypt/decrypt and never stores it.

**Client-Side Encryption**: You encrypt before uploading, decrypt after downloading. AWS never sees plaintext.

For most use cases: use SSE-KMS (auditable and controllable).

### Lifecycle Rules

Lifecycle rules automate moving objects between storage classes or deleting them.

Example rule:
- After 30 days → move to S3 Infrequent Access (cheaper, but slower retrieval)
- After 90 days → move to S3 Glacier (very cheap archival, retrieval takes minutes to hours)
- After 365 days → delete

This dramatically reduces storage costs for data that's accessed rarely over time.

### S3 Storage Classes

| Class | Use Case | Retrieval Time | Cost |
|-------|---------|---------------|------|
| S3 Standard | Frequently accessed | Immediate | Highest |
| S3-IA (Infrequent Access) | Accessed monthly | Immediate | Medium |
| S3 One Zone-IA | Non-critical, infrequent | Immediate | Lower |
| S3 Glacier Instant | Archives, accessed occasionally | Immediate | Low |
| S3 Glacier Flexible | Archival | Minutes to hours | Very Low |
| S3 Glacier Deep Archive | Long-term archival | 12 hours | Lowest |

## EBS vs EFS vs FSx

### EBS (Elastic Block Store)
- Block storage, like a hard drive
- Attached to ONE EC2 instance at a time (multi-attach possible but complex)
- Single AZ — must be in same AZ as EC2
- Good for: Databases, OS disks, applications needing low-latency disk I/O

### EFS (Elastic File System)
- Managed NFS (Network File System)
- Can be mounted by MANY EC2 instances simultaneously, across multiple AZs
- Automatically grows and shrinks as you add/remove files
- Good for: Shared file storage, web serving, content management

### FSx
- Managed file systems for specific use cases:
  - **FSx for Windows**: Windows native file system (SMB protocol)
  - **FSx for Lustre**: High-performance computing, ML training, big data

### Data Durability vs Availability

**Durability**: Will my data survive hardware failures? S3 is designed for 99.999999999% (11 nines) durability — storing 10 million objects, you'd expect to lose one in 10,000 years.

**Availability**: Can I access my data right now? S3 Standard offers 99.99% availability. That's about 52 minutes of downtime per year.

---

# 6. Databases

## RDS — Relational Database Service

RDS is a managed relational database service. AWS handles patching, backups, failover, and replication. You just manage schema and queries.

Supported engines: MySQL, PostgreSQL, MariaDB, Oracle, SQL Server, and Aurora (AWS's own).

### Multi-AZ

Multi-AZ is about **high availability**, not read performance.

AWS synchronously replicates your data to a standby instance in a different AZ. If the primary fails (hardware issue, AZ outage), AWS automatically fails over to the standby in 60–120 seconds. No data loss because replication is synchronous.

The standby is not accessible for reads — it's purely a failover target.

```
Primary (AZ-a) ──sync replication──> Standby (AZ-b)
     |                                      |
     v                                      v
Your app connects to endpoint        Becomes primary on failover
```

### Read Replicas

Read Replicas are about **read scalability**, not high availability.

AWS asynchronously replicates from primary to replicas. Replicas can be read from — useful for read-heavy workloads (reporting, analytics).

Key differences from Multi-AZ:
- Asynchronous replication means a small lag (replica might be seconds behind)
- Replicas can be in different Regions (useful for global read access)
- You can have up to 5 read replicas (MySQL/PostgreSQL), more for Aurora
- Replicas can be promoted to become standalone primary databases (for DR scenarios)

## Aurora

Aurora is AWS's own cloud-native relational database, compatible with MySQL and PostgreSQL clients. It's 2–5x faster than standard MySQL/PostgreSQL.

### Aurora Architecture

Unlike traditional databases where storage and compute are tightly coupled, Aurora separates them:

- **Storage**: A distributed, fault-tolerant, self-healing storage system. Data is automatically replicated 6 ways across 3 AZs. You don't manage storage — it grows automatically up to 128 TB.
- **Compute**: Aurora instances (writer and readers) that query the shared storage.

This means:
- Adding a read replica takes under 10 minutes (no data copying needed)
- Failover is faster than standard RDS (typically under 30 seconds)
- No data loss on failure (6 copies across 3 AZs)

### Aurora Serverless

Aurora Serverless v2 automatically scales database capacity (ACUs — Aurora Capacity Units) up and down based on actual demand. You pay per ACU-second, not for a fixed instance size.

Good for: Variable workloads, development environments, applications with unpredictable traffic.

## DynamoDB

DynamoDB is AWS's fully managed NoSQL key-value and document database. It's designed for single-digit millisecond performance at any scale.

Unlike relational databases, DynamoDB has no fixed schema — each item can have different attributes.

### Data Modeling

The most important (and challenging) part of DynamoDB. You must model your data around your access patterns — DynamoDB is optimized for specific queries, not arbitrary ones.

**Primary Key**: Every table has a primary key. Two types:

1. **Simple primary key**: Just a Partition Key. Each item uniquely identified by one attribute.
2. **Composite primary key**: Partition Key + Sort Key. Allows multiple items with the same partition key, sorted by the sort key.

### Partition Keys

DynamoDB distributes data across many physical servers based on the partition key. Items with the same partition key are stored together.

**Hot partition problem**: If many requests hit the same partition key (e.g., a user ID with tons of traffic), that partition becomes a bottleneck. Design partition keys to distribute traffic evenly.

Good partition keys: user ID (if traffic is spread across users), order ID, UUID

Bad partition keys: date (all today's data in one partition), status (only 3 values, so 3 partitions), a single fixed value

### Capacity Modes

**On-Demand Mode**: DynamoDB automatically scales to handle any request volume. You pay per request. Good for unpredictable workloads or new applications.

**Provisioned Mode**: You specify Read Capacity Units (RCUs) and Write Capacity Units (WCUs). One RCU = one strongly consistent read of an item up to 4KB per second. One WCU = one write of an item up to 1KB per second.

Can use Auto Scaling to automatically adjust provisioned capacity.

### Global Tables

DynamoDB Global Tables replicate your table across multiple AWS Regions. Any Region can handle reads and writes. DynamoDB handles multi-region conflict resolution (last-writer-wins).

Use for: Global applications needing low-latency access worldwide, disaster recovery across regions.

## Caching with ElastiCache

**ElastiCache** is a managed in-memory caching service. Supports Redis and Memcached.

**Why cache?** Database queries take milliseconds. Cache lookups take microseconds. For frequently accessed data that doesn't change often, caching dramatically improves performance and reduces database load.

### Cache Strategies

**Cache-Aside (Lazy Loading)**: Application checks cache first. On cache miss, reads from database and writes to cache.

```
Request → Check Cache → HIT: return cached data
                       MISS: query DB → store in cache → return data
```

**Write-Through**: Write to cache and database simultaneously on every write. Cache is always up-to-date.

**TTL (Time to Live)**: Every cached item has an expiry. After TTL, item is deleted from cache and next request fetches fresh data.

### When to Cache

- Data that's expensive to compute (complex queries, aggregations)
- Data that's read frequently but changes infrequently (product catalog, user profiles)
- Session data
- Results of external API calls

**Don't cache**: Highly personalized data, financial transaction data that must be always accurate, data that changes every second.

## Database Decision Guide

```
Need relational/SQL schema? 
├── Yes → Need massive scale or global? 
│   ├── Yes → Aurora
│   └── No → RDS (MySQL or PostgreSQL)
└── No → Need key-value or document store?
    ├── Single-digit ms at any scale → DynamoDB
    └── Need Redis data structures → ElastiCache Redis
```

---

# 7. Messaging & Event-Driven Systems

## Why Messaging?

Imagine an e-commerce system where placing an order triggers: sending a confirmation email, updating inventory, notifying the warehouse, and charging the credit card. If you do all this synchronously in one request, the user waits 5+ seconds. Worse, if the email service is slow, your order fails.

**Messaging** decouples these operations. The order service writes a message to a queue and returns immediately. Other services consume messages independently.

Benefits:
- **Decoupling**: Services don't need to know about each other
- **Resilience**: If one service is down, messages queue up and are processed when it recovers
- **Scalability**: Scale consumers independently based on queue depth

## SQS — Simple Queue Service

SQS is a managed message queue. Producers send messages; consumers poll for and process them.

### Standard vs FIFO

**Standard Queue**:
- Virtually unlimited throughput (thousands of messages/second)
- At-least-once delivery (rarely, messages may be delivered twice)
- Best-effort ordering (order not guaranteed)
- Good for: Most use cases where you can handle duplicates

**FIFO Queue**:
- Guaranteed exactly-once processing
- Guaranteed in-order delivery within a message group
- Limited to 3,000 messages/second with batching
- Good for: Financial transactions, order processing where sequence matters

### Visibility Timeout

When a consumer reads a message from SQS, the message is hidden (not deleted) for a period called the **Visibility Timeout** (default: 30 seconds).

Why? Because the consumer might crash mid-processing. If processing is successful, the consumer deletes the message. If processing fails or the consumer crashes, the timeout expires, the message reappears, and another consumer can try again.

Set the visibility timeout to be longer than your maximum processing time.

### Dead Letter Queue (DLQ)

A DLQ is a separate SQS queue that receives messages that failed to process after N attempts (configurable).

Instead of losing problematic messages or retrying endlessly, they go to the DLQ for investigation. You can inspect them, fix the bug, and replay them.

**Always set up a DLQ in production.**

## SNS — Simple Notification Service

SNS is a publish-subscribe (pub-sub) service. A **publisher** sends a message to an SNS **Topic**. All **subscribers** to that topic receive the message simultaneously.

Unlike SQS (point-to-point), SNS is fan-out — one message reaches many consumers.

Subscriber types: SQS queues, Lambda functions, HTTP endpoints, email, SMS, mobile push.

**Common pattern — SNS → SQS fan-out:**
```
Order Placed Event
        |
        v
    SNS Topic
   /    |    \
SQS    SQS   SQS
(email) (inventory) (warehouse)
```
This ensures each downstream system has its own queue, can fail independently, and processes at its own pace.

## EventBridge

EventBridge is a serverless event bus. It's more powerful than SNS+SQS for event-driven architectures.

Key features:
- **Rules**: Route events based on content (e.g., only forward "OrderFailed" events to the alert Lambda)
- **Schema Registry**: Discover and define the structure of events
- **Cross-account and cross-service**: Events from AWS services, custom apps, and SaaS partners
- **Scheduling**: Run targets on a cron schedule

EventBridge is the preferred choice for building event-driven architectures in AWS because of its routing flexibility.

## Real Use Cases

**Order Processing:**
1. User places order → API writes to SQS Order Queue
2. Order Processor Lambda consumes from queue, validates order
3. On success, publishes "OrderConfirmed" to SNS
4. SNS fans out to: Payment Queue, Inventory Queue, Notification Queue
5. Each downstream service processes independently

**Background Jobs:**
- Image upload triggers S3 event → EventBridge → Lambda (resize images)
- Daily report generation: EventBridge cron → Lambda → generate report → save to S3

---

# 8. Load Balancing & Scalability

## Why Load Balancers?

A single server can only handle so many requests. A **Load Balancer** distributes incoming traffic across multiple servers, ensuring no single server is overwhelmed. It also provides a single DNS name for your application, regardless of how many instances are behind it.

Additionally, load balancers perform **health checks** — if a server fails, the load balancer stops sending traffic to it.

## ALB vs NLB

AWS offers two main types:

**Application Load Balancer (ALB)** — Layer 7 (HTTP/HTTPS):
- Routes based on URL path (`/api/*` to backend, `/static/*` to S3)
- Routes based on host headers (API v1 vs API v2)
- Routes based on HTTP headers, query strings
- Supports WebSockets
- Native integration with ECS, Lambda
- Good for: Web applications, REST APIs, microservices

**Network Load Balancer (NLB)** — Layer 4 (TCP/UDP):
- Routes based on IP and port, not HTTP content
- Extremely high performance (handles millions of requests/second, ultra-low latency)
- Preserves client source IP
- Static IP per AZ (useful for IP whitelisting)
- Good for: Gaming, IoT, financial systems requiring ultra-low latency, TCP protocols

### Health Checks

Both ALB and NLB continuously check if targets are healthy. If a target fails health checks, the load balancer removes it from rotation.

ALB health check: HTTP GET to a path (e.g., `/health`). Expects a 2xx response.

Configure:
- Interval: How often to check (every 30 seconds)
- Threshold: How many failures before marking unhealthy
- Timeout: How long to wait for a response

**Always implement a `/health` endpoint** that verifies your app's dependencies (database connection, etc.).

## Auto Scaling Groups with Load Balancers

ASGs and load balancers work together:

1. ASG launches new EC2 instances
2. ALB automatically detects and adds them to the target group
3. ALB starts sending traffic to healthy new instances
4. On scale-in, ALB drains connections from instances being terminated (connection draining)

### Zero-Downtime Deployments

**Rolling deployment**: Update instances one at a time. Old and new versions run simultaneously briefly.

**Blue-Green deployment**: 
1. Run the current version (Blue) behind your load balancer
2. Deploy new version to a separate set of instances (Green)
3. Test Green
4. Switch load balancer to point to Green
5. Blue stands by for quick rollback
6. After confidence, terminate Blue

ALB makes blue-green easy with **weighted target groups** — send 10% of traffic to Green, 90% to Blue, monitor, then shift gradually to 100% Green.

---

# 9. CDN & DNS

## Route 53

Route 53 is AWS's DNS service. DNS translates human-readable domain names (`api.mycompany.com`) to IP addresses.

### DNS Basics

When you type `myapp.com` in a browser:
1. Browser checks its local cache
2. Asks your ISP's DNS resolver
3. Resolver asks the root DNS servers
4. Root servers point to the `.com` TLD name servers
5. TLD name servers point to Route 53 name servers
6. Route 53 returns the IP address
7. Browser connects to that IP

This whole process takes milliseconds. Route 53 serves over 100 billion DNS queries per day.

### Route 53 Routing Policies

**Simple**: One record, one IP. Basic.

**Weighted**: Distribute traffic across multiple resources by weight. 70% to us-east-1, 30% to us-west-2. Great for blue-green deployments and A/B testing.

**Failover**: Primary endpoint normally receives traffic. If health check fails, automatically routes to secondary.

**Latency-Based**: Route each user to the AWS Region with lowest latency for them. User in Tokyo gets routed to ap-northeast-1; user in London gets eu-west-1.

**Geolocation**: Route based on user's physical location. Users in Germany get routed to the EU endpoint (for data residency compliance).

**Geoproximity**: Like geolocation, but you can bias toward specific regions.

**Multi-Value Answer**: Returns multiple healthy records; client picks one. Simple load balancing at DNS level.

## CloudFront

CloudFront is AWS's Content Delivery Network (CDN). It caches copies of your content at 400+ Edge Locations worldwide.

### How CDN Works

Without CDN: Every user, regardless of location, fetches content from your origin server (e.g., an EC2 in us-east-1). Users in Australia experience high latency.

With CloudFront:
1. First user in Sydney requests your website
2. CloudFront checks its Sydney edge cache — miss
3. CloudFront fetches from origin (us-east-1)
4. Stores a copy in Sydney edge cache
5. Returns content to user
6. Next user in Sydney: edge cache hit, served instantly

### Caching Behavior

You configure cache behavior per path:
- `/images/*`: Cache for 7 days (images rarely change)
- `/api/*`: Don't cache (dynamic data)
- `/static/*`: Cache for 30 days, include versioning in filename for cache busting

**Cache Invalidation**: When you update content, you can invalidate specific paths in CloudFront. Costs $0.005 per path. Alternatively, use versioned filenames (`app-v1.2.3.js`) and never invalidate.

**CloudFront + S3**: A very common pattern — S3 stores static assets, CloudFront serves them globally. S3 bucket stays private; only CloudFront can access it (using Origin Access Control).

---

# 10. Observability & Monitoring

## Why Observability?

You can't fix what you can't see. When production breaks at 3 AM, you need:
- **Metrics**: Numbers over time (CPU%, requests/second, error rate)
- **Logs**: Detailed text records of what happened and when
- **Traces**: The path a request took through your distributed system

## CloudWatch

CloudWatch is AWS's primary monitoring and observability service.

### Metrics

Every AWS service publishes metrics to CloudWatch automatically:
- EC2: CPUUtilization, NetworkIn/Out, DiskReadOps
- Lambda: Invocations, Duration, Errors, Throttles
- RDS: DatabaseConnections, FreeStorageSpace, ReadLatency

You can also publish **custom metrics** from your application:
```
// Publish a metric using the AWS SDK
aws cloudwatch put-metric-data --namespace "MyApp" \
  --metric-name "OrdersProcessed" --value 47 --unit Count
```

### Logs

CloudWatch Logs stores log data from your applications and AWS services. Lambda logs go here automatically. EC2 requires the CloudWatch agent.

**Log Groups**: Collection of log streams for one application/service.
**Log Streams**: Sequence of log events from one instance/function.

**CloudWatch Logs Insights**: A query language to search and analyze logs.
```sql
fields @timestamp, @message
| filter @message like /ERROR/
| sort @timestamp desc
| limit 20
```

### Alarms

Alarms watch a metric and take action when it crosses a threshold.

Example: "If Lambda error rate > 1% for 5 consecutive minutes → send email to engineering team and trigger PagerDuty."

Alarm states: OK, ALARM, INSUFFICIENT_DATA

**Composite Alarms**: Combine multiple alarms with AND/OR logic.

## X-Ray

X-Ray is AWS's distributed tracing service. When a user request flows through multiple services (API Gateway → Lambda → DynamoDB → SQS → another Lambda), X-Ray traces the entire journey.

X-Ray shows you:
- End-to-end latency breakdown (which service is slow?)
- Error locations (which service failed?)
- Service map (visual graph of how services connect)

To use X-Ray, you instrument your code with the X-Ray SDK. For Lambda, simply enable Active Tracing in the function configuration.

## Debugging Production Issues

A systematic approach:
1. Check **CloudWatch Alarms** — what's in ALARM state?
2. Look at **metrics** — what spiked? (CPU, error rate, latency, queue depth)
3. Check **logs** — what error messages appeared?
4. Use **X-Ray** — where in the request chain did the failure occur?
5. Check **AWS Service Health Dashboard** — is AWS having an incident?

---

# 11. Infrastructure as Code

## Why Manual Setup Fails

Clicking through the AWS console to set up infrastructure is fine for learning, but terrible for production because:
- It's not reproducible: Can you recreate your exact setup from scratch?
- It's not auditable: Who changed what and when?
- It's error-prone: Manual steps get missed
- It doesn't scale: Setting up 10 environments by hand is painful

**Infrastructure as Code (IaC)** means defining your infrastructure in code files that are version-controlled, reviewed, and automatically applied.

## CloudFormation

CloudFormation is AWS's native IaC service. You write templates (JSON or YAML) that describe your desired infrastructure, and CloudFormation creates, updates, or deletes resources accordingly.

### Templates

A CloudFormation template has sections:

```yaml
AWSTemplateFormatVersion: '2010-09-09'
Description: Simple web application stack

Parameters:
  EnvironmentName:
    Type: String
    Default: dev

Resources:
  MyBucket:
    Type: AWS::S3::Bucket
    Properties:
      BucketName: !Sub "${EnvironmentName}-my-app-assets"
      VersioningConfiguration:
        Status: Enabled

  MyLambda:
    Type: AWS::Lambda::Function
    Properties:
      FunctionName: !Sub "${EnvironmentName}-my-function"
      Runtime: python3.11
      Handler: index.handler
      Role: !GetAtt LambdaRole.Arn
      Code:
        ZipFile: |
          def handler(event, context):
              return {"statusCode": 200}

Outputs:
  BucketName:
    Value: !Ref MyBucket
```

### Stacks

A **Stack** is a deployed instance of a CloudFormation template. You can create multiple stacks from the same template (dev, staging, production).

CloudFormation handles **drift detection** (did someone manually change something?), **rollback** (if an update fails, revert), and **change sets** (preview what will change before applying).

## CDK — Cloud Development Kit

CDK lets you define AWS infrastructure using real programming languages: TypeScript, Python, Java, C#, Go.

### How It Works

You write CDK code → CDK synthesizes it into CloudFormation templates → CloudFormation deploys the infrastructure.

```typescript
import * as cdk from 'aws-cdk-lib';
import * as s3 from 'aws-cdk-lib/aws-s3';
import * as lambda from 'aws-cdk-lib/aws-lambda';

class MyStack extends cdk.Stack {
  constructor(scope: cdk.App, id: string) {
    super(scope, id);

    const bucket = new s3.Bucket(this, 'MyBucket', {
      versioned: true,
      removalPolicy: cdk.RemovalPolicy.DESTROY
    });

    const fn = new lambda.Function(this, 'MyFunction', {
      runtime: lambda.Runtime.PYTHON_3_11,
      handler: 'index.handler',
      code: lambda.Code.fromAsset('lambda'),
    });

    bucket.grantRead(fn); // CDK automatically creates the right IAM policy
  }
}
```

### CDK Benefits Over Raw CloudFormation

- **Abstraction**: `bucket.grantRead(fn)` is much simpler than writing an IAM policy manually
- **Logic**: Use loops, conditionals, and functions — hard in YAML
- **Type safety**: IDE catches errors before deployment
- **Sharing**: Package and share infrastructure patterns as libraries
- **Constructs**: Pre-built patterns (e.g., an entire ALB + ECS + RDS setup in a few lines)

---

# 12. Security Deep Dive

## KMS — Key Management Service

KMS is AWS's managed encryption key service. It creates, stores, and manages cryptographic keys. The key material never leaves KMS (you can't extract it).

### Envelope Encryption

Encrypting large amounts of data directly with KMS would be slow and expensive (you'd have to send all data to KMS for each operation).

Instead, AWS uses **envelope encryption**:

1. KMS generates a **Data Key** (a symmetric key)
2. Your application encrypts your data with the Data Key (locally, fast)
3. KMS encrypts the Data Key itself using your KMS key (CMK)
4. Store the encrypted data AND the encrypted Data Key together
5. To decrypt: send encrypted Data Key to KMS → get plaintext Data Key → decrypt your data locally

This way, only the small Data Key goes to KMS, not your entire dataset.

## Secrets Manager vs Parameter Store

### AWS Secrets Manager

Designed specifically for secrets (database passwords, API keys, OAuth tokens).

Features:
- Automatic secret rotation (can rotate RDS passwords automatically)
- Versioning of secrets
- Cross-account sharing
- Integration with RDS, Redshift, DocumentDB for automatic rotation

Cost: $0.40/secret/month + API call costs

### SSM Parameter Store

More general-purpose configuration store. Can store strings, string lists, and SecureStrings (encrypted with KMS).

Features:
- Hierarchical naming: `/myapp/prod/database/password`
- Free tier available (standard parameters)
- Parameter histories
- No auto-rotation

Cost: Free for standard parameters. $0.05/advanced parameter/month.

**When to use what:**
- Secrets with rotation (DB passwords, API keys) → Secrets Manager
- App configuration, feature flags, non-sensitive config → Parameter Store (free tier)

## WAF and Shield

### AWS WAF (Web Application Firewall)

WAF sits in front of ALB, CloudFront, or API Gateway and inspects HTTP requests. It can block:
- SQL injection attempts
- Cross-site scripting (XSS)
- Specific IP addresses or countries
- Requests with specific headers or user agents
- Known bad bots

You define **Web ACL Rules**. AWS provides **Managed Rule Groups** (pre-configured rules maintained by AWS or security vendors).

### AWS Shield

Protects against DDoS (Distributed Denial of Service) attacks.

- **Shield Standard**: Automatically included for all AWS customers. Protects against common Layer 3/4 attacks.
- **Shield Advanced**: $3,000/month. Protects against sophisticated attacks, includes 24/7 DDoS Response Team access, and cost protection (AWS absorbs DDoS-related cost spikes).

## CloudTrail

CloudTrail records every API call made in your AWS account: who made the call, when, from what IP, what was changed.

This is your audit log. It answers: "Who deleted that S3 bucket?" "Who modified that Security Group?" "What changed before the outage?"

Enable CloudTrail in all regions, with a multi-region trail sending logs to a protected S3 bucket (with S3 Object Lock to prevent tampering).

## AWS Config

Config continuously records the configuration of your AWS resources over time. It can detect when resources drift from your desired state and can automatically remediate.

Example Config rules:
- "All S3 buckets must have encryption enabled"
- "No Security Groups allow 0.0.0.0/0 inbound on port 22"
- "All RDS instances must have Multi-AZ enabled"

---

# 13. Cost Optimization

## AWS Pricing Basics

Most AWS services charge based on:
- **Usage**: Requests, data transfer, API calls
- **Provisioned resources**: EC2 instance hours, RDS instance hours
- **Storage**: GB/month for S3, EBS, RDS storage

Data transfer costs are often surprising:
- Data INTO AWS: Generally free
- Data WITHIN a Region, same AZ: Free
- Data WITHIN a Region, different AZs: Small fee ($0.01/GB)
- Data OUT to internet: $0.09/GB (first 10TB/month from us-east-1)

## Common Cost Mistakes

**Idle resources**: EC2 instances running 24/7 but only used during business hours. Use Auto Scaling or schedule stop/start.

**Data transfer costs**: Lot of data moving between AZs or out to the internet. Design to minimize cross-AZ traffic, use VPC endpoints to keep S3/DynamoDB traffic free.

**Oversized instances**: Running m5.4xlarge when m5.large would suffice. Right-size based on actual utilization (AWS Compute Optimizer helps).

**Forgotten NAT Gateway data**: NAT Gateway charges per GB processed. Use VPC endpoints for AWS services.

**S3 request costs**: Making millions of tiny S3 API calls. Batch operations where possible.

**Unattached EBS volumes**: Instance terminated but EBS volume remains. Set volumes to delete on termination.

## Reserved Instances and Savings Plans

**On-Demand**: Pay for compute by the hour with no commitment. Most expensive. Good for variable workloads.

**Reserved Instances (RI)**: Commit to a specific instance type in a specific region for 1 or 3 years. Up to 75% discount.

**Savings Plans**: More flexible commitment. Commit to a dollar amount of compute usage per hour. Discount applies automatically across instance types, sizes, OS, regions.

**Spot Instances**: Bid on unused EC2 capacity. Up to 90% discount. But AWS can reclaim with 2-minute warning. Good for: batch processing, ML training, stateless workloads.

## Cost Monitoring

**AWS Cost Explorer**: Visualize spending over time, by service, by tag. Forecast future costs. Identify savings opportunities.

**AWS Budgets**: Set budgets and get alerts when you exceed thresholds ($500 budget → email when spend reaches $400).

**Cost Allocation Tags**: Tag your resources (`Environment: production`, `Team: payments`). Use tags to break down costs by team, project, or environment.

**AWS Compute Optimizer**: Analyzes usage patterns and recommends right-sized instances.

---

# 14. Migration & Real-World Architecture

## Migration Strategies (The 7 Rs)

**Retire**: Just turn it off. Some applications are no longer needed.

**Retain**: Keep it on-premises for now. Migrate later or never.

**Rehost (Lift and Shift)**: Move the application to AWS as-is. No code changes. Fast migration. Less optimized — doesn't use cloud-native features.

**Replatform (Lift, Tinker, and Shift)**: Small optimizations. Move database to RDS (managed), but keep application code the same.

**Repurchase**: Replace with a SaaS product (e.g., replace custom CRM with Salesforce).

**Refactor/Re-architect**: Redesign the application to use cloud-native services. Most effort, biggest benefits. Break monolith into microservices, use Lambda, etc.

**Relocate**: Move to a different cloud or hyperscaler without re-architecting.

## Database Migration

**AWS Database Migration Service (DMS)**: Migrates databases to AWS with minimal downtime. Supports homogeneous (MySQL → MySQL) and heterogeneous (Oracle → Aurora PostgreSQL) migrations.

**Schema Conversion Tool (SCT)**: Converts database schemas and stored procedures from one engine to another. Handles the tricky schema conversion before DMS migration.

**Strategy for minimal downtime:**
1. Set up target database in AWS
2. Use DMS to do full initial load (copy all existing data)
3. Enable DMS Change Data Capture (CDC) — continuously replicates ongoing changes
4. At cutover time, stop writes to source, let CDC catch up, switch connection strings

## Hybrid Cloud

Some workloads stay on-premises (compliance, latency, existing investments). Hybrid cloud connects your on-premises data center to AWS.

**AWS Direct Connect**: Dedicated physical network connection from your data center to AWS. More reliable and consistent latency than VPN over internet.

**AWS VPN**: Encrypted tunnel over the public internet. Cheaper than Direct Connect. Latency varies.

**AWS Outposts**: AWS brings its hardware to your data center. Run AWS services on-premises.

## Typical AWS System Architecture

### Standard 3-Tier Web Application

```
Internet Users
      |
CloudFront (CDN)
      |
      ├──> S3 (static assets)
      |
      v
Application Load Balancer
      |
      ├──> EC2 Auto Scaling Group (app servers) in Private Subnets
      |         |
      |         ├──> ElastiCache (Redis for sessions/caching)
      |         |
      |         └──> RDS Aurora (Multi-AZ) in Isolated Subnets
      |
Route 53 (DNS)

Supporting:
- CloudWatch (monitoring + alarms)
- CloudTrail (audit logging)
- AWS WAF (on CloudFront/ALB)
- Secrets Manager (DB credentials)
- S3 + CloudWatch Logs (centralized logging)
```

### Event-Driven Serverless Architecture

```
Client
  |
API Gateway
  |
Lambda (API handlers)
  |
  ├──> DynamoDB (data store)
  |
  ├──> SQS (async task queue)
  |         |
  |         v
  |    Lambda (worker)
  |         |
  |         v
  |    S3 (output storage)
  |
  └──> SNS (notifications)
            |
            ├──> Email
            └──> Lambda (other handlers)

Supporting:
- X-Ray (distributed tracing)
- CloudWatch (metrics + alarms)
- EventBridge (event routing)
```

### Microservices Architecture

```
API Gateway
    |
    ├──> User Service (ECS/Fargate)
    |         └──> RDS PostgreSQL
    |
    ├──> Product Service (ECS/Fargate)
    |         └──> DynamoDB
    |
    ├──> Order Service (ECS/Fargate)
    |         └──> RDS Aurora
    |         └──> SQS → Payment Service
    |
    └──> Search Service (ECS/Fargate)
              └──> OpenSearch Service

Service Discovery: AWS Cloud Map
Inter-service communication: ALB or App Mesh
```

---

# Quick Reference: Key Decision Frameworks

## Compute Decision

```
Short-lived, event-driven tasks (< 15 min) → Lambda
Containerized applications → ECS/EKS + Fargate
Need full OS control, specialized hardware → EC2
Long-running background jobs (containers) → ECS/Fargate
```

## Storage Decision

```
Static files, objects, backups → S3
Block storage for EC2 → EBS (gp3)
Shared file system → EFS
Windows file system → FSx for Windows
High-performance computing → FSx for Lustre
```

## Database Decision

```
Relational, standard scale → RDS (MySQL/PostgreSQL)
Relational, high scale or global → Aurora
Key-value or document, any scale → DynamoDB
Real-time caching → ElastiCache (Redis)
Search and analytics → OpenSearch
```

## Messaging Decision

```
Simple job queue → SQS Standard
Ordered, exactly-once → SQS FIFO
Fan-out to multiple consumers → SNS
Complex event routing → EventBridge
```