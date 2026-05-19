package com.smartgrid.strategy;

import com.smartgrid.model.CityZone;
import java.util.List;

/**
 * PATTERN 2: Strategy (Concrete Strategy C)
 * Emergency: Gives 100% to Critical zones first. Non-critical zones get NOTHING.
 */
public class EmergencyStrategy implements DistributionStrategy {
    @Override
    public void distributePower(int totalAvailablePower, List<CityZone> zones) {
        int remainingPower = totalAvailablePower;

        // Step 1: Supply critical zones first
        for (CityZone zone : zones) {
            if (zone.isCritical()) {
                int toGive = Math.min(zone.getDemand(), remainingPower);
                zone.setAllocated(toGive);
                remainingPower -= toGive;
            } else {
                zone.setAllocated(0); // Cut power initially
            }
        }
    }

    @Override
    public String getStrategyName() {
        return "Emergency Strategy 🚨";
    }
}
