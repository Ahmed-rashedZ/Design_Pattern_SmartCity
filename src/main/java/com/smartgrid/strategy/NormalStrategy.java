package com.smartgrid.strategy;

import com.smartgrid.model.CityZone;
import java.util.List;

/**
 * PATTERN 2: Strategy (Concrete Strategy A)
 * Normal Day: Tries to give everyone exactly what they demand.
 */
public class NormalStrategy implements DistributionStrategy {
    @Override
    public void distributePower(int totalAvailablePower, List<CityZone> zones) {
        int remainingPower = totalAvailablePower;

        for (CityZone zone : zones) {
            int toGive = Math.min(zone.getDemand(), remainingPower);
            zone.setAllocated(toGive);
            remainingPower -= toGive;
        }
    }

    @Override
    public String getStrategyName() {
        return "Normal Strategy ☀️";
    }
}
