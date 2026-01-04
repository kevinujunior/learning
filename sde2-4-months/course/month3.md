# Month 3: Advanced Algorithms, Spring Boot & Database, and Mock Interviews

## Week 11: Advanced Algorithms and Data Structures

#### Topic 1: Heaps, Priority Queues, and Hash Tables (Revisited)

*   **Interview Perspective:** Heaps are crucial for priority queues and algorithms like Heap Sort. A deeper understanding of `HashMap` internal workings (load factor, resizing) and potential `TreeMap`/`LinkedHashMap` use cases is valuable.

*   **Code Snippet: PriorityQueue (Min-Heap)**

    ```java
    import java.util.Collections;
    import java.util.PriorityQueue;

    public class PriorityQueueDemo {
        public static void main(String[] args) {
            // Min-Heap (default behavior)
            PriorityQueue<Integer> minHeap = new PriorityQueue<>();
            minHeap.add(10);
            minHeap.add(5);
            minHeap.add(20);
            minHeap.add(3);

            System.out.println("Min-Heap elements (polling):");
            while (!minHeap.isEmpty()) {
                System.out.print(minHeap.poll() + " "); // Output: 3 5 10 20
            }
            System.out.println();

            // Max-Heap (using Collections.reverseOrder() or custom Comparator)
            PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            maxHeap.add(10);
            maxHeap.add(5);
            maxHeap.add(20);
            maxHeap.add(3);

            System.out.println("Max-Heap elements (polling):");
            while (!maxHeap.isEmpty()) {
                System.out.print(maxHeap.poll() + " "); // Output: 20 10 5 3
            }
            System.out.println();
        }
    }
    ```

*   **Interview Questions:**
    1.  What is a Heap? Explain Min-Heap and Max-Heap.
    2.  How is `PriorityQueue` implemented in Java?
    3.  Give use cases for `PriorityQueue`.
    4.  Explain the importance of `load factor` in `HashMap`.
    5.  How do `TreeMap` and `LinkedHashMap` differ in their internal structure and use cases?

*   **Practice Questions:**
    1.  Find the Kth smallest element in an array using a `Min-Heap`.
    2.  Merge K sorted lists using a `Min-Heap`.
    3.  Implement a `Max-Heap` from scratch (optional, but good for understanding).
    4.  Given a stream of numbers, find the median at any point (using two heaps).

#### Topic 2: Advanced Graph Algorithms (Minimum Spanning Tree, Shortest Path)

*   **Interview Perspective:** Understand Prim's and Kruskal's for MST. For shortest paths, deepen your understanding of Dijkstra's for single-source and Floyd-Warshall for all-pairs. Focus on concepts and general approach rather than memorizing complex code.

*   **Key Concepts:**
    *   **Minimum Spanning Tree (MST):** Prim's Algorithm, Kruskal's Algorithm (greedy approach).
    *   **Shortest Path Algorithms:**
        *   Dijkstra's Algorithm (single source, non-negative edge weights).
        *   Bellman-Ford Algorithm (single source, handles negative edge weights, detects negative cycles).
        *   Floyd-Warshall Algorithm (all-pairs shortest path).
    *   **Topological Sort:** For Directed Acyclic Graphs (DAGs).

*   **Interview Questions:**
    1.  What is a Minimum Spanning Tree? Explain Prim's or Kruskal's algorithm.
    2.  Explain Dijkstra's algorithm. What are its limitations?
    3.  When would you use Bellman-Ford over Dijkstra's?
    4.  What is a topological sort? Give an example of its application.
    5.  What is the time complexity of BFS/DFS for a graph represented by an adjacency list?

*   **Practice Questions:**
    1.  Implement Dijkstra's algorithm to find the shortest path from a source node to all other nodes.
    2.  Given a list of courses and their prerequisites, find a valid order to take all courses (Topological Sort).
    3.  Implement Prim's algorithm for MST.
    4.  Find the number of islands in a 2D grid (DFS/BFS application).

## Week 12: Spring Boot Fundamentals

#### Topic 1: Introduction to Spring Boot and Core Concepts

*   **Interview Perspective:** Spring Boot is industry-standard for Java backend development. Understand its advantages, the concept of IoC and DI, and how to create basic RESTful services.

*   **Key Concepts:**
    *   **Spring vs. Spring Boot:** Differences and advantages of Spring Boot.
    *   **Dependency Injection (DI):** `@Autowired`, `@Component`, `@Service`, `@Repository`, `@Controller`.
    *   **Inversion of Control (IoC):** The Spring IoC container.
    *   **Spring Annotations:** `@SpringBootApplication`, `@RestController`, `@GetMapping`, `@PostMapping`, `@RequestMapping`.
    *   **Starter Dependencies:** `spring-boot-starter-web`, `spring-boot-starter-data-jpa`.
    *   **Auto-configuration:** How Spring Boot automatically configures applications.

*   **Code Snippet: Simple Spring Boot REST Controller**

    ```java
    // Application.java (Main class)
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    public class MyApplication {
        public static void main(String[] args) {
            SpringApplication.run(MyApplication.class, args);
        }
    }

    // HelloController.java
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RestController;

    @RestController // Combines @Controller and @ResponseBody
    public class HelloController {

        @GetMapping("/hello")
        public String sayHello() {
            return "Hello from Spring Boot!";
        }

        @GetMapping("/greet/{name}")
        public String greetUser(@PathVariable String name) {
            return "Greetings, " + name + "!";
        }
    }
    ```

*   **Interview Questions:**
    1.  What is Spring Boot? What benefits does it offer over traditional Spring?
    2.  Explain Inversion of Control (IoC) and Dependency Injection (DI).
    3.  What is a `Bean` in Spring? How is it managed by the IoC container?
    4.  What does the `@SpringBootApplication` annotation do?
    5.  Differentiate between `@Component`, `@Service`, `@Repository`, and `@Controller`.
    6.  How does Spring Boot achieve auto-configuration?

*   **Practice Questions:**
    1.  Create a simple Spring Boot application with a REST endpoint that returns a JSON object.
    2.  Implement a service layer to handle business logic in your Spring Boot app.
    3.  Use `@Autowired` to inject a dependency from one component to another.

#### Topic 2: Spring Data JPA and Database Interaction

*   **Interview Perspective:** Focus on how Spring Data JPA simplifies database access, the role of `EntityManager`, and common repository methods. Understand the ORM concept.

*   **Key Concepts:**
    *   **ORM (Object-Relational Mapping):** How Java objects map to database tables.
    *   **JPA (Java Persistence API):** Standard for ORM.
    *   **Hibernate:** A popular JPA implementation.
    *   **Spring Data JPA:** Abstraction layer over JPA, simplifies repository implementation.
    *   **`@Entity`:** To mark a class as a JPA entity.
    *   **`@Id`, `@GeneratedValue`:** For primary keys.
    *   **`JpaRepository`:** Provides CRUD operations and custom query methods.
    *   **`application.properties`/`application.yml`:** Database configuration.

*   **Code Snippet: Spring Data JPA (Entity and Repository)**

    ```java
    // Student.java (Entity)
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;

    @Entity
    public class Student {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private int age;

        // Constructors, Getters, Setters, toString()
        public Student() {}

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        @Override
        public String toString() {
            return "Student{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
        }
    }

    // StudentRepository.java (Repository Interface)
    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.List;

    public interface StudentRepository extends JpaRepository<Student, Long> {
        List<Student> findByAgeGreaterThan(int age); // Custom query method
    }

    // StudentController.java (Using Repository)
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/students")
    public class StudentController {

        @Autowired
        private StudentRepository studentRepository;

        @GetMapping
        public List<Student> getAllStudents() {
            return studentRepository.findAll();
        }

        @GetMapping("/{id}")
        public Optional<Student> getStudentById(@PathVariable Long id) {
            return studentRepository.findById(id);
        }

        @PostMapping
        public Student createStudent(@RequestBody Student student) {
            return studentRepository.save(student);
        }

        @GetMapping("/age/{age}")
        public List<Student> getStudentsOlderThan(@PathVariable int age) {
            return studentRepository.findByAgeGreaterThan(age);
        }
    }
    ```

*   **Interview Questions:**
    1.  What is ORM? How does JPA facilitate it?
    2.  Explain the role of `JpaRepository`. How does it differ from `CrudRepository`?
    3.  How do you define custom queries in Spring Data JPA? (`@Query` annotation, method naming conventions)
    4.  What is the N+1 problem in JPA? How can it be avoided?
    5.  Explain `@Transactional` annotation. What is its purpose?

*   **Practice Questions:**
    1.  Build a full CRUD REST API for a `Product` entity using Spring Boot and Spring Data JPA.
    2.  Implement custom query methods in your repository (e.g., `findByNameStartingWith`, `findByCategoryAndPriceLessThan`).
    3.  Configure your Spring Boot application to connect to an H2 in-memory database.

## Week 13: Microservices, Caching, and Security Basics

#### Topic 1: Microservices Architecture and RESTful APIs

*   **Interview Perspective:** Understand the benefits and challenges of microservices. Be able to discuss REST principles in detail.

*   **Key Concepts:**
    *   **Monolithic vs. Microservices:** Pros and cons of each.
    *   **Service Discovery:** Eureka, Consul.
    *   **API Gateway:** Zuul, Spring Cloud Gateway.
    *   **Inter-service Communication:** REST, gRPC, Message Queues.
    *   **Statelessness:** Important for scaling microservices.
    *   **Idempotency:** For API design.

*   **Interview Questions:**
    1.  What are Microservices? What are their advantages and disadvantages?
    2.  When would you choose a microservices architecture over a monolith?
    3.  What is an API Gateway? Why is it used in microservices?
    4.  Explain the different HTTP methods (GET, POST, PUT, DELETE) and their conventional usage in REST.
    5.  What does it mean for a REST API to be stateless?
    6.  What is `idempotence` in the context of REST APIs?

*   **Practice Questions:**
    1.  Design a high-level microservices architecture for an e-commerce platform.
    2.  Explain how two microservices can communicate with each other.
    3.  Describe how you would ensure data consistency in a microservices environment.

#### Topic 2: Caching Strategies and Basic Security

*   **Interview Perspective:** Caching is crucial for performance. Be familiar with different cache types and eviction policies. Basic authentication/authorization concepts are also important.

*   **Key Concepts:**
    *   **Cache Types:** In-memory (Guava, Caffeine), distributed (Redis, Memcached), CDN.
    *   **Cache Eviction Policies:** LRU (Least Recently Used), LFU (Least Frequently Used), FIFO.
    *   **Spring Cache Abstraction:** `@Cacheable`, `@CachePut`, `@CacheEvict`.
    *   **Authentication:** Verifying user identity (e.g., username/password).
    *   **Authorization:** Verifying user permissions (e.g., roles, access control).
    *   **OAuth2 / JWT:** For securing APIs (brief overview).
    *   **Cross-Site Scripting (XSS), SQL Injection:** Basic awareness of common vulnerabilities.

*   **Code Snippet: Spring Caching (Conceptual)**

    ```java
    import org.springframework.cache.annotation.CacheEvict;
    import org.springframework.cache.annotation.Cacheable;
    import org.springframework.stereotype.Service;

    import java.util.concurrent.TimeUnit;

    @Service
    public class ProductService {

        // Simulate a slow database call
        private Product findProductInDatabase(Long id) {
            try {
                TimeUnit.SECONDS.sleep(2); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return new Product(id, "Product " + id, 100.0 * id);
        }

        @Cacheable(value = "products", key = "#id")
        public Product getProductById(Long id) {
            System.out.println("Fetching product from database: " + id);
            return findProductInDatabase(id);
        }

        @CacheEvict(value = "products", key = "#id")
        public void deleteProduct(Long id) {
            System.out.println("Deleting product and evicting from cache: " + id);
            // logic to delete from database
        }

        // Dummy Product class for demonstration
        static class Product {
            Long id;
            String name;
            double price;

            public Product(Long id, String name, double price) {
                this.id = id;
                this.name = name;
                this.price = price;
            }

            @Override
            public String toString() {
                return "Product{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + '}';
            }
        }
    }

    // Main class (add @EnableCaching to your @SpringBootApplication)
    // To run this, you would need to add spring-boot-starter-cache and a cache provider like Caffeine or Redis.
    // @SpringBootApplication
    // @EnableCaching
    // public class MyCachingApplication { ... }
    ```

*   **Interview Questions:**
    1.  What is caching? Why is it important in distributed systems?
    2.  Explain different cache eviction policies.
    3.  How can you implement caching in a Spring Boot application?
    4.  Differentiate between authentication and authorization.
    5.  What are some common web security vulnerabilities and how to prevent them?
    6.  Briefly explain JWT (JSON Web Tokens).

*   **Practice Questions:**
    1.  Add caching to your existing Spring Boot CRUD application using `@Cacheable` and `@CacheEvict`.
    2.  Implement basic HTTP Basic Authentication in a Spring Boot application (using Spring Security).
    3.  Explain a scenario where you would use a CDN for caching.

## Week 14: Design Patterns and DevOps Basics

#### Topic 1: Design Patterns (Gang of Four)

*   **Interview Perspective:** Design patterns demonstrate problem-solving skills and understanding of good software design principles. Focus on Creational, Structural, and Behavioral patterns.

*   **Key Concepts:**
    *   **Creational Patterns:** Singleton, Factory Method, Abstract Factory, Builder, Prototype.
    *   **Structural Patterns:** Adapter, Decorator, Facade, Proxy, Composite, Bridge.
    *   **Behavioral Patterns:** Observer, Strategy, Command, Iterator, State, Template Method.
    *   **When to use each pattern:** Understand the problem they solve.

*   **Code Snippet: Singleton Pattern (Lazy Initialization, Thread-Safe)**

    ```java
    public class Singleton {
        private static Singleton instance; // Volatile ensures visibility of changes

        private Singleton() {
            // Private constructor to prevent instantiation from outside
            System.out.println("Singleton instance created.");
        }

        // Double-checked locking for thread-safe lazy initialization
        public static Singleton getInstance() {
            if (instance == null) { // First check: avoid synchronization overhead if instance exists
                synchronized (Singleton.class) { // Synchronize only if instance is null
                    if (instance == null) { // Second check: ensure only one instance is created
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }

        public void showMessage() {
            System.out.println("Hello from Singleton!");
        }

        public static void main(String[] args) {
            Singleton s1 = Singleton.getInstance();
            s1.showMessage();

            Singleton s2 = Singleton.getInstance();
            s2.showMessage();

            System.out.println("Are s1 and s2 the same instance? " + (s1 == s2)); // true
        }
    }
    ```

*   **Interview Questions:**
    1.  What are Design Patterns? Why are they important?
    2.  Explain the Singleton pattern. When would you use it?
    3.  Describe the Factory Method pattern. How does it improve code flexibility?
    4.  Explain the Observer pattern with a real-world example.
    5.  What is the Decorator pattern? How does it differ from inheritance?
    6.  Differentiate between Strategy and Template Method patterns.

*   **Practice Questions:**
    1.  Implement the Builder pattern for a complex object (e.g., a `Car` with many options).
    2.  Apply the Strategy pattern to implement different payment methods (e.g., credit card, PayPal).
    3.  Design a logging system using the Factory Method pattern to create different types of loggers (file, console).

#### Topic 2: Introduction to DevOps and Cloud

*   **Interview Perspective:** Awareness of CI/CD, containerization, and cloud platforms is increasingly expected. Focus on understanding the concepts rather than deep technical details.

*   **Key Concepts:**
    *   **DevOps Principles:** Culture, automation, lean, measurement, sharing.
    *   **Continuous Integration (CI):** Automating build and test. (Jenkins, GitLab CI, GitHub Actions)
    *   **Continuous Delivery/Deployment (CD):** Automating release process.
    *   **Containerization:** Docker (images, containers).
    *   **Orchestration:** Kubernetes (briefly).
    *   **Cloud Platforms:** AWS, Azure, GCP (basic services like EC2, S3, RDS).
    *   **Monitoring & Logging:** Prometheus, Grafana, ELK Stack.

*   **Interview Questions:**
    1.  What is DevOps? What are its core principles?
    2.  Explain Continuous Integration and Continuous Delivery/Deployment.
    3.  What is Docker? What problem does it solve?
    4.  Briefly explain what Kubernetes is used for.
    5.  What are some common cloud services you are familiar with (e.g., from AWS, Azure, or GCP)?
    6.  Why is monitoring and logging important in production environments?

*   **Practice Questions:**
    1.  Describe the steps in a typical CI/CD pipeline for a Java application.
    2.  Explain how Docker helps in creating reproducible development environments.
    3.  Research and briefly explain one common AWS/Azure/GCP service relevant to deploying a Java application.

## Week 15-16: Revision and Mock Interviews

#### Topic 1: Comprehensive Revision

*   **Interview Perspective:** This is where everything comes together. Review all topics from Months 1 and 2. Focus on areas where you felt weaker. Revisit important code snippets and interview questions.

*   **Action Items:**
    *   **Review all notes:** Go through your notes and highlights from the past 14 weeks.
    *   **Re-solve tough problems:** Attempt challenging LeetCode/HackerRank problems you struggled with earlier.
    *   **Flashcards:** Create flashcards for definitions, concepts, and time complexities.
    *   **Whiteboard Practice:** Practice explaining concepts and writing code on a whiteboard or virtual whiteboard tool.
    *   **Focus on weak areas:** Spend extra time on topics you're less confident about.

#### Topic 2: Mock Interviews and Behavioral Questions

*   **Interview Perspective:** Technical knowledge is only part of the equation. Practice articulating your thoughts, discussing past experiences, and demonstrating problem-solving skills.

*   **Action Items:**
    *   **Schedule Mock Interviews:** Ask peers, mentors, or use online platforms for mock interviews. Get feedback on your communication, problem-solving approach, and technical accuracy.
    *   **Behavioral Questions:** Prepare answers for common behavioral questions (e.g., "Tell me about yourself," "Why this company?", "Describe a challenging project," "How do you handle conflict?"). Use the STAR method (Situation, Task, Action, Result).
    *   **System Design Discussions:** Practice discussing high-level system design problems. Focus on trade-offs, scalability, and justifying your choices.
    *   **Questions for the Interviewer:** Always have intelligent questions ready to ask the interviewer. This shows engagement and curiosity.
    *   **Resume Review:** Ensure your resume is up-to-date and highlights your skills and experiences effectively.

---

## Indexed Solution Section (Coming Soon in a Separate Document/File for brevity)

This section would typically include detailed solutions to all the practice questions mentioned throughout the 3 months, indexed by topic and question number. Each solution would include:
*   Problem statement
*   Solution code
*   Explanation of the logic
*   Time and Space Complexity analysis

**Note:** Providing detailed solutions for all practice questions here would make this document excessively long. It is recommended to maintain a separate repository or document for your practice solutions, allowing you to link to specific solutions for each question.

---

Good luck with your preparation! Consistency and dedicated practice are key to success.