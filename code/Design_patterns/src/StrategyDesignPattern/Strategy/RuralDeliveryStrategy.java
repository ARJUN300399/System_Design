package StrategyDesignPattern.Strategy;

public class RuralDeliveryStrategy implements DeliveryChargeStrategy {
    @Override
    public double calculateDeliveryCharge(double distance) {
        System.out.println("Rural delivery strategy");
        return distance * 20;
    }
}
