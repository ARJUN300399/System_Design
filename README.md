# System Design Notes

This repository collects notes about system design and software architecture. Over time it will expand with topics such as design patterns and scalability. The first section focuses on building an intuitive understanding of the **SOLID** principles.

## Why SOLID?

* Encourages modular and maintainable code
* Makes future changes easier to introduce
* Enables better testability and separation of concerns

## The SOLID Design Principles

The best way to learn these principles is to examine common problems and then see how SOLID provides a solution. Each section begins with a problematic example and finishes with an improved approach in Java.

### 1. Single Responsibility Principle (SRP)

**Problem**
When a class tries to handle multiple concerns, one change can accidentally break another feature:

```java
class InvoiceService {
    void createInvoice(Order order) { /* create and save invoice */ }
    double calculateTax(Order order) { /* compute tax */ }
}
```

**Solution**
Split the logic so that each class has one reason to change:

```java
class InvoiceCalculator {
    double calculateTax(Order order) { /* ... */ }
}

class InvoiceRepository {
    void save(Invoice invoice) { /* ... */ }
}
```

* Business rules are separate from storage concerns.
* Focused classes are easier to maintain and test.

### 2. Open/Closed Principle (OCP)

**Problem**
Consider a method that computes area for multiple shapes using conditionals. Every time a new shape is added, the method must be modified:

```java
class AreaCalculator {
    double area(Object shape) {
        if (shape instanceof Rectangle rect) {
            return rect.width * rect.height;
        } else if (shape instanceof Circle circle) {
            return Math.PI * circle.radius * circle.radius;
        }
        throw new IllegalArgumentException("Unknown shape");
    }
}
```

**Solution**
Define an interface that each shape implements. The calculator then works with the abstraction and never needs modification:

```java
interface Shape {
    double area();
}

class Rectangle implements Shape {
    double width, height;
    public double area() { return width * height; }
}

class Circle implements Shape {
    double radius;
    public double area() { return Math.PI * radius * radius; }
}

class AreaCalculator {
    double area(Shape shape) { return shape.area(); }
}
```

* New shapes are introduced by extension, not by changing existing logic.
* Core logic remains stable as the system grows.

### 3. Liskov Substitution Principle (LSP)

**Problem**
A subclass should be usable anywhere its base class is expected. The classic counterexample uses Rectangle and Square:

```java
class Rectangle {
    protected int width, height;
    void setWidth(int w) { width = w; }
    void setHeight(int h) { height = h; }
    int area() { return width * height; }
}

class Square extends Rectangle {
    @Override
    void setWidth(int w) { width = height = w; }
    @Override
    void setHeight(int h) { height = width = h; }
}
```

Using `Square` where a `Rectangle` is expected breaks assumptions about independent width and height setters.

**Solution**
Design separate abstractions or avoid inheritance where it does not fit:

```java
interface Shape {
    int area();
}

class BetterRectangle implements Shape {
    private final int width, height;
    BetterRectangle(int w, int h) { width = w; height = h; }
    public int area() { return width * height; }
}

class Square implements Shape {
    private final int side;
    Square(int s) { side = s; }
    public int area() { return side * side; }
}
```

* Subtypes follow the expectations of the interface.
* Clients can rely on consistent behavior across implementations.

### 4. Interface Segregation Principle (ISP)

**Problem**
Large interfaces force implementations to provide methods they don't need:

```java
interface MultiFunctionDevice {
    void print(Document d);
    void scan(Document d);
    void fax(Document d);
}

class BasicPrinter implements MultiFunctionDevice {
    public void print(Document d) { /* ... */ }
    public void scan(Document d) { /* unused */ }
    public void fax(Document d) { /* unused */ }
}
```

**Solution**
Split the interface into smaller, role-specific ones:

```java
interface Printer {
    void print(Document d);
}
interface Scanner {
    void scan(Document d);
}
interface Fax {
    void fax(Document d);
}

class SimplePrinter implements Printer {
    public void print(Document d) { /* ... */ }
}

class MultiFunctionMachine implements Printer, Scanner, Fax {
    public void print(Document d) { /* ... */ }
    public void scan(Document d) { /* ... */ }
    public void fax(Document d) { /* ... */ }
}
```

* Implementers only depend on the capabilities they actually use.
* Changes to one behavior don't impact classes that don't need it.

### 5. Dependency Inversion Principle (DIP)

**Problem**
A high-level module directly instantiates its dependencies, making it hard to swap implementations or test in isolation:

```java
class EmailService {
    void send(String msg) { /* ... */ }
}

class AlertManager {
    private final EmailService service = new EmailService();
    void trigger(String msg) { service.send(msg); }
}
```

**Solution**
Depend on abstractions and supply concrete implementations from the outside:

```java
interface NotificationService {
    void send(String msg);
}

class EmailService implements NotificationService {
    public void send(String msg) { /* ... */ }
}

class AlertManager {
    private final NotificationService service;
    AlertManager(NotificationService svc) { this.service = svc; }
    void trigger(String msg) { service.send(msg); }
}
```

* High-level code is decoupled from low-level details.
* Implementations can vary (e.g., email, SMS) without changing `AlertManager`.

### Final Thoughts

SOLID principles act as guardrails when designing software. They are not strict rules but guidelines that help you build systems that are easier to maintain and extend. Future sections of this repository will explore additional design topics.
