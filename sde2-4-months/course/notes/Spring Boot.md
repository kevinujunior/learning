# Spring Boot SDE-2 Interview Preparation Guide

> **Target Audience:** SDE-1 developers aiming for SDE-2 proficiency  
> **Coverage:** Beginner concepts → Production-level depth → Interview readiness

---

# Table of Contents

1. [Spring Core](#1-spring-core)
2. [Spring Boot Fundamentals](#2-spring-boot-fundamentals)
3. [REST API Development](#3-rest-api-development)
4. [Data Access Layer — JPA & Hibernate](#4-data-access-layer--jpa--hibernate)
5. [Database Design](#5-database-design-sde-2-level)
6. [Spring Security](#6-spring-security)
7. [Microservices](#7-microservices-concepts)
8. [Performance & Scalability](#8-performance--scalability)
9. [Testing](#9-testing)
10. [Logging & Monitoring](#10-logging--monitoring)
11. [Build & Deployment](#11-build--deployment)
12. [Real-World Use Cases](#12-real-world-use-cases)
13. [Interview Preparation](#13-interview-preparation)

---

# 1. Spring Core

## What is Spring and Why Does It Exist?

Before Spring (early 2000s), Java EE (J2EE) developers had to write huge amounts of boilerplate code just to wire classes together, manage their lifecycles, and connect to databases. Creating an enterprise app required XML configuration files hundreds of lines long.

**Spring was created to solve:**
- Excessive boilerplate code
- Tight coupling between classes (hard to test, hard to swap implementations)
- Complex lifecycle management of objects

**Real-life analogy:** Think of a restaurant kitchen. Before Spring, every chef (class) had to personally go buy their own ingredients (dependencies), cook everything from scratch, and clean up after. Spring is like a kitchen manager — it buys ingredients, delivers them to the right chef, and manages the whole workflow. Chefs just focus on cooking.

---

## Inversion of Control (IoC)

**The Problem Without IoC:**
```java
// Tight coupling — UserService creates its own dependency
public class UserService {
    private UserRepository repo = new UserRepository(); // BAD
    // If UserRepository changes, UserService must change too
}
```

**With IoC:**
The control of creating and managing objects is *inverted* — instead of your code creating dependencies, a container (Spring) creates and manages them for you.

```java
public class UserService {
    private UserRepository repo; // Spring will provide this

    public UserService(UserRepository repo) {
        this.repo = repo;
    }
}
```

**Key Insight:** You don't call `new`. Spring does. Your class just *declares what it needs*.

---

## Dependency Injection (DI)

DI is the *mechanism* through which IoC is implemented. Spring *injects* the dependencies your class needs.

### Three Types of DI

**1. Constructor Injection (Recommended)**
```java
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Spring automatically injects these via constructor
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
}
```

**Why it's best:** Dependencies are immutable (`final`), object is always in a valid state, easy to test.

**2. Setter Injection (Avoid for mandatory deps)**
```java
@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**3. Field Injection (Avoid in production)**
```java
@Service
public class UserService {
    @Autowired // Spring injects via reflection
    private UserRepository userRepository;
}
```

**Why to avoid field injection:** Can't make field `final`, hard to test (must use reflection), hides dependencies.

---

## Bean Lifecycle (Step-by-Step)

A **Bean** is any object managed by the Spring container. Here's what happens from start to finish:

```
Spring starts
     │
     ▼
1. Load Configuration (scan @Component, @Bean, XML, etc.)
     │
     ▼
2. Instantiate the Bean (calls constructor)
     │
     ▼
3. Populate Dependencies (inject @Autowired fields/constructors)
     │
     ▼
4. Call @PostConstruct (your custom init logic)
     │
     ▼
5. Bean is READY — used by your application
     │
     ▼
6. Application shuts down
     │
     ▼
7. Call @PreDestroy (your custom cleanup logic)
     │
     ▼
8. Bean is destroyed
```

**Code Example:**
```java
@Component
public class DatabaseConnectionPool {

    @PostConstruct
    public void init() {
        // Called after Spring injects all dependencies
        System.out.println("Initializing connection pool...");
    }

    @PreDestroy
    public void cleanup() {
        // Called before Spring destroys this bean
        System.out.println("Closing all connections...");
    }
}
```

---

## ApplicationContext vs BeanFactory

| Feature | BeanFactory | ApplicationContext |
|---|---|---|
| Bean creation | Lazy (on first request) | Eager (at startup) |
| Event publishing | No | Yes |
| Internationalization (i18n) | No | Yes |
| AOP integration | Limited | Full |
| Used in production? | Rarely | Always |

**Bottom line:** Always use `ApplicationContext`. `BeanFactory` is just its parent interface. Spring Boot gives you `ApplicationContext` by default.

---

## Stereotype Annotations

These annotations tell Spring: "Hey, this class should be managed as a bean."

| Annotation | Used For | Extra Behavior |
|---|---|---|
| `@Component` | Generic Spring bean | None |
| `@Service` | Business logic layer | None (just semantic) |
| `@Repository` | Data access layer | Translates DB exceptions to Spring exceptions |
| `@Controller` | Web layer (MVC) | Handles HTTP requests |
| `@RestController` | REST API | `@Controller` + `@ResponseBody` |

```java
@Repository
public class UserRepository {
    // Spring wraps DB exceptions in DataAccessException hierarchy
}

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}

@RestController
public class UserController {
    @Autowired
    private UserService userService;
}
```

---

## @Bean vs @Component

| | `@Component` | `@Bean` |
|---|---|---|
| Where it goes | On the **class** | On a **method** inside `@Configuration` |
| Who creates it | Spring (via classpath scan) | You (method body creates the object) |
| Use when | You own the class | You don't own the class (3rd party lib) |

```java
// @Component — you own the class
@Component
public class MyEmailService {
    // Spring instantiates this
}

// @Bean — you don't own the class (e.g., Gson, RestTemplate)
@Configuration
public class AppConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---

## Bean Scopes

### Singleton (Default)
One instance per Spring container. All requests for this bean get the **same object**.

```java
@Component
@Scope("singleton") // default, no need to specify
public class UserService {
    // ONE instance shared across the entire application
}
```

**Use when:** Stateless services (most services, repositories, controllers).

### Prototype
New instance every time it's requested from the container.

```java
@Component
@Scope("prototype")
public class ShoppingCart {
    // NEW instance for each injection/getBean() call
    private List<Item> items = new ArrayList<>();
}
```

**Use when:** Stateful beans that should not be shared (e.g., shopping cart, request processors).

**Common Mistake:** Injecting a prototype bean into a singleton — the singleton will always get the same prototype instance (the one injected at creation time). To get a new prototype every time, use `ApplicationContext.getBean()` or Spring's `@Lookup`.

---

## Autowiring and Resolving Ambiguity

### @Primary
When multiple beans of the same type exist, mark one as the default.

```java
public interface PaymentGateway {
    void processPayment(double amount);
}

@Component
@Primary // Used by default when PaymentGateway is injected
public class StripePaymentGateway implements PaymentGateway { }

@Component
public class PaypalPaymentGateway implements PaymentGateway { }
```

### @Qualifier
Specify exactly which bean to inject by name.

```java
@Service
public class OrderService {

    @Autowired
    @Qualifier("paypalPaymentGateway") // inject PayPal explicitly
    private PaymentGateway paymentGateway;
}
```

---

## Circular Dependency

A circular dependency occurs when Bean A needs Bean B, and Bean B needs Bean A.

```
UserService → EmailService → UserService (CIRCULAR!)
```

```java
@Service
public class UserService {
    @Autowired
    private EmailService emailService; // needs EmailService
}

@Service
public class EmailService {
    @Autowired
    private UserService userService; // needs UserService — CIRCULAR!
}
```

**Spring will throw:** `BeanCurrentlyInCreationException`

**Solutions:**
1. **Refactor** — most circular dependencies indicate a design problem. Extract shared logic to a third service.
2. **@Lazy** — tells Spring to inject a proxy initially and resolve lazily:
```java
@Autowired
@Lazy
private EmailService emailService;
```
3. **Setter injection** — Spring can resolve circular deps via setters (not constructors).

**Interview Answer:** "Circular dependency is a design smell. I first look to refactor — often there's a missing service abstraction. If refactoring isn't immediately possible, I use `@Lazy`."

---

## Lazy Initialization

By default, Spring creates all singleton beans at startup. With `@Lazy`, the bean is only created when first requested.

```java
@Component
@Lazy
public class HeavyReportService {
    // Only initialized when someone first requests this bean
}
```

**Global lazy init in application.properties:**
```properties
spring.main.lazy-initialization=true
```

**Tradeoff:**
- ✅ Faster startup time
- ❌ Errors surface later (first request instead of startup)
- ❌ First request is slower

---

## Common Interview Questions — Spring Core

**Q: What is the difference between IoC and DI?**  
A: IoC is the *principle* — control of object creation is inverted from your code to the framework. DI is the *pattern* used to implement IoC — dependencies are *injected* into classes rather than created inside them.

**Q: Why prefer constructor injection over field injection?**  
A: Constructor injection allows fields to be `final` (immutable), makes dependencies explicit, and makes unit testing easy without Spring (just call the constructor with mocks).

**Q: What happens if two beans of the same type exist and you don't use @Primary or @Qualifier?**  
A: Spring throws `NoUniqueBeanDefinitionException`.

**Q: Can you have a singleton bean depend on a prototype bean?**  
A: Yes, but you need to be careful — the singleton will always receive the same prototype instance injected at creation. Use `@Lookup` or `ObjectProvider<T>` if you need a new prototype each time.

---

# 2. Spring Boot Fundamentals

## Why Spring Boot Was Introduced

Spring Framework was powerful but had a steep learning curve. Setting up a simple web app required:
- Dozens of XML configuration files
- Manual dependency version management (incompatibility nightmares)
- Manual server setup (deploy WAR to Tomcat)
- Hours of boilerplate before writing any business logic

**Spring Boot solves this with three ideas:**
1. **Auto-configuration** — guess and configure what you need
2. **Starter dependencies** — curated, compatible dependency bundles
3. **Embedded server** — run as a JAR, no external server needed

---

## Spring vs Spring Boot

| Aspect | Spring Framework | Spring Boot |
|---|---|---|
| Configuration | Manual XML or Java config | Auto-configured |
| Server | External (deploy WAR) | Embedded (run JAR) |
| Dependencies | Manual version management | Starters manage versions |
| Startup time | Slow setup | `spring-boot-starter-web` and go |
| Production features | Manual setup | Actuator out of the box |

---

## How Auto-Configuration Actually Works

This is a favorite interview question. Let's trace exactly what happens.

**Step 1:** Spring Boot scans `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` (in spring-boot-autoconfigure JAR).

**Step 2:** This file lists hundreds of `AutoConfiguration` classes, e.g., `DataSourceAutoConfiguration`, `WebMvcAutoConfiguration`.

**Step 3:** Each auto-config class uses `@ConditionalOn*` annotations to only activate if conditions are met:

```java
@Configuration
@ConditionalOnClass(DataSource.class)        // Only if DataSource class is on classpath
@ConditionalOnMissingBean(DataSource.class)  // Only if YOU haven't defined a DataSource bean
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceAutoConfiguration {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        // Creates HikariCP DataSource with properties from application.properties
        return properties.initializeDataSourceBuilder().build();
    }
}
```

**Common `@Conditional` annotations:**

| Annotation | Activates When |
|---|---|
| `@ConditionalOnClass` | A class exists on the classpath |
| `@ConditionalOnMissingBean` | A bean of that type doesn't exist |
| `@ConditionalOnProperty` | A property is set in config |
| `@ConditionalOnWebApplication` | Running as a web app |

**Practical implication:** If you define your own `DataSource` bean, Spring Boot's auto-configuration backs off (`@ConditionalOnMissingBean`). You always override auto-config by defining your own bean.

**To see what was configured:** Add `--debug` flag or `logging.level.org.springframework.boot.autoconfigure=DEBUG` and check the conditions report.

---

## Starter Dependencies

Instead of adding 10 individual dependencies, you add one starter:

```xml
<!-- Instead of adding spring-web, spring-webmvc, jackson-databind, etc. separately -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- Spring Boot manages the version via spring-boot-dependencies BOM -->
</dependency>
```

**Common starters:**

| Starter | What It Includes |
|---|---|
| `spring-boot-starter-web` | Spring MVC, Embedded Tomcat, Jackson |
| `spring-boot-starter-data-jpa` | Hibernate, Spring Data JPA, connection pooling |
| `spring-boot-starter-security` | Spring Security |
| `spring-boot-starter-test` | JUnit 5, Mockito, AssertJ |
| `spring-boot-starter-actuator` | Health, metrics, info endpoints |
| `spring-boot-starter-data-redis` | Redis client, Spring Data Redis |

---

## application.properties vs application.yml

Both serve the same purpose. `yml` is hierarchical and avoids repetition.

**application.properties:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=secret
spring.datasource.hikari.maximum-pool-size=10
server.port=8080
```

**application.yml:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: postgres
    password: secret
    hikari:
      maximum-pool-size: 10
server:
  port: 8080
```

**Which to use:** `yml` for complex, nested configs. Properties for simple configs. Never mix in same project.

---

## Profiles

Profiles let you have different configurations for different environments.

```yaml
# application.yml (common config)
spring:
  application:
    name: my-service

---
# application-dev.yml
spring:
  datasource:
    url: jdbc:h2:mem:devdb  # in-memory DB for dev
  jpa:
    show-sql: true

---
# application-prod.yml
spring:
  datasource:
    url: jdbc:postgresql://prod-server:5432/mydb
  jpa:
    show-sql: false
```

**Activating profiles:**
```bash
# Via command line
java -jar app.jar --spring.profiles.active=prod

# Via environment variable
export SPRING_PROFILES_ACTIVE=prod

# Via application.properties
spring.profiles.active=dev
```

**Profile-specific beans:**
```java
@Service
@Profile("dev")
public class MockEmailService implements EmailService {
    // Used only in dev — doesn't actually send emails
}

@Service
@Profile("prod")
public class SmtpEmailService implements EmailService {
    // Used in prod — sends real emails
}
```

---

## Embedded Server (Tomcat)

Spring Boot embeds Tomcat (or Jetty/Undertow) inside the JAR. Your app starts with `main()`.

```
Traditional:       Spring Boot:
+-----------+      +------------------+
| Tomcat    |      | JAR              |
|   +-------+      |  +------------+ |
|   | WAR   |      |  | App Code   | |
|   +-------+      |  +------------+ |
+-----------+      |  +------------+ |
                   |  | Tomcat     | |
                   |  | (embedded) | |
                   |  +------------+ |
                   +------------------+
```

**Switching to Jetty:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

---

## Spring Boot Startup Flow

```
main() → SpringApplication.run()
    │
    ├── Create ApplicationContext
    ├── Load Properties & Profiles
    ├── Run Auto-Configuration (ConditionalOn checks)
    ├── Instantiate all Singleton Beans
    ├── Run ApplicationRunner / CommandLineRunner beans
    └── Start Embedded Server (Tomcat listens on port 8080)
```

```java
@SpringBootApplication // = @Configuration + @ComponentScan + @EnableAutoConfiguration
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

**`@SpringBootApplication` is shorthand for:**
- `@Configuration` — this is a configuration class
- `@ComponentScan` — scan this package and subpackages for beans
- `@EnableAutoConfiguration` — enable auto-configuration

---

# 3. REST API Development

## What is REST?

REST (Representational State Transfer) is an architectural style for building APIs over HTTP. Key constraints:
- **Stateless** — server stores no client state between requests
- **Resource-based** — everything is a resource (User, Order, Product), accessed via URLs
- **Uniform interface** — consistent use of HTTP methods and status codes

---

## HTTP Methods and Idempotency

| Method | Purpose | Idempotent? | Safe? | Has Body? |
|---|---|---|---|---|
| GET | Retrieve resource | ✅ Yes | ✅ Yes | No |
| POST | Create resource | ❌ No | ❌ No | Yes |
| PUT | Replace resource completely | ✅ Yes | ❌ No | Yes |
| PATCH | Partially update resource | ❌ No* | ❌ No | Yes |
| DELETE | Remove resource | ✅ Yes | ❌ No | No |

**Idempotent** = calling it N times has the same effect as calling it once.  
**Safe** = doesn't modify data.

*PATCH is not idempotent by definition, though specific implementations can be.

---

## Important HTTP Status Codes

| Code | Meaning | Use When |
|---|---|---|
| 200 OK | Success | GET, PUT, PATCH success |
| 201 Created | Resource created | POST success |
| 204 No Content | Success, no body | DELETE success |
| 400 Bad Request | Client sent invalid data | Validation failure |
| 401 Unauthorized | Not authenticated | Missing/invalid token |
| 403 Forbidden | Authenticated but no permission | Access denied |
| 404 Not Found | Resource doesn't exist | Wrong ID |
| 409 Conflict | State conflict | Duplicate resource |
| 422 Unprocessable Entity | Semantic validation failure | Business rule violation |
| 500 Internal Server Error | Server crashed | Unexpected exception |

---

## Controllers

```java
@RestController // = @Controller + @ResponseBody
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDto created = userService.create(request);
        URI location = URI.create("/api/v1/users/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## @PathVariable vs @RequestParam vs @RequestBody

```
GET /users/123          → @PathVariable id = 123
GET /users?page=2&size=10 → @RequestParam page = 2, size = 10
POST /users (body JSON) → @RequestBody CreateUserRequest
```

```java
@GetMapping("/{id}/orders")
public List<OrderDto> getUserOrders(
        @PathVariable Long id,                          // /123/orders
        @RequestParam(required = false) String status,  // ?status=PENDING
        @RequestBody FilterRequest filter) {            // JSON body (rare for GET)
```

---

## Validation

```java
// 1. Add to pom.xml
// spring-boot-starter-validation

// 2. Annotate your DTO
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    @Email(message = "Must be a valid email")
    private String email;

    @Min(18)
    @Max(120)
    private Integer age;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}

// 3. Use @Valid in controller
@PostMapping
public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
    // If validation fails, Spring throws MethodArgumentNotValidException
}
```

---

## Exception Handling

### @ExceptionHandler (controller-level)
```java
@RestController
public class UserController {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse("USER_NOT_FOUND", ex.getMessage()));
    }
}
```

### @ControllerAdvice (global — preferred)
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_FAILED", errors.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "Something went wrong"));
    }
}
```

**Standard error response structure:**
```json
{
  "errorCode": "USER_NOT_FOUND",
  "message": "User with id 42 not found",
  "timestamp": "2024-01-15T10:30:00Z",
  "path": "/api/v1/users/42"
}
```

---

## Pagination & Sorting

```java
@GetMapping
public ResponseEntity<Page<UserDto>> getUsers(
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable) {
    return ResponseEntity.ok(userService.getAll(pageable));
}
```

Request: `GET /users?page=0&size=10&sort=name,asc`

Response includes pagination metadata:
```json
{
  "content": [...],
  "pageable": { "pageNumber": 0, "pageSize": 10 },
  "totalElements": 250,
  "totalPages": 25,
  "last": false
}
```

---

## API Versioning Strategies

| Strategy | Example | Pros | Cons |
|---|---|---|---|
| URL versioning | `/api/v1/users` | Simple, cacheable | URL pollution |
| Header versioning | `Accept: application/vnd.myapi.v1+json` | Clean URLs | Complex |
| Query param | `/users?version=1` | Simple | Easy to miss |

**Most common in practice:** URL versioning (`/api/v1/`, `/api/v2/`).

---

# 4. Data Access Layer — JPA & Hibernate

## ORM Basics

**The Problem:** Java works with objects. Databases work with tables. Translating between them is tedious and error-prone.

**ORM (Object-Relational Mapping)** bridges this gap — you work with Java objects, and the ORM translates to SQL automatically.

```
Java Object         →  Database Table
──────────────────────────────────────
User class          →  users table
User.id field       →  id column
User.name field     →  name column
new User()          →  INSERT INTO users
userRepo.findById() →  SELECT * FROM users WHERE id=?
```

---

## JPA vs Hibernate

- **JPA (Jakarta Persistence API):** A *specification* (interface) for ORM in Java. Defines annotations, EntityManager, JPQL.
- **Hibernate:** An *implementation* of JPA. The most popular ORM library that actually does the work.
- **Spring Data JPA:** A Spring abstraction on top of JPA that eliminates boilerplate.

```
Your Code → Spring Data JPA → JPA (specification) → Hibernate (implementation) → Database
```

---

## Entities

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-increment
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
```

### @GeneratedValue Strategies

| Strategy | Behavior | Best For |
|---|---|---|
| `IDENTITY` | DB auto-increment | MySQL, PostgreSQL (most common) |
| `SEQUENCE` | DB sequence object | PostgreSQL (more efficient) |
| `TABLE` | Uses a table to track IDs | Portable but slow |
| `UUID` | Generate UUID | Distributed systems |
| `AUTO` | JPA picks strategy | Avoid — unpredictable |

---

## Relationships

### OneToMany / ManyToOne
```java
@Entity
public class User {
    @Id
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}

@Entity
public class Order {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK column in orders table
    private User user;
}
```

### ManyToMany
```java
@Entity
public class Student {
    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "student_courses",              // junction table
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
public class Course {
    @Id
    private Long id;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
```

### OneToOne
```java
@Entity
public class User {
    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile profile;
}
```

---

## Fetch Types

| Type | Behavior | Default For |
|---|---|---|
| `EAGER` | Load immediately when parent is loaded | `@ManyToOne`, `@OneToOne` |
| `LAZY` | Load only when accessed | `@OneToMany`, `@ManyToMany` |

**Rule of thumb:** Always use `LAZY` fetching. EAGER can cause massive data loads and N+1 problems.

```java
// BAD - loads all orders every time you load a user
@OneToMany(fetch = FetchType.EAGER)
private List<Order> orders;

// GOOD - loads orders only when you access user.getOrders()
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;
```

---

## Cascade Types

Cascading means: "when I do this operation on the parent, do it on children too."

| CascadeType | What it does |
|---|---|
| `PERSIST` | Save parent → also save children |
| `MERGE` | Update parent → also update children |
| `REMOVE` | Delete parent → also delete children |
| `REFRESH` | Refresh parent → also refresh children |
| `ALL` | All of the above |
| `DETACH` | Detach parent → also detach children |

```java
// CAREFUL with CascadeType.REMOVE
@OneToMany(cascade = CascadeType.ALL) // Deleting a user deletes all their orders
private List<Order> orders;

// Safer for most cases:
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
private List<Order> orders;
```

---

## The N+1 Problem

This is one of the most important JPA concepts for SDE-2 interviews.

**The Problem:**
```java
// Scenario: Load all users and their orders

// This runs 1 query to get all users
List<User> users = userRepository.findAll();

// Then for EACH user, runs another query to get orders
for (User user : users) {
    System.out.println(user.getOrders().size()); // 1 extra query per user!
}
// For 100 users → 1 + 100 = 101 queries! This is N+1.
```

**Solution 1: JOIN FETCH (JPQL)**
```java
@Query("SELECT u FROM User u JOIN FETCH u.orders")
List<User> findAllWithOrders();
// Runs 1 query with JOIN — loads everything at once
```

**Solution 2: EntityGraph**
```java
@EntityGraph(attributePaths = {"orders"})
List<User> findAll();
```

**Solution 3: Batch fetching (less common)**
```java
@BatchSize(size = 20) // Loads orders in batches of 20
private List<Order> orders;
```

**How to detect N+1:** Enable SQL logging (`spring.jpa.show-sql=true`) and count queries. Tools like Hibernate's statistics or p6spy also help.

---

## Spring Data JPA Repository Hierarchy

```
Repository (marker)
    └── CrudRepository<T, ID>     (save, findById, findAll, delete, count)
            └── PagingAndSortingRepository<T, ID>  (findAll with Pageable)
                    └── JpaRepository<T, ID>       (flush, saveAndFlush, findAll with Sort)
```

**Always extend JpaRepository** — it gives you everything.

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // Derived queries — Spring generates SQL from method names
    List<User> findByEmail(String email);
    List<User> findByNameContainingIgnoreCase(String name);
    Optional<User> findByEmailAndActive(String email, boolean active);
    long countByActive(boolean active);
    void deleteByEmail(String email);

    // JPQL
    @Query("SELECT u FROM User u WHERE u.createdAt > :date")
    List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);

    // Native SQL
    @Query(value = "SELECT * FROM users WHERE LOWER(email) = LOWER(:email)",
           nativeQuery = true)
    Optional<User> findByEmailNative(@Param("email") String email);
}
```

---

## JPQL vs Native Queries

| | JPQL | Native SQL |
|---|---|---|
| Syntax | Uses entity/field names | Uses table/column names |
| Portable | Yes (works on any supported DB) | No (DB-specific SQL) |
| Performance | Good | Better for complex queries |
| Use when | Simple to moderate queries | Complex joins, DB-specific features |

---

## @Transactional

```java
@Service
public class TransferService {

    @Transactional // Wraps method in a transaction
    public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
        Account from = accountRepo.findById(fromId).orElseThrow();
        Account to = accountRepo.findById(toId).orElseThrow();

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepo.save(from);
        accountRepo.save(to);
        // If any exception occurs here, both saves are rolled back
    }
}
```

**Important Rules:**
- `@Transactional` only works on **public** methods
- Only works when called from **outside** the class (Spring proxy limitation)
- Self-invocation (calling `this.methodName()`) bypasses the proxy — transaction is NOT applied

### Propagation Types

| Propagation | Behavior |
|---|---|
| `REQUIRED` (default) | Join existing transaction or create new one |
| `REQUIRES_NEW` | Always create new transaction (suspend existing) |
| `SUPPORTS` | Join if exists, else run without transaction |
| `NOT_SUPPORTED` | Suspend existing transaction, run without |
| `MANDATORY` | Must have existing transaction, else throw |
| `NEVER` | Must NOT have transaction, else throw |
| `NESTED` | Nested transaction within existing one |

### Isolation Levels

| Level | Dirty Read | Non-Repeatable Read | Phantom Read |
|---|---|---|---|
| READ_UNCOMMITTED | Possible | Possible | Possible |
| READ_COMMITTED (common default) | Protected | Possible | Possible |
| REPEATABLE_READ | Protected | Protected | Possible |
| SERIALIZABLE | Protected | Protected | Protected |

```java
@Transactional(isolation = Isolation.READ_COMMITTED,
               propagation = Propagation.REQUIRED,
               rollbackFor = Exception.class,
               readOnly = true)  // Optimization hint for read-only operations
public List<User> getAllUsers() {
    return userRepository.findAll();
}
```

---

# 5. Database Design (SDE-2 Level)

## Normalization

Normalization eliminates redundancy and ensures data integrity.

| Normal Form | Rule |
|---|---|
| 1NF | Each column has atomic values; no repeating groups |
| 2NF | 1NF + Every non-key column depends on the WHOLE primary key |
| 3NF | 2NF + No transitive dependencies (non-key column depends on another non-key column) |
| BCNF | Stricter 3NF — every determinant is a candidate key |

**When NOT to normalize:** High-read analytics systems often denormalize intentionally for performance. OLAP vs OLTP tradeoff.

---

## Indexing

An index is a data structure (usually B-Tree) that allows fast data lookup.

```sql
-- Single column index
CREATE INDEX idx_users_email ON users(email);

-- Composite index (column order matters!)
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
-- Efficient for: WHERE user_id = ? AND status = ?
-- Efficient for: WHERE user_id = ?
-- NOT efficient for: WHERE status = ? (leftmost prefix rule)

-- Unique index
CREATE UNIQUE INDEX idx_users_email_unique ON users(email);
```

**Index best practices:**
- Index columns used in WHERE, JOIN, ORDER BY
- Don't over-index — every index slows down writes (INSERT/UPDATE/DELETE)
- Composite indexes: most selective column first
- Covering index: includes all columns needed by a query

---

## ACID Properties

| Property | Meaning | Example |
|---|---|---|
| **Atomicity** | All or nothing — transaction completes fully or not at all | Transfer money: both debit and credit happen, or neither |
| **Consistency** | Database stays in valid state before and after transaction | Balance can't go negative if business rule prohibits it |
| **Isolation** | Concurrent transactions don't interfere | Two users booking last seat — only one succeeds |
| **Durability** | Committed data survives system crashes | After commit, data is safe even if server crashes |

---

## Optimistic vs Pessimistic Locking

### Optimistic Locking
Assumes conflicts are rare. Checks for conflicts only at commit time.

```java
@Entity
public class Product {
    @Id
    private Long id;

    @Version // Hibernate manages this
    private Integer version; // Incremented on each update

    private int stock;
}

// If two threads load version=1 and both try to update:
// Thread 1 commits → version becomes 2
// Thread 2 tries to commit with version=1 → OptimisticLockException!
```

**Use when:** Low contention, reads >> writes.

### Pessimistic Locking
Locks the row immediately when reading. Other transactions must wait.

```java
// Lock the row immediately — no other transaction can modify until we release
@Query("SELECT p FROM Product p WHERE p.id = :id")
@Lock(LockModeType.PESSIMISTIC_WRITE)
Optional<Product> findByIdWithLock(@Param("id") Long id);
```

**Use when:** High contention, data must be accurate (e.g., inventory management, ticket booking).

---

## Schema Design Best Practices

- Use surrogate primary keys (`id BIGSERIAL`) not natural keys
- Always add `created_at` and `updated_at` timestamps
- Use `NOT NULL` constraints wherever possible
- Soft delete with `deleted_at` or `is_deleted` flag rather than hard DELETE
- Use appropriate data types (don't store dates as strings)
- Name tables as plural nouns (`users`, `orders`), columns as snake_case
- Add FK constraints at the DB level, not just application level

---

# 6. Spring Security

## Authentication vs Authorization

- **Authentication:** Verifying WHO you are (login with username/password)
- **Authorization:** Verifying WHAT you are allowed to do (admin can delete users, user cannot)

---

## Spring Security Architecture

```
HTTP Request
     │
     ▼
FilterChain (series of filters)
     │
     ├── SecurityContextPersistenceFilter  (load/save SecurityContext)
     ├── UsernamePasswordAuthenticationFilter  (handle form login)
     ├── BearerTokenAuthenticationFilter  (handle JWT)
     ├── ExceptionTranslationFilter  (convert auth exceptions to HTTP 401/403)
     └── FilterSecurityInterceptor  (access control — allow or deny)
     │
     ▼
DispatcherServlet → Controller
```

Every HTTP request passes through a chain of filters. Security logic lives in these filters.

---

## JWT Authentication (Step-by-Step)

JWT (JSON Web Token) is a stateless token — the server doesn't need to store session data.

**JWT Structure:** `header.payload.signature`
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjA...
[  HEADER (base64)  ].[         PAYLOAD (base64)          ].[  SIGNATURE  ]
```

**Implementation Flow:**

```
1. User sends POST /auth/login with credentials
2. Server verifies credentials against DB
3. Server creates JWT with user info (email, roles) and signs it
4. Server returns JWT to client
5. Client stores JWT (localStorage or cookie)
6. Client sends JWT in header: Authorization: Bearer <token>
7. Server validates JWT signature on every request
8. Server extracts user info from JWT, no DB call needed
```

**Code Implementation:**

```java
// 1. Add dependency: io.jsonwebtoken:jjwt-api

// 2. JWT Utility class
@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}

// 3. JWT Filter — runs before every request
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired private JwtService jwtService;
    @Autowired private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null,
                                                            userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

// 4. Security Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable for REST APIs
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## Password Encoding

**NEVER store plain text passwords.** Always hash with BCrypt.

```java
@Service
public class AuthService {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;

    public void register(String email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email, hashedPassword);
        userRepository.save(user);
    }

    public boolean login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return passwordEncoder.matches(rawPassword, user.getPassword()); // compares safely
    }
}
```

---

## Role-Based Access Control (RBAC)

```java
// Method-level security
@EnableMethodSecurity
@Configuration
public class SecurityConfig { }

@RestController
public class AdminController {

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() { }

    @DeleteMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void deleteUser(@PathVariable Long id) { }
}
```

---

## Common Security Mistakes

- Storing passwords in plain text
- Using JWT without expiration
- Not validating JWT signature (trusting the payload blindly)
- Exposing sensitive data in error messages
- Not using HTTPS
- SQL Injection — always use parameterized queries (JPA handles this)
- Not rate-limiting authentication endpoints (brute force)
- Logging sensitive data (passwords, tokens in logs)

---

# 7. Microservices Concepts

## What Are Microservices?

Microservices is an architectural style where a large application is decomposed into small, independently deployable services, each responsible for a specific business capability.

```
Monolith:                    Microservices:
+------------------+         +----------+  +----------+  +----------+
|   User Mgmt      |         |  User    |  |  Order   |  | Payment  |
|   Order Mgmt     |   →     |  Service |  |  Service |  |  Service |
|   Payment        |         +----------+  +----------+  +----------+
|   Notifications  |         +----------+  +----------+
|   Reports        |         | Notif.   |  | Reports  |
+------------------+         | Service  |  | Service  |
                             +----------+  +----------+
```

---

## Monolith vs Microservices

| Aspect | Monolith | Microservices |
|---|---|---|
| Deployment | Deploy everything together | Deploy services independently |
| Scaling | Scale the whole app | Scale individual services |
| Development | Simple to start | Complex distributed system |
| Failure | One bug can take down everything | Isolated failures |
| Technology | Single tech stack | Polyglot (each service can use different tech) |
| Team size | Small teams | Larger, independent teams |

**When to use microservices:** Large teams, complex domain, different scaling requirements per service, need for independent deployments.

**When NOT to use:** Small teams, early stage startups, simple domain. Start monolith, extract services when pain is clear.

---

## Inter-Service Communication

### Synchronous (REST / gRPC)
```
Service A  →  REST call  →  Service B
         ←  Response   ←
```

**Pros:** Simple, real-time response  
**Cons:** Tight coupling, cascading failures

### Asynchronous (Message Queues)
```
Service A  →  Kafka/RabbitMQ  →  Service B processes when ready
```

**Pros:** Decoupled, resilient, handles traffic spikes  
**Cons:** Eventual consistency, harder to debug

---

## Service Discovery

Services register themselves and discover each other dynamically (IPs can change with auto-scaling).

**Using Spring Cloud Eureka:**
```java
// Eureka Server
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer { }

// Service registration
@SpringBootApplication
@EnableEurekaClient
public class OrderService { }

// application.yml for each service
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: order-service

// Calling another service by name (not hardcoded IP)
@Autowired private RestTemplate restTemplate;

// @LoadBalanced RestTemplate resolves "user-service" to actual IP via Eureka
ResponseEntity<User> response = restTemplate.getForEntity(
    "http://user-service/api/users/{id}", User.class, userId);
```

---

## API Gateway

Single entry point for all client requests. Routes to appropriate microservices.

```yaml
# Spring Cloud Gateway config
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service    # load-balanced
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1
            - AddRequestHeader=X-Tenant-Id, mycompany
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
```

**Gateway responsibilities:** Authentication, rate limiting, request routing, SSL termination, logging.

---

## Circuit Breaker

Prevents cascading failures. When a service is failing, stop calling it and return a fallback.

```
CLOSED → service is healthy, requests pass through
    ↓ (failures exceed threshold)
OPEN → stop calling service, return fallback immediately
    ↓ (after timeout period)
HALF-OPEN → allow a few test requests
    ↓ (if successful)
CLOSED (back to normal)
```

**Using Resilience4j:**
```java
@Service
public class OrderService {

    @CircuitBreaker(name = "payment-service", fallbackMethod = "paymentFallback")
    @Retry(name = "payment-service")
    public PaymentResult processPayment(Order order) {
        return paymentClient.charge(order.getTotalAmount());
    }

    public PaymentResult paymentFallback(Order order, Throwable t) {
        // Return cached result or queue for later processing
        return PaymentResult.pending(order.getId(), "Payment queued for retry");
    }
}
```

---

## Distributed Transactions (Saga Pattern)

In microservices, you can't use a single database transaction across services. The Saga pattern manages distributed transactions using a sequence of local transactions.

**Choreography-based Saga (Event-driven):**
```
OrderService → emits OrderCreated event
PaymentService → listens, processes payment → emits PaymentCompleted event
InventoryService → listens, reserves items → emits ItemsReserved event
NotificationService → listens, sends confirmation email
```

If payment fails:
```
PaymentService → emits PaymentFailed event
OrderService → listens, cancels order
```

**Orchestration-based Saga:**
A central Saga orchestrator directs each step and handles compensation.

---

# 8. Performance & Scalability

## Caching with Redis

```java
// application.yml
spring:
  cache:
    type: redis
  redis:
    host: localhost
    port: 6379

// Enable caching
@SpringBootApplication
@EnableCaching
public class App { }

// Use caching
@Service
public class UserService {

    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        // Only called on cache MISS; result cached on first call
        return userRepository.findById(id).map(this::toDto).orElseThrow();
    }

    @CachePut(value = "users", key = "#result.id")
    public UserDto updateUser(Long id, UpdateRequest request) {
        // Always executes AND updates cache
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        // Removes from cache
        userRepository.deleteById(id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void clearAllCache() { }
}
```

**Cache TTL configuration:**
```java
@Bean
public RedisCacheConfiguration cacheConfig() {
    return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .disableCachingNullValues();
}
```

---

## Connection Pooling (HikariCP)

HikariCP is Spring Boot's default connection pool. Instead of creating a new DB connection per request (slow), it maintains a pool of reusable connections.

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20        # Max connections in pool
      minimum-idle: 5              # Min idle connections maintained
      connection-timeout: 30000    # Wait up to 30s for a connection
      idle-timeout: 600000         # Close idle connections after 10min
      max-lifetime: 1800000        # Max connection lifetime: 30min
```

**How to size the pool:** CPU cores × 2 + effective_spindle_count is a common heuristic. Too large wastes memory; too small causes timeout under load.

---

## Asynchronous Processing

```java
// Enable async
@SpringBootApplication
@EnableAsync
public class App { }

// Configure thread pool
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}

// Use @Async
@Service
public class EmailService {

    @Async
    public CompletableFuture<Void> sendWelcomeEmail(String to) {
        // Runs in a separate thread — doesn't block the caller
        emailClient.send(to, "Welcome!", "Hello!");
        return CompletableFuture.completedFuture(null);
    }
}

// Caller
@Service
public class UserService {

    public UserDto createUser(CreateRequest request) {
        User user = userRepository.save(new User(request));
        emailService.sendWelcomeEmail(user.getEmail()); // Non-blocking!
        return toDto(user); // Returns immediately
    }
}
```

---

## Rate Limiting

```java
// Using Bucket4j library
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(clientIp, this::newBucket);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining",
                               String.valueOf(probe.getRemainingTokens()));
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return false;
    }

    private Bucket newBucket(String clientIp) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1))))
                .build();
    }
}
```

---

# 9. Testing

## Testing Pyramid

```
          /\
         /  \
        / E2E\        Few — slow, expensive
       /------\
      /  Integ \      Some — test components together
     /----------\
    /  Unit Tests\    Many — fast, isolated
   /--------------\
```

---

## Unit Testing with Mockito

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService; // Mocks injected automatically

    @Test
    void shouldCreateUser_WhenEmailNotExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("john@example.com", "John");
        User savedUser = new User(1L, "john@example.com", "John");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDto result = userService.createUser(request);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(emailService, times(1)).sendWelcomeEmail("john@example.com");
    }

    @Test
    void shouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(new CreateUserRequest("john@example.com", "John")))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage("Email already registered");
    }
}
```

---

## Integration Testing with @SpringBootTest

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Use H2
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnUser_WhenValidIdGiven() {
        User user = userRepository.save(new User("john@example.com", "John"));

        ResponseEntity<UserDto> response = restTemplate.getForEntity(
                "/api/v1/users/" + user.getId(), UserDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
    }
}
```

### @MockBean

```java
@SpringBootTest
class OrderServiceTest {

    @MockBean // Creates a Mockito mock AND registers it as a Spring bean
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Test
    void shouldCreateOrder() {
        when(paymentService.charge(any())).thenReturn(new PaymentResult("SUCCESS"));
        // Test order creation with mocked payment
    }
}
```

### Web Layer Only (@WebMvcTest)
```java
@WebMvcTest(UserController.class) // Only loads controller layer
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // Mock the service

    @Test
    void shouldReturn200_WhenUserExists() throws Exception {
        when(userService.getById(1L)).thenReturn(new UserDto(1L, "john@example.com"));

        mockMvc.perform(get("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }
}
```

---

## Testcontainers

For integration tests with real databases (not H2 in-memory):

```java
@SpringBootTest
@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPersistUser() {
        User user = userRepository.save(new User("test@example.com", "Test User"));
        assertThat(user.getId()).isNotNull();
    }
}
```

---

# 10. Logging & Monitoring

## Logging Levels

```
ERROR → Critical failures, system cannot continue
WARN  → Potential issues, system continues
INFO  → Normal operations (request received, user created)
DEBUG → Detailed info for debugging
TRACE → Very detailed (method entry/exit, all variables)
```

**Production:** INFO and above  
**Development:** DEBUG and above

---

## Logback Configuration

```xml
<!-- logback-spring.xml -->
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/> <!-- Structured JSON -->
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/app.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder><pattern>%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n</pattern></encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>

    <logger name="com.mycompany" level="DEBUG"/>
</configuration>
```

**In code:**
```java
@Slf4j // Lombok annotation — generates: private static final Logger log = ...
@Service
public class UserService {

    public UserDto createUser(CreateRequest request) {
        log.info("Creating user with email: {}", request.getEmail()); // Use {} not concatenation!
        log.debug("Full request: {}", request);

        try {
            User user = userRepository.save(toEntity(request));
            log.info("User created successfully. id={}, email={}", user.getId(), user.getEmail());
            return toDto(user);
        } catch (Exception e) {
            log.error("Failed to create user for email: {}", request.getEmail(), e);
            throw e;
        }
    }
}
```

---

## Spring Boot Actuator

Actuator exposes production-ready endpoints for health checks, metrics, and more.

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env,loggers
  endpoint:
    health:
      show-details: when-authorized
```

**Key endpoints:**

| Endpoint | What it shows |
|---|---|
| `/actuator/health` | App health (UP/DOWN), DB, Redis status |
| `/actuator/info` | App info (version, git commit) |
| `/actuator/metrics` | JVM memory, CPU, HTTP request counts |
| `/actuator/env` | All environment properties |
| `/actuator/loggers` | Current log levels (can change at runtime!) |
| `/actuator/threaddump` | All threads and their stack traces |
| `/actuator/heapdump` | Heap dump for memory analysis |

---

## Structured Logging & Distributed Tracing

Add correlation ID to trace requests across services:

```java
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String correlationId = Optional.ofNullable(request.getHeader("X-Correlation-ID"))
                .orElse(UUID.randomUUID().toString());

        MDC.put("correlationId", correlationId);
        MDC.put("userId", extractUserId(request));
        response.addHeader("X-Correlation-ID", correlationId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
```

This adds `correlationId` to every log line in that request's thread, making it easy to trace a single request through all services.

---

# 11. Build & Deployment

## Maven Lifecycle

```
validate → compile → test → package → verify → install → deploy
```

| Phase | What happens |
|---|---|
| `compile` | Compile source code |
| `test` | Run unit tests |
| `package` | Create JAR/WAR |
| `install` | Install to local Maven repository |
| `deploy` | Deploy to remote repository (e.g., Nexus) |

**Common commands:**
```bash
mvn clean package          # Compile, test, and package
mvn clean package -DskipTests  # Skip tests
mvn clean install          # Package and install locally
mvn spring-boot:run        # Run the application
mvn dependency:tree        # Show all dependencies
```

---

## Packaging a Spring Boot App

```xml
<!-- pom.xml -->
<packaging>jar</packaging>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <!-- Creates an executable "fat JAR" with all dependencies -->
        </plugin>
    </plugins>
</build>
```

```bash
mvn clean package
java -jar target/my-app-1.0.0.jar
java -jar target/my-app-1.0.0.jar --spring.profiles.active=prod
```

---

## Docker

```dockerfile
# Dockerfile
FROM openjdk:21-jre-slim

WORKDIR /app

# Copy the JAR
COPY target/my-app-1.0.0.jar app.jar

# Create non-root user for security
RUN adduser --system --group appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
```

**Multi-stage build (optimized):**
```dockerfile
# Stage 1: Build
FROM maven:3.9-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline  # Cache dependencies
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime (smaller image)
FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=build /app/target/my-app-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t my-app:1.0.0 .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod my-app:1.0.0
```

**docker-compose.yml:**
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
    depends_on:
      - db

  db:
    image: postgres:15
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=secret
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

---

## CI/CD Overview

```
Developer pushes code
        │
        ▼
GitHub Actions / Jenkins / GitLab CI
        │
        ├── Run tests (mvn test)
        ├── Code quality check (SonarQube)
        ├── Build Docker image
        ├── Push to Container Registry (ECR/GCR/DockerHub)
        │
        ▼
Deploy to Kubernetes (k8s) or ECS
        │
        ├── Rolling deployment (zero downtime)
        ├── Run smoke tests
        └── Monitor health checks
```

**GitHub Actions example:**
```yaml
# .github/workflows/ci.yml
name: CI/CD

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - uses: actions/setup-java@v3
        with:
          java-version: '21'

      - name: Build and Test
        run: mvn clean package

      - name: Build Docker Image
        run: docker build -t my-app:${{ github.sha }} .

      - name: Push to ECR
        run: |
          aws ecr get-login-password | docker login --username AWS --password-stdin $ECR_URI
          docker push my-app:${{ github.sha }}
```

---

# 12. Real-World Use Cases

## Caching — When to Use and When Not To

**Use caching when:**
- Data is read much more often than written (e.g., product catalog, user profiles)
- Data computation is expensive (e.g., complex reports, aggregations)
- Low tolerance for latency (e.g., home page content)

**Don't use caching when:**
- Data changes frequently and must be real-time (e.g., stock prices, live inventory)
- Data is user-specific and you're on a single-server setup
- The cache invalidation logic is more complex than just fetching fresh data

**Production example:** E-commerce product listing pages cache product data for 5 minutes. When a product is updated, the specific product cache key is evicted. This reduces DB load by 80% during peak traffic.

---

## @Transactional — When to Use

**Use it when:**
- Multiple DB operations must succeed or fail together
- You're reading data that other transactions shouldn't modify mid-read (set isolation)

**Don't use it when:**
- Single read operations (use `@Transactional(readOnly = true)` at most)
- Transactions spanning across microservices (use Saga instead)

**Production mistake:** A developer wrapped a method that calls an external payment API in `@Transactional`. If the DB write succeeds but the transaction rollback happens later due to a downstream failure, money was already charged. External API calls should happen AFTER the transaction commits.

---

## Circuit Breaker — When to Use

**Use it when:**
- Service A depends on Service B, and B can be slow or unavailable
- You have a fallback behavior (cache, default response, queue)

**Don't use it when:**
- All operations are critical with no acceptable fallback
- Services run within the same JVM (no network failures)

**Production example:** Payment service has a 3-second SLA. When it's degraded, the circuit opens after 5 consecutive failures, returns a "payment queued" response, and retries asynchronously via a Kafka queue.

---

## Microservices — When to Use

**Use it when:**
- Large team (10+ developers) working on the same codebase causes friction
- Different parts of the system have vastly different scaling needs
- You need independent deployability (deploy feature A without touching B)

**Start with a monolith when:**
- Team is small (< 5 devs)
- Domain is not fully understood yet
- MVP stage — move fast, don't over-engineer

---

# 13. Interview Preparation

## Frequently Asked SDE-2 Questions

### Spring Core
- "Explain the difference between BeanFactory and ApplicationContext."
- "What happens if you have two beans of the same type? How do you resolve it?"
- "Explain bean scopes. When would you use Prototype scope?"
- "What is circular dependency and how do you resolve it?"
- "What is the difference between @Bean and @Component?"
- "Explain constructor vs field injection. Which is better and why?"

### Spring Boot
- "How does auto-configuration work internally?"
- "How would you create a custom auto-configuration?"
- "What is the use of @SpringBootApplication?"
- "How do you manage different configurations for dev/staging/prod?"

### REST & JPA
- "What is idempotency? Why is PUT idempotent but POST is not?"
- "Explain the N+1 problem and how you fix it."
- "What is the difference between EAGER and LAZY fetching?"
- "Explain @Transactional. What happens if you call a @Transactional method from within the same class?"
- "What is optimistic locking? When would you use pessimistic locking?"

### Security
- "How does JWT authentication work? What's in the token?"
- "What is the difference between authentication and authorization?"
- "How does Spring Security filter chain work?"
- "Why is `@Transactional` on a method calling an external payment API dangerous?"

### System Design
- "How would you handle 1 million concurrent users on your Spring Boot service?"
- "How would you implement rate limiting?"
- "How would you design a notification system (email/SMS) without blocking the main request?"
- "How would you handle database migrations in production?"

---

## How to Explain Concepts Confidently

**Framework for answering technical questions:**

1. **Define it simply** — "X is..."
2. **Explain the problem it solves** — "Without X, you'd have..."
3. **Show with code or example** — "Here's how it looks in practice..."
4. **Mention tradeoffs** — "The downside is... but you use it when..."

**Example — Answering "Explain @Transactional":**
> "@Transactional is a Spring annotation that wraps a method in a database transaction. Without it, if you save two objects and the second save fails, the first save remains committed — leaving your data inconsistent. With @Transactional, both saves happen atomically — either both commit or both rollback. One important gotcha: it only works when called from outside the class, because Spring creates a proxy around your bean. If you call a @Transactional method from within the same class using `this.method()`, the proxy is bypassed and no transaction is started."

---

## Common Traps and Wrong Answers

**Trap 1: "Field injection is fine because it's simpler"**  
Wrong. Field injection makes testing hard (must use Spring context or reflection), makes dependencies hidden, and prevents `final` fields. Always use constructor injection.

**Trap 2: "I'll use CascadeType.ALL everywhere"**  
Dangerous. Cascading DELETE from parent will delete all children. Be explicit about which cascade operations you need.

**Trap 3: "I disabled CSRF because it was causing issues"**  
For REST APIs with stateless JWT, disabling CSRF is actually correct (CSRF exploits session cookies, not tokens). But for form-based web apps, never disable it.

**Trap 4: "@Transactional works when calling from the same class"**  
No — Spring proxy is bypassed. Self-invocation doesn't trigger AOP (including transactions). The fix: inject the bean into itself (`@Autowired MyService self;`) or extract the method to another bean.

**Trap 5: "EAGER fetching is better because data is ready immediately"**  
Usually wrong. EAGER loading fetches associations for EVERY query — even when you don't need them. This causes massive performance issues. Default to LAZY.

**Trap 6: "N+1 is when you have N tables plus 1 query"**  
Wrong definition. N+1 is when loading N parent records triggers N additional queries for children. Total = N+1 queries.

---

## System Design — Spring Boot Angle

**"How would you design a high-traffic user service?"**

Key points to cover:
1. **Caching:** Cache frequently-read user profiles in Redis with 5-10 min TTL
2. **Database:** Read replicas for read traffic, primary for writes; connection pooling (HikariCP, max-pool-size tuned)
3. **Async:** Non-critical operations (notifications, audit logs) via async events or message queue
4. **Rate limiting:** Per-user or per-IP rate limiting at API Gateway or with Bucket4j
5. **Observability:** Actuator endpoints, structured logging with correlation IDs, distributed tracing (Zipkin/Jaeger)
6. **Horizontal scaling:** Stateless service (JWT not session), multiple instances behind load balancer

**"How would you handle database schema changes in production?"**

Use **Flyway** or **Liquibase** for database migration versioning:
```java
// Dependency: flyway-core

// Create: src/main/resources/db/migration/V1__create_users_table.sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

// V2__add_phone_to_users.sql
ALTER TABLE users ADD COLUMN phone VARCHAR(20);
```

Flyway runs migrations on startup automatically. Each migration has a version number and is applied only once. This ensures all environments (dev, staging, prod) have the same schema.

---

## Quick Reference Cheat Sheet

### Bean Annotations
| Annotation | Purpose |
|---|---|
| `@Component` | Generic bean |
| `@Service` | Business logic |
| `@Repository` | Data access |
| `@Controller` | MVC controller |
| `@RestController` | REST API controller |
| `@Configuration` | Configuration class |
| `@Bean` | Define a bean from a method |

### JPA Annotations
| Annotation | Purpose |
|---|---|
| `@Entity` | Mark class as JPA entity |
| `@Table` | Specify table name |
| `@Id` | Primary key |
| `@GeneratedValue` | Auto-generate ID |
| `@Column` | Column mapping |
| `@OneToMany` | One-to-many relationship |
| `@ManyToOne` | Many-to-one relationship |
| `@ManyToMany` | Many-to-many relationship |
| `@Transactional` | Wrap in DB transaction |

### HTTP Status Codes
| Code | Meaning |
|---|---|
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 500 | Internal Server Error |

---

*End of Spring Boot SDE-2 Interview Preparation Guide*

---

> **Pro Tip for Interviews:** Always think out loud. Interviewers want to see your reasoning process, not just the final answer. When you're unsure, say "I think it works like X because of Y — but let me reason through that." Intellectual honesty + good fundamentals beats memorized answers every time.
