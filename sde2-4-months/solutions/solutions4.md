# Month 4: Advanced System Design, Design Patterns, SOLID Principles & Interview Mastery - Solutions

## Week 17: Distributed Systems Concepts & Design Patterns Deep Dive

#### Topic 1: Consistency Models and CAP Theorem

1.  **Explain the CAP Theorem in detail with an example.**
    The CAP Theorem states that a distributed data store can only guarantee two out of three properties: Consistency, Availability, and Partition Tolerance.
    *   **Consistency (C):** All clients see the same data at the same time, regardless of which node they connect to.
    *   **Availability (A):** Every request receives a (non-error) response, without guarantee that the response contains the most recent write.
    *   **Partition Tolerance (P):** The system continues to operate despite arbitrary message loss or failure of parts of the system (network partitions).
    *   **Example (CP System - MongoDB before sharding):** Prioritizes Consistency and Partition Tolerance. If a network partition occurs, some nodes become unavailable to maintain consistency across the remaining connected nodes.
    *   **Example (AP System - Cassandra, DynamoDB):** Prioritizes Availability and Partition Tolerance. If a network partition occurs, all nodes remain available, but data inconsistencies may arise across partitions.
    *   **Example (CA System - Single RDBMS):** Prioritizes Consistency and Availability. It cannot tolerate partitions; if a partition occurs, it becomes unavailable or inconsistent.

2.  **Differentiate between strong consistency and eventual consistency. When would you choose one over the other?**
    *   **Strong Consistency:** After a write, any subsequent read operation is guaranteed to return the updated value. Examples: Linearizability (strict ordering), Sequential Consistency (preserving program order).
        *   **Choice:** Banking transactions, critical inventory systems, or any scenario where stale data is unacceptable.
    *   **Eventual Consistency:** After a write, the system will eventually propagate the updates to all nodes, but there's no guarantee that a subsequent read will return the latest value immediately.
        *   **Choice:** Social media feeds, e-commerce product catalogs (where minor delays are acceptable), highly available systems that prioritize uptime over immediate data freshness.

3.  **What is the difference between ACID properties and BASE properties?**
    *   **ACID (Atomicity, Consistency, Isolation, Durability):** Properties of traditional relational databases, ensuring reliable transaction processing.
        *   **Atomicity:** All-or-nothing transactions.
        *   **Consistency:** Transaction brings the database from one valid state to another.
        *   **Isolation:** Concurrent transactions execute without interfering with each other.
        *   **Durability:** Committed transactions persist even after system failure.
        *   **Use:** Financial systems, inventory management, where data integrity is paramount.
    *   **BASE (Basically Available, Soft state, Eventually consistent):** Properties of many NoSQL databases, prioritizing availability and flexibility.
        *   **Basically Available:** The system guarantees availability for most operations.
        *   **Soft State:** The state of the system may change over time, even without input, due to eventual consistency.
        *   **Eventually Consistent:** The system will eventually become consistent once all updates have propagated.
        *   **Use:** Web-scale applications, social media, real-time analytics, where high availability and partition tolerance are crucial.

4.  **How do distributed transactions work? What are their challenges?**
    *   **How they work:** A distributed transaction involves operations across multiple independent network hosts or databases, aiming to maintain atomicity and consistency. Often coordinated using protocols like Two-Phase Commit (2PC).
    *   **Challenges:**
        *   **Complexity:** More intricate logic to coordinate multiple participants.
        *   **Performance:** Increased latency due to network communication and coordination steps.
        *   **Failure Modes:** Participant failures (crash, network issues) can lead to blocking states (e.g., coordinator waits indefinitely), data inconsistencies, or difficult recovery.
        *   **Scalability:** Can become a bottleneck due to synchronous nature.

5.  **Explain the concept of Two-Phase Commit (2PC) and its drawbacks.**
    *   **2PC Concept:** A distributed algorithm that ensures atomicity in distributed transactions across multiple participants.
        *   **Phase 1 (Prepare):** The coordinator sends a "prepare" message to all participants. Each participant executes the transaction locally and, if successful, logs a "prepared" state and replies "yes." If it fails, it replies "no."
        *   **Phase 2 (Commit/Abort):**
            *   If all participants replied "yes," the coordinator sends a "commit" message. Participants commit their transactions and release resources, then reply "done."
            *   If any participant replied "no" or timed out, the coordinator sends an "abort" message. Participants roll back their transactions and release resources, then reply "done."
    *   **Drawbacks:**
        *   **Blocking:** If the coordinator fails *after* sending "prepare" but *before* sending "commit/abort," participants remain in a "prepared" (blocked) state, holding resources until the coordinator recovers or an external intervention.
        *   **Single Point of Failure:** The coordinator is a critical component. Its failure can halt transactions.
        *   **Performance:** High latency due to synchronous communication and multiple network round trips.
        *   **Scalability:** Limited scalability due to centralized coordination and blocking nature.

#### Topic 2: Behavioral Design Patterns & SOLID Principles Applied

1.  **Explain the Observer pattern. Provide a scenario where you would use it.**
    *   **Explanation:** The Observer pattern defines a one-to-many dependency between objects so that when one object (the subject) changes state, all its dependents (the observers) are notified and updated automatically. It promotes loose coupling between the subject and its observers.
    *   **Scenario:** A stock trading application.
        *   **Subject:** `StockExchange` (or individual `Stock` objects)
        *   **Observers:** `TradingBot`, `UserPortfolioDisplay`, `AlertService`
        *   When a stock's price changes (subject state change), all registered observers (trading bots, UI, alert services) are notified and can react accordingly (e.g., update display, trigger a trade, send an alert).

2.  **What is the Strategy pattern? How does it adhere to the Open/Closed Principle?**
    *   **Explanation:** The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. It allows the algorithm to vary independently from clients that use it.
    *   **Adherence to OCP:**
        *   **Open for Extension:** New strategies (algorithms) can be added to the system by creating new concrete strategy classes without modifying existing code. For example, a new `GooglePayPayment` strategy can be added without touching the `ShoppingCart` class.
        *   **Closed for Modification:** The client (`ShoppingCart`) depends on the `PaymentStrategy` interface (abstraction), not concrete implementations. Thus, adding new payment methods doesn't require modifying the `ShoppingCart` class, only extending the `PaymentStrategy` interface with new implementations.

3.  **Describe the Single Responsibility Principle (SRP). Give an example of a class violating SRP and how to refactor it.**
    *   **Explanation:** SRP states that a class should have only one reason to change, meaning it should have only one primary responsibility.
    *   **Violation Example:**
        ```java
        class UserSettings {
            public void changeUsername(String newUsername) { /* ... */ }
            public void changeEmail(String newEmail) { /* ... */ }
            public void saveUserSettingsToDatabase() { /* ... */ } // Database persistence
            public void sendEmailNotification(String message) { /* ... */ } // Email sending
            public void validateInput(String input) { /* ... */ } // Input validation
        }
        ```
        `UserSettings` violates SRP because it has multiple reasons to change:
        1.  Business logic for changing user settings.
        2.  Database persistence logic.
        3.  Email notification logic.
        4.  Input validation logic.
    *   **Refactoring:**
        ```java
        class UserProfileManager { // Handles user setting changes
            public void changeUsername(String newUsername) { /* ... */ }
            public void changeEmail(String newEmail) { /* ... */ }
        }

        class UserSettingsRepository { // Handles persistence
            public void save(UserSettings settings) { /* ... */ }
            public UserSettings load(String userId) { /* ... */ }
        }

        class NotificationService { // Handles notifications
            public void sendEmail(String recipient, String subject, String body) { /* ... */ }
        }

        class UserInputValidator { // Handles validation
            public boolean isValidUsername(String username) { /* ... */ }
            public boolean isValidEmail(String email) { /* ... */ }
        }
        ```
        Each new class now has a single responsibility, making the system more modular, testable, and maintainable.

4.  **How does Dependency Inversion Principle (DIP) relate to Dependency Injection frameworks like Spring?**
    *   **DIP Explanation:** The Dependency Inversion Principle states:
        1.  High-level modules should not depend on low-level modules. Both should depend on abstractions.
        2.  Abstractions should not depend on details. Details should depend on abstractions.
    *   **Relation to Spring DI:** Spring (and other DI frameworks) directly facilitate DIP. Instead of high-level components creating or directly managing their low-level dependencies, they declare their dependencies on interfaces (abstractions). Spring then "injects" the concrete implementations of these interfaces at runtime.
    *   **Example:**
        ```java
        // High-level module depends on abstraction
        class OrderService {
            private PaymentProcessor paymentProcessor; // Abstraction

            // Constructor injection: Spring provides the concrete implementation
            public OrderService(PaymentProcessor paymentProcessor) {
                this.paymentProcessor = paymentProcessor;
            }

            public void placeOrder(double amount) {
                paymentProcessor.processPayment(amount);
            }
        }

        // Abstraction
        interface PaymentProcessor {
            void processPayment(double amount);
        }

        // Low-level module (concrete implementation) depends on abstraction
        class CreditCardPaymentProcessor implements PaymentProcessor {
            @Override
            public void processPayment(double amount) { /* ... */ }
        }
        ```
        `OrderService` (high-level) doesn't depend on `CreditCardPaymentProcessor` (low-level concrete detail); it depends on `PaymentProcessor` (abstraction). Spring's DI container is responsible for wiring `OrderService` with an instance of `CreditCardPaymentProcessor`, adhering to DIP.

5.  **Explain the Liskov Substitution Principle (LSP) with an example, perhaps using geometric shapes.**
    *   **Explanation:** LSP states that objects of a superclass should be replaceable with objects of its subclasses without breaking the application. More formally, if S is a subtype of T, then objects of type T may be replaced with objects of type S without altering any of the desirable properties of that program (correctness, task performed, etc.).
    *   **Violation Example (Rectangle and Square):**
        ```java
        class Rectangle {
            protected int width;
            protected int height;

            public void setWidth(int width) { this.width = width; }
            public void setHeight(int height) { this.height = height; }
            public int getArea() { return width * height; }
        }

        class Square extends Rectangle { // A square is a rectangle, but...
            @Override
            public void setWidth(int width) {
                this.width = width;
                this.height = width; // Changes height too
            }

            @Override
            public void setHeight(int height) {
                this.width = height; // Changes width too
                this.height = height;
            }
        }

        // Client code
        public static void enforceLSP(Rectangle r) {
            r.setWidth(5);
            r.setHeight(10);
            // We expect area to be 5 * 10 = 50
            System.out.println("Area: " + r.getArea());
        }

        public static void main(String[] args) {
            Rectangle rect = new Rectangle();
            enforceLSP(rect); // Output: Area: 50

            Square sq = new Square();
            enforceLSP(sq); // Output: Area: 100 (because setHeight(10) also changed width to 10)
        }
        ```
        Here, `Square` is not a perfect substitute for `Rectangle`. When `enforceLSP` receives a `Square` object, calling `setHeight` also changes the `width`, which violates the client's expectation that `width` remains `5`. The `Square` subclass changes the inherent behavior of `Rectangle`'s setters in a way that breaks client assumptions.

6.  **When would you use the Command pattern?**
    *   **Use Cases:**
        *   **Undo/Redo Functionality:** Each action can be encapsulated as a command object, which can then be stored in a history list for easy undo/redo.
        *   **Transaction Logging/Auditing:** Commands can be logged and replayed, useful for recovery or auditing.
        *   **Queueing Requests:** Commands can be placed in a queue and executed at a later time, or by a different thread.
        *   **Parameterizing Clients with Operations:** Allowing a client to invoke different operations (commands) without knowing their concrete implementations.
        *   **GUI Applications:** Buttons or menu items can be associated with command objects, decoupling the UI from the business logic.
        *   **Macro Recording:** A sequence of commands can be recorded and replayed as a single macro.

## Week 18: Advanced Database Design, Scaling & Structural Patterns

#### Topic 1: Database Sharding, Replication, and Indexes

1.  **What is database replication? Explain Master-Slave replication and its limitations.**
    *   **Database Replication:** The process of copying and maintaining database objects (like tables) in multiple locations, synchronizing them to ensure consistency. It's used for high availability, fault tolerance, and read scalability.
    *   **Master-Slave Replication:**
        *   **Architecture:** One database server (the "master") handles all write operations (inserts, updates, deletes). One or more other servers (the "slaves" or "replicas") receive a copy of the master's data and can handle read operations. Slaves typically replicate data from the master asynchronously or synchronously.
        *   **How it works:** The master records all data modifications in a binary log (binlog in MySQL). Slaves connect to the master, read the binlog, and apply the same changes to their local copies of the database.
        *   **Benefits:**
            *   **Read Scalability:** Distribute read requests across multiple slaves.
            *   **High Availability:** If the master fails, a slave can be promoted to become the new master.
            *   **Disaster Recovery:** A replica in a different geographical location can serve as a backup.
            *   **Reporting/Analytics:** Run heavy analytical queries on slaves without impacting master performance.
        *   **Limitations:**
            *   **Write Scalability:** Writes are still confined to the single master, making it a bottleneck for write-heavy applications.
            *   **Single Point of Failure (Master):** While a slave can be promoted, the failover process might involve data loss (for asynchronous replication) and downtime.
            *   **Replication Lag:** Asynchronous replication can lead to delays in data propagation to slaves, resulting in "eventual consistency" issues (reading stale data from slaves).
            *   **Complexity:** Managing replication, failover, and promoting new masters adds operational overhead.
            *   **Data Inconsistency during Failover:** If the master fails before all changes are propagated to slaves, the newly promoted master might be missing some recent writes.

2.  **What is database sharding? Why is it used?**
    *   **Database Sharding:** A method of horizontal partitioning data across multiple independent database instances (shards). Each shard holds a subset of the total data and runs on its own server or cluster.
    *   **Why it is used:**
        *   **Scalability:** Allows a database to scale beyond the capacity of a single server (vertical scaling limits). Distributes both read and write loads across multiple machines.
        *   **Performance:** Spreading data reduces the total amount of data that any single server needs to manage, leading to faster queries and operations.
        *   **Increased Throughput:** Can handle a higher number of concurrent requests by distributing the load.
        *   **High Availability:** A failure in one shard does not necessarily impact the availability of other shards.
        *   **Cost Efficiency:** Often more cost-effective to scale out horizontally with commodity hardware than to scale up vertically with very powerful (and expensive) single servers.

3.  **Describe different sharding strategies and their pros and cons. When would you choose a hash-based vs. range-based shard key?**
    *   **Sharding Strategies:**
        *   **Hash-based Sharding:**
            *   **Method:** A hash function is applied to the sharding key (e.g., user ID), and the resulting hash value determines which shard the data belongs to. `shard_id = hash(sharding_key) % num_shards`.
            *   **Pros:** Distributes data evenly across shards, minimizing hotspots.
            *   **Cons:** Range queries become inefficient (may require querying all shards). Adding or removing shards ("rebalancing") is complex and expensive, as it changes the mapping for many data items.
            *   **Choice:** When even data distribution is critical, and point lookups (e.g., fetching a user by ID) are the most common query pattern. Not suitable for range queries (e.g., "all users registered in January").
        *   **Range-based Sharding:**
            *   **Method:** Data is partitioned based on a range of values of the sharding key (e.g., user IDs 1-1000 on shard 1, 1001-2000 on shard 2).
            *   **Pros:** Efficient for range queries (e.g., "get all users created between X and Y date" if date is the shard key). Simpler to add new shards (just define new ranges).
            *   **Cons:** Can lead to uneven data distribution and hotspots if data grows disproportionately in certain ranges (e.g., new users are always added to the latest range, making the newest shard a hotspot).
            *   **Choice:** When range queries are frequent, and data growth is relatively predictable or can be managed to avoid hotspots.
        *   **Directory-based Sharding:**
            *   **Method:** A lookup service (a "shard map" or "metadata store") maintains a mapping between sharding keys (or ranges) and their respective shards. When data needs to be accessed, the client first queries the lookup service to find the correct shard.
            *   **Pros:** Most flexible. Easy to add/remove shards and rebalance data without changing the sharding logic for the application. Allows for custom and complex sharding rules.
            *   **Cons:** Adds an extra hop for every query (querying the lookup service), introducing latency. The lookup service itself can become a single point of failure or a bottleneck if not highly available and scalable.
            *   **Choice:** For systems requiring high flexibility in sharding strategy, frequent rebalancing, or complex mapping rules.

4.  **What are the challenges of sharding a database, especially concerning data locality and schema changes?**
    *   **Challenges of Sharding:**
        *   **Data Locality (Cross-shard Joins):** If related data (e.g., orders and customers) are on different shards, joining them becomes very complex and inefficient, often requiring fetching data from multiple shards and joining in the application layer. This can lead to increased latency and network overhead.
        *   **Rebalancing:** As data grows or query patterns change, some shards might become hotspots (imbalanced load). Rebalancing data (moving data between shards) is a complex, time-consuming, and resource-intensive operation that can impact system performance and availability.
        *   **Distributed Transactions:** Ensuring ACID properties for transactions that span multiple shards is extremely difficult. Often, developers resort to eventual consistency or two-phase commit (with its drawbacks).
        *   **Schema Changes:** Applying schema migrations (e.g., adding a column) across hundreds or thousands of shards consistently and without downtime is a significant operational challenge.
        *   **Complexity:** Adds significant complexity to application logic, database management, monitoring, and backup/restore procedures.
        *   **Sharding Key Choice:** Choosing the right sharding key is crucial. A poor choice can lead to hot shards, inefficient queries, and difficulty in future scaling.
        *   **Increases Operational Overhead:** Requires more effort for deployment, maintenance, and troubleshooting.

5.  **How do you handle cross-shard transactions or joins?**
    *   **Cross-shard Transactions:**
        *   **Avoid if possible:** The best strategy is often to design the system to avoid transactions that span multiple shards by carefully choosing the sharding key (e.g., all data for a single user on one shard).
        *   **Two-Phase Commit (2PC):** Can be used, but comes with the drawbacks mentioned earlier (blocking, performance, SPoF).
        *   **Saga Pattern:** A sequence of local transactions, where each transaction updates data within a single service/shard and publishes an event to trigger the next step. If a step fails, compensating transactions are executed to undo previous steps. This provides eventual consistency.
        *   **Distributed Lock Managers:** Services acquire locks across shards to coordinate updates, but this can also lead to deadlocks and performance issues.
    *   **Cross-shard Joins:**
        *   **Denormalization:** Duplicate data across shards or within entities to avoid joins. Trade-off: increased storage, potential for inconsistencies if not managed carefully.
        *   **Application-Level Joins:** Fetch data from multiple shards into the application layer and perform the join there. This is often inefficient and latency-prone.
        *   **Materialized Views/Data Warehousing:** Create a separate, aggregated data store (e.g., a data warehouse or data lake) that combines data from all shards for complex analytical queries, leaving transactional systems optimized for high-volume operational queries.
        *   **Broadcast Tables:** For small, frequently joined tables, replicate them to all shards.
        *   **Distributed Query Engines:** Use tools like Apache Drill or Presto that can query data across disparate data sources, including multiple shards.

6.  **Explain the purpose of database indexes, how B-tree indexes work, and when to use a composite index.**
    *   **Purpose of Database Indexes:** To speed up data retrieval operations (SELECT queries) by providing quick lookup paths to data rows. Without an index, the database would have to perform a full table scan, checking every row, which is slow for large tables.
    *   **How B-tree Indexes Work:**
        *   A B-tree (Balanced Tree) is a self-balancing tree data structure that maintains sorted data and allows searches, sequential access, insertions, and deletions in logarithmic time.
        *   **Structure:**
            *   **Root Node:** The starting point of the tree.
            *   **Internal Nodes:** Contain keys and pointers to child nodes. Keys in internal nodes act as separators, guiding the search down the tree.
            *   **Leaf Nodes:** Contain the actual data pointers (or the data itself in clustered indexes) to the rows in the table. All leaf nodes are at the same depth, making search times consistent.
        *   **Search Process:** To find a value, the database starts at the root, compares the search key with keys in the current node, and follows the appropriate pointer to a child node. This process continues until the leaf node containing (or that would contain) the desired value is reached.
        *   **Efficiency:** Due to its balanced nature and large fan-out (many children per node), B-trees are highly efficient for range queries and point lookups on disk-based storage, minimizing disk I/O operations.
    *   **When to use a Composite Index:**
        *   A composite index (or multicolumn index) is an index on two or more columns of a table.
        *   **Use Cases:**
            *   **Queries with multiple conditions in `WHERE` clause:** If queries frequently filter by `col1 AND col2`, a composite index on `(col1, col2)` can be very efficient. The order of columns in the index matters; typically, the most selective column (the one that filters out the most rows) comes first.
            *   **Sorting and Grouping:** If queries often `ORDER BY col1, col2` or `GROUP BY col1, col2`, a composite index can help avoid additional sorting steps.
            *   **Covering Indexes:** If all columns required by a query (in `SELECT`, `WHERE`, `ORDER BY`) are part of the composite index, the database can retrieve all necessary data directly from the index without accessing the table data, significantly improving performance.
            *   **Prefix Optimization:** Can serve queries that only filter on the leading columns of the composite index (e.g., an index on `(col1, col2, col3)` can be used for queries on `col1`, or `col1 AND col2`).

#### Topic 2: NoSQL Databases, Data Modeling & Structural Patterns Applied

1.  **When would you choose a NoSQL database over a relational database? Provide specific use cases for each NoSQL category.**
    *   **Choose NoSQL over Relational when:**
        *   **High Scalability:** Need to scale horizontally to handle massive amounts of data or high traffic.
        *   **Flexible Schema:** Data model is dynamic, unstructured, or frequently changing.
        *   **High Availability/Partition Tolerance:** Prioritize continuous operation even during network failures.
        *   **Specific Data Models:** When the data naturally fits a key-value, document, column, or graph structure better than a rigid relational table.
        *   **High Throughput for Specific Operations:** Optimized for specific access patterns (e.g., very fast writes, specific read patterns).
    *   **Use Cases for Each NoSQL Category:**
        *   **Key-Value Stores (e.g., Redis, DynamoDB):**
            *   **Characteristics:** Simple schema, data stored as key-value pairs, extremely fast reads/writes by key.
            *   **Use Cases:** Caching (session management, page caching), real-time leaderboards, shopping cart data, simple lookup data.
        *   **Document Databases (e.g., MongoDB, Couchbase):**
            *   **Characteristics:** Data stored as semi-structured documents (e.g., JSON/BSON), flexible schema, rich query capabilities, good for hierarchical data.
            *   **Use Cases:** Content management systems, user profiles, e-commerce product catalogs (with varying attributes), blogging platforms, mobile applications.
        *   **Column-Family Stores (e.g., Cassandra, HBase):**
            *   **Characteristics:** Data stored in rows with flexible columns, optimized for high write throughput and specific read patterns over large datasets, excellent for time-series data.
            *   **Use Cases:** IoT sensor data, time-series data, operational logging, real-time analytics, event sourcing, social media activity feeds.
        *   **Graph Databases (e.g., Neo4j, Amazon Neptune):**
            *   **Characteristics:** Data stored as nodes (entities) and edges (relationships), optimized for traversing complex relationships.
            *   **Use Cases:** Social networks (friends-of-friends queries), recommendation engines, fraud detection, master data management, network and IT operations.

2.  **Differentiate between Document, Key-Value, and Column-Family databases, focusing on their data model and typical query patterns.**
    *   **Key-Value Databases:**
        *   **Data Model:** Simplest. Data is stored as an opaque value associated with a unique key. Values can be strings, blobs, JSON objects, etc. The database doesn't typically understand the internal structure of the value.
        *   **Query Patterns:** Primarily direct lookups by key. No complex queries, joins, or secondary indexing (unless explicitly supported as an add-on). Extremely fast for `GET(key)` and `PUT(key, value)`.
    *   **Document Databases:**
        *   **Data Model:** Data is stored as self-contained "documents," typically in formats like JSON, BSON, or XML. Documents can have nested structures, arrays, and varying schemas. The database understands the internal structure of the documents.
        *   **Query Patterns:** Can query fields within documents, perform aggregations, and support secondary indexes on document fields. More flexible querying than key-value stores, but typically not designed for complex multi-document joins like relational databases. Good for fetching a whole "aggregate root."
    *   **Column-Family Databases:**
        *   **Data Model:** Organized into rows, each with a unique row key. Within each row, data is grouped into "column families." Each column family can contain an arbitrary number of columns, and columns can vary from row to row. Optimized for appending data and efficient retrieval of specific columns for a given row key.
        *   **Query Patterns:** Optimized for queries that retrieve all columns for a given row key, or specific columns within a column family for a range of row keys. Excellent for time-series data or when you need to store vast amounts of data where you often access only subsets of columns. Not designed for ad-hoc queries across many columns or complex joins.

3.  **Explain the concept of denormalization in NoSQL databases. What are the trade-offs?**
    *   **Denormalization in NoSQL:** The practice of storing redundant copies of data or combining related data within a single document or record in a NoSQL database. This is often done to optimize read performance and simplify query logic by avoiding the need for complex joins or multiple database lookups. Instead of normalizing data across separate "tables" (documents/collections) as in relational databases, frequently accessed related data is embedded or duplicated.
    *   **Example:** In an e-commerce application, instead of storing `Order` and `Customer` in separate documents and linking them by `customerId`, an `Order` document might embed a snapshot of the `Customer`'s name and address directly within it.
    *   **Trade-offs:**
        *   **Advantages (Pros):**
            *   **Improved Read Performance:** Queries are faster as all necessary data is often in one place, requiring fewer database operations (no joins).
            *   **Simpler Queries:** Application code is simpler because there's less need for complex aggregation or multi-step lookups.
            *   **Reduced I/O Operations:** Fewer disk reads/network requests to fetch complete entities.
            *   **Better Scaling for Reads:** Easier to scale read operations by replicating single, comprehensive documents.
        *   **Disadvantages (Cons):**
            *   **Data Redundancy and Storage Cost:** Storing the same data multiple times increases storage requirements.
            *   **Data Inconsistency Risk:** If the original source data changes (e.g., customer address), all denormalized copies must be updated. This introduces complexity in maintaining consistency and can lead to stale data if updates are not handled carefully (e.g., using eventual consistency or transaction mechanisms).
            *   **Increased Write Complexity:** Updating denormalized data requires writing to multiple locations, increasing the complexity of write operations.
            *   **Data Anomaly Potential:** If updates are not atomic or consistent across all copies, data can become inconsistent.
            *   **Schema Evolution Challenges:** Changing the structure of embedded data might require migrating all affected documents.

4.  **What are the advantages and disadvantages of using MongoDB for a social media application?**
    *   **Advantages of MongoDB for Social Media:**
        *   **Flexible Schema:** Social media data (user profiles, posts, comments, likes) is often semi-structured and evolves rapidly. MongoDB's document model allows for easily adding new fields without schema migrations.
        *   **Scalability (Horizontal):** MongoDB scales horizontally through sharding, distributing data across multiple servers to handle large user bases and high data volumes (posts, interactions).
        *   **High Read/Write Throughput:** Can handle high volumes of reads (e.g., fetching a user's feed) and writes (e.g., new posts, likes, comments).
        *   **Good for Hierarchical Data:** A single document can embed a post with its comments, or a user with their basic profile details, reducing the need for joins.
        *   **Rich Query Language:** Supports complex queries on document fields, indexing, and aggregation framework for analytics.
        *   **Geospatial Queries:** Useful for features like "friends nearby" or location-based posts.
    *   **Disadvantages of MongoDB for Social Media:**
        *   **Limited Transaction Support (Historically):** While modern MongoDB versions (4.0+) support multi-document ACID transactions, they can be more complex and have performance implications, especially across shards, compared to traditional RDBMS. This can be a challenge for features requiring strong consistency (e.g., accurate follower counts, financial transactions within the app).
        *   **Complex Relationships:** Managing deeply nested or highly interconnected data (e.g., complex friend networks, recommendations based on multiple degrees of separation) can be less intuitive or performant than with a graph database.
        *   **Denormalization Challenges:** While beneficial for reads, maintaining consistency across denormalized data (e.g., user's display name appearing in many posts) requires careful application-level logic.
        *   **Storage Overhead:** The flexible schema and BSON overhead can lead to higher storage consumption compared to tightly packed relational tables.
        *   **Lack of Strong Referential Integrity:** No foreign key constraints at the database level, relying on application logic to maintain relationships.

5.  **When would a Graph Database be the most appropriate choice, and why?**
    *   **When to Use Graph Databases:** When the relationships between data entities are as important as the entities themselves, and queries primarily involve traversing these relationships.
    *   **Why:**
        *   **Performance for Relationship Traversal:** Graph databases are fundamentally optimized for quickly traversing connections (edges) between nodes, regardless of the depth or complexity of the relationship path. In relational databases, such queries often involve expensive, self-joining operations.
        *   **Flexible Schema for Relationships:** Easily add new types of relationships or properties to existing relationships without altering the entire schema.
        *   **Intuitive Modeling:** The graph model (nodes, relationships, properties) often mirrors the real-world domain more naturally for highly connected data.
    *   **Specific Use Cases:**
        *   **Social Networks:** "Who are my friends' friends?", "Find paths between two users," "Community detection."
        *   **Recommendation Engines:** "Users who bought X also bought Y," "Recommend content based on friends' likes."
        *   **Fraud Detection:** Identifying unusual patterns of connections between accounts, transactions, and devices that indicate fraud.
        *   **Knowledge Graphs/Semantic Web:** Storing complex interconnected information and inferring new facts.
        *   **Network and IT Operations:** Modeling network topology, identifying dependencies between systems, impact analysis.
        *   **Identity and Access Management:** Managing complex user roles and permissions.

6.  **How can the Facade pattern be used to simplify interactions with a complex microservice ecosystem?**
    *   **Facade Pattern Explanation:** Provides a unified interface to a set of interfaces in a subsystem. It defines a higher-level interface that makes the subsystem easier to use.
    *   **Simplifying Microservice Interactions:**
        *   **Scenario:** Imagine an e-commerce platform with separate microservices for `Order Management`, `Inventory`, `Payment Processing`, and `Notification`. A client (e.g., a frontend application) might need to interact with all these services to perform a single logical operation like "place an order."
        *   **Without Facade:** The client would need to know the specific APIs of each microservice, the order in which to call them, and how to handle their individual responses and errors. This leads to complex client-side logic, tight coupling to the internal microservice architecture, and a bloated client codebase.
        *   **With Facade (API Gateway/Backend for Frontend):** An **API Gateway** or a **Backend for Frontend (BFF)** acts as a Facade.
            *   It provides a single, simplified API endpoint to the client for a high-level operation (e.g., `/api/v1/orders/place`).
            *   Internally, the Facade handles the orchestration:
                1.  Receives the client request.
                2.  Calls the `Order Management` service to create an order.
                3.  Calls the `Inventory` service to reserve items.
                4.  Calls the `Payment Processing` service to charge the customer.
                5.  Calls the `Notification` service to send an order confirmation.
                6.  Aggregates the responses and returns a single, coherent response to the client.
            *   **Benefits:**
                *   **Simplifies Client Code:** Clients only interact with the Facade's simpler API, unaware of the underlying microservice complexity.
                *   **Reduces Network Calls:** Client makes one call instead of many.
                *   **Decoupling:** The client is decoupled from the specific implementation details and changes in the microservices.
                *   **Centralized Error Handling/Logging:** The Facade can centralize these concerns.
                *   **Security:** Can enforce security policies at a single entry point.

## Week 19: Caching at Scale, Search, & Creational Patterns Deep Dive

#### Topic 1: Advanced Caching Strategies

1.  **What is a distributed cache? When is it necessary, and what are the challenges in implementing one?**
    *   **Distributed Cache:** A system that pools memory across multiple servers to store frequently accessed data. Instead of each application server maintaining its own local cache, a distributed cache allows all application servers to access a common, shared cache. Examples: Redis, Memcached, Apache Ignite.
    *   **When it is necessary:**
        *   **Horizontal Scaling of Applications:** When you have multiple instances of an application (e.g., a web server cluster) and need a consistent view of cached data across all instances. Local caches would lead to data inconsistencies.
        *   **High Concurrency/Traffic:** To reduce the load on the backend database or services by serving requests from faster in-memory caches.
        *   **Session Management:** Storing user session data that needs to be accessible by any server in a cluster.
        *   **Global Data Sharing:** Sharing frequently used lookup data or configuration across various microservices or applications.
        *   **Real-time Analytics/Leaderboards:** Fast access to rapidly changing but aggregated data.
    *   **Challenges in Implementing One:**
        *   **Cache Coherence/Consistency:** Ensuring that all cached copies of data are up-to-date and consistent with the source of truth (e.g., database). This is a major challenge, especially with eventual consistency models.
        *   **Network Latency:** Accessing data from a remote distributed cache still involves network calls, which are slower than local memory access.
        *   **Complexity:** Managing a distributed system adds overhead for deployment, monitoring, scaling, and fault tolerance.
        *   **Single Point of Failure (or more):** If a cache node fails, its cached data is lost. Designing for high availability (replication, sharding of the cache itself) is crucial.
        *   **Data Serialization/Deserialization:** Data needs to be serialized before being stored in the cache and deserialized upon retrieval, incurring CPU overhead.
        *   **Eviction Policies:** Managing what data to evict when the cache is full in a distributed environment requires careful coordination.
        *   **Cache Stampede/Thundering Herd:** Multiple clients simultaneously try to access data not in the cache, leading to all of them hitting the backend database, overwhelming it.
        *   **Cost:** Running and maintaining a distributed cache cluster can be expensive.

2.  **Explain the Cache-Aside pattern. How does it handle read and write operations, and what are its potential drawbacks?**
    *   **Cache-Aside Pattern (Lazy Loading):** The application is responsible for managing both the data in the cache and the data in the underlying data store (e.g., database). The cache is "aside" the application.
    *   **How it handles operations:**
        *   **Read Operation:**
            1.  Application first checks if the data exists in the cache.
            2.  If **cache hit**, it retrieves the data from the cache and returns it.
            3.  If **cache miss**, it retrieves the data from the database.
            4.  Stores this newly fetched data in the cache (for future requests).
            5.  Returns the data to the client.
        *   **Write Operation:**
            1.  Application writes (updates or inserts) the data directly to the database.
            2.  It then **invalidates or removes** the corresponding entry from the cache. (Crucially, it does NOT update the cache with the new value, as that might still lead to stale data if the write to DB fails after cache update.)
    *   **Potential Drawbacks:**
        *   **Stale Data During Updates:** There's a small window of inconsistency during a write operation. If a read occurs immediately after the database write but before the cache invalidation, a client might read stale data from the cache.
        *   **Cache Miss on First Read:** Data is only cached after the first read, meaning initial requests for fresh or rarely accessed data will incur database latency.
        *   **Read Throughput Burstiness:** Can suffer from "cache stampede" if many requests simultaneously miss the cache for the same item, all hitting the database.
        *   **Complexity of Invalidation:** Ensuring all relevant cache entries are correctly invalidated upon writes can be complex, especially with derived data or multiple related entities.
        *   **Dual Responsibility:** The application code is responsible for managing both the cache and the database interactions, which can increase complexity.

3.  **How do you handle cache invalidation in a large-scale distributed system? Discuss pros and cons of different strategies.**
    *   **Challenges:** In a distributed system, updates can originate from various sources, and multiple cache nodes might hold copies of the same data. Ensuring consistency is hard.
    *   **Strategies:**
        1.  **Time-to-Live (TTL):**
            *   **Description:** Each cached item is given an expiration time. After this time, the item is automatically removed or marked as stale.
            *   **Pros:** Simple to implement, guarantees eventual freshness, helps manage cache size.
            *   **Cons:** Data can be stale for the duration of the TTL. Not suitable for highly critical data that needs immediate consistency.
        2.  **Explicit Invalidation (Write-through/Cache-aside with Invalidate):**
            *   **Description:** When data in the primary data store (e.g., database) is updated, the application explicitly removes or invalidates the corresponding entry in the cache.
            *   **Pros:** Offers stronger consistency than TTL. Data is fresh after an update.
            *   **Cons:** Adds complexity to write operations. Requires careful coordination in distributed systems (e.g., using a message queue to broadcast invalidation messages to all cache nodes that might hold the data). If an invalidation message is lost, inconsistency can occur.
        3.  **Publish/Subscribe Model:**
            *   **Description:** When data is updated in the database, the service responsible publishes an "data updated" event to a message queue/bus. Cache nodes (or a dedicated cache invalidator service) subscribe to these events and invalidate their local copies upon receiving them.
            *   **Pros:** Decouples the data update from cache invalidation, robust for distributed systems.
            *   **Cons:** Adds complexity with a message broker. Eventual consistency model – invalidation might not be immediate due to message propagation delays.
        4.  **Version Numbers/Checksums:**
            *   **Description:** Store a version number or checksum along with the data in both the database and the cache. When reading from the cache, the application can verify if the cached version matches the latest version in the database (or a metadata store).
            *   **Pros:** Can detect stale data without immediately invalidating.
            *   **Cons:** Requires an extra database lookup (for version) on cache hits, adding latency. Doesn't proactively remove stale data.
        5.  **Write-through Caching:**
            *   **Description:** All write operations go through the cache first, and the cache writes the data to the database *synchronously*.
            *   **Pros:** Strong consistency between cache and database on writes.
            *   **Cons:** Slower write performance due to synchronous write to both. Database is still a bottleneck. Cache failure can block writes.

4.  **Differentiate between Write-through and Write-back caching, giving scenarios where each would be preferred.**
    *   **Write-through Caching:**
        *   **Mechanism:** When data is written, it is written to the cache *and* simultaneously to the underlying data store (e.g., database). The write operation completes only after both writes are successful.
        *   **Consistency:** High consistency between cache and database on writes.
        *   **Write Performance:** Slower writes, as they are bottlenecked by the database's write speed.
        *   **Data Safety:** High. Data is immediately persistent.
        *   **Scenario Preference:**
            *   **Critical data where immediate durability is essential:** Banking transactions, order processing.
            *   **When data consistency is paramount:** Especially if a cache failure should not lead to data loss.
            *   **When writes are less frequent than reads.**
    *   **Write-back Caching (Write-behind):**
        *   **Mechanism:** When data is written, it is written *only* to the cache first. The write operation completes immediately. The cache then asynchronously writes the data to the underlying data store later.
        *   **Consistency:** Eventual consistency between cache and database. There's a window where the cache has newer data than the database.
        *   **Write Performance:** Much faster writes, as they are performed at cache speed.
        *   **Data Safety:** Lower. If the cache node fails before data is written back to the database, data can be lost. Requires robust mechanisms (e.g., journaling, replication) to prevent data loss.
        *   **Scenario Preference:**
            *   **High-performance applications with frequent writes:** Social media updates, IoT sensor data, real-time analytics.
            *   **When write throughput is more important than immediate durability.**
            *   **When temporary data loss in the event of a cache crash is acceptable or mitigated by other means.**
            *   **When batching writes can improve database efficiency.**

5.  **How does a CDN improve user experience and system performance for geographically distributed users?**
    *   **CDN (Content Delivery Network):** A geographically distributed network of proxy servers and their data centers. The goal is to provide high availability and performance by distributing the service spatially relative to end-users.
    *   **Improve User Experience:**
        *   **Reduced Latency:** Content is served from a "edge server" (PoP - Point of Presence) physically closer to the user. This minimizes the geographical distance data has to travel, significantly reducing network latency and speeding up page load times.
        *   **Faster Loading of Static Assets:** Images, videos, CSS, JavaScript files are cached at edge locations, leading to quicker downloads.
        *   **Improved Responsiveness:** For dynamic content, CDNs can optimize routing to the origin server, or some modern CDNs can even run serverless functions at the edge.
        *   **Higher Availability:** If an origin server is down, the CDN might still serve cached content. Also, if a specific PoP fails, traffic can be routed to another healthy PoP.
    *   **Improve System Performance (for the origin server):**
        *   **Reduced Load on Origin Server:** A significant portion of traffic (especially for static assets) is offloaded from the origin server to the CDN's edge servers. This frees up the origin server's resources (CPU, memory, network bandwidth) to handle dynamic content and database operations more efficiently.
        *   **Reduced Bandwidth Costs:** CDNs typically have more favorable bandwidth rates than individual cloud providers or data centers, leading to cost savings.
        *   **DDoS Protection:** CDNs act as a first line of defense against DDoS attacks by absorbing large volumes of malicious traffic at their distributed network edges, preventing it from reaching the origin.
        *   **Scalability:** CDNs are designed to handle massive spikes in traffic, dynamically scaling their edge capacity to meet demand without impacting the origin.

#### Topic 2: Search, Analytics & Creational Patterns Applied

1.  **How does a search engine like Elasticsearch work at a high level? Explain the role of an inverted index.**
    *   **Elasticsearch (High-Level):**
        *   Elasticsearch is a distributed, RESTful search and analytics engine built on Apache Lucene. It stores data as JSON documents.
        *   **Indexing:** When data is ingested, it's processed (tokenized, normalized) and stored in an inverted index across multiple shards.
        *   **Sharding & Replication:** Data is distributed across shards (Lucene indexes), and these shards are replicated for high availability and read scalability.
        *   **Searching:** When a search query comes in, it's sent to all relevant shards. Each shard performs a local search, and the results are aggregated and ranked by a coordinating node before being returned to the client.
        *   **RESTful API:** Provides a simple HTTP interface for indexing, searching, and analyzing data.
    *   **Role of an Inverted Index:**
        *   **Purpose:** The core data structure that enables fast full-text searches. Instead of mapping documents to words (like a normal forward index), it maps words (terms) to the documents (or locations within documents) in which they appear.
        *   **Structure:** It's essentially a hashmap or a sorted list where:
            *   **Key:** Each unique word (term) found in the documents.
            *   **Value:** A list of documents (or document IDs) where that word appears, often including metadata like:
                *   **Term Frequency:** How many times the word appears in each document.
                *   **Position:** The exact location (offset) of the word within the document.
                *   **Payloads:** Additional information associated with the term.
        *   **How it works for search:**
            1.  When a query (e.g., "fast full-text") comes in, the search engine takes the query terms.
            2.  It looks up each term in the inverted index to quickly find all documents containing "fast" and all documents containing "full-text."
            3.  It then intersects these lists to find documents that contain *both* terms.
            4.  Using position information, it can also support phrase searches (e.g., "fast full-text" as an exact phrase).
            5.  Metadata like term frequency helps in ranking the relevance of documents.
        *   **Benefit:** Dramatically speeds up search queries because it avoids scanning every document. Instead, it directly jumps to the documents relevant to the query terms.

2.  **What are the challenges in implementing an autocomplete feature for a global search bar?**
    *   **Challenges:**
        1.  **Performance at Scale:** Autocomplete queries are extremely frequent (every keystroke). A global search bar with millions/billions of items requires very low latency responses (tens of milliseconds). This means efficient indexing and retrieval.
        2.  **Data Volume and Storage:** Storing all possible prefixes and associated suggestions for a vast dataset can require significant storage.
        3.  **Relevance and Ranking:**
            *   **Prefix Matching:** Simple prefix matching might yield too many irrelevant results.
            *   **Popularity:** Suggesting popular items first, even if not a perfect prefix match (e.g., "iph" -> "iPhone 15").
            *   **Personalization:** Tailoring suggestions based on user history, location, language, etc.
            *   **Context:** Understanding if the user is searching for a product, a user, a location, etc.
        4.  **Language and Internationalization:**
            *   Handling different languages, character sets, accents, and stemming rules.
            *   Transliteration (e.g., "Nikon" vs. "Никон").
            *   Multi-word suggestions.
        5.  **Fault Tolerance and High Availability:** The autocomplete service must be highly available as it's a critical user interface component.
        6.  **Data Freshness:** If underlying data changes frequently (e.g., new products, trending topics), the autocomplete suggestions need to be updated rapidly.
        7.  **Resource Management:** Managing CPU and memory effectively to handle the high query load.
        8.  **Typo Tolerance:** Suggesting correct terms even with minor misspellings (e.g., "elephent" -> "elephant").
        9.  **Security:** Preventing injection attacks or exposing sensitive data through suggestions.
    *   **Techniques used to address these:** Tries/Radix trees, N-grams, weighting based on popularity/recency, approximate nearest neighbor search, dedicated search indices (Elasticsearch, Solr), caching.

3.  **Explain the difference between OLTP and OLAP systems. When would you use each?**
    *   **OLTP (Online Transaction Processing) Systems:**
        *   **Purpose:** Designed for day-to-day transaction-oriented tasks. Focus on fast data insertion, updates, and deletion, and quick, short, single-row or few-row queries.
        *   **Characteristics:**
            *   **Data Model:** Highly normalized (to reduce redundancy and ensure data integrity), often with complex relational schemas.
            *   **Data Volume:** Moderate to large, but focused on individual records.
            *   **Operations:** High volume of small, atomic transactions (INSERT, UPDATE, DELETE).
            *   **Workload:** Write-heavy, concurrent.
            *   **Consistency:** ACID properties are crucial.
            *   **Response Time:** Milliseconds (for individual transactions).
        *   **Use Cases:** E-commerce transaction systems, online banking, airline reservation systems, CRM systems, inventory management.
    *   **OLAP (Online Analytical Processing) Systems:**
        *   **Purpose:** Designed for complex analytical queries on historical data to support business intelligence, reporting, and strategic decision-making. Focus on reading large amounts of data.
        *   **Characteristics:**
            *   **Data Model:** Often denormalized or star/snowflake schemas (dimensional modeling) for faster aggregation. Data is typically historical and aggregated.
            *   **Data Volume:** Massive (terabytes to petabytes), often aggregated from multiple OLTP sources.
            *   **Operations:** Low volume of complex, read-only queries involving aggregations, summaries, and calculations across large datasets.
            *   **Workload:** Read-heavy, batch processing.
            *   **Consistency:** Eventual consistency is often acceptable as data is typically loaded in batches.
            *   **Response Time:** Seconds to minutes (for complex queries).
        *   **Use Cases:** Business intelligence dashboards, data warehousing, financial forecasting, market analysis, sales trend analysis, customer behavior analysis.

4.  **When would you use a stream processing framework like Apache Kafka Streams or Flink for analytics?**
    *   **Use Cases for Stream Processing (Real-time Analytics):**
        1.  **Real-time Dashboards and Monitoring:** Displaying up-to-the-minute metrics (e.g., website traffic, sensor readings, system health) rather than waiting for batch processing.
        2.  **Fraud Detection:** Analyzing transaction streams in real time to identify suspicious patterns and trigger alerts before fraud occurs.
        3.  **Personalized Recommendations:** Generating immediate recommendations based on a user's current browsing or purchasing behavior.
        4.  **Anomaly Detection:** Identifying unusual patterns in data streams (e.g., sudden spikes in error rates, unexpected sensor readings) as they happen.
        5.  **IoT Data Processing:** Ingesting, filtering, transforming, and aggregating massive volumes of data from connected devices in real time.
        6.  **Real-time ETL (Extract, Transform, Load):** Continuously transforming data from various sources and loading it into data warehouses or other systems with minimal latency.
        7.  **Clickstream Analysis:** Analyzing user interactions on a website or app in real time to understand behavior, optimize UX, or drive marketing campaigns.
        8.  **Event-Driven Architectures:** As a core component for reacting to events in real time across microservices.
    *   **Why use them:** They provide low-latency processing, allowing for immediate insights and actions on data as it arrives, contrasting with traditional batch processing which has higher latency. They are designed for continuous, unbounded data streams.

5.  **How can the Abstract Factory pattern be used to support multiple types of database connections (e.g., MySQL, PostgreSQL) without changing client code?**
    *   **Abstract Factory Pattern:** Provides an interface for creating families of related or dependent objects without specifying their concrete classes. It decouples the client from the concrete implementations of products and factories.
    *   **Supporting Multiple Database Connections:**
        ```java
        // Abstract Products: Interfaces for database-related objects
        interface Connection {
            void connect();
        }

        interface Statement {
            void executeQuery(String query);
        }

        // Concrete Products for MySQL
        class MySqlConnection implements Connection {
            @Override public void connect() { System.out.println("Connecting to MySQL..."); }
        }
        class MySqlStatement implements Statement {
            @Override public void executeQuery(String query) { System.out.println("MySQL executing: " + query); }
        }

        // Concrete Products for PostgreSQL
        class PostgreSqlConnection implements Connection {
            @Override public void connect() { System.out.println("Connecting to PostgreSQL..."); }
        }
        class PostgreSqlStatement implements Statement {
            @Override public void executeQuery(String query) { System.out.println("PostgreSQL executing: " + query); }
        }

        // Abstract Factory: Interface for creating families of related products
        interface DatabaseFactory {
            Connection createConnection();
            Statement createStatement();
        }

        // Concrete Factory for MySQL
        class MySqlFactory implements DatabaseFactory {
            @Override
            public Connection createConnection() { return new MySqlConnection(); }
            @Override
            public Statement createStatement() { return new MySqlStatement(); }
        }

        // Concrete Factory for PostgreSQL
        class PostgreSqlFactory implements DatabaseFactory {
            @Override
            public Connection createConnection() { return new PostgreSqlConnection(); }
            @Override
            public Statement createStatement() { return new PostgreSqlStatement(); }
        }

        // Client Code: Uses the Abstract Factory, unaware of concrete implementations
        public class DatabaseClient {
            private DatabaseFactory factory;

            public DatabaseClient(DatabaseFactory factory) {
                this.factory = factory;
            }

            public void performDatabaseOperation(String query) {
                Connection conn = factory.createConnection();
                conn.connect();
                Statement stmt = factory.createStatement();
                stmt.executeQuery(query);
            }

            public static void main(String[] args) {
                // To use MySQL:
                DatabaseFactory mysqlFactory = new MySqlFactory();
                DatabaseClient mysqlClient = new DatabaseClient(mysqlFactory);
                mysqlClient.performDatabaseOperation("SELECT * FROM users");
                // Output: Connecting to MySQL... MySQL executing: SELECT * FROM users

                System.out.println("---");

                // To use PostgreSQL: (Client code remains unchanged, only the factory instance differs)
                DatabaseFactory postgresFactory = new PostgreSqlFactory();
                DatabaseClient postgresClient = new DatabaseClient(postgresFactory);
                postgresClient.performDatabaseOperation("SELECT * FROM products");
                // Output: Connecting to PostgreSQL... PostgreSQL executing: SELECT * FROM products
            }
        }
        ```
        *   **Explanation:**
            1.  We define `Connection` and `Statement` as abstract product interfaces.
            2.  We create concrete product implementations for each database type (e.g., `MySqlConnection`, `PostgreSqlConnection`).
            3.  We define `DatabaseFactory` as an abstract factory interface, specifying methods to create a family of products (e.g., `createConnection()`, `createStatement()`).
            4.  We create concrete factory implementations for each database type (e.g., `MySqlFactory`, `PostgreSqlFactory`). Each concrete factory is responsible for creating a family of related concrete products (e.g., `MySqlFactory` creates `MySqlConnection` and `MySqlStatement`).
            5.  The `DatabaseClient` (client code) depends only on the `DatabaseFactory` interface and the abstract product interfaces (`Connection`, `Statement`). It doesn't know or care whether it's dealing with MySQL or PostgreSQL specific implementations.
        *   **Benefit:** To switch between databases, only the concrete factory passed to the client needs to change, not the client code itself. This provides high flexibility and adherence to the Open/Closed Principle (open for extension with new database types, closed for modification of client code).

6.  **Discuss how the Builder pattern improves the creation of immutable objects with many parameters.**
    *   **Builder Pattern:** A creational design pattern that allows for the step-by-step construction of a complex object. It separates the construction of a complex object from its representation, so that the same construction process can create different representations.
    *   **Improving Creation of Immutable Objects with Many Parameters:**
        *   **Problem with Many Constructor Parameters:** When an object has many (e.g., 5+) optional or required parameters, using a single constructor becomes problematic:
            *   **Readability:** The constructor call is long and hard to read, making it unclear what each parameter represents (e.g., `new User("Alice", "Wonderland", 30, "alice@example.com", "123 Main St", true, false, null)`).
            *   **Maintainability:** Adding new parameters requires changing the constructor and all its call sites.
            *   **Immutability:** To make an object immutable, all fields must be set in the constructor. If there are many optional fields, you end up with a "telescoping constructor" anti-pattern (many overloaded constructors with increasing numbers of parameters).
        *   **How Builder Solves This:**
            1.  **Separate Builder Class:** A separate `Builder` class is created for the complex object.
            2.  **Chaining Methods:** The `Builder` class provides methods for setting each parameter. Each method typically returns the `Builder` itself, allowing for method chaining (e.g., `builder.withName("Alice").withEmail("alice@example.com").build()`).
            3.  **`build()` Method:** The `Builder` has a `build()` method that creates and returns the immutable instance of the complex object, usually by calling a private constructor on the object and passing all collected parameters.
            4.  **Immutability:** The main object's fields are typically `final` and set only once in its constructor, ensuring immutability after construction.
        *   **Example (Java):**
            ```java
            public final class User { // Immutable class
                private final String firstName;
                private final String lastName;
                private final int age;
                private final String email;
                private final String address;
                private final boolean isActive;
                private final String phoneNumber; // Optional

                private User(Builder builder) { // Private constructor
                    this.firstName = builder.firstName;
                    this.lastName = builder.lastName;
                    this.age = builder.age;
                    this.email = builder.email;
                    this.address = builder.address;
                    this.isActive = builder.isActive;
                    this.phoneNumber = builder.phoneNumber;
                }

                // Getters only (no setters, enforcing immutability)
                public String getFirstName() { return firstName; }
                public String getLastName() { return lastName; }
                public int getAge() { return age; }
                public String getEmail() { return email; }
                public String getAddress() { return address; }
                public boolean isActive() { return isActive; }
                public String getPhoneNumber() { return phoneNumber; }

                public static class Builder { // Static nested Builder class
                    // Required parameters
                    private final String firstName;
                    private final String lastName;
                    private final int age;

                    // Optional parameters (with default values or null)
                    private String email;
                    private String address;
                    private boolean isActive = true;
                    private String phoneNumber = null;

                    public Builder(String firstName, String lastName, int age) { // Required fields in Builder constructor
                        this.firstName = firstName;
                        this.lastName = lastName;
                        this.age = age;
                    }

                    public Builder withEmail(String email) { this.email = email; return this; }
                    public Builder withAddress(String address) { this.address = address; return this; }
                    public Builder withActiveStatus(boolean isActive) { this.isActive = isActive; return this; }
                    public Builder withPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }

                    public User build() {
                        // Optional: Add validation here before building
                        if (email == null && phoneNumber == null) {
                            throw new IllegalStateException("User must have either email or phone number.");
                        }
                        return new User(this);
                    }
                }

                public static void main(String[] args) {
                    User user1 = new User.Builder("John", "Doe", 30)
                                        .withEmail("john.doe@example.com")
                                        .withAddress("123 Main St")
                                        .build();

                    User user2 = new User.Builder("Jane", "Smith", 25)
                                        .withActiveStatus(false)
                                        .withPhoneNumber("555-1234")
                                        .build();
                }
            }
            ```
        *   **Benefits:**
            *   **Readability:** Named methods clearly indicate which parameter is being set.
            *   **Flexibility:** Allows creating objects with only the necessary parameters, without needing many overloaded constructors. Optional parameters are handled gracefully.
            *   **Immutability:** The object is constructed fully at once via a private constructor, ensuring its state doesn't change after creation.
            *   **Validation:** Validation logic can be placed in the `build()` method, ensuring the constructed object is always in a valid state.
            *   **Separation of Concerns:** Separates the concerns of object construction from the object's representation.


## Week 20: System Design Deep Dives & Advanced Topics - Interview Solutions (Java)

This document provides crisp, indexed solutions to the interview questions outlined in the Week 20 System Design curriculum, with a focus on Java implementations where applicable.

---

## Topic 1: Common System Design Problems (Applied Design & SOLID)

### Interview Questions

#### 1. Design a distributed rate limiter for an API Gateway. How would you ensure extensibility if new rate-limiting algorithms need to be added? (Focus on Strategy Pattern, OCP).

**Solution:**

A distributed rate limiter requires shared state (e.g., counts, timestamps) across instances.

*   **Core Idea:** Use a distributed data store (like Redis) for counter/timestamp storage. Each API Gateway instance checks and updates this store before allowing a request.
*   **Rate Limiting Algorithms:**
    *   **Fixed Window:** Count requests in a fixed time window.
    *   **Sliding Log:** Store timestamps of each request and count those within the window.
    *   **Sliding Window Counter:** Combine fixed windows for a smoother estimation.
    *   **Token Bucket:** A bucket holds tokens, requests consume tokens. If empty, request is denied. Tokens are added at a fixed rate.
*   **Extensibility (Strategy Pattern & OCP):**
    *   Define an `RateLimitingStrategy` interface.
    *   Implement concrete strategies for each algorithm (e.g., `FixedWindowRateLimiter`, `SlidingLogRateLimiter`, `TokenBucketRateLimiter`).
    *   The API Gateway's `RateLimiterService` would accept a `RateLimitingStrategy` instance at runtime.
    *   To add a new algorithm, create a new implementation of `RateLimitingStrategy` without modifying existing code (Open/Closed Principle).

**Java Example (Strategy Pattern):**

```java
// RateLimitingStrategy.java
public interface RateLimitingStrategy {
    boolean allowRequest(String clientId, int limit, long windowMillis);
}

// FixedWindowRateLimiter.java
public class FixedWindowRateLimiter implements RateLimitingStrategy {
    private RedisClient redisClient; // Assume Redis client is injected

    public FixedWindowRateLimiter(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public boolean allowRequest(String clientId, int limit, long windowMillis) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime / windowMillis * windowMillis; // Current window key
        String key = "rate:" + clientId + ":" + windowStart;

        // Use Redis INCR and EXPIRE to manage the counter
        long currentCount = redisClient.incr(key);
        if (currentCount == 1) {
            redisClient.expire(key, (int) (windowMillis / 1000)); // Set expiry for the window
        }
        return currentCount <= limit;
    }
}

// TokenBucketRateLimiter.java (simplified concept)
public class TokenBucketRateLimiter implements RateLimitingStrategy {
    private RedisClient redisClient;
    private long refillRateTokensPerSecond;

    public TokenBucketRateLimiter(RedisClient redisClient, long refillRateTokensPerSecond) {
        this.redisClient = redisClient;
        this.refillRateTokensPerSecond = refillRateTokensPerSecond;
    }

    @Override
    public boolean allowRequest(String clientId, int limit, long windowMillis) {
        String lastRefillTimeKey = "tokenbucket:" + clientId + ":lastRefill";
        String tokensKey = "tokenbucket:" + clientId + ":tokens";

        long currentTime = System.currentTimeMillis();
        long lastRefillTime = Long.parseLong(redisClient.getOrDefault(lastRefillTimeKey, "0"));
        long currentTokens = Long.parseLong(redisClient.getOrDefault(tokensKey, String.valueOf(limit)));

        // Refill tokens
        long timeSinceLastRefillSeconds = (currentTime - lastRefillTime) / 1000;
        long tokensToRefill = timeSinceLastRefillSeconds * refillRateTokensPerSecond;
        currentTokens = Math.min(limit, currentTokens + tokensToRefill);

        // Consume token
        if (currentTokens >= 1) {
            redisClient.set(tokensKey, String.valueOf(currentTokens - 1));
            redisClient.set(lastRefillTimeKey, String.valueOf(currentTime));
            return true;
        }
        return false;
    }
}

// RateLimiterService.java (API Gateway component)
public class RateLimiterService {
    private RateLimitingStrategy strategy;

    public RateLimiterService(RateLimitingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(RateLimitingStrategy strategy) {
        this.strategy = strategy; // Allows changing strategy at runtime if needed
    }

    public boolean isAllowed(String clientId, int limit, long windowMillis) {
        return strategy.allowRequest(clientId, limit, windowMillis);
    }

    public static void main(String[] args) {
        // In a real application, RedisClient would be managed by a framework like Spring
        RedisClient mockRedis = new MockRedisClient(); // Mock for demonstration
        RateLimiterService service = new RateLimiterService(new FixedWindowRateLimiter(mockRedis));

        System.out.println("Fixed Window Test:");
        for (int i = 0; i < 7; i++) {
            System.out.println("Request " + (i + 1) + ": " + (service.isAllowed("user1", 5, 60000) ? "Allowed" : "Denied"));
        }

        System.out.println("\nSwitching to Token Bucket Strategy:");
        service.setStrategy(new TokenBucketRateLimiter(mockRedis, 1)); // 1 token per second
        for (int i = 0; i < 7; i++) {
            System.out.println("Request " + (i + 1) + ": " + (service.isAllowed("user2", 5, 0) ? "Allowed" : "Denied"));
            try { Thread.sleep(500); } catch (InterruptedException e) {} // Simulate some delay
        }
    }
}

// MockRedisClient.java (for demonstration purposes)
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class MockRedisClient {
    private final Map<String, String> data = new ConcurrentHashMap<>();
    private final Map<String, Long> expiry = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public MockRedisClient() {
        scheduler.scheduleAtFixedRate(this::cleanExpiredKeys, 1, 1, TimeUnit.SECONDS);
    }

    private void cleanExpiredKeys() {
        long currentTime = System.currentTimeMillis();
        expiry.entrySet().removeIf(entry -> {
            if (entry.getValue() <= currentTime) {
                data.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }

    public String getOrDefault(String key, String defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    public void set(String key, String value) {
        data.put(key, value);
        expiry.remove(key); // Remove expiry if set explicitly
    }

    public long incr(String key) {
        long value = Long.parseLong(data.getOrDefault(key, "0"));
        value++;
        data.put(key, String.valueOf(value));
        return value;
    }

    public void expire(String key, int seconds) {
        expiry.put(key, System.currentTimeMillis() + (long)seconds * 1000);
    }
}
```

#### 2. What are the challenges in designing a real-time chat application? How would you structure your codebase to adhere to SRP and ISP?

**Solution:**

*   **Challenges:**
    *   **Real-time Communication:** Low-latency message delivery (WebSockets, Long Polling, Server-Sent Events).
    *   **Scalability:** Handling millions of concurrent connections and messages.
    *   **Message Persistence:** Storing messages reliably for history and offline delivery.
    *   **Presence:** Tracking user online/offline status.
    *   **Fan-out:** Efficiently delivering messages to multiple recipients in groups/channels.
    *   **Ordering & Duplication:** Ensuring messages are delivered exactly once and in order.
    *   **Error Handling:** Disconnections, message loss.
    *   **Security:** E2E encryption, authentication, authorization.
    *   **Media Support:** Handling images, videos, files.

*   **Codebase Structure (SRP & ISP):**

    *   **SRP (Single Responsibility Principle):** Each class/module should have only one reason to change.
        *   **`MessageSender`:** Responsible for sending messages over the network.
        *   **`MessageStore`:** Responsible for persisting messages to the database.
        *   **`PresenceService`:** Manages user online/offline status.
        *   **`NotificationService`:** Handles push notifications for offline users.
        *   **`ChatRoomManager`:** Manages chat room membership and message fan-out logic.
        *   **`AuthService`:** Handles user authentication and authorization.
        *   **`WebSocketHandler`:** Manages WebSocket connections and low-level communication.
        *   **`MediaUploader`:** Handles media file uploads and storage.

    *   **ISP (Interface Segregation Principle):** Clients should not be forced to depend on interfaces they do not use.

        *   Instead of a monolithic `ChatService` interface, break it down:
        *   `IMessageProducer`: `sendMessage(Message msg, String recipientId)`
        *   `IMessageConsumer`: `onMessageReceived(Message msg)`
        *   `IPresenceTracker`: `setUserOnline(String userId)`, `setUserOffline(String userId)`
        *   `IChatRoomParticipant`: `joinRoom(String roomId)`, `leaveRoom(String roomId)`
        *   `IMediaProcessor`: `processImage(byte[] imageData)`
        *   `IAuthGateway`: `authenticateUser(Credentials creds)`

    *   **Example (Java Interfaces):**

        ```java
        // Interfaces for SRP and ISP
        public interface MessageSender {
            void send(String senderId, String recipientId, String content);
            void sendToGroup(String senderId, String groupId, String content);
        }

        public interface MessagePersistence {
            void saveMessage(Message message);
            List<Message> getMessageHistory(String conversationId, int count);
        }

        public interface PresenceManager {
            void goOnline(String userId);
            void goOffline(String userId);
            boolean isOnline(String userId);
            Set<String> getOnlineUsersInRoom(String roomId);
        }

        public interface NotificationPublisher {
            void publishPushNotification(String userId, String message);
        }

        // Concrete implementations would adhere to these.
        // E.g., a service consuming `MessageSender` does not need to know about `PresenceManager` internals.
        public class WebSocketMessageSender implements MessageSender { /* ... */ }
        public class KafkaMessageProducer implements MessageSender { /* ... */ } // For async sending
        public class DatabaseMessageStore implements MessagePersistence { /* ... */ }
        public class RedisPresenceManager implements PresenceManager { /* ... */ }
        ```

#### 3. How would you handle data consistency and eventual consistency in a distributed system with multiple services writing to different databases? Consider the Saga pattern for managing distributed transactions.

**Solution:**

*   **Data Consistency in Distributed Systems:**
    *   **Strong Consistency:** All replicas see the most recent write. Achieved via 2PC, Paxos, Raft. High latency, lower availability. Suitable for critical financial transactions, user authentication data.
    *   **Eventual Consistency:** Replicas eventually converge, but temporary inconsistencies are possible. High availability, lower latency. Suitable for chat messages, social media feeds, recommendations.

*   **Multiple Services, Different Databases:**
    *   **Challenge:** The ACID properties of a single database transaction are lost when operations span multiple services and their respective databases.
    *   **Solution: Saga Pattern:** A saga is a sequence of local transactions, where each local transaction updates its own database and publishes an event to trigger the next local transaction in the saga. If a local transaction fails, the saga executes compensating transactions to undo the preceding transactions.

*   **Saga Implementation Approaches:**
    *   **Choreography:** Each service produces events that trigger other services.
        *   **Pros:** Loose coupling, simpler for small sagas.
        *   **Cons:** Harder to track overall saga state, potential for cyclic dependencies, debugging can be complex.
    *   **Orchestration:** A dedicated "saga orchestrator" service manages the entire transaction flow, telling each participant service what to do.
        *   **Pros:** Clear separation of concerns, easier to manage complex sagas, easier to implement compensating transactions.
        *   **Cons:** Centralized orchestrator can be a single point of failure (mitigate with distributed orchestrators/workflow engines), potential for coupling between orchestrator and participants.

*   **Example (Online Order Saga - Orchestration):**

    1.  **`OrderService`** receives `CreateOrder` request.
    2.  `OrderService` creates `Order` in its DB with "PENDING" status.
    3.  `OrderService` sends `CREATE_ORDER_SAGA` command to **`SagaOrchestrator`**.
    4.  `SagaOrchestrator` sends `ReserveStock` command to **`InventoryService`**.
    5.  `InventoryService` reserves stock, updates its DB, publishes `StockReservedEvent`.
    6.  `SagaOrchestrator` receives `StockReservedEvent`, sends `ProcessPayment` command to **`PaymentService`**.
    7.  `PaymentService` processes payment, updates its DB, publishes `PaymentProcessedEvent`.
    8.  `SagaOrchestrator` receives `PaymentProcessedEvent`, sends `ConfirmOrder` command to **`OrderService`**.
    9.  `OrderService` updates `Order` status to "CONFIRMED", publishes `OrderConfirmedEvent`.
    10. `SagaOrchestrator` completes.

*   **Compensating Transaction Example (if `PaymentService` fails):**
    1.  If `PaymentService` fails (e.g., card declined), it publishes `PaymentFailedEvent`.
    2.  `SagaOrchestrator` receives `PaymentFailedEvent`.
    3.  `SagaOrchestrator` sends `CancelStockReservation` command to **`InventoryService`**.
    4.  `InventoryService` releases reserved stock.
    5.  `SagaOrchestrator` sends `RejectOrder` command to **`OrderService`**.
    6.  `OrderService` updates `Order` status to "REJECTED".

*   **Java Implementation Considerations:**
    *   Use a message broker (Kafka, RabbitMQ) for reliable event/command delivery.
    *   Libraries like Axon Framework or Spring Sagas can help implement the Saga pattern.
    *   Ensure idempotency for local transactions to handle retries.

#### 4. Discuss the trade-offs between strong consistency and high availability for a critical banking transaction system. How would you design the API for transactions to be robust and follow the Command pattern for operations?

**Solution:**

*   **Trade-offs (CAP Theorem):**
    *   For a critical banking transaction system, **strong consistency is paramount**. You cannot tolerate a user seeing an incorrect balance or a transaction being recorded inconsistently.
    *   This means **availability might be sacrificed during network partitions** (P). While banking systems strive for very high availability, **consistency takes precedence**. In a partition, some operations might be blocked until consistency can be guaranteed, rather than allowing inconsistent states.
    *   **Strong Consistency Requirements:** ACID properties for transactions (Atomicity, Consistency, Isolation, Durability) are non-negotiable for money movement.

*   **API Design for Robust Transactions (Command Pattern):**

    The Command pattern encapsulates a request as an object, allowing parameterization of clients with different requests, queuing requests, and supporting undoable operations. This is excellent for auditing, retries, and managing complex transaction logic.

    *   **Core Idea:** Each transaction type (deposit, withdraw, transfer) is a concrete `Command` object. A `TransactionProcessor` (Invoker) executes these commands.
    *   **Benefits:**
        *   **Decoupling:** The client requesting the transaction is decoupled from the actual execution logic.
        *   **Auditing/Logging:** Commands can be easily logged before and after execution.
        *   **Queuing:** Commands can be put into a queue for asynchronous processing or throttling.
        *   **Retry Mechanism:** Failed commands can be retried automatically.
        *   **Undo/Redo:** Possible to implement compensating commands for rollback (though complex in distributed systems).
        *   **Extensibility (OCP):** New transaction types can be added by creating new `Command` implementations without changing the `TransactionProcessor`.

*   **Java Example (Command Pattern for Banking Transactions):**

    ```java
    // Command.java
    public interface TransactionCommand {
        void execute();
        void undo(); // For potential rollback, though complex in distributed systems
        String getTransactionId();
    }

    // Account.java (Receiver)
    public class Account {
        private String accountNumber;
        private double balance;

        public Account(String accountNumber, double initialBalance) {
            this.accountNumber = accountNumber;
            this.balance = initialBalance;
        }

        public String getAccountNumber() { return accountNumber; }
        public double getBalance() { return balance; }

        public void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive.");
            this.balance += amount;
            System.out.println("Deposited " + amount + " to " + accountNumber + ". New balance: " + balance);
        }

        public void withdraw(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Withdraw amount must be positive.");
            if (this.balance < amount) throw new IllegalArgumentException("Insufficient funds in " + accountNumber + ".");
            this.balance -= amount;
            System.out.println("Withdrew " + amount + " from " + accountNumber + ". New balance: " + balance);
        }
    }

    // DepositCommand.java
    public class DepositCommand implements TransactionCommand {
        private String transactionId;
        private Account account;
        private double amount;

        public DepositCommand(String transactionId, Account account, double amount) {
            this.transactionId = transactionId;
            this.account = account;
            this.amount = amount;
        }

        @Override
        public void execute() {
            System.out.println("Executing Deposit Command [" + transactionId + "]");
            account.deposit(amount);
        }

        @Override
        public void undo() {
            System.out.println("Undoing Deposit Command [" + transactionId + "]");
            account.withdraw(amount); // Simple undo, real-world much more complex
        }

        @Override
        public String getTransactionId() { return transactionId; }
    }

    // WithdrawCommand.java
    public class WithdrawCommand implements TransactionCommand {
        private String transactionId;
        private Account account;
        private double amount;

        public WithdrawCommand(String transactionId, Account account, double amount) {
            this.transactionId = transactionId;
            this.account = account;
            this.amount = amount;
        }

        @Override
        public void execute() {
            System.out.println("Executing Withdraw Command [" + transactionId + "]");
            account.withdraw(amount);
        }

        @Override
        public void undo() {
            System.out.println("Undoing Withdraw Command [" + transactionId + "]");
            account.deposit(amount); // Simple undo
        }

        @Override
        public String getTransactionId() { return transactionId; }
    }

    // TransactionProcessor.java (Invoker)
    public class TransactionProcessor {
        private List<TransactionCommand> commandHistory = new ArrayList<>();
        private ExecutorService executor = Executors.newFixedThreadPool(5); // For asynchronous processing

        public void submitTransaction(TransactionCommand command) {
            System.out.println("Submitting transaction: " + command.getTransactionId());
            executor.submit(() -> {
                try {
                    command.execute();
                    commandHistory.add(command); // Add only if successful
                    System.out.println("Transaction " + command.getTransactionId() + " completed successfully.");
                } catch (Exception e) {
                    System.err.println("Transaction " + command.getTransactionId() + " failed: " + e.getMessage());
                    // In a real system, trigger compensating transactions or alert
                }
            });
        }

        public void shutdown() {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }

        // For demonstration, not typically exposed in a public API
        public void undoLastCommand() {
            if (!commandHistory.isEmpty()) {
                TransactionCommand lastCommand = commandHistory.remove(commandHistory.size() - 1);
                lastCommand.undo();
            }
        }
    }

    // Main class to demonstrate
    public class BankingApp {
        public static void main(String[] args) {
            Account userAccount = new Account("ACC001", 1000.0);
            Account merchantAccount = new Account("ACC002", 500.0);
            TransactionProcessor processor = new TransactionProcessor();

            // Simulate transactions
            processor.submitTransaction(new DepositCommand("TXN1001", userAccount, 200.0));
            processor.submitTransaction(new WithdrawCommand("TXN1002", userAccount, 150.0));
            processor.submitTransaction(new DepositCommand("TXN1003", merchantAccount, 100.0));

            // Simulate a failed transaction
            processor.submitTransaction(new WithdrawCommand("TXN1004", userAccount, 2000.0)); // Should fail due to insufficient funds

            // Allow some time for async commands to process
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("\nFinal Balance ACC001: " + userAccount.getBalance());
            System.out.println("Final Balance ACC002: " + merchantAccount.getBalance());

            processor.shutdown();
        }
    }
    ```

---

### Practice Questions

#### 1. Design a Netflix-like streaming service. Focus on video storage, delivery (CDN), user recommendations. Discuss how the `Strategy` pattern could be used for different recommendation algorithms, and how `Observer` could update user progress.

**Solution:**

*   **Core Components:**
    *   **Client Apps:** Web, mobile, smart TVs.
    *   **API Gateway:** Routes requests, authentication.
    *   **User Service:** User profiles, authentication.
    *   **Content Management Service (CMS):** Ingests, processes (transcoding), stores video metadata.
    *   **Video Storage:** Blob storage (S3-like) for raw and transcoded video files.
    *   **Transcoding Service:** Converts raw video into multiple formats/resolutions.
    *   **CDN (Content Delivery Network):** Caches and delivers video streams globally with low latency.
    *   **Streaming Service:** Handles adaptive bitrate streaming protocols (HLS, DASH).
    *   **Recommendation Service:** Generates personalized content suggestions.
    *   **Watch History/Progress Service:** Tracks user viewing progress.
    *   **Billing Service:** Handles subscriptions.
    *   **Monitoring/Logging:** Observability for the entire system.

*   **Video Storage & Delivery:**
    *   **Ingestion:** Content creators upload high-quality source video to CMS.
    *   **Transcoding:** CMS triggers a Transcoding Service (e.g., AWS Elemental MediaConvert, FFMPEG farm) to convert video into various bitrates, resolutions, and formats (e.g., MP4, HLS, DASH). These are stored in object storage (e.g., AWS S3, Google Cloud Storage).
    *   **CDN Integration:** The streaming URLs (pointing to the transcoded segments) are served via a CDN (e.g., Akamai, Cloudflare, CloudFront). CDN edge servers cache content close to users.
    *   **Adaptive Bitrate Streaming:** Client players request an manifest file, which lists available video segments at different bitrates. The player dynamically switches bitrates based on network conditions to ensure smooth playback.

*   **User Recommendations (Strategy Pattern):**

    *   **Problem:** Recommendation algorithms evolve (collaborative filtering, content-based, matrix factorization, deep learning). The system needs to switch or combine them easily.
    *   **Strategy Pattern Application:**
        *   Define `RecommendationStrategy` interface with a `List<Video> getRecommendations(User user)` method.
        *   Implement concrete strategies: `CollaborativeFilteringStrategy`, `ContentBasedStrategy`, `HybridStrategy`.
        *   A `RecommendationEngine` (context) holds a reference to the current `RecommendationStrategy`.
        *   The engine can switch strategies at runtime or select based on user profile/context. This adheres to OCP – new algorithms can be added without modifying the `RecommendationEngine`.

    **Java Example:**

    ```java
    // RecommendationStrategy.java
    public interface RecommendationStrategy {
        List<Video> getRecommendations(User user, VideoRepository videoRepo);
    }

    // CollaborativeFilteringStrategy.java
    public class CollaborativeFilteringStrategy implements RecommendationStrategy {
        @Override
        public List<Video> getRecommendations(User user, VideoRepository videoRepo) {
            System.out.println("Applying Collaborative Filtering for user: " + user.getId());
            // Logic to find similar users and recommend videos they watched
            // For simplicity, return some dummy videos
            return List.of(new Video("V005", "Fantasy Movie"), new Video("V006", "Drama Series"));
        }
    }

    // ContentBasedStrategy.java
    public class ContentBasedStrategy implements RecommendationStrategy {
        @Override
        public List<Video> getRecommendations(User user, VideoRepository videoRepo) {
            System.out.println("Applying Content-Based Filtering for user: " + user.getId());
            // Logic to recommend videos similar to what the user liked based on genre, actors, etc.
            return List.of(new Video("V007", "Action Thriller"), new Video("V008", "Sci-Fi Documentary"));
        }
    }

    // RecommendationEngine.java (Context)
    public class RecommendationEngine {
        private RecommendationStrategy strategy;
        private VideoRepository videoRepository;

        public RecommendationEngine(RecommendationStrategy strategy, VideoRepository videoRepository) {
            this.strategy = strategy;
            this.videoRepository = videoRepository;
        }

        public void setStrategy(RecommendationStrategy strategy) {
            this.strategy = strategy;
        }

        public List<Video> getRecommendations(User user) {
            return strategy.getRecommendations(user, videoRepository);
        }
    }
    ```

*   **User Progress Updates (Observer Pattern):**

    *   **Problem:** When a user watches a video, multiple components might need to react: `WatchHistoryService` (save progress), `RecommendationService` (update user profile for future recommendations), `AnalyticsService` (track viewing metrics).
    *   **Observer Pattern Application:**
        *   The `VideoPlayerService` (or the backend component receiving player events) acts as the `Subject` (or `Observable`).
        *   Components like `WatchHistoryService`, `RecommendationService`, `AnalyticsService` register as `Observers`.
        *   When a `VideoWatchedEvent` (e.g., progress update, video finished) occurs, the `VideoPlayerService` notifies all registered observers. This promotes loose coupling; the `VideoPlayerService` doesn't need to know the specifics of how each component processes the event.

    **Java Example:**

    ```java
    // VideoWatchedEvent.java
    public class VideoWatchedEvent {
        private User user;
        private Video video;
        private long currentProgressSeconds;
        private boolean finished;

        public VideoWatchedEvent(User user, Video video, long progress, boolean finished) {
            this.user = user;
            this.video = video;
            this.currentProgressSeconds = progress;
            this.finished = finished;
        }
        // Getters
        public User getUser() { return user; }
        public Video getVideo() { return video; }
        public long getCurrentProgressSeconds() { return currentProgressSeconds; }
        public boolean isFinished() { return finished; }
    }

    // VideoProgressObserver.java
    public interface VideoProgressObserver {
        void onVideoProgressUpdate(VideoWatchedEvent event);
    }

    // WatchHistoryService.java (Concrete Observer)
    public class WatchHistoryService implements VideoProgressObserver {
        @Override
        public void onVideoProgressUpdate(VideoWatchedEvent event) {
            System.out.println("WatchHistoryService: User " + event.getUser().getId() +
                               " watched " + event.getVideo().getTitle() +
                               " to " + event.getCurrentProgressSeconds() + "s. Finished: " + event.isFinished());
            // Logic to save/update watch progress in DB
        }
    }

    // AnalyticsService.java (Concrete Observer)
    public class AnalyticsService implements VideoProgressObserver {
        @Override
        public void onVideoProgressUpdate(VideoWatchedEvent event) {
            System.out.println("AnalyticsService: Tracking viewing metrics for user " + event.getUser().getId() +
                               " on " + event.getVideo().getTitle());
            // Logic to send data to analytics platform
        }
    }

    // VideoPlayerService.java (Subject/Observable)
    public class VideoPlayerService {
        private List<VideoProgressObserver> observers = new ArrayList<>();

        public void addObserver(VideoProgressObserver observer) {
            observers.add(observer);
        }

        public void removeObserver(VideoProgressObserver observer) {
            observers.remove(observer);
        }

        public void notifyObservers(VideoWatchedEvent event) {
            for (VideoProgressObserver observer : observers) {
                observer.onVideoProgressUpdate(event);
            }
        }

        // Simulate user watching a video
        public void userWatchesVideo(User user, Video video, long progress, boolean finished) {
            VideoWatchedEvent event = new VideoWatchedEvent(user, video, progress, finished);
            notifyObservers(event);
        }
    }

    // Supporting classes (for demonstration)
    class User {
        private String id;
        public User(String id) { this.id = id; }
        public String getId() { return id; }
    }
    class Video {
        private String id;
        private String title;
        public Video(String id, String title) { this.id = id; this.title = title; }
        public String getId() { return id; }
        public String getTitle() { return title; }
    }
    class VideoRepository { /* Dummy repo */ }

    // Main demonstrating both patterns
    public class NetflixApp {
        public static void main(String[] args) {
            User currentUser = new User("user123");
            Video movie = new Video("M001", "The Great Adventure");
            Video series = new Video("S001", "Mysteries of the Deep");

            // Recommendation System
            VideoRepository videoRepo = new VideoRepository(); // Assume it has videos
            RecommendationEngine recEngine = new RecommendationEngine(new CollaborativeFilteringStrategy(), videoRepo);
            System.out.println("Recommendations for user123 (Collaborative): " + recEngine.getRecommendations(currentUser));

            recEngine.setStrategy(new ContentBasedStrategy());
            System.out.println("Recommendations for user123 (Content-Based): " + recEngine.getRecommendations(currentUser));

            System.out.println("\n--- Video Progress Updates ---");
            // Video Progress with Observer
            VideoPlayerService playerService = new VideoPlayerService();
            playerService.addObserver(new WatchHistoryService());
            playerService.addObserver(new AnalyticsService());
            // RecommendationService might also observe to update user profile weights
            playerService.addObserver(event -> System.out.println("RecommendationService: Update profile for " + event.getUser().getId() + " after watching " + event.getVideo().getTitle()));

            playerService.userWatchesVideo(currentUser, movie, 120, false); // 120 seconds progress
            playerService.userWatchesVideo(currentUser, series, 3600, true); // Finished
        }
    }
    ```

#### 2. Design an online gaming leaderboard. Consider high read/write loads and real-time updates. How would you apply `SRP` to separate concerns like score calculation, ranking, and display?

**Solution:**

*   **Requirements:**
    *   **High Write Load:** Many players submitting scores frequently.
    *   **High Read Load:** Leaderboard views by many players.
    *   **Real-time Updates:** Leaderboard should reflect changes quickly.
    *   **Ranking:** Efficiently calculate and maintain ranks.
    *   **Scalability:** Handle millions of players and scores.

*   **Architecture:**
    1.  **Client (Game):** Sends score updates to an API.
    2.  **API Gateway:** Authentication, rate limiting.
    3.  **Score Ingestion Service:** Receives raw score updates. Publishes to a message queue.
    4.  **Message Queue (Kafka/RabbitMQ):** Decouples ingestion from processing, handles bursts.
    5.  **Score Processing Service:** Consumes messages, validates scores, updates the primary score data store.
    6.  **Real-time Ranking Service:** Maintains an in-memory/in-Redis sorted set for fast queries.
    7.  **Leaderboard API/Service:** Provides endpoints for fetching top N, player's rank, scores around a player.
    8.  **WebSocket Service:** Pushes real-time leaderboard updates to connected clients.
    9.  **Database:**
        *   **Primary Score Store (e.g., PostgreSQL):** For historical scores, auditing, and fallback.
        *   **Real-time Rank Store (e.g., Redis Sorted Sets):** For current, high-performance leaderboard queries.

*   **Data Structures for Leaderboard:**
    *   **Redis Sorted Sets (ZSETs):** Ideal for leaderboards.
        *   `ZADD leaderboard_key score member` to add/update scores.
        *   `ZREVRANGE leaderboard_key 0 N` to get top N players.
        *   `ZREVRANK leaderboard_key member` to get a player's rank.
        *   `ZRANGEBYSCORE leaderboard_key (min_score (max_score` for ranges.
    *   **Why Redis:** In-memory, fast, atomic operations, built-in sorted set data type.

*   **Real-time Updates:**
    1.  Score Processing Service updates Redis ZSET.
    2.  It also publishes an "LeaderboardUpdated" event to a message broker.
    3.  WebSocket Service subscribes to "LeaderboardUpdated" events.
    4.  Upon receiving an event, WebSocket Service fetches the latest top N from Redis and pushes it to all connected clients.

*   **Applying SRP:**

    *   **`ScoreIngestionService`:**
        *   **Responsibility:** Receive raw score submission, basic validation, and enqueue it.
        *   **Reason to Change:** API contract changes, new ingestion endpoints.
        *   Example: `ScoreSubmitController.java`

    *   **`ScoreCalculationService`:**
        *   **Responsibility:** Consume raw score events, apply complex game-specific rules (e.g., bonus points, anti-cheat validation), and determine the canonical score.
        *   **Reason to Change:** Game rules for scoring change.
        *   Example: `ScoreProcessor.java`, `ScoreValidator.java`

    *   **`RankingUpdateService`:**
        *   **Responsibility:** Update the real-time ranking store (e.g., Redis Sorted Set) with the canonical scores.
        *   **Reason to Change:** Change in the ranking data store technology (e.g., from Redis to another in-memory DB) or optimization for rank updates.
        *   Example: `RedisLeaderboardUpdater.java`

    *   **`LeaderboardQueryService`:**
        *   **Responsibility:** Provide APIs for retrieving leaderboard data (top N, rank of a specific player, nearby players).
        *   **Reason to Change:** New API endpoints, different query parameters.
        *   Example: `LeaderboardController.java`

    *   **`LeaderboardDisplayService` (WebSocket Publisher):**
        *   **Responsibility:** Push real-time leaderboard updates to connected clients (e.g., via WebSockets).
        *   **Reason to Change:** Change in real-time communication protocol or display format.
        *   Example: `WebSocketLeaderboardPublisher.java`

**Java Example (Conceptual SRP separation):**

```java
// Interfaces defining responsibilities
public interface IScoreIngester {
    void submitScore(String userId, long score, String gameId);
}

public interface IScoreCalculator {
    ProcessedScore calculateFinalScore(RawScore rawScore);
}

public interface IRankingUpdater {
    void updateRank(String gameId, String userId, long finalScore);
}

public interface ILeaderboardQuery {
    List<PlayerScore> getTopPlayers(String gameId, int limit);
    PlayerRank getPlayerRank(String gameId, String userId);
}

public interface ILeaderboardPublisher {
    void publishRealtimeUpdate(String gameId, List<PlayerScore> topScores);
}


// Concrete Implementations
public class ScoreIngestionService implements IScoreIngester {
    private MessageProducer messageProducer; // e.g., KafkaProducer

    public ScoreIngestionService(MessageProducer producer) { this.messageProducer = producer; }

    @Override
    public void submitScore(String userId, long score, String gameId) {
        // Basic validation
        if (score < 0) throw new IllegalArgumentException("Score cannot be negative.");
        RawScore rawScore = new RawScore(userId, score, gameId, System.currentTimeMillis());
        // Publish to a Kafka topic for further processing
        messageProducer.send("raw_scores_topic", rawScore);
        System.out.println("Ingested raw score for " + userId + ": " + score);
    }
}

public class ScoreCalculationService implements IScoreCalculator {
    // Consumes messages from raw_scores_topic
    public ProcessedScore calculateFinalScore(RawScore rawScore) {
        // Apply complex game logic, anti-cheat, bonuses etc.
        long finalScore = rawScore.getScore(); // Simplified
        System.out.println("Calculated final score for " + rawScore.getUserId() + ": " + finalScore);
        return new ProcessedScore(rawScore.getUserId(), finalScore, rawScore.getGameId());
    }
}

public class RankingUpdateService implements IRankingUpdater {
    private RedisClient redisClient; // Using Redis ZSETs

    public RankingUpdateService(RedisClient client) { this.redisClient = client; }

    @Override
    public void updateRank(String gameId, String userId, long finalScore) {
        String leaderboardKey = "leaderboard:" + gameId;
        redisClient.zadd(leaderboardKey, finalScore, userId);
        System.out.println("Updated Redis rank for " + userId + " in " + gameId + " with score " + finalScore);
        // After updating Redis, publish an event for real-time subscribers
        // messageProducer.send("leaderboard_updates_topic", new LeaderboardUpdateEvent(gameId, userId, finalScore));
    }
}

public class LeaderboardQueryService implements ILeaderboardQuery {
    private RedisClient redisClient;

    public LeaderboardQueryService(RedisClient client) { this.redisClient = client; }

    @Override
    public List<PlayerScore> getTopPlayers(String gameId, int limit) {
        String leaderboardKey = "leaderboard:" + gameId;
        // Fetch from Redis, assuming zrevrange returns userId and score
        List<PlayerScore> topPlayers = redisClient.zrevrangeWithScores(leaderboardKey, 0, limit - 1);
        System.out.println("Fetched top " + limit + " players for " + gameId);
        return topPlayers;
    }

    @Override
    public PlayerRank getPlayerRank(String gameId, String userId) {
        String leaderboardKey = "leaderboard:" + gameId;
        long rank = redisClient.zrevrank(leaderboardKey, userId);
        long score = redisClient.zscore(leaderboardKey, userId);
        System.out.println("Fetched rank for " + userId + " in " + gameId + ": " + (rank + 1));
        return new PlayerRank(userId, score, rank + 1); // Ranks are 0-indexed in Redis
    }
}

public class WebSocketLeaderboardPublisher implements ILeaderboardPublisher {
    // In a real system, this would manage WebSocket connections and push updates
    @Override
    public void publishRealtimeUpdate(String gameId, List<PlayerScore> topScores) {
        System.out.println("Publishing real-time update for " + gameId + " leaderboard to connected clients.");
        // Logic to send WebSocket messages to subscribed clients
    }
}

// Dummy classes for demonstration
class RawScore {
    String userId; long score; String gameId; long timestamp;
    public RawScore(String userId, long score, String gameId, long timestamp) {
        this.userId = userId; this.score = score; this.gameId = gameId; this.timestamp = timestamp;
    }
    public String getUserId() { return userId; }
    public long getScore() { return score; }
    public String getGameId() { return gameId; }
}
class ProcessedScore {
    String userId; long finalScore; String gameId;
    public ProcessedScore(String userId, long finalScore, String gameId) {
        this.userId = userId; this.finalScore = finalScore; this.gameId = gameId;
    }
    public String getUserId() { return userId; }
    public long getFinalScore() { return finalScore; }
    public String getGameId() { return gameId; }
}
class PlayerScore {
    String userId; long score;
    public PlayerScore(String userId, long score) { this.userId = userId; this.score = score; }
    @Override public String toString() { return userId + ": " + score; }
}
class PlayerRank {
    String userId; long score; long rank;
    public PlayerRank(String userId, long score, long rank) { this.userId = userId; this.score = score; this.rank = rank; }
    @Override public String toString() { return userId + ": Rank " + rank + ", Score " + score; }
}

// Mock Redis and Message Producer for example
class MockRedisClient {
    private Map<String, TreeMap<Long, String>> leaderboards = new ConcurrentHashMap<>(); // score -> userId

    public void zadd(String key, long score, String member) {
        leaderboards.computeIfAbsent(key, k -> new TreeMap<>()).put(score, member);
    }

    public List<PlayerScore> zrevrangeWithScores(String key, int start, int end) {
        TreeMap<Long, String> board = leaderboards.get(key);
        if (board == null || board.isEmpty()) return Collections.emptyList();

        List<PlayerScore> scores = new ArrayList<>();
        int count = 0;
        for (Map.Entry<Long, String> entry : board.descendingMap().entrySet()) {
            if (count >= start && count <= end) {
                scores.add(new PlayerScore(entry.getValue(), entry.getKey()));
            }
            count++;
            if (count > end) break;
        }
        return scores;
    }

    public long zrevrank(String key, String member) {
        TreeMap<Long, String> board = leaderboards.get(key);
        if (board == null || board.isEmpty()) return -1;

        long rank = 0;
        for (Map.Entry<Long, String> entry : board.descendingMap().entrySet()) {
            if (entry.getValue().equals(member)) return rank;
            rank++;
        }
        return -1;
    }

    public long zscore(String key, String member) {
        TreeMap<Long, String> board = leaderboards.get(key);
        if (board == null) return -1;
        for (Map.Entry<Long, String> entry : board.entrySet()) {
            if (entry.getValue().equals(member)) return entry.getKey();
        }
        return -1;
    }
}

class MessageProducer {
    public void send(String topic, Object message) {
        System.out.println("Sending message to topic '" + topic + "': " + message);
    }
}

public class LeaderboardSystem {
    public static void main(String[] args) throws InterruptedException {
        MockRedisClient redis = new MockRedisClient();
        MessageProducer messageProducer = new MessageProducer();

        IScoreIngester ingester = new ScoreIngestionService(messageProducer);
        IScoreCalculator calculator = new ScoreCalculationService();
        IRankingUpdater updater = new RankingUpdateService(redis);
        ILeaderboardQuery queryService = new LeaderboardQueryService(redis);
        ILeaderboardPublisher publisher = new WebSocketLeaderboardPublisher();

        // Simulate score submissions
        ingester.submitScore("playerA", 100, "game1");
        ingester.submitScore("playerB", 150, "game1");
        ingester.submitScore("playerC", 120, "game1");
        ingester.submitScore("playerA", 180, "game1"); // Player A improves score

        // Simulate processing (in real-world, this would be async via message queue listeners)
        ProcessedScore s1 = calculator.calculateFinalScore(new RawScore("playerA", 100, "game1", 0));
        ProcessedScore s2 = calculator.calculateFinalScore(new RawScore("playerB", 150, "game1", 0));
        ProcessedScore s3 = calculator.calculateFinalScore(new RawScore("playerC", 120, "game1", 0));
        ProcessedScore s4 = calculator.calculateFinalScore(new RawScore("playerA", 180, "game1", 0));

        updater.updateRank("game1", s1.getUserId(), s1.getFinalScore());
        updater.updateRank("game1", s2.getUserId(), s2.getFinalScore());
        updater.updateRank("game1", s3.getUserId(), s3.getFinalScore());
        updater.updateRank("game1", s4.getUserId(), s4.getFinalScore()); // Update A's score

        // Query leaderboard
        System.out.println("\nTop 3 players for game1:");
        queryService.getTopPlayers("game1", 3).forEach(System.out::println);

        System.out.println("\nPlayer A's rank:");
        System.out.println(queryService.getPlayerRank("game1", "playerA"));

        // Simulate real-time publishing
        publisher.publishRealtimeUpdate("game1", queryService.getTopPlayers("game1", 5));
    }
}
```

#### 3. Design a distributed logging and monitoring system. Think about data ingestion, storage, search, and visualization. How would `Factory` patterns help in creating different types of log collectors?

**Solution:**

*   **Requirements:**
    *   **Distributed Ingestion:** Collect logs/metrics from many sources (servers, microservices).
    *   **Scalable Storage:** Handle high volume and velocity of data.
    *   **Efficient Search:** Allow quick querying of log data.
    *   **Real-time Monitoring & Alerting:** Visualize metrics, trigger alerts on anomalies.
    *   **Observability:** Support logging, metrics, tracing.

*   **Architecture (Common ELK/Prometheus Stack Analog):**
    1.  **Log/Metric Agents (Collectors):** On each host/service (e.g., Filebeat, Fluentd, Prometheus Node Exporter, custom Java agents). Collects logs/metrics and forwards them.
    2.  **Log/Metric Ingestion Pipeline:** Centralized entry point (e.g., Kafka, Redis, Logstash, Fluentd aggregator). Buffers, filters, transforms data.
    3.  **Storage:**
        *   **Logs:** Elasticsearch, Loki (for Prometheus-style logging), object storage (S3) for archives.
        *   **Metrics:** Prometheus (time-series database), InfluxDB, VictoriaMetrics.
        *   **Traces:** Jaeger, Zipkin (distributed tracing backends).
    4.  **Search & Analytics Engine:** Elasticsearch (for full-text log search), PromQL (for Prometheus queries).
    5.  **Visualization & Dashboarding:** Kibana (for Elasticsearch), Grafana (for Prometheus, Loki, etc.).
    6.  **Alerting:** Alertmanager (for Prometheus), custom rules engines.

*   **Data Ingestion Flow:**
    *   **Logs:** Application logs to local file -> Filebeat/Fluentd reads -> Kafka/Logstash -> Elasticsearch.
    *   **Metrics:** Application metrics (e.g., Micrometer in Spring Boot) -> Prometheus client library -> Prometheus server scrapes endpoints.
    *   **Traces:** Application instruments code (e.g., OpenTelemetry, Brave) -> sends spans to Jaeger/Zipkin agent -> Jaeger/Zipkin collector -> storage.

*   **`Factory` Patterns for Log Collectors:**

    *   **Problem:** Different environments (bare metal, Kubernetes, serverless), different logging sources (file, syslog, Kafka), and different output formats (JSON, plain text) require different log collector configurations or even entirely different collector implementations. Manually managing these can be cumbersome.
    *   **Factory Pattern Application:** A `LogCollectorFactory` can encapsulate the logic for creating appropriate log collector instances based on configuration or environment. This promotes `SRP` (the factory is solely responsible for creation) and `OCP` (new collector types can be added without modifying existing client code).

    **Java Example:**

    ```java
    // LogCollector.java (Product interface)
    public interface LogCollector {
        void startCollection();
        void stopCollection();
        void configure(Map<String, String> config);
    }

    // FileLogCollector.java (Concrete Product)
    public class FileLogCollector implements LogCollector {
        private String filePath;
        private String format; // JSON, plain text

        @Override
        public void configure(Map<String, String> config) {
            this.filePath = config.get("file.path");
            this.format = config.getOrDefault("log.format", "plain");
            System.out.println("Configured FileLogCollector for " + filePath + " (format: " + format + ")");
        }

        @Override
        public void startCollection() {
            System.out.println("Starting FileLogCollector for " + filePath + "...");
            // Logic to tail file, parse lines, and send to ingestion pipeline
        }

        @Override
        public void stopCollection() {
            System.out.println("Stopping FileLogCollector for " + filePath);
        }
    }

    // KafkaLogCollector.java (Concrete Product for consuming from Kafka)
    public class KafkaLogCollector implements LogCollector {
        private String topic;
        private String consumerGroup;

        @Override
        public void configure(Map<String, String> config) {
            this.topic = config.get("kafka.topic");
            this.consumerGroup = config.get("kafka.group");
            System.out.println("Configured KafkaLogCollector for topic " + topic + " (group: " + consumerGroup + ")");
        }

        @Override
        public void startCollection() {
            System.out.println("Starting KafkaLogCollector for topic " + topic + "...");
            // Logic to subscribe to Kafka topic and process messages
        }

        @Override
        public void stopCollection() {
            System.out.println("Stopping KafkaLogCollector for topic " + topic);
        }
    }

    // LogCollectorFactory.java (Factory)
    public class LogCollectorFactory {
        public LogCollector createCollector(String type, Map<String, String> config) {
            LogCollector collector;
            switch (type.toLowerCase()) {
                case "file":
                    collector = new FileLogCollector();
                    break;
                case "kafka":
                    collector = new KafkaLogCollector();
                    break;
                // Add more types as needed (e.g., "syslog", "http")
                default:
                    throw new IllegalArgumentException("Unknown log collector type: " + type);
            }
            collector.configure(config);
            return collector;
        }
    }

    // Main class to demonstrate
    public class MonitoringSystemSetup {
        public static void main(String[] args) {
            LogCollectorFactory factory = new LogCollectorFactory();

            // Create a file log collector
            Map<String, String> fileConfig = new HashMap<>();
            fileConfig.put("file.path", "/var/log/myapp/app.log");
            fileConfig.put("log.format", "json");
            LogCollector fileCollector = factory.createCollector("file", fileConfig);
            fileCollector.startCollection();

            System.out.println("\n---");

            // Create a Kafka log collector
            Map<String, String> kafkaConfig = new HashMap<>();
            kafkaConfig.put("kafka.topic", "service_logs");
            kafkaConfig.put("kafka.group", "log_aggregator_group");
            LogCollector kafkaCollector = factory.createCollector("kafka", kafkaConfig);
            kafkaCollector.startCollection();

            // Later, stop collectors
            // fileCollector.stopCollection();
            // kafkaCollector.stopCollection();
        }
    }
    ```

---

## Topic 2 Advanced System Design Concepts & Interview Mastery - Solutions

## Interview Questions & Solutions

### 1. What is a Circuit Breaker pattern? When should it be used, and how does it improve system resilience?

The Circuit Breaker pattern is a design pattern used in distributed systems to prevent a single failing service from taking down other services. It monitors calls to a service and, if the failure rate exceeds a certain threshold, "opens" the circuit, preventing further calls to that service for a specified duration. After a timeout, it allows a limited number of "half-open" requests to test if the service has recovered.

**When to Use:**
*   When interacting with external services or microservices that might be temporarily unavailable or slow.
*   To protect your application from cascading failures.
*   To provide graceful degradation when dependent services are struggling.

**How it Improves Resilience:**
*   **Prevents Cascading Failures:** By stopping requests to a failing service, it prevents the caller's resources (threads, connections) from being exhausted, allowing the caller to continue serving other requests.
*   **Faster Failure Detection:** Instead of waiting for a timeout on a failing service, the circuit breaker fails fast.
*   **Graceful Degradation:** Allows for fallback mechanisms to be implemented when the circuit is open, providing a better user experience.
*   **Reduces Load on Failing Service:** Gives the failing service time to recover by not bombarding it with requests.

**Java Example (Conceptual with Resilience4j):**

```java
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import java.util.function.Supplier;

public class CircuitBreakerService {

    private final CircuitBreaker circuitBreaker;
    private final MyExternalService externalService;

    public CircuitBreakerService(MyExternalService externalService) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // Percentage of failures above which the CB trips
                .waitDurationInOpenState(Duration.ofSeconds(5)) // Time CB stays open
                .permittedNumberOfCallsInHalfOpenState(3) // Number of calls allowed in half-open
                .slidingWindowSize(10) // Number of calls to record when evaluating failure rate
                .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        this.circuitBreaker = registry.circuitBreaker("myExternalService");
        this.externalService = externalService;
    }

    public String callExternalService() {
        Supplier<String> supplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
            // Simulate calling an external service
            return externalService.getData();
        });

        try {
            return supplier.get();
        } catch (Exception e) {
            // Fallback logic
            System.err.println("External service is down or slow, falling back. Error: " + e.getMessage());
            return "Fallback Data";
        }
    }

    // Dummy external service
    static class MyExternalService {
        private int callCount = 0;
        public String getData() {
            callCount++;
            if (callCount % 3 != 0) { // Simulate failure every 2 calls
                System.out.println("External Service: Success");
                return "Data from External Service";
            } else {
                System.out.println("External Service: Failure");
                throw new RuntimeException("Service Unavailable");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CircuitBreakerService service = new CircuitBreakerService(new MyExternalService());
        for (int i = 0; i < 20; i++) {
            System.out.println("Attempt " + (i + 1) + ": " + service.callExternalService());
            Thread.sleep(1000); // Simulate calls over time
        }
    }
}
```

### 2. Explain the difference between logging, metrics, and tracing for system observability. How would you implement them in a microservices architecture?

**Logging:**
*   **Purpose:** Records discrete events or messages from an application. It provides detailed, human-readable information about what happened at a specific point in time.
*   **Content:** Textual messages, error stack traces, request/response payloads, specific actions performed.
*   **Use Cases:** Debugging, error analysis, auditing, understanding application flow.
*   **Granularity:** Event-level details.

**Metrics:**
*   **Purpose:** Collects numerical data points over time, representing the health and performance of a system or service. They are aggregated and used for monitoring, alerting, and trend analysis.
*   **Content:** Counters (e.g., total requests, error count), gauges (e.g., CPU utilization, memory usage, queue size), histograms/summaries (e.g., request latency distribution).
*   **Use Cases:** Real-time monitoring, dashboarding, alerting on thresholds, capacity planning.
*   **Granularity:** Aggregated, time-series data.

**Tracing:**
*   **Purpose:** Tracks a single request or transaction as it propagates through multiple services in a distributed system. It provides an end-to-end view of the request's journey.
*   **Content:** A sequence of "spans," where each span represents an operation (e.g., a service call, a database query) with its start time, end time, duration, and metadata. Spans are linked to form a trace.
*   **Use Cases:** Performance bottleneck identification in distributed systems, understanding service dependencies, debugging latency issues across services.
*   **Granularity:** Request-level, distributed end-to-end flow.

**Implementation in a Microservices Architecture:**

*   **Logging:**
    *   **Standardization:** Use a structured logging format (e.g., JSON) to make logs machine-readable and parsable. Include common fields like timestamp, service name, trace ID, span ID, log level, and message.
    *   **Centralization:** Ship logs from all microservices to a centralized logging system (e.g., ELK Stack - Elasticsearch, Logstash, Kibana; or Splunk, Grafana Loki). This allows for easy searching, filtering, and analysis across services.
    *   **Java:** Use SLF4J with Logback or Log4j2. Configure appenders to write logs to `stdout` and use a log shipper (e.g., Filebeat, Fluentd) to forward them to the centralized system.

    ```java
    // Example using SLF4J and Logback with structured logging (conceptual)
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import net.logstash.logback.argument.StructuredArguments;

    public class MyService {
        private static final Logger logger = LoggerFactory.getLogger(MyService.class);

        public void processRequest(String requestId, String userId) {
            logger.info("Processing request",
                StructuredArguments.keyValue("requestId", requestId),
                StructuredArguments.keyValue("userId", userId),
                StructuredArguments.keyValue("service", "MyService"),
                StructuredArguments.keyValue("event", "request_received")
            );
            try {
                // business logic
                logger.info("Request processed successfully",
                    StructuredArguments.keyValue("requestId", requestId),
                    StructuredArguments.keyValue("status", "success")
                );
            } catch (Exception e) {
                logger.error("Error processing request", e,
                    StructuredArguments.keyValue("requestId", requestId),
                    StructuredArguments.keyValue("status", "failed")
                );
            }
        }
    }
    ```

*   **Metrics:**
    *   **Instrumentation:** Each microservice should expose its internal metrics. Common libraries like Micrometer provide a facade for various monitoring systems. Spring Boot Actuator integrates Micrometer by default.
    *   **Collection:** Use a metrics collector/scraper (e.g., Prometheus) to periodically pull metrics from each service's `/actuator/prometheus` endpoint.
    *   **Visualization & Alerting:** Use a dashboarding tool (e.g., Grafana) to visualize metrics and configure alerts based on predefined thresholds.
    *   **Java:** Spring Boot Actuator with Micrometer and Prometheus client.

    ```java
    // Example with Spring Boot Actuator and Micrometer (application.properties)
    // management.endpoints.web.exposure.include=health,info,prometheus
    // management.metrics.tags.application=${spring.application.name}

    // Example in a Spring component
    import io.micrometer.core.instrument.Counter;
    import io.micrometer.core.instrument.MeterRegistry;
    import org.springframework.stereotype.Service;

    @Service
    public class ProductService {
        private final Counter productCreatedCounter;
        private final MeterRegistry meterRegistry; // Autowired by Spring Boot

        public ProductService(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
            this.productCreatedCounter = meterRegistry.counter("product.created.total");
        }

        public void createProduct(String productName) {
            // ... logic to create product
            productCreatedCounter.increment();
            meterRegistry.timer("product.creation.time").record(() -> {
                // Simulate work
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            });
            // ...
        }
    }
    ```

*   **Tracing:**
    *   **Instrumentation:** Use a distributed tracing library (e.g., OpenTelemetry, Brave/Zipkin, Jaeger client) to automatically or manually instrument services. This involves propagating a "trace context" (trace ID, span ID) across service boundaries, typically via HTTP headers.
    *   **Context Propagation:** Ensure that every request passing between services includes the trace context. When a service receives a request, it should extract the context and continue the trace, creating new child spans for its internal operations and outgoing calls.
    *   **Collector/Backend:** Send collected trace data (spans) to a centralized tracing system (e.g., Jaeger, Zipkin, OpenTelemetry Collector).
    *   **Visualization:** Use the tracing system's UI to visualize call graphs and analyze latency.
    *   **Java:** OpenTelemetry Java agent or SDK for automatic and manual instrumentation. Spring Cloud Sleuth (now deprecated in favor of Micrometer Tracing/OpenTelemetry) was a popular choice.

    ```java
    // Example using OpenTelemetry (conceptual)
    import io.opentelemetry.api.trace.Span;
    import io.opentelemetry.api.trace.Tracer;
    import io.opentelemetry.api.OpenTelemetry;
    import io.opentelemetry.context.Scope;

    public class OrderProcessor {
        private final Tracer tracer;
        private final ProductClient productClient;

        public OrderProcessor(OpenTelemetry openTelemetry, ProductClient productClient) {
            this.tracer = openTelemetry.getTracer("order-service", "1.0.0");
            this.productClient = productClient;
        }

        public void processOrder(String orderId) {
            Span parentSpan = tracer.spanBuilder("processOrder").startSpan();
            try (Scope scope = parentSpan.makeCurrent()) {
                parentSpan.setAttribute("orderId", orderId);
                System.out.println("Processing order: " + orderId);

                // Call product service (assuming ProductClient is also instrumented)
                productClient.getProductDetails("prod-123");

                // Simulate some work
                try { Thread.sleep(50); } catch (InterruptedException e) {}

                Span dbSpan = tracer.spanBuilder("saveOrderToDB").startSpan();
                try (Scope dbScope = dbSpan.makeCurrent()) {
                    System.out.println("Saving order to database...");
                    try { Thread.sleep(20); } catch (InterruptedException e) {}
                } finally {
                    dbSpan.end();
                }

            } finally {
                parentSpan.end();
            }
        }
    }

    // ProductClient would propagate context
    class ProductClient {
        private final Tracer tracer;
        public ProductClient(OpenTelemetry openTelemetry) {
            this.tracer = openTelemetry.getTracer("product-client", "1.0.0");
        }

        public String getProductDetails(String productId) {
            Span clientSpan = tracer.spanBuilder("getProductDetails").startSpan();
            try (Scope scope = clientSpan.makeCurrent()) {
                clientSpan.setAttribute("productId", productId);
                System.out.println("Calling product service for: " + productId);
                // Simulate HTTP call
                try { Thread.sleep(30); } catch (InterruptedException e) {}
                return "Product Details";
            } finally {
                clientSpan.end();
            }
        }
    }
    ```

### 3. How do you ensure data integrity in a distributed system, especially across service boundaries?

Ensuring data integrity in a distributed system is challenging due to network latency, partial failures, and the lack of a single, global transaction manager. Key strategies include:

1.  **Eventual Consistency with Idempotency:**
    *   **Principle:** Many distributed systems opt for eventual consistency over strong consistency for scalability. Operations might not be immediately consistent everywhere, but they will converge over time.
    *   **Idempotency:** Design operations to be idempotent, meaning applying them multiple times produces the same result as applying them once. This is crucial for retries without side effects.
        *   **Implementation:** Include a unique correlation ID or a specific state check in requests. For example, when processing an order, check if the order has already been processed before applying changes.

2.  **Distributed Transactions (Saga Pattern):**
    *   **Principle:** Avoid two-phase commit (2PC) across service boundaries as it's often a performance bottleneck and introduces strong coupling. Instead, use the Saga pattern, which is a sequence of local transactions, each updating its own service's database. If a step fails, compensating transactions are executed to undo previous steps.
    *   **Types:**
        *   **Choreography Saga:** Services publish events, and other services react, driving the saga.
        *   **Orchestration Saga:** A central orchestrator service (saga coordinator) directs the participants via commands and events.
    *   **Implementation:**
        *   **Message Queues:** Use reliable message queues (e.g., Apache Kafka, RabbitMQ) to communicate events between services.
        *   **Guaranteed Delivery:** Ensure messages are reliably delivered and processed (e.g., "at-least-once" delivery with idempotent consumers).
        *   **Outbox Pattern:** To ensure atomicity between saving data to a local database and publishing a message, use the Outbox pattern. The event is saved in the same transaction as the business data in a dedicated "outbox" table. A separate process then reads from the outbox table and publishes the events.

3.  **Data Replication and Consistency Models:**
    *   **Principle:** Replicate data across multiple nodes or data centers for availability and fault tolerance. Different consistency models (strong, eventual, causal) offer trade-offs between consistency and availability/latency.
    *   **Implementation:** Choose appropriate database technologies (e.g., PostgreSQL for strong consistency within a service, Cassandra for eventual consistency across geo-distributed services).

4.  **Optimistic Concurrency Control:**
    *   **Principle:** Allow multiple users to read and modify data concurrently, but only permit updates if the data hasn't changed since it was read.
    *   **Implementation:** Use version numbers (`optimistic locking`) or timestamps (`ETag` for REST APIs). When an update request comes in, check if the version matches the current version in the database. If not, the update is rejected, and the client is asked to retry with the latest data.

5.  **Validation and Constraints:**
    *   **Principle:** Enforce data integrity at the service boundary through rigorous validation and database constraints.
    *   **Implementation:**
        *   **Service-level Validation:** Implement comprehensive input validation in each microservice to ensure data adheres to business rules before processing.
        *   **Database Constraints:** Use foreign keys, unique constraints, and check constraints within each service's database to maintain local data integrity. While foreign keys *across* services are discouraged, local constraints are essential.

6.  **Monitoring and Alerting:**
    *   **Principle:** Continuously monitor data consistency and integrity, and alert promptly if anomalies are detected.
    *   **Implementation:**
        *   **Auditing:** Implement auditing logs for critical data changes.
        *   **Reconciliation Jobs:** Periodically run background jobs to check for inconsistencies between services and reconcile them if found.
        *   **Metrics:** Track metrics related to data integrity, such as failed transactions, number of inconsistencies found by reconciliation, etc.

**Java Example (Saga Orchestration with Outbox Pattern):**

```java
// Conceptual example for an Order Creation Saga

// 1. Order Service (Orchestrator)
// Represents the Order entity in its own database
public class Order {
    private String orderId;
    private String customerId;
    private double totalAmount;
    private OrderStatus status; // PENDING, APPROVED, REJECTED
    // ... other fields
}

// Outbox entity (stored in Order Service's DB)
public class OutboxEvent {
    private String eventId;
    private String aggregateType; // "Order"
    private String aggregateId; // orderId
    private String eventType; // "OrderCreatedEvent", "OrderApprovedEvent"
    private String payload; // JSON representation of the event
    private String status; // PENDING, PUBLISHED
    private long timestamp;
}

public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxEventRepository;
    // ... other dependencies

    public String createOrder(String customerId, double amount) {
        // 1. Start a local transaction
        Order order = new Order(UUID.randomUUID().toString(), customerId, amount, OrderStatus.PENDING);
        OrderCreatedEvent event = new OrderCreatedEvent(order.getOrderId(), customerId, amount);

        // 2. Save order and the event in the same local DB transaction (Outbox Pattern)
        orderRepository.save(order);
        outboxEventRepository.save(new OutboxEvent(
            UUID.randomUUID().toString(), "Order", order.getOrderId(), "OrderCreatedEvent",
            new ObjectMapper().writeValueAsString(event), "PENDING", System.currentTimeMillis()
        ));
        // Commit transaction

        // A separate "Outbox Relayer" process (e.g., a background scheduler or Debezium)
        // picks up PENDING events from OutboxEvent table and publishes them to a message broker.

        return order.getOrderId();
    }

    public void handlePaymentApproved(PaymentApprovedEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).orElseThrow();
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
        // Publish OrderApprovedEvent via Outbox
    }

    public void handlePaymentRejected(PaymentRejectedEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).orElseThrow();
        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
        // Optionally, publish OrderRejectedEvent via Outbox or trigger a refund saga
    }
}

// 2. Payment Service (Participant)
// Listens for OrderCreatedEvent
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final MessagePublisher messagePublisher; // Publishes PaymentApproved/RejectedEvent

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Idempotency check: Have we already processed this order?
        if (paymentRepository.findByOrderId(event.getOrderId()).isPresent()) {
            return; // Already processed
        }

        try {
            // Simulate payment processing
            boolean success = performPayment(event.getCustomerId(), event.getAmount());

            Payment payment = new Payment(UUID.randomUUID().toString(), event.getOrderId(), event.getCustomerId(), event.getAmount(), success ? PaymentStatus.PAID : PaymentStatus.FAILED);
            paymentRepository.save(payment);

            if (success) {
                messagePublisher.publish(new PaymentApprovedEvent(event.getOrderId(), payment.getPaymentId()));
            } else {
                messagePublisher.publish(new PaymentRejectedEvent(event.getOrderId(), "Insufficient funds"));
            }
        } catch (Exception e) {
            // Log error, retry mechanism, dead-letter queue
            messagePublisher.publish(new PaymentRejectedEvent(event.getOrderId(), "Payment processing error"));
        }
    }
}

// 3. Inventory Service (Participant)
// Listens for OrderApprovedEvent
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    // ...

    @KafkaListener(topics = "payment-events", groupId = "inventory-group")
    public void handlePaymentApproved(PaymentApprovedEvent event) {
        // Idempotency check: Already decremented inventory for this order?
        if (inventoryRepository.hasDecrementedForOrder(event.getOrderId())) {
            return;
        }

        try {
            // Simulate inventory decrement
            boolean success = decrementInventory(event.getOrderId()); // Needs order details from event or separate lookup

            if (!success) {
                // If inventory fails, need to publish InventoryRejectedEvent
                // and OrderService needs to trigger a compensating transaction (e.g., refund)
            }
            // ...
        } catch (Exception e) {
            // Handle and potentially publish compensating event
        }
    }
}
```

### 4. Discuss different API versioning strategies (URI, header, query parameter) and their pros/cons.

API versioning is crucial for evolving an API without breaking existing clients.

1.  **URI Versioning (Path Versioning):**
    *   **Strategy:** Include the API version directly in the URI path.
    *   **Example:** `/api/v1/products`, `/api/v2/products`
    *   **Pros:**
        *   **Discoverability:** Very clear and explicit. Clients can easily see the version they are interacting with.
        *   **Caching:** Proxies and browsers can easily cache different versions separately.
        *   **Simplicity:** Easy to implement and understand for developers.
        *   **Bookmarkable:** URLs are bookmarkable.
    *   **Cons:**
        *   **URI Pollution:** The API version becomes part of the resource identifier, which some argue violates REST principles (resources should not change their identifier based on representation).
        *   **Routing Complexity:** Can lead to more complex routing configurations on the server-side.
        *   **Redundancy:** If the base URL changes, all versions will need to be updated.

2.  **Header Versioning:**
    *   **Strategy:** Include the API version in a custom HTTP header or the `Accept` header.
    *   **Examples:**
        *   Custom Header: `X-API-Version: 1`
        *   Accept Header (Content Negotiation): `Accept: application/vnd.myapi.v1+json`
    *   **Pros:**
        *   **URI Cleanliness:** Keeps the URI clean and focused on resource identification.
        *   **RESTful:** Adheres more closely to REST principles by using content negotiation (Accept header).
        *   **Flexibility:** Allows for more complex negotiation (e.g., requesting a specific version and format).
    *   **Cons:**
        *   **Discoverability:** Less obvious than URI versioning. Requires clients to know about the custom header or specific media type.
        *   **Caching Challenges:** Caching can be more complex if proxies don't consider custom headers in their cache keys. With `Accept` header, it's generally handled well.
        *   **Browser Limitations:** Harder to test directly in a browser without browser extensions or command-line tools (like `curl`).

3.  **Query Parameter Versioning:**
    *   **Strategy:** Include the API version as a query parameter.
    *   **Example:** `/api/products?version=1`, `/api/products?v=2`
    *   **Pros:**
        *   **Easy to Test:** Simple to test in a browser or tools like Postman.
        *   **Flexible:** Easy to specify different versions without changing the entire path.
        *   **Bookmarkable:** URLs are bookmarkable.
    *   **Cons:**
        *   **Caching Challenges:** Caching can be problematic if proxies treat query parameters as part of the cache key and a `version` parameter changes frequently or is optional.
        *   **URI Pollution (Minor):** While not as disruptive as path versioning, query parameters are often used for filtering or pagination, and adding versioning can clutter them.
        *   **Semantics:** Query parameters are typically for filtering or manipulating resources, not identifying a specific representation of the API itself.

**Recommendation:**

*   For most public-facing APIs, **URI Versioning (`/v1/resource`) is often preferred** due to its clarity, discoverability, and ease of caching. While it might slightly deviate from strict REST purity, its practical benefits often outweigh the theoretical concerns.
*   For internal APIs or when strict REST adherence is a high priority, **Header Versioning (especially using the `Accept` header for content negotiation)** can be a good choice.
*   **Query Parameter Versioning is generally discouraged for primary API versioning** due to caching issues and semantic confusion, but it might be acceptable for minor revisions within a major version.

**API Gateway Integration:**
Regardless of the chosen strategy, an API Gateway can simplify version management by routing requests to the correct backend service version based on the versioning scheme.

### 5. How would you secure a RESTful API using OAuth2 and JWT? Explain the token issuance and validation flow.

Securing a RESTful API with OAuth2 and JWT is a common and robust approach.

**OAuth2 (Authorization Framework):**
*   **Purpose:** An authorization framework that enables an application (client) to obtain limited access to an HTTP service (resource server) on behalf of a resource owner (user) by orchestrating an interaction between the resource owner, client, and an authorization server.
*   **Key Roles:**
    *   **Resource Owner:** The user who owns the data (e.g., your social media profile).
    *   **Client:** The application requesting access to the resource owner's protected resources (e.g., a mobile app, a web app).
    *   **Authorization Server:** The server that authenticates the resource owner and issues access tokens to the client.
    *   **Resource Server:** The server hosting the protected resources (this is your RESTful API).

**JWT (JSON Web Token):**
*   **Purpose:** A compact, URL-safe means of representing claims to be transferred between two parties. The claims in a JWT are encoded as a JSON object that is digitally signed, guaranteeing its integrity.
*   **Structure:** A JWT consists of three parts, separated by dots: `Header.Payload.Signature`
    *   **Header:** Contains metadata about the token, such as the type of token (JWT) and the signing algorithm (e.g., HS256, RS256).
    *   **Payload (Claims):** Contains the actual information (claims) about the entity (typically the user) and additional data. Standard claims (e.g., `iss` for issuer, `exp` for expiration, `sub` for subject), public claims, and private claims.
    *   **Signature:** Created by taking the encoded header, the encoded payload, a secret key (for HS256) or a private key (for RS256), and signing them. This ensures the token's integrity and authenticity.

---

**Token Issuance and Validation Flow:**

Let's use the **Authorization Code Grant** flow, which is recommended for web applications and mobile apps, as it's the most secure.

**A. Token Issuance Flow (Authorization Code Grant):**

1.  **Client Requests Authorization:**
    *   The user interacts with the **Client Application** (e.g., clicks "Login with Google").
    *   The client redirects the user's browser to the **Authorization Server's** authorization endpoint. This request includes:
        *   `client_id`: Identifies the client application.
        *   `redirect_uri`: Where the authorization server should send the user back.
        *   `response_type=code`: Specifies the Authorization Code flow.
        *   `scope`: The permissions the client is requesting (e.g., `read:profile`, `write:orders`).
        *   `state`: A unique, unguessable string to prevent CSRF attacks.

2.  **Resource Owner Authenticates and Authorizes:**
    *   The **Authorization Server** prompts the **Resource Owner** (user) to log in (if not already logged in).
    *   It then asks the user to grant or deny the permissions requested by the client application (based on `scope`).

3.  **Authorization Server Redirects with Code:**
    *   If the user grants permission, the **Authorization Server** redirects the user's browser back to the `redirect_uri` provided by the client.
    *   This redirect includes an `authorization_code` and the original `state` parameter.

4.  **Client Exchanges Code for Token:**
    *   The **Client Application** receives the `authorization_code`.
    *   The client then makes a **direct, backend-channel request** (server-to-server) to the **Authorization Server's** token endpoint. This request includes:
        *   `grant_type=authorization_code`
        *   `code`: The authorization code received in the previous step.
        *   `redirect_uri`: Must match the one used in step 1.
        *   `client_id`: Client's ID.
        *   `client_secret`: A confidential secret known only to the client and authorization server (for confidential clients like web applications).

5.  **Authorization Server Issues Tokens:**
    *   The **Authorization Server** validates the `authorization_code` and `client_secret`.
    *   If valid, it issues an **Access Token** (typically a JWT) and optionally a **Refresh Token**.
    *   The access token has an expiration time (usually short-lived, e.g., 5-60 minutes).
    *   The refresh token is long-lived and used to obtain new access tokens without re-authenticating the user.

    ```
    +----------+                               +---------------+
    | Resource | -- (1) Authorization Request ->| Authorization |
    |  Owner   |                               |     Server    |
    |          |   <- (2) Authorization Grant --|               |
    |          |                               +---------------+
    |          |                                      ^
    |          |                                      |
    |          |                                      |
    |          |                                      | (4) Access Token Request
    |          |                                      |
    |          |                                      |
    +----------+                                      |
         ^                                            |
         |                                            |
        (3) Authorization Code                         |
         |                                            |
         v                                            v
    +--------+                                  +----------+
    | Client | --- (5) Access Token Response -->|  Resource  |
    |        |                                  |   Server   |
    +--------+                                  +----------+
    ```

**B. Token Validation Flow (by Resource Server / Your RESTful API):**

1.  **Client Sends Access Token:**
    *   The **Client Application** includes the received **Access Token (JWT)** in the `Authorization` header of subsequent API requests to the **Resource Server** (your RESTful API).
    *   `Authorization: Bearer <JWT>`

2.  **Resource Server Validates Access Token:**
    *   When your **Resource Server** receives a request, it performs several checks on the JWT:
        *   **Signature Verification:**
            *   If the JWT is signed with a symmetric key (HS256), the resource server uses the same shared secret key.
            *   If signed with an asymmetric key pair (RS256), the resource server fetches the **Authorization Server's public key** (often from a JWKS endpoint - JSON Web Key Set) and uses it to verify the signature. This is crucial for ensuring the token hasn't been tampered with.
        *   **Expiration (exp claim):** Checks if the token has expired.
        *   **Issuer (iss claim):** Verifies that the token was issued by the expected Authorization Server.
        *   **Audience (aud claim):** Checks if the token is intended for this specific Resource Server.
        *   **Not Before (nbf claim, optional):** Checks if the token is not yet valid.
        *   **Scopes/Permissions:** Extracts the `scope` or custom permission claims from the payload and verifies if the client has the necessary permissions to access the requested resource.

3.  **Resource Server Grants/Denies Access:**
    *   If all validations pass, the **Resource Server** trusts the claims in the JWT (e.g., the user ID from the `sub` claim, their roles) and grants access to the requested resource.
    *   If validation fails (e.g., invalid signature, expired token), access is denied (e.g., `401 Unauthorized` or `403 Forbidden`).

    ```
    +--------+                               +----------+
    | Client |--- (1) API Request with JWT -->| Resource |
    |        |                               |   Server   |
    +--------+                               +----------+
                                                 |
                                                 | (2) Validate JWT:
                                                 |     - Verify Signature (using Auth Server's public key)
                                                 |     - Check exp, iss, aud claims
                                                 |     - Check scopes/permissions
                                                 |
                                                 v
                                             (3) Access Granted/Denied
    ```

**Java Implementation Considerations (Spring Security):**

*   **Authorization Server:** Use an OAuth2 Authorization Server (e.g., Spring Authorization Server, Keycloak, Auth0, Okta).
*   **Resource Server (Your API):**
    *   **Spring Boot with Spring Security:**
        *   Add `spring-boot-starter-oauth2-resource-server` dependency.
        *   Configure `application.properties` with the JWKS URI of your Authorization Server. Spring Security will automatically fetch the public keys for signature verification.
        *   Use `@PreAuthorize("hasAuthority('SCOPE_read:profile')")` or similar annotations to secure endpoints based on scopes/roles in the JWT.
        *   Spring Security will automatically parse the JWT, validate it, and populate the `SecurityContext` with an `Authentication` object containing the claims.

```java
// Example application.properties for Spring Boot Resource Server
# This is the base URI of your Authorization Server
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://your-auth-server.com/realms/your-realm
# Or if using a specific JWKS URI:
# spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://your-auth-server.com/realms/your-realm/protocol/openid-connect/certs

// Example Spring Security Configuration for Resource Server
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Allow unauthenticated access to /public
                .requestMatchers("/public/**").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {
                    // This is sufficient if issuer-uri is configured
                    // If you need custom JWT validation logic, you'd configure a JwtDecoder
                })
            );
        return http.build();
    }
}

// Example Controller with secured endpoints
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyResourceController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello from public endpoint!";
    }

    @GetMapping("/protected/profile")
    @PreAuthorize("hasAuthority('SCOPE_read:profile')") // Requires 'read:profile' scope
    public String getProfile(Authentication authentication) {
        // The 'authentication' object will contain details from the JWT
        return "User: " + authentication.getName() + ", Authorities: " + authentication.getAuthorities();
    }

    @GetMapping("/protected/admin")
    @PreAuthorize("hasRole('ADMIN')") // Requires 'ADMIN' role (mapped from 'roles' claim)
    public String adminOnly() {
        return "Welcome, Admin!";
    }
}
```

### 6. How can the `Proxy` pattern be used to add security or logging to existing service calls without modifying the service itself?

The `Proxy` pattern is a structural design pattern that provides a surrogate or placeholder for another object to control access to it. It allows you to add functionality (like security, logging, caching, or remote access) around an object without changing the object's original code.

**How the Proxy Pattern Works:**

1.  **Subject Interface:** Define an interface that both the real service and the proxy will implement. This ensures that the proxy can be used wherever the real service is expected.
2.  **Real Subject (Service):** The actual service object that performs the core business logic.
3.  **Proxy:** An object that holds a reference to the real subject. It implements the same interface as the real subject and, before (or after) delegating calls to the real subject, it performs additional logic (e.g., security checks, logging, performance monitoring).

**Using Proxy for Security:**

A security proxy can intercept calls to a service and verify if the caller has the necessary permissions before allowing the call to proceed to the actual service.

```java
// 1. Subject Interface
public interface BankAccountService {
    double getBalance(String accountNumber);
    void deposit(String accountNumber, double amount);
    void withdraw(String accountNumber, double amount);
}

// 2. Real Subject (Original Service)
public class RealBankAccountService implements BankAccountService {
    @Override
    public double getBalance(String accountNumber) {
        System.out.println("Getting balance for " + accountNumber + " from database.");
        // Simulate actual database call
        return 1000.0;
    }

    @Override
    public void deposit(String accountNumber, double amount) {
        System.out.println("Depositing " + amount + " into " + accountNumber + ".");
        // Simulate actual deposit logic
    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        System.out.println("Withdrawing " + amount + " from " + accountNumber + ".");
        // Simulate actual withdrawal logic
    }
}

// 3. Security Proxy
public class SecurityBankAccountServiceProxy implements BankAccountService {
    private final RealBankAccountService realService;
    private final String authenticatedUser; // Represents the current authenticated user

    public SecurityBankAccountServiceProxy(String authenticatedUser) {
        this.realService = new RealBankAccountService();
        this.authenticatedUser = authenticatedUser;
    }

    private boolean hasPermission(String accountNumber, String requiredRole) {
        // Simulate a real authorization check
        System.out.println("Security check: User '" + authenticatedUser + "' attempting to access '" + accountNumber + "' with role '" + requiredRole + "'.");
        if ("admin".equals(authenticatedUser) || ("john_doe".equals(authenticatedUser) && accountNumber.equals("12345"))) {
            return true;
        }
        return false;
    }

    @Override
    public double getBalance(String accountNumber) {
        if (hasPermission(accountNumber, "VIEW_BALANCE")) { // Example permission check
            return realService.getBalance(accountNumber);
        } else {
            System.out.println("Access Denied: " + authenticatedUser + " does not have permission to view balance for " + accountNumber);
            throw new SecurityException("Unauthorized access");
        }
    }

    @Override
    public void deposit(String accountNumber, double amount) {
        if (hasPermission(accountNumber, "DEPOSIT")) { // Example permission check
            realService.deposit(accountNumber, amount);
        } else {
            System.out.println("Access Denied: " + authenticatedUser + " does not have permission to deposit into " + accountNumber);
            throw new SecurityException("Unauthorized access");
        }
    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        if (hasPermission(accountNumber, "WITHDRAW")) { // Example permission check
            realService.withdraw(accountNumber, amount);
        } else {
            System.out.println("Access Denied: " + authenticatedUser + " does not have permission to withdraw from " + accountNumber);
            throw new SecurityException("Unauthorized access");
        }
    }

    public static void main(String[] args) {
        // User John Doe
        BankAccountService johnAccount = new SecurityBankAccountServiceProxy("john_doe");
        try {
            johnAccount.getBalance("12345"); // Should succeed
            johnAccount.deposit("12345", 500); // Should succeed
            johnAccount.withdraw("67890", 200); // Should fail (no permission for other account)
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n---");

        // User Jane Smith (no permissions)
        BankAccountService janeAccount = new SecurityBankAccountServiceProxy("jane_smith");
        try {
            janeAccount.getBalance("12345"); // Should fail
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }
}
```

**Using Proxy for Logging:**

A logging proxy can record information about method calls (e.g., arguments, return values, execution time) before and after delegating to the real service.

```java
// (Reusing BankAccountService interface and RealBankAccountService)

// Logging Proxy
public class LoggingBankAccountServiceProxy implements BankAccountService {
    private final BankAccountService realService; // Can be RealBankAccountService or another proxy

    public LoggingBankAccountServiceProxy(BankAccountService realService) {
        this.realService = realService;
    }

    @Override
    public double getBalance(String accountNumber) {
        long startTime = System.nanoTime();
        System.out.println("LOG: Calling getBalance for account: " + accountNumber);
        double balance = realService.getBalance(accountNumber);
        long endTime = System.nanoTime();
        System.out.println("LOG: getBalance for " + accountNumber + " returned " + balance + " in " + (endTime - startTime) / 1_000_000.0 + " ms.");
        return balance;
    }

    @Override
    public void deposit(String accountNumber, double amount) {
        long startTime = System.nanoTime();
        System.out.println("LOG: Calling deposit for account: " + accountNumber + ", amount: " + amount);
        realService.deposit(accountNumber, amount);
        long endTime = System.nanoTime();
        System.out.println("LOG: deposit for " + accountNumber + " completed in " + (endTime - startTime) / 1_000_000.0 + " ms.");
    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        long startTime = System.nanoTime();
        System.out.println("LOG: Calling withdraw for account: " + accountNumber + ", amount: " + amount);
        realService.withdraw(accountNumber, amount);
        long endTime = System.nanoTime();
        System.out.println("LOG: withdraw for " + accountNumber + " completed in " + (endTime - startTime) / 1_000_000.0 + " ms.");
    }

    public static void main(String[] args) {
        BankAccountService realService = new RealBankAccountService();
        BankAccountService loggedService = new LoggingBankAccountServiceProxy(realService);

        loggedService.getBalance("12345");
        loggedService.deposit("12345", 300);
    }
}
```

**Benefits of the Proxy Pattern for Cross-Cutting Concerns:**

*   **Separation of Concerns:** Keeps the core business logic of the service clean and focused, while external concerns like security and logging are handled by the proxy.
*   **No Modification to Original Service:** The original service (`RealBankAccountService`) doesn't need to know anything about the security or logging aspects.
*   **Flexibility:** Different proxies can be chained together (e.g., a `SecurityProxy` wrapping a `LoggingProxy` wrapping the `RealService`).
*   **Runtime Weaving:** Can be implemented dynamically using AOP (Aspect-Oriented Programming) frameworks like Spring AOP, which often use dynamic proxies (JDK dynamic proxies or CGLIB) to "weave" cross-cutting concerns around existing methods at runtime. This provides a more powerful and less intrusive way to apply proxies system-wide.

## Practice Questions & Solutions

### 1. Discuss how you would incorporate Circuit Breakers and Bulkheads into a microservices interaction to prevent cascading failures.

To prevent cascading failures in a microservices environment, Circuit Breakers and Bulkheads are essential resilience patterns often used together.

**Scenario:** Consider a `ProductService` that calls `InventoryService` (to check stock) and `RecommendationService` (for related products).

**1. Circuit Breaker Implementation:**

*   **Purpose:** The Circuit Breaker protects the calling service (`ProductService`) from waiting indefinitely for a failing downstream service (`InventoryService` or `RecommendationService`). It "trips" (opens) when a service repeatedly fails, preventing further calls for a period.
*   **Where to Apply:** Wrap *each outbound call* to a different microservice with a Circuit Breaker. Each downstream service should have its *own* circuit breaker.

    *   **ProductService -> InventoryService:** A dedicated circuit breaker for calls to `InventoryService`.
    *   **ProductService -> RecommendationService:** A separate circuit breaker for calls to `RecommendationService`.

*   **Mechanism:**
    *   **Closed State:** Requests go through. If failures exceed a threshold (e.g., 50% in X requests/time), the circuit opens.
    *   **Open State:** Requests are immediately rejected (fail-fast) without attempting to call the downstream service. After a configurable `waitDurationInOpenState`, it transitions to `Half-Open`.
    *   **Half-Open State:** A limited number of test requests are allowed. If these succeed, the circuit closes. If they fail, it returns to `Open`.
    *   **Fallback:** When the circuit is open or a call fails, a fallback mechanism should be invoked (e.g., return cached data, default recommendations, an empty list, or a generic error message).

*   **Java Implementation (with Resilience4j):**

    ```java
    import io.github.resilience4j.circuitbreaker.CircuitBreaker;
    import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
    import java.time.Duration;
    import java.util.function.Supplier;

    public class ProductService {

        private final InventoryClient inventoryClient;
        private final RecommendationClient recommendationClient;

        private final CircuitBreaker inventoryCircuitBreaker;
        private final CircuitBreaker recommendationCircuitBreaker;

        public ProductService(InventoryClient inventoryClient, RecommendationClient recommendationClient) {
            this.inventoryClient = inventoryClient;
            this.recommendationClient = recommendationClient;

            CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults(); // Can customize config
            this.inventoryCircuitBreaker = circuitBreakerRegistry.circuitBreaker("inventoryService");
            this.recommendationCircuitBreaker = circuitBreakerRegistry.circuitBreaker("recommendationService");
        }

        public ProductDetails getProductDetails(String productId) {
            // ... (fetch basic product info)

            // Call Inventory Service with Circuit Breaker and Fallback
            Integer stock = getStockWithCircuitBreaker(productId);

            // Call Recommendation Service with Circuit Breaker and Fallback
            // List<String> recommendations = getRecommendationsWithCircuitBreaker(productId);

            return new ProductDetails(productId, stock); // recommendations
        }

        private Integer getStockWithCircuitBreaker(String productId) {
            Supplier<Integer> inventorySupplier = CircuitBreaker.decorateSupplier(inventoryCircuitBreaker,
                () -> inventoryClient.getStock(productId) // Actual call to Inventory Service
            );
            try {
                return inventorySupplier.get();
            } catch (Exception e) {
                // Fallback: return -1 to indicate unknown stock or throw custom exception
                System.err.println("Inventory Service is unavailable. Returning fallback stock for product " + productId);
                return -1;
            }
        }

        // Similar method for getRecommendationsWithCircuitBreaker
    }

    // Dummy Clients for illustration
    class InventoryClient {
        public Integer getStock(String productId) {
            // Simulate network call and potential failure
            if (Math.random() < 0.3) { // 30% chance of failure
                throw new RuntimeException("Inventory Service unavailable");
            }
            return 10;
        }
    }

    class RecommendationClient {
        public String getRecommendations(String productId) { return "Recs"; }
    }

    record ProductDetails(String id, Integer stock) {}
    ```

**2. Bulkhead Implementation:**

*   **Purpose:** Bulkheads isolate resources (thread pools, connection pools) for different downstream services, preventing one overloaded or slow service from consuming all resources and affecting calls to other, healthy services.
*   **Where to Apply:** Allocate separate resource pools (e.g., thread pools) for each *critical outbound dependency* from a given service.

    *   **ProductService -> InventoryService Bulkhead:** A dedicated thread pool (and connection pool) for calls to `InventoryService`. If `InventoryService` is slow, only threads in *its* pool will be tied up.
    *   **ProductService -> RecommendationService Bulkhead:** A separate thread pool for calls to `RecommendationService`.
    *   **Incoming Requests Bulkhead:** Also consider bulkheads for incoming requests to `ProductService` itself, separating different types of requests if applicable.

*   **Mechanism:** When a call is made to a downstream service, it uses a thread from that service's dedicated thread pool. If that pool is exhausted, subsequent calls to that specific downstream service will be rejected immediately, but calls to other services (using their own pools) will remain unaffected.

*   **Java Implementation (with Resilience4j ThreadPoolBulkhead):**

    ```java
    import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
    import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
    import java.util.concurrent.CompletionStage;
    import java.util.function.Supplier;

    public class ProductServiceWithBulkhead {

        private final InventoryClient inventoryClient;
        private final RecommendationClient recommendationClient;

        private final ThreadPoolBulkhead inventoryBulkhead;
        private final ThreadPoolBulkhead recommendationBulkhead;

        public ProductServiceWithBulkhead(InventoryClient inventoryClient, RecommendationClient recommendationClient) {
            this.inventoryClient = inventoryClient;
            this.recommendationClient = recommendationClient;

            ThreadPoolBulkheadRegistry bulkheadRegistry = ThreadPoolBulkheadRegistry.ofDefaults(); // Can customize config
            this.inventoryBulkhead = bulkheadRegistry.bulkhead("inventoryThreadPoolBulkhead");
            this.recommendationBulkhead = bulkheadRegistry.bulkhead("recommendationThreadPoolBulkhead");
        }

        public CompletionStage<ProductDetails> getProductDetailsAsync(String productId) {
            // Use CompletableFuture and combine results
            CompletionStage<Integer> stockFuture = getStockWithBulkhead(productId);
            // CompletionStage<List<String>> recommendationsFuture = getRecommendationsWithBulkhead(productId);

            return stockFuture.thenApply(stock -> new ProductDetails(productId, stock));
        }

        private CompletionStage<Integer> getStockWithBulkhead(String productId) {
            Supplier<Integer> inventorySupplier = () -> inventoryClient.getStock(productId);
            Supplier<CompletionStage<Integer>> decoratedSupplier = ThreadPoolBulkhead.decorateSupplier(
                inventoryBulkhead,
                () -> {
                    // This supplier is executed in the Bulkhead's dedicated thread pool
                    return inventoryClient.getStock(productId);
                }
            );

            try {
                return decoratedSupplier.get(); // Returns a CompletionStage
            } catch (io.github.resilience4j.bulkhead.BulkheadFullException e) {
                System.err.println("Inventory Bulkhead is full. Returning fallback stock for product " + productId);
                return CompletableFuture.completedFuture(-1); // Fallback if bulkhead full
            } catch (Exception e) {
                 System.err.println("Inventory call failed. Returning fallback stock for product " + productId);
                return CompletableFuture.completedFuture(-1); // Fallback on other errors
            }
        }
    }
    ```

**Combining Both (Circuit Breaker and Bulkhead):**

It's crucial to use both patterns together. The Bulkhead provides resource isolation, preventing a single service from starving others. The Circuit Breaker then monitors the health of a specific downstream service *within its bulkhead*, providing fast failure detection and temporary rejection.

**Order of Application:**
Typically, the Bulkhead acts as the outer layer of protection, controlling the number of concurrent requests. The Circuit Breaker then wraps the actual call within that bulkhead's controlled execution context.

```java
// Combining both with Resilience4j (decorate functions are composable)
// The order matters: Bulkhead usually comes first to control concurrent execution,
// then Circuit Breaker to monitor and trip.

public Integer getStockSafely(String productId) {
    Supplier<Integer> inventorySupplier = () -> inventoryClient.getStock(productId);

    // 1. Decorate with Circuit Breaker (monitors failures)
    Supplier<Integer> circuitBreakerDecorated = CircuitBreaker.decorateSupplier(inventoryCircuitBreaker, inventorySupplier);

    // 2. Decorate with Thread Pool Bulkhead (isolates resources)
    // decorateSupplier returns a CompletionStage, so the signature changes
    Supplier<CompletionStage<Integer>> bulkheadAndCircuitBreakerDecorated =
        ThreadPoolBulkhead.decorateSupplier(inventoryBulkhead, () -> CompletableFuture.completedFuture(circuitBreakerDecorated.get()));

    try {
        // Execute the combined decorated supplier
        return bulkheadAndCircuitBreakerDecorated.get().toCompletableFuture().join();
    } catch (io.github.resilience4j.bulkhead.BulkheadFullException e) {
        System.err.println("Inventory ThreadPoolBulkhead is full. Fallback: -1");
        return -1;
    } catch (io.github.resilience4j.circuitbreaker.CallNotPermittedException e) {
        System.err.println("Inventory CircuitBreaker is OPEN. Fallback: -1");
        return -1;
    } catch (Exception e) {
        System.err.println("Inventory Service call failed. Fallback: -1. Error: " + e.getMessage());
        return -1;
    }
}
```

This combination ensures that:
1.  If the `InventoryService` is slow, only its dedicated thread pool (`inventoryBulkhead`) will be saturated, not the entire `ProductService`'s resources.
2.  If the `InventoryService` starts consistently failing, its `inventoryCircuitBreaker` will open, immediately rejecting requests and preventing wasted resources.
3.  Both patterns provide opportunities for graceful degradation through fallback mechanisms, improving the overall user experience.

### 2. Propose a comprehensive monitoring stack for a production Spring Boot application deployed on a cloud platform.

A comprehensive monitoring stack for a production Spring Boot application on a cloud platform (e.g., AWS, GCP, Azure) typically involves collecting metrics, logs, and traces, then visualizing and alerting on this data.

Here's a recommended stack:

**1. Metrics Collection & Aggregation:**

*   **Technology:** **Prometheus** (for pulling metrics) + **Grafana** (for visualization).
    *   **Spring Boot Integration:** Spring Boot Actuator with Micrometer provides native support for exposing metrics in Prometheus format (`/actuator/prometheus` endpoint).
    *   **Mechanism:** Prometheus servers are configured to scrape (pull) metrics from the `/actuator/prometheus` endpoint of each Spring Boot application instance.
*   **Key Metrics to Monitor:**
    *   **JVM Metrics:** CPU usage, memory (heap, non-heap), garbage collection, thread counts.
    *   **HTTP Request Metrics:** Request latency, request volume, error rates (4xx, 5xx), active requests per endpoint.
    *   **Database Connection Pool Metrics:** Active connections, idle connections, wait times.
    *   **Cache Metrics:** Hit/miss ratios, size.
    *   **Custom Business Metrics:** Application-specific counters (e.g., `orders.created.total`), timers (e.g., `payment.processing.time`), gauges.
    *   **System Metrics:** Disk I/O, network I/O (often collected by cloud-specific agents or Node Exporter).
    *   **Resilience Metrics:** Circuit breaker state, bulkhead queue size, retry attempts.

**2. Logging Management:**

*   **Technology:** **ELK Stack** (Elasticsearch, Logstash, Kibana) or **Grafana Loki + Promtail** or a cloud-native solution like **AWS CloudWatch Logs / GCP Cloud Logging**.
    *   **Spring Boot Integration:** Use SLF4J with Logback/Log4j2. Configure logging to output in a structured format (e.g., JSON).
    *   **Mechanism:**
        *   Applications log to `stdout` in JSON format.
        *   A log shipper (e.g., **Filebeat**, **Fluentd**, **Promtail**, or the cloud provider's agent) collects these logs from containers/VMs.
        *   Logs are then sent to a centralized log aggregation system (Elasticsearch for ELK, Loki for Grafana Loki, or directly to cloud logging services).
        *   **Kibana** or **Grafana's Explore** feature is used to search, filter, and visualize logs.
*   **Key Logging Practices:**
    *   **Structured Logging:** JSON format is crucial for easy parsing and querying.
    *   **Contextual Information:** Include `traceId`, `spanId`, `userId`, `sessionId`, `serviceName`, `environment`, `hostname` in every log entry for distributed debugging.
    *   **Log Levels:** Use appropriate levels (DEBUG, INFO, WARN, ERROR) effectively.
    *   **Centralization:** All logs from all services in one place.

**3. Distributed Tracing:**

*   **Technology:** **Jaeger** or **Zipkin** or **OpenTelemetry Collector + Backend** (e.g., Tempo for Grafana, DataDog, New Relic).
    *   **Spring Boot Integration:**
        *   **OpenTelemetry Java Agent:** A zero-code-change solution that automatically instruments common libraries.
        *   **OpenTelemetry SDK:** For custom instrumentation if needed.
        *   (Historically: Spring Cloud Sleuth integrated with Zipkin).
    *   **Mechanism:**
        *   The tracing library/agent injects `traceId` and `spanId` into request headers when calls are made between services.
        *   Each service extracts this context, creates new child spans for its operations, and forwards the context.
        *   Spans are sent to a **Tracing Collector** (e.g., OpenTelemetry Collector), which then stores them in a backend (e.g., Cassandra for Jaeger, Elasticsearch for Zipkin).
        *   The UI (Jaeger UI, Zipkin UI) visualizes the end-to-end flow of a request.
*   **Key Benefits:**
    *   Pinpointing performance bottlenecks in complex microservice interactions.
    *   Debugging errors that span multiple services.
    *   Understanding service dependencies and call graphs.

**4. Alerting:**

*   **Technology:** **Prometheus Alertmanager**, **Grafana Alerting**, or cloud-native alerting (e.g., **AWS CloudWatch Alarms, GCP Monitoring Alerts**).
    *   **Mechanism:** Define rules (e.g., Prometheus `Alerting Rules` or Grafana `Alerts`) based on metrics thresholds, log patterns (e.g., number of ERROR logs per minute), or trace anomalies.
    *   **Notification Channels:** Send alerts to Slack, PagerDuty, email, SMS, etc.
*   **Key Alerts:**
    *   High error rates (5xx HTTP responses) for critical endpoints.
    *   High latency for critical endpoints.
    *   Service unavailability (liveness/readiness probes failing).
    *   Resource exhaustion (CPU, memory, disk).
    *   Database connection pool exhaustion.
    *   Queue backlogs.
    *   Circuit breakers opening.

**5. Visualization & Dashboards:**

*   **Technology:** **Grafana** (primary), **Kibana** (for logs), **Jaeger/Zipkin UI** (for traces).
*   **Mechanism:** Create dashboards to display real-time and historical data from Prometheus, Loki, or cloud metrics.
*   **Key Dashboards:**
    *   **Overview/Golden Signals Dashboard:** RED (Rate, Errors, Duration) metrics for all services.
    *   **Per-Service Dashboard:** Detailed metrics for each individual microservice.
    *   **Infrastructure Dashboard:** Server, container, and network metrics.
    *   **Business Dashboard:** Key business metrics (e.g., orders processed, user sign-ups).

**6. Cloud Platform Specific Tools (Optional/Alternative):**

*   **AWS:**
    *   **CloudWatch:** For logs, metrics, alarms.
    *   **X-Ray:** For distributed tracing.
    *   **Managed Prometheus/Grafana:** AWS offers managed services for these.
*   **GCP:**
    *   **Cloud Monitoring:** For metrics, dashboards, alerts.
    *   **Cloud Logging:** For log aggregation and analysis.
    *   **Cloud Trace:** For distributed tracing.
*   **Azure:**
    *   **Azure Monitor:** For collecting, analyzing, and acting on telemetry data.
    *   **Application Insights:** For application performance monitoring (APM) and distributed tracing.

**Deployment Strategy:**

*   **Containers (Docker) & Orchestration (Kubernetes/ECS/EKS/GKE):** This is the ideal deployment environment for microservices.
    *   Prometheus, Grafana, Jaeger/Zipkin, Elasticsearch, Logstash, Kibana, etc., can all be deployed as containers within the same cluster.
    *   Sidecar containers (e.g., for log shippers) or DaemonSets (for node-level metrics/logs) can be used.
*   **Configuration as Code:** Manage monitoring configuration (Prometheus scrape configs, Grafana dashboards/alerts) using tools like Git and potentially GitOps.

**Example Diagram (Conceptual):**

```
+----------------------------------------------------------------------------------------------------------------------------------+
|                                                          Cloud Platform                                                            |
|                                                                                                                                  |
|                                                                                                                                  |
|  +---------------------+        +---------------------+        +---------------------+                                       |
|  | Spring Boot App 1   |        | Spring Boot App 2   |        | Spring Boot App 3   |                                       |
|  |  (with Actuator,    |        |  (with Actuator,    |        |  (with Actuator,    |                                       |
|  |   Micrometer, OTel)  |        |   Micrometer, OTel)  |        |   Micrometer, OTel)  |                                       |
|  +---------------------+        +---------------------+        +---------------------+                                       |
|        ^   ^   ^                      ^   ^   ^                      ^   ^   ^                                                  |
|        |   |   |                      |   |   |                      |   |   |                                                  |
|        |   |   +--Logs (JSON stdout)  |   |   +--Logs (JSON stdout)  |   |   +--Logs (JSON stdout)                            |
|        |   +------Metrics (/prometheus)|   +------Metrics (/prometheus)|   +------Metrics (/prometheus)                       |
|        +----------Traces (OTLP)       +----------Traces (OTLP)       +----------Traces (OTLP)                                 |
|                                                                                                                                  |
|  +--------------+   +--------------+   +-------------------+                                                                   |
|  | Log Shipper  |-->|  Promtail /  |-->| Log Aggregation   | (e.g., Loki / Elasticsearch / CloudWatch Logs / GCP Logging)        |
|  | (Filebeat,   |   | Fluentd      |   +-------------------+                                                                   |
|  | Fluentd)     |   +--------------+                                                                                            |
|  +--------------+                                                                                                               |
|                                                                                                                                  |
|  +-----------------+                                                                                                            |
|  | Metrics Scraper |                                                                                                            |
|  | (Prometheus)    |--------------------------------------------------------------------------------------------------------+    |
|  +-----------------+                                                                                                         |    |
|                                                                                                                              |    |
|  +-----------------------+                                                                                                   |    |
|  | Tracing Collector     |                                                                                                   |    |
|  | (OpenTelemetry        |--------------------------------------------------------------------------------------------------+    |
|  |  Collector)           |                                                                                                   |    |
|  +-----------------------+                                                                                                   |    |
|                                                                                                                              |    |
|                                                                                                                              |    |
|  +--------------------------------------------------------------------------------------------------------------------------+    |
|  |                                                                                                                          |    |
|  |  +-----------------+      +---------------------+      +----------------+      +-----------------+                     |    |
|  |  |                 |      |                     |      |                |      |                 |                     |    |
|  |  |   Grafana       |----->|   Prometheus        |<-----|  Alertmanager  |----->| Notification    |                     |    |
|  |  | (Dashboards,   |<-----+| (Metrics Store)     |      |                |      | Channels        |                     |    |
|  |  |  Alerting)      |      +---------------------+      +----------------+      | (Slack, PagerDuty)|                     |    |
|  |  +-----------------+                                                           +-----------------+                     |    |
|  |          ^ ^                                                                                                           |    |
|  |          | |                                                                                                           |    |
|  |          | +-------------------------------------------------------------------------------------------------------------+    |
|  |          |                                                                                                                    |
|  |          +-------------------------------------------->| Loki / Elasticsearch / CloudWatch Logs / GCP Logging |                  |
|  |                                                                                                                    |
|  |          +-------------------------------------------->| Jaeger / Zipkin / Cloud Trace          |                  |
|  |                                                                                                                    |
+----------------------------------------------------------------------------------------------------------------------------------+
```

This stack provides robust visibility into application health, performance, and behavior in a distributed environment, enabling proactive problem detection and faster resolution.

### 3. Explain how HTTPS works at a high level to secure communication, including certificate exchange and symmetric key negotiation.

HTTPS (Hypertext Transfer Protocol Secure) is the secure version of HTTP, primarily achieved by encrypting communication between a client (e.g., web browser) and a server using TLS/SSL (Transport Layer Security/Secure Sockets Layer).

At a high level, HTTPS ensures three main properties:

1.  **Confidentiality:** Eavesdroppers cannot read the data exchanged.
2.  **Integrity:** Data cannot be tampered with in transit without detection.
3.  **Authentication:** The client can verify the identity of the server (and optionally, the server can verify the client's identity).

Here's a breakdown of the process:

**Phase 1: TLS Handshake (Authentication and Key Negotiation)**

This phase establishes a secure connection and involves two main steps:

**A. Server Authentication (Certificate Exchange):**

1.  **Client Hello:** The client initiates the connection by sending a "Client Hello" message to the server. This message includes:
    *   The TLS version it supports (e.g., TLS 1.2, TLS 1.3).
    *   A list of cipher suites (combinations of encryption and hashing algorithms) it supports.
    *   A random number.

2.  **Server Hello & Certificate:** The server responds with a "Server Hello" message, choosing:
    *   The highest TLS version supported by both client and server.
    *   A cipher suite from the client's list.
    *   Another random number.
    *   **Crucially, the server sends its digital certificate** (X.509 certificate). This certificate contains:
        *   The server's public key.
        *   The server's domain name (e.g., `www.example.com`).
        *   The validity period.
        *   The digital signature of a trusted Certificate Authority (CA).

3.  **Client Verifies Server Certificate:** The client then performs several checks on the received certificate:
    *   **Trust:** It checks if the CA that signed the server's certificate is in its list of trusted root CAs (pre-installed in browsers and operating systems).
    *   **Validity:** It verifies the certificate's expiry date.
    *   **Domain Match:** It checks if the domain name in the certificate matches the domain name it's trying to connect to.
    *   **Signature:** It uses the CA's public key (from its trusted store) to verify the digital signature on the server's certificate. If the signature is valid, it confirms that the certificate hasn't been tampered with and was indeed issued by the claimed CA.

    *If any of these checks fail, the client will display a warning or terminate the connection (e.g., "This connection is not private").*

**B. Symmetric Key Negotiation:**

Assuming the server's certificate is valid:

4.  **Client Generates Pre-Master Secret:** The client generates a new, random "pre-master secret."

5.  **Client Encrypts Pre-Master Secret:** The client encrypts this pre-master secret using the **server's public key** (obtained from the server's certificate). This encrypted pre-master secret is sent to the server.
    *   *Why encrypt with the server's public key?* Only the server, with its corresponding private key, can decrypt this. This ensures that only the client and server know the pre-master secret, even if an attacker intercepts the communication.

6.  **Server Decrypts Pre-Master Secret:** The server uses its **private key** to decrypt the message and retrieve the pre-master secret.

7.  **Both Parties Generate Master Secret and Session Keys:** Both the client and the server independently use the client's random number, the server's random number, and the pre-master secret to deterministically generate the same "master secret." From this master secret, they then generate a set of **symmetric session keys** for encryption, decryption, and message authentication.

    *   *Why symmetric keys?* Asymmetric encryption (public/private key) is computationally intensive and slow. Symmetric encryption (using a single shared secret key) is much faster for bulk data transfer.

8.  **Finished Message:** Both parties send "Finished" messages, encrypted with the newly generated session keys, indicating they are ready to proceed with secure communication.

**Phase 2: Encrypted Data Transfer**

9.  **Application Data Exchange:** Now that the secure channel is established and both parties share the same symmetric session keys, all subsequent application data (HTTP requests and responses) is:
    *   **Encrypted:** Using the symmetric encryption key.
    *   **Authenticated (Integrity Checked):** Using a MAC (Message Authentication Code) generated with the symmetric authentication key. This ensures data hasn't been tampered with.

    *Each HTTP request and response is encrypted before sending and decrypted upon receiving.*

**Summary Flow:**

```
Client                                                         Server
----------------------------------------------------------------------
1. Client Hello (TLS version, cipher suites, random C)
                                      --------------------->
2. Server Hello (chosen TLS version, cipher suite, random S)
   Server Certificate (public key, domain, CA signature)
                                      <---------------------
3. Client verifies Server Certificate
   (checks CA trust, validity, domain, signature using CA's public key)

4. Client generates Pre-Master Secret (PMS)
5. Client encrypts PMS with Server's Public Key
                                      --------------------->
6. Server decrypts PMS with its Private Key

7. Both Client & Server generate Master Secret & Symmetric Session Keys
   (from C, S, and PMS)

8. Client Finished (encrypted with session key)
                                      --------------------->
9. Server Finished (encrypted with session key)
                                      <---------------------
         TLS Handshake Complete - Secure Channel Established

10. Encrypted HTTP Request (using symmetric session key)
                                      --------------------->
11. Encrypted HTTP Response (using symmetric session key)
                                      <---------------------
```

This entire process happens transparently to the user, typically within milliseconds. The use of a Certificate Authority and the combination of asymmetric and symmetric encryption ensures strong authentication, confidentiality, and data integrity for web communication.

### 4. Design a system for user authentication and authorization that supports multiple client types (web, mobile).

Designing a robust authentication and authorization system for multiple client types (web, mobile) requires a flexible and secure approach. **OAuth2** (for authorization) and **OpenID Connect (OIDC)** (for authentication on top of OAuth2) are the industry standards for this.

**Core Components:**

1.  **Authorization Server (Identity Provider - IdP):**
    *   Responsible for user authentication (verifying credentials) and issuing tokens.
    *   Manages user identities, roles, and permissions.
    *   Provides standard OAuth2/OIDC endpoints (authorization, token, userinfo, JWKS).
    *   Examples: Keycloak, Auth0, Okta, AWS Cognito, Google Identity Platform, Spring Authorization Server.

2.  **Resource Server(s) (Your APIs):**
    *   Your backend services that host protected resources.
    *   Accept and validate access tokens issued by the Authorization Server.
    *   Enforce authorization rules based on token scopes/claims.

3.  **Client Applications:**
    *   **Web Application (Frontend):** Typically a Single Page Application (SPA) or traditional web app.
    *   **Mobile Application (iOS/Android):** Native applications.

4.  **User Database / Directory:**
    *   Stores user credentials (hashed passwords), profiles, roles, etc.
    *   Can be integrated with the Authorization Server.

**Authentication & Authorization Flow (Recommended for Web & Mobile):**

The **Authorization Code Flow with PKCE (Proof Key for Code Exchange)** is the most secure and recommended flow for public clients (like SPAs and mobile apps) that cannot securely store a `client_secret`.

**A. User Authentication & Access Token Issuance:**

1.  **Client Initiates Login:**
    *   **Web App:** User clicks "Login." The SPA redirects the browser to the Authorization Server's `/authorize` endpoint.
    *   **Mobile App:** User taps "Login." The app opens a system browser tab (or web view) to the Authorization Server's `/authorize` endpoint.
    *   **Request Parameters:** `client_id`, `redirect_uri`, `response_type=code`, `scope` (e.g., `openid profile email`), `state` (CSRF protection), and crucially for PKCE, `code_challenge` and `code_challenge_method`.

2.  **User Authenticates with IdP:**
    *   The **Authorization Server** displays its login page.
    *   User enters credentials (username/password, social login, MFA).
    *   The Authorization Server authenticates the user.

3.  **User Grants Consent (if required):**
    *   If the client is requesting new scopes or it's the first time, the Authorization Server may ask the user to consent to the requested permissions.

4.  **Authorization Server Redirects with Code:**
    *   If authentication and consent are successful, the Authorization Server redirects the user's browser back to the `redirect_uri` provided by the client.
    *   This redirect includes an `authorization_code` and the `state` parameter.

5.  **Client Exchanges Code for Tokens:**
    *   The client application (backend for web, or direct for mobile) receives the `authorization_code`.
    *   The client makes a **direct, backend-channel POST request** to the Authorization Server's `/token` endpoint.
    *   **Request Parameters:** `grant_type=authorization_code`, `code` (received in step 4), `redirect_uri`, `client_id`, and for PKCE, `code_verifier` (the original secret used to generate the `code_challenge`).
    *   **Authorization Server Verification:** The server verifies the `code`, `redirect_uri`, `client_id`, and importantly, verifies that `code_verifier` matches the `code_challenge` sent in step 1.

6.  **Authorization Server Issues Tokens:**
    *   If valid, the Authorization Server issues:
        *   **`Access Token` (JWT):** Short-lived, used to access protected resources on the Resource Server.
        *   **`ID Token` (JWT):** For OIDC, contains basic user profile information (claims) used for client-side authentication.
        *   **`Refresh Token`:** Long-lived, used to obtain new access tokens without re-authenticating the user.

**B. API Authorization (Resource Server Access):**

1.  **Client Stores Tokens Securely:**
    *   **Web App (SPA):** Access Token and ID Token can be stored in browser memory (for short sessions) or securely in an HttpOnly cookie (if a backend for frontend (BFF) is used). Refresh Token should be HttpOnly and secure.
    *   **Mobile App:** Access Token and ID Token can be stored in secure storage (e.g., iOS KeyChain, Android Keystore). Refresh Token also securely.

2.  **Client Makes API Calls:**
    *   When the client needs to access a protected resource on your **Resource Server**, it includes the `Access Token` in the `Authorization` header: `Authorization: Bearer <ACCESS_TOKEN>`.

3.  **Resource Server Validates Access Token:**
    *   Your API gateway or individual backend services intercept the request.
    *   They validate the incoming `Access Token` (which is a JWT):
        *   **Signature Verification:** Using the Authorization Server's public key (fetched from its JWKS endpoint).
        *   **Expiration (`exp` claim):** Token is not expired.
        *   **Issuer (`iss` claim):** Token issued by the trusted Authorization Server.
        *   **Audience (`aud` claim):** Token is intended for this Resource Server.
        *   **Scopes/Permissions:** Extract scopes (e.g., `read:users`, `write:products`) or roles from the JWT payload and enforce granular access control.

4.  **Resource Server Grants/Denies Access:**
    *   If the token is valid and the client has the required permissions, the request proceeds to the business logic.
    *   Otherwise, the Resource Server returns `401 Unauthorized` or `403 Forbidden`.

**C. Refreshing Access Tokens:**

1.  **Access Token Expires:** When the `Access Token` expires, API calls will fail with `401 Unauthorized`.
2.  **Client Uses Refresh Token:** The client uses the `Refresh Token` (stored securely) to make a backend POST request to the Authorization Server's `/token` endpoint.
3.  **Request Parameters:** `grant_type=refresh_token`, `refresh_token`, `client_id`. (For confidential clients, `client_secret` would also be included).
4.  **Authorization Server Issues New Tokens:** If the refresh token is valid, the Authorization Server issues a new `Access Token` (and optionally a new `Refresh Token`).
5.  **Client Continues Operations:** The client replaces the old tokens with the new ones and retries the failed API request.

**D. Logout:**

1.  **Client Invalidates Local Tokens:** The client clears locally stored tokens.
2.  **Optional: Revoke Refresh Token:** The client can make a request to the Authorization Server's `/revoke` endpoint to explicitly invalidate the `Refresh Token` on the server-side, preventing it from being used to obtain new access tokens.
3.  **Optional: Redirect to IdP for Single Logout (SLO):** For more robust logout across multiple applications.

**Architecture Diagram:**

```
+-------------------------------------------------------------------------------------------------------------------------------------+
|                                                            Authentication & Authorization System                                      |
|                                                                                                                                     |
|  +---------------------+                                       +-------------------------+                                          |
|  |                     |                                       |                         |                                          |
|  |     User Database   |<------------------------------------->|   Authorization Server  |                                          |
|  |  (Users, Roles,     |                                       |   (Keycloak, Auth0,     |                                          |
|  |  Permissions)       |                                       |    Spring Auth Server)  |                                          |
|  +---------------------+                                       +-------------------------+                                          |
|           ^                                                        ^           ^           ^                                        |
|           |                                                        |           |           | (6) Issues Access/ID/Refresh Tokens    |
|           |                                                        |           |           |                                        |
|           |                                                        |           |           |                                        |
|           |                                                 (5) Exchanges Code for Tokens (PKCE)                                   |
|           |                                                        |           |                                                    |
|           |                                                        |           |                                                    |
|  +---------------------------------------------------------------------------------------------------------------------------------+
|  |                                                                                                                                 |
|  |  +---------------------+      +----------------------+      +---------------------+                                         |
|  |  |                     |      |                      |      |                     |                                         |
|  |  |    Web Application  |      |   Mobile Application |      |   Client Application|                                         |
|  |  |  (SPA / Backend for |      |   (iOS/Android Native)|      |   (Backend Service  |                                         |
|  |  |   Frontend (BFF))   |      |                      |      |    acting as client)|                                         |
|  |  +----------^----------+      +----------^----------+      +----------^----------+                                         |
|  |             |                       |                       |                                                                |
|  |  (1) Redirect to Auth Server       (1) Opens System Browser  (1) Direct API Calls (Client Credentials Grant)                |
|  |  (via browser) / Direct API        Tab to Auth Server                                                                       |
|  |                                                                                                                              |
|  
```