package com.smartgrid.strategy;

import com.smartgrid.model.CityZone;
import java.util.List;

/**
 * PATTERN 2: Strategy (Concrete Strategy B)
 * Eco-Friendly: Reduces power to non-critical zones by 30% to save energy,
 * while giving critical zones 100%.
 */
public class EcoStrategy implements DistributionStrategy {
    @Override
    public void distributePower(int totalAvailablePower, List<CityZone> zones) {
        int remainingPower = totalAvailablePower;

        for (CityZone zone : zones) {
            int toGive;
            if (zone.isCritical()) {
                toGive = zone.getDemand(); // Give critical full power
            } else {
                toGive = (int) (zone.getDemand() * 0.7); // Reduce 30% for others
            }
            
            toGive = Math.min(toGive, remainingPower);
            zone.setAllocated(toGive);
            remainingPower -= toGive;
        }
    }

    @Override
    public String getStrategyName() {
        return "Eco-Friendly Strategy 🌿";
    }
}
