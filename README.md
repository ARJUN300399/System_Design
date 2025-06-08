# System Design Notes

This repository collects notes about system design and software architecture. The focus of this document is a detailed primer on the **SOLID** design principles.

## SOLID Design Principles

**SOLID** is a mnemonic for five key guidelines that encourage clean and maintainable object-oriented code.

### 1. Single Responsibility Principle (SRP)
Each class or module should have one clear purpose and only one reason to change.

```java
class Invoice {
    void calculateTotal() { /* logic */ }
}

class InvoiceRepository {
    void save(Invoice invoice) { /* persistence logic */ }
}
```

### 2. Open/Closed Principle (OCP)
Software entities should be open for extension but closed for modification. You can introduce new behavior without altering existing source.

```java
interface PaymentProcessor {
    void pay(Order order);
}

class CreditCardProcessor implements PaymentProcessor { /* ... */ }
class PaypalProcessor implements PaymentProcessor { /* ... */ }
```

Adding a new payment option only requires implementing the `PaymentProcessor` interface.

### 3. Liskov Substitution Principle (LSP)
Subtypes must be substitutable for their base types without altering the desirable properties of a program.

```java
class Bird { 
    void fly() {}
}

class Sparrow extends Bird { /* can fly */ }
class Ostrich extends Bird { /* cannot fly */ }
```

`Ostrich` violates LSP because it cannot fulfill the expectation that all `Bird` instances can fly.

### 4. Interface Segregation Principle (ISP)
Prefer many small, client-specific interfaces over a single general-purpose interface.

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
Depend on abstractions rather than concrete implementations. High-level modules should not rely on low-level details.

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

These examples illustrate how SOLID principles guide the construction of flexible and testable systems. Future updates to this repository will cover other system design topics.

