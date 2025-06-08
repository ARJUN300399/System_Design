# System Design Notes

This repository collects notes about system design and software architecture. The first topic is a short primer on the **SOLID** design principles.

## SOLID Design Principles

**SOLID** is an acronym for five guidelines that encourage clean and maintainable object-oriented code. Each principle is defined below with a brief example.

### 1. Single Responsibility Principle (SRP)
A class should have only one reason to change and focus on a single responsibility.

```java
class Invoice {
    void calculateTotal() { /* logic */ }
}

class InvoiceRepository {
    void save(Invoice invoice) { /* persistence logic */ }
}
```

### 2. Open/Closed Principle (OCP)
Software entities should be open for extension but closed for modification. New behaviour is added by extension rather than editing existing source.

```java
interface PaymentProcessor {
    void pay(Order order);
}

class CreditCardProcessor implements PaymentProcessor { /* ... */ }
class PaypalProcessor implements PaymentProcessor { /* ... */ }
```

Adding a new payment option only requires implementing the `PaymentProcessor` interface.

### 3. Liskov Substitution Principle (LSP)
Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

```java
class Bird {
    void fly() {}
}

class Sparrow extends Bird { /* can fly */ }
class Ostrich extends Bird { /* cannot fly */ }
```

`Ostrich` violates LSP because it cannot fulfill the expectation that all `Bird` instances can fly.

### 4. Interface Segregation Principle (ISP)
Clients should not be forced to depend on interfaces they do not use. Prefer several small, role-specific interfaces.

```java
interface Printable {
    void print(Document d);
}

interface Scannable {
    void scan(Document d);
}

class Printer implements Printable { /* ... */ }
class MultiFunctionMachine implements Printable, Scannable { /* ... */ }
```

### 5. Dependency Inversion Principle (DIP)
Depend on abstractions, not on concrete implementations. High-level modules should not rely on low-level details.

```java
interface NotificationService {
    void send(String message);
}

class EmailService implements NotificationService { /* ... */ }

class AlertManager {
    private final NotificationService service;

    AlertManager(NotificationService service) {
        this.service = service;
    }

    void triggerAlert(String msg) {
        service.send(msg);
    }
}
```

By injecting the `NotificationService` interface, `AlertManager` can work with any implementation (email, SMS, etc.).

---

## Quick Revision
- **SRP** – one responsibility per class.
- **OCP** – extend behaviour without modifying existing code.
- **LSP** – subtypes must be usable in place of their base types.
- **ISP** – favor several focused interfaces over a single general one.
- **DIP** – depend on abstractions, not concretions.

These principles act as guardrails for building flexible and testable systems. Future updates to this repository will cover additional design topics.
