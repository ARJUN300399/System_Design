package StrategyDesignPattern.Strategy;

public class SuburbanDeliveryStrategy implements DeliveryChargeStrategy {
    @Override
    public double calculateDeliveryCharge(double distance) {
        System.out.println("Suburban delivery strategy");
        return distance * 15;
    }

}
