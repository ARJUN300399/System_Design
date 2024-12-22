package StrategyDesignPattern.Context;
import StrategyDesignPattern.Strategy.DeliveryChargeStrategy;
public class DeliveryChargeCalculator {
    private DeliveryChargeStrategy deliveryChargeStrategy;
    
    public void setStrategy(DeliveryChargeStrategy deliveryChargeStrategy) {
        System.out.println("DeliveryChargeCalculator class is loaded");
        this.deliveryChargeStrategy = deliveryChargeStrategy;
    }
    public double calculateDeliveryCharge(double distance) {
        return deliveryChargeStrategy.calculateDeliveryCharge(distance);
    }
}
