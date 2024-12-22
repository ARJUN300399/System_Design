package StrategyDesignPattern.Strategy;

public class UrbanDeliveryStrategy implements DeliveryChargeStrategy {
    @Override
    public double calculateDeliveryCharge(double distance) {
        System.out.println("Urban delivery strategy");
        return distance * 10;
    }

}
