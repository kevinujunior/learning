
## Topic 4: Design Patterns (Gang of Four)

### Interview Perspective

Design patterns demonstrate your maturity as an engineer. Interviewers use them to gauge your ability to solve common software design problems efficiently, communicate solutions effectively, and understand the trade-offs of different approaches. For an SDE-2, it's less about memorizing all 23 patterns and more about:
1.  Knowing the purpose of key patterns.
2.  Identifying situations where a pattern is applicable.
3.  Explaining the benefits and drawbacks in an interview setting.
4.  Relating them to real-world software components or analogies.
You don't need to write full code, but conceptual understanding and basic code intuition are expected.

### Concepts to Cover

Design patterns are categorized into three groups: Creational, Structural, and Behavioral.

#### Creational Patterns

These patterns deal with object creation mechanisms, trying to create objects in a manner suitable for the situation while increasing flexibility and reuse of the code.

##### Singleton

-   **Problem statement:** Ensure a class has only one instance and provide a global point of access to it.
-   **When to use:** For resources that are inherently unique (e.g., a database connection pool, a logging service, a configuration manager).
-   **When NOT to use:** When global state is problematic, introduces tight coupling, or when concurrency issues arise if not implemented carefully. Overuse can lead to "anti-pattern" status.
-   **Real-world analogy:** The President of a country. There's only one at a time.
-   **Simple code-level intuition:**
    ```java
    public class Logger {
        private static Logger instance; // Static instance
        private Logger() {} // Private constructor
        public static Logger getInstance() { // Global access method
            if (instance == null) {
                // Thread-safe check for multi-threaded environments
                synchronized (Logger.class) {
                    if (instance == null) {
                        instance = new Logger();
                    }
                }
            }
            return instance;
        }
        public void log(String message) { /* ... */ }
    }
    ```
-   **Interview example question:** "How would you design a configuration manager that ensures all parts of your application use the exact same settings?"

##### Factory Method

-   **Problem statement:** Define an interface for creating an object, but let subclasses decide which class to instantiate. Defers instantiation to subclasses.
-   **When to use:** When a class cannot anticipate the class of objects it needs to create, or when a library needs to provide a uniform way to create different types of objects without specifying their concrete classes.
-   **When NOT to use:** When object creation is simple and does not require polymorphism or complex logic. Can add unnecessary complexity for simple cases.
-   **Real-world analogy:** A car factory. They have a process for making cars, but different assembly lines (subclasses) might produce different car models (concrete products).
-   **Simple code-level intuition:**
    ```java
    interface Notification { void send(); }
    class EmailNotification implements Notification { public void send() { /* email logic */ } }
    class SMSNotification implements Notification { public void send() { /* sms logic */ } }

    abstract class NotificationFactory {
        public abstract Notification createNotification();
    }
    class EmailNotificationFactory extends NotificationFactory {
        public Notification createNotification() { return new EmailNotification(); }
    }
    ```
-   **Interview example question:** "You need to send various types of notifications (email, SMS, push). How would you design a system to create these notifications without tying your client code to specific notification types?"

##### Abstract Factory

-   **Problem statement:** Provide an interface for creating families of related or dependent objects without specifying their concrete classes.
-   **When to use:** When the system needs to be independent of how its products are created, composed, and represented, or when a family of product objects is designed to be used together.
-   **When NOT to use:** When only a single family of products is ever needed, or when adding new product types is frequent (can be complex).
-   **Real-world analogy:** A furniture store that sells matching sets (chairs, tables, sofas) in different styles (Victorian, Modern, Rustic). You pick a style, and get all matching pieces.
-   **Simple code-level intuition:** Builds on Factory Method, providing a "factory of factories."
    ```java
    // Imagine families of UI elements:
    interface Button { void render(); }
    interface Checkbox { void render(); }

    class WindowsButton implements Button { /* ... */ }
    class MacOSButton implements Button { /* ... */ }

    abstract class GUIFactory { // Abstract Factory
        abstract Button createButton();
        abstract Checkbox createCheckbox();
    }
    class WindowsFactory extends GUIFactory { // Concrete Factory for Windows
        Button createButton() { return new WindowsButton(); }
        Checkbox createCheckbox() { return new WindowsCheckbox(); }
    }
    ```
-   **Interview example question:** "Your application needs to support different operating system themes (Windows, MacOS) for its UI components (buttons, checkboxes). How can you ensure that all components used for a specific theme are consistent without hardcoding the concrete classes?"

##### Builder

-   **Problem statement:** Separate the construction of a complex object from its representation so that the same construction process can create different representations.
-   **When to use:** When an object has many optional parameters, making its constructor unwieldy, or when an object needs to be constructed in a step-by-step manner.
-   **When NOT to use:** For simple objects with few parameters.
-   **Real-world analogy:** Building a custom computer. You choose a CPU, RAM, GPU, etc., step by step, and the builder assembles them into a coherent system.
-   **Simple code-level intuition:**
    ```java
    class Pizza { // The complex object
        String dough; String sauce; List<String> toppings;
        private Pizza(Builder builder) { /* assign fields */ }

        public static class Builder { // The Builder
            String dough; String sauce; List<String> toppings = new ArrayList<>();
            public Builder setDough(String d) { this.dough = d; return this; }
            public Builder setSauce(String s) { this.sauce = s; return this; }
            public Builder addTopping(String t) { this.toppings.add(t); return this; }
            public Pizza build() { return new Pizza(this); }
        }
    }
    // Usage: Pizza deluxePizza = new Pizza.Builder().setDough("thick").setSauce("tomato").addTopping("cheese").build();
    ```
-   **Interview example question:** "You're designing a user registration system where users can have many optional profile fields. How would you create user objects cleanly without having a huge, unreadable constructor?"

##### Prototype

-   **Problem statement:** Specify the kinds of objects to create using a prototypical instance, and create new objects by copying this prototype.
-   **When to use:** When creating objects is expensive, or when you need to avoid coupling the client code to specific concrete classes (similar to Factory, but by cloning).
-   **When NOT to use:** When objects are simple to create, or when a deep copy is complex to implement.
-   **Real-world analogy:** A 3D printer. You design one model (prototype), and then you can print many identical copies of it.
-   **Simple code-level intuition:** Requires implementing a `clone()` method.
    ```java
    interface ClonableObject extends Cloneable {
        ClonableObject clone();
    }
    class Document implements ClonableObject {
        private String content;
        public Document(String content) { this.content = content; }
        public ClonableObject clone() {
            try { return (Document) super.clone(); } // Shallow copy example
            catch (CloneNotSupportedException e) { return null; }
        }
    }
    // Usage: Document original = new Document("initial content");
    //        Document copy = (Document) original.clone();
    ```
-   **Interview example question:** "You have a complex object that takes a long time to initialize. How can you efficiently create many similar instances of this object without repeatedly going through the full initialization process?"

#### Structural Patterns

These patterns concern class and object composition. They describe how to assemble objects and classes into larger structures while maintaining flexibility and efficiency.

##### Adapter

-   **Problem statement:** Convert the interface of a class into another interface clients expect. Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.
-   **When to use:** When you want to use an existing class, but its interface doesn't match the one you need.
-   **When NOT to use:** When the interfaces are already compatible, or when it overcomplicates simple object integration.
-   **Real-world analogy:** A power adapter that lets you plug a two-prong US appliance into a three-prong European socket.
-   **Simple code-level intuition:**
    ```java
    interface OldPrinter { void printOldFormat(); } // Existing incompatible interface
    class LegacyPrinter implements OldPrinter { public void printOldFormat() { /* ... */ } }

    interface NewPrinter { void printNewFormat(); } // Desired interface

    class PrinterAdapter implements NewPrinter { // The Adapter
        private OldPrinter oldPrinter;
        public PrinterAdapter(OldPrinter oldPrinter) { this.oldPrinter = oldPrinter; }
        public void printNewFormat() { // Adapts new to old
            System.out.println("Adapting new format to old...");
            oldPrinter.printOldFormat();
        }
    }
    ```
-   **Interview example question:** "You have a new reporting module that expects data in a specific format, but your existing legacy data source provides it differently. How can you make them work together without modifying the legacy source?"

##### Decorator

-   **Problem statement:** Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.
-   **When to use:** To add functionality to an object without altering its structure, often for specific instances of an object, not an entire class.
-   **When NOT to use:** When functionality needs to be added to all instances of a class (subclassing is simpler), or when there are too many small decorators, leading to complex wrapping.
-   **Real-world analogy:** Adding features to a basic coffee: espresso (base), then add milk, then add foam. Each addition is a "decorator."
-   **Simple code-level intuition:**
    ```java
    interface Coffee { String getDescription(); double getCost(); }
    class BasicCoffee implements Coffee {
        public String getDescription() { return "Basic Coffee"; }
        public double getCost() { return 2.0; }
    }
    abstract class CoffeeDecorator implements Coffee { // Abstract decorator
        protected Coffee decoratedCoffee;
        public CoffeeDecorator(Coffee coffee) { this.decoratedCoffee = coffee; }
        public String getDescription() { return decoratedCoffee.getDescription(); }
        public double getCost() { return decoratedCoffee.getCost(); }
    }
    class MilkDecorator extends CoffeeDecorator {
        public MilkDecorator(Coffee coffee) { super(coffee); }
        public String getDescription() { return decoratedCoffee.getDescription() + ", Milk"; }
        public double getCost() { return decoratedCoffee.getCost() + 0.5; }
    }
    // Usage: Coffee myCoffee = new MilkDecorator(new BasicCoffee());
    ```
-   **Interview example question:** "You have a core `TextProcessor` component. How would you dynamically add functionalities like compression, encryption, or logging to it, potentially in various combinations, without creating a huge subclass hierarchy?"

##### Facade

-   **Problem statement:** Provide a unified interface to a set of interfaces in a subsystem. Facade defines a higher-level interface that makes the subsystem easier to use.
-   **When to use:** To simplify the interface of a complex subsystem, or to decouple the client from the subsystem components.
-   **When NOT to use:** When the subsystem is already simple, or when clients need direct, fine-grained control over the subsystem's components.
-   **Real-world analogy:** Turning on your car. You just turn the key (or push a button), but internally, many complex systems (starter, fuel pump, ignition) are orchestrated. The key is the facade.
-   **Simple code-level intuition:**
    ```java
    // Complex Subsystem Components
    class Amplifier { void on() {} void setVolume(int v) {} /* ... */ }
    class Tuner { void on() {} /* ... */ }
    class DVDPlayer { void on() {} /* ... */ }

    class HomeTheaterFacade { // The Facade
        private Amplifier amp; private Tuner tuner; private DVDPlayer dvd;
        public HomeTheaterFacade(Amplifier a, Tuner t, DVDPlayer d) { /* ... */ }
        public void watchMovie(String movie) {
            amp.on(); amp.setVolume(10);
            dvd.on(); dvd.play(movie);
            System.out.println("Enjoy the movie!");
        }
        public void endMovie() { /* ... */ }
    }
    // Usage: HomeTheaterFacade facade = new HomeTheaterFacade(new Amp(), new Tuner(), new DVDPlayer());
    //        facade.watchMovie("The Matrix");
    ```
-   **Interview example question:** "Your application interacts with a complex third-party library that has many classes and intricate initialization steps. How can you simplify its usage for your application's modules?"

##### Proxy

-   **Problem statement:** Provide a surrogate or placeholder for another object to control access to it.
-   **When to use:** When you need to add a layer of control (e.g., security, lazy loading, logging, remote access) around an object without changing its core interface.
-   **When NOT to use:** When simple, direct access to the object is sufficient.
-   **Real-world analogy:** A credit card. You don't interact directly with your bank account for every purchase; the card (proxy) handles the transaction on your behalf.
-   **Simple code-level intuition:**
    ```java
    interface Image { void display(); }
    class RealImage implements Image { // The heavy object
        private String filename;
        public RealImage(String filename) { this.filename = filename; loadFromDisk(); }
        private void loadFromDisk() { System.out.println("Loading " + filename); }
        public void display() { System.out.println("Displaying " + filename); }
    }
    class ProxyImage implements Image { // The Proxy
        private RealImage realImage;
        private String filename;
        public ProxyImage(String filename) { this.filename = filename; }
        public void display() {
            if (realImage == null) { // Lazy loading
                realImage = new RealImage(filename);
            }
            realImage.display();
        }
    }
    // Usage: Image image = new ProxyImage("my_large_image.jpg");
    //        image.display(); // Image loads only now
    ```
-   **Interview example question:** "You have a high-resolution image object that consumes a lot of memory and takes time to load. How can you display its placeholder and only load the actual image data when it's explicitly requested by the user?"

##### Composite

-   **Problem statement:** Compose objects into tree structures to represent part-whole hierarchies. Composite lets clients treat individual objects and compositions of objects uniformly.
-   **When to use:** When you want to represent a hierarchy of objects, and clients should be able to ignore the difference between individual objects and groups of objects.
-   **When NOT to use:** When the "leaf" and "composite" objects have significantly different interfaces or behaviors.
-   **Real-world analogy:** A file system. You can perform operations (like `ls` or `delete`) on individual files (leaves) or directories (composites), and the behavior is consistent.
-   **Simple code-level intuition:**
    ```java
    interface Graphic { void draw(); } // Common interface
    class Dot implements Graphic { public void draw() { System.out.println("Draws a dot."); } }
    class Circle implements Graphic { public void draw() { System.out.println("Draws a circle."); } }

    class CompoundGraphic implements Graphic { // The Composite
        private List<Graphic> children = new ArrayList<>();
        public void add(Graphic g) { children.add(g); }
        public void remove(Graphic g) { children.remove(g); }
        public void draw() {
            System.out.println("Drawing a compound graphic:");
            for (Graphic child : children) { child.draw(); }
        }
    }
    // Usage: CompoundGraphic scene = new CompoundGraphic();
    //        scene.add(new Dot()); scene.add(new Circle());
    //        scene.draw(); // Draws both dot and circle
    ```
-   **Interview example question:** "Design a graphics editor where users can group shapes (circles, squares) and treat a group as a single shape (e.g., move or resize a group of shapes). How would you model this hierarchy?"

##### Bridge

-   **Problem statement:** Decouple an abstraction from its implementation so that the two can vary independently.
-   **When to use:** When you need to avoid a permanent binding between an abstraction and its implementation, or when both the abstraction and implementation hierarchies can be extended independently.
-   **When NOT to use:** When the abstraction and implementation are tightly coupled and unlikely to evolve independently.
-   **Real-world analogy:** A remote control (abstraction) and a TV (implementation). You can have different brands of TVs, and different types of remotes, but they can still work together as long as they adhere to an interface (e.g., power on/off).
-   **Simple code-level intuition:**
    ```java
    // Abstraction Interface
    interface Device { void powerOn(); void powerOff(); }

    // Implementor Interfaces (can be multiple)
    class TV implements Device { public void powerOn() {} public void powerOff() {} }
    class Radio implements Device { public void powerOn() {} public void powerOff() {} }

    // Abstraction
    abstract class RemoteControl {
        protected Device device;
        public RemoteControl(Device device) { this.device = device; }
        public abstract void togglePower();
    }

    // Refined Abstraction
    class BasicRemote extends RemoteControl {
        public BasicRemote(Device device) { super(device); }
        public void togglePower() {
            if (device.isOn()) device.powerOff();
            else device.powerOn();
        }
    }
    ```
-   **Interview example question:** "You are building a cross-platform drawing application. How can you separate the high-level drawing operations (e.g., `drawCircle()`) from the low-level rendering specifics for different operating systems (e.g., Windows GDI, MacOS Quartz)?"

#### Behavioral Patterns

These patterns are concerned with algorithms and the assignment of responsibilities between objects. They describe how objects and classes interact and distribute responsibility.

##### Observer

-   **Problem statement:** Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.
-   **When to use:** When a change in one object requires changing others, and you don't know how many objects need to be changed or which ones.
-   **When NOT to use:** When the subject changes very frequently and observers are complex, leading to performance issues, or when the notification order is critical.
-   **Real-world analogy:** A newspaper subscription. The newspaper (subject) publishes new editions, and all subscribers (observers) are notified by delivery.
-   **Simple code-level intuition:**
    ```java
    interface Observer { void update(String message); }
    class ConcreteObserver implements Observer {
        private String name;
        public ConcreteObserver(String name) { this.name = name; }
        public void update(String message) { System.out.println(name + " received: " + message); }
    }
    class Subject { // The observable
        private List<Observer> observers = new ArrayList<>();
        public void addObserver(Observer o) { observers.add(o); }
        public void notifyObservers(String message) {
            for (Observer o : observers) { o.update(message); }
        }
        public void setState(String state) { notifyObservers(state); }
    }
    // Usage: Subject s = new Subject(); s.addObserver(new ConcreteObserver("A")); s.setState("New state!");
    ```
-   **Interview example question:** "You are building a stock monitoring application. How would you design it so that multiple users can subscribe to stock price updates, and they get notified whenever a particular stock's price changes?"

##### Strategy

-   **Problem statement:** Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently from clients that use it.
-   **When to use:** When you have multiple related algorithms, and you want to choose between them at runtime, or when a class has a large conditional statement that selects different behaviors.
-   **When NOT to use:** When there are very few algorithms and their implementation is simple, or when the behavior is fixed.
-   **Real-world analogy:** Different ways to travel: car, train, plane. Each is a strategy to get from A to B, and you choose one based on circumstances.
-   **Simple code-level intuition:**
    ```java
    interface PaymentStrategy { void pay(double amount); }
    class CreditCardPayment implements PaymentStrategy { public void pay(double amount) { /* Credit card logic */ } }
    class PayPalPayment implements PaymentStrategy { public void pay(double amount) { /* PayPal logic */ } }

    class ShoppingCart { // The context
        private PaymentStrategy paymentStrategy;
        public void setPaymentStrategy(PaymentStrategy strategy) { this.paymentStrategy = strategy; }
        public void checkout(double amount) { paymentStrategy.pay(amount); }
    }
    // Usage: ShoppingCart cart = new ShoppingCart();
    //        cart.setPaymentStrategy(new CreditCardPayment());
    //        cart.checkout(100.0);
    ```
-   **Interview example question:** "Your e-commerce application needs to support various payment methods (credit card, PayPal, bank transfer). How can you design the checkout process to easily switch between these methods without modifying the core shopping cart logic?"

##### Command

-   **Problem statement:** Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations.
-   **When to use:** When you need to decouple the sender of a request from the receiver, support undo/redo, or queue commands for later execution.
-   **When NOT to use:** For very simple, direct method calls where no additional capabilities (undo, logging, queuing) are required.
-   **Real-world analogy:** A restaurant order. The waiter takes your order (command object), which encapsulates what you want. The kitchen (receiver) then executes that specific order. You can ask to undo an order or queue multiple orders.
-   **Simple code-level intuition:**
    ```java
    interface Command { void execute(); void undo(); }

    class Light { // The Receiver
        public void turnOn() { System.out.println("Light is ON."); }
        public void turnOff() { System.out.println("Light is OFF."); }
    }

    class TurnOnLightCommand implements Command { // Concrete Command
        private Light light;
        public TurnOnLightCommand(Light light) { this.light = light; }
        public void execute() { light.turnOn(); }
        public void undo() { light.turnOff(); }
    }

    class RemoteControl { // The Invoker
        private Command command;
        public void setCommand(Command command) { this.command = command; }
        public void pressButton() { command.execute(); }
        public void pressUndo() { command.undo(); }
    }
    //Usage: Light light = new Light();
    //        Command turnOn = new TurnOnLightCommand(light);
    //        RemoteControl remote = new RemoteControl();
    //        remote.setCommand(turnOn);
    //        remote.pressButton(); // Light is ON.
    ```
-   **Interview example question:** "You are building a text editor with undo/redo functionality. How would you design the system so that each action (typing, deleting, formatting) can be easily reversed?"

##### Iterator

-   **Problem statement:** Provide a way to access the elements of an aggregate object sequentially without exposing its underlying representation.
-   **When to use:** When you need to traverse elements of a collection without exposing its internal structure, or when you need different traversal algorithms.
-   **When NOT to use:** For simple collections where direct access is fine and no alternative traversals are needed (e.g., standard Java `for-each` loop often suffices).
-   **Real-world analogy:** A TV remote's channel buttons. You can cycle through channels without knowing how the TV stores them internally.
-   **Simple code-level intuition:**
    ```java
     //Java's built-in Iterator interface is the best example:
     interface Iterator<E> {
        boolean hasNext();
        E next();
        void remove(); // Optional
     }
     List<String> names = Arrays.asList("Alice", "Bob");
     Iterator<String> iterator = names.iterator();
     while (iterator.hasNext()) {
        System.out.println(iterator.next());
     }
    ```
-   **Interview example question:** "You have a custom collection class that stores data in a complex, non-standard way. How can you allow other parts of your application to iterate over its elements without exposing its internal data structure?"

##### State

-   **Problem statement:** Allow an object to alter its behavior when its internal state changes. The object will appear to change its class.
-   **When to use:** When an object's behavior depends on its state, and it must change its behavior at runtime depending on that state. Reduces complex conditional logic (if-else or switch statements).
-   **When NOT to use:** For objects with very few states or simple state transitions.
-   **Real-world analogy:** A traffic light. It changes its behavior (light color) based on its current state (red, yellow, green).
-   **Simple code-level intuition:**
    ```java
    interface VendingMachineState { void selectProduct(); void dispenseProduct(); }

    class HasSelectionState implements VendingMachineState {
        private VendingMachine machine;
        public HasSelectionState(VendingMachine machine) { this.machine = machine; }
        public void selectProduct() { System.out.println("Already selected."); }
        public void dispenseProduct() { System.out.println("Dispensing product."); machine.setState(new NoSelectionState(machine)); }
    }
    class NoSelectionState implements VendingMachineState { /* ... */ } // Other state

    class VendingMachine { // The Context
        private VendingMachineState currentState;
        public VendingMachine() { currentState = new NoSelectionState(this); }
        public void setState(VendingMachineState state) { this.currentState = state; }
        public void selectProduct() { currentState.selectProduct(); }
        public void dispenseProduct() { currentState.dispenseProduct(); }
    }
    ```
-   **Interview example question:** "Design a workflow engine where a task can be in different states (e.g., `PENDING`, `IN_PROGRESS`, `COMPLETED`, `FAILED`), and available actions depend on the current state. How can you avoid a massive `switch` statement for state transitions?"

##### Template Method

-   **Problem statement:** Define the skeleton of an algorithm in an operation, deferring some steps to subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.
-   **When to use:** When you want to implement an algorithm once and reuse it, while allowing specific parts to be customized by subclasses.
-   **When NOT to use:** When there's little or no commonality in the algorithm steps, or when the algorithm is too simple to warrant an abstract template.
-   **Real-world analogy:** A recipe for baking a cake. The overall steps (mix ingredients, bake, cool, frost) are fixed, but the specific ingredients or frosting (subclasses) can vary.
-   **Simple code-level intuition:**
    ```java
    abstract class ReportGenerator { // Abstract Class with Template Method
        public final void generateReport() { // The Template Method (final to prevent override)
            collectData();
            formatHeader();
            formatBody();
            formatFooter();
            sendReport();
        }
        protected abstract void collectData(); // Abstract steps to be implemented by subclasses
        protected abstract void formatHeader();
        protected abstract void formatBody();
        protected abstract void formatFooter();
        protected void sendReport() { System.out.println("Sending generic report."); } // Optional hook
    }
    class ExcelReportGenerator extends ReportGenerator {
        protected void collectData() { System.out.println("Collecting data for Excel..."); }
        protected void formatHeader() { System.out.println("Formatting Excel header..."); }
        // ... rest of abstract methods ...
    }
    // Usage: ReportGenerator excelGen = new ExcelReportGenerator();
    //        excelGen.generateReport(); // Executes the fixed sequence of steps
    ```
-   **Interview example question:** - "Your application needs to process various types of documents (e.g., PDF, Word, plain text) from different sources. Each document type requires a slightly different parsing logic, but the overall workflow (fetch, parse, validate, store) is similar. How would you design this processing pipeline to allow for document-specific customizations while maintaining a consistent overall structure?"

---



# Most Important Design Patterns & Problems They Solve

| Pattern                  | Category     | Problem It Solves                                                                 | Real-World Use Case                          |
|--------------------------|-------------|----------------------------------------------------------------------------------|----------------------------------------------|
| Singleton                | Creational  | Ensures only one instance exists with global access                             | Config manager, logging, cache               |
| Factory Method           | Creational  | Decouples object creation from usage                                            | Object creation in frameworks                |
| Builder                  | Creational  | Handles complex object construction step-by-step                                | Creating immutable objects, DTOs             |
| Strategy                 | Behavioral  | Replaces multiple conditionals with interchangeable algorithms                  | Payment methods, sorting logic               |
| Observer                 | Behavioral  | Enables event-driven communication (one-to-many dependency)                     | Pub/Sub systems, notifications               |
| Command                  | Behavioral  | Encapsulates requests as objects                                                | Undo/Redo, job queues                        |
| State                    | Behavioral  | Changes behavior based on internal state                                        | Order lifecycle, workflow systems            |
| Decorator                | Structural  | Adds behavior dynamically without modifying original class                      | Java I/O streams, feature extensions         |
| Adapter                  | Structural  | Makes incompatible interfaces work together                                     | Legacy system integration                    |
| Facade                   | Structural  | Provides a simplified interface to complex subsystems                           | API gateway, service layer abstraction       |
| Proxy                    | Structural  | Controls access to an object (lazy loading, security, remote access)            | Virtual proxy, access control                |