
# Strategy Design Pattern with Zomato Example

## Definition
The **Strategy Design Pattern** is a behavioral design pattern that allows you to define a family of algorithms, encapsulate each one, and make them interchangeable at runtime. It is used when there are multiple ways to achieve a behavior, and the best one can be chosen dynamically.

---

## Real-Life Example: Zomato Delivery Charge Calculation

Zomato calculates delivery charges based on:
- **Location** (urban, suburban, rural).
- **Time of Day** (peak hours or not).
- **User Type** (premium, regular).

Each of these requires a different algorithm to compute the delivery charge.

---

## Implementation

### 1. Define the Strategy Interface
Create a common interface for all delivery charge calculation strategies.

```java
public interface DeliveryChargeStrategy {
    double calculateCharge(double distance);
}
```

---

### 2. Implement Concrete Strategies
Each strategy implements a different algorithm.

```java
public class UrbanDeliveryStrategy implements DeliveryChargeStrategy {
    @Override
    public double calculateCharge(double distance) {
        return distance * 10; // Flat rate for urban areas
    }
}

public class SuburbanDeliveryStrategy implements DeliveryChargeStrategy {
    @Override
    public double calculateCharge(double distance) {
        return distance * 8 + 20; // Slightly cheaper, but with a base charge
    }
}

public class RuralDeliveryStrategy implements DeliveryChargeStrategy {
    @Override
    public double calculateCharge(double distance) {
        return distance * 15; // Higher cost for rural areas
    }
}
```

---

### 3. Create the Context Class
The context class uses a `DeliveryChargeStrategy` to calculate charges dynamically.

```java
public class DeliveryChargeCalculator {
    private DeliveryChargeStrategy strategy;

    public void setStrategy(DeliveryChargeStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateCharge(double distance) {
        if (strategy == null) {
            throw new IllegalStateException("Strategy not set");
        }
        return strategy.calculateCharge(distance);
    }
}
```

---

### 4. Client Code
Use the context class and set the appropriate strategy at runtime.

```java
public class ZomatoApp {
    public static void main(String[] args) {
        DeliveryChargeCalculator calculator = new DeliveryChargeCalculator();

        // Urban delivery
        calculator.setStrategy(new UrbanDeliveryStrategy());
        System.out.println("Urban Charge: " + calculator.calculateCharge(5)); // 50.0

        // Suburban delivery
        calculator.setStrategy(new SuburbanDeliveryStrategy());
        System.out.println("Suburban Charge: " + calculator.calculateCharge(5)); // 60.0

        // Rural delivery
        calculator.setStrategy(new RuralDeliveryStrategy());
        System.out.println("Rural Charge: " + calculator.calculateCharge(5)); // 75.0
    }
}
```

---

## Advantages
1. **Flexibility:** Easily add new strategies without modifying existing code.
2. **Separation of Concerns:** Each delivery calculation algorithm is encapsulated in its own class.
3. **Reusability:** Strategies can be reused across different contexts.

---

## Real-Life Analogy with Zomato
- **UrbanDeliveryStrategy:** Used for deliveries within metro cities.
- **SuburbanDeliveryStrategy:** Applied for towns on the outskirts.
- **RuralDeliveryStrategy:** Used for remote areas with fewer resources.

Using the Strategy Design Pattern, Zomato can easily adjust its delivery algorithms for different locations or scenarios without altering the core application logic.