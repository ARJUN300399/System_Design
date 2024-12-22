package StrategyDesignPattern;
import StrategyDesignPattern.Context.DeliveryChargeCalculator;
import StrategyDesignPattern.Strategy.RuralDeliveryStrategy;
import StrategyDesignPattern.Strategy.SuburbanDeliveryStrategy;
import StrategyDesignPattern.Strategy.UrbanDeliveryStrategy;
//Client class
public class ZomatoCharge {
 public static void main(String[] args) {
       DeliveryChargeCalculator calculator = new DeliveryChargeCalculator();
         // Urban delivery
        calculator.setStrategy(new UrbanDeliveryStrategy());
        System.out.println("Urban Charge: " + calculator.calculateDeliveryCharge(5)); // 50.0

        // Suburban delivery
        calculator.setStrategy(new SuburbanDeliveryStrategy());
        System.out.println("Suburban Charge: " + calculator.calculateDeliveryCharge(5)); // 60.0

        // Rural delivery
        calculator.setStrategy(new RuralDeliveryStrategy());
        System.out.println("Rural Charge: " + calculator.calculateDeliveryCharge(5));
 }
}