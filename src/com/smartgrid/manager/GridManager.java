package com.smartgrid.manager;

import com.smartgrid.model.CityZone;
import com.smartgrid.observer.EventManager;
import com.smartgrid.strategy.DistributionStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN 3: Singleton
 * Central manager for the Grid.
 */
public class GridManager {
    private static GridManager instance;
    private List<CityZone> zones;
    private EventManager eventManager;

    private GridManager() {
        zones = new ArrayList<>();
        eventManager = new EventManager();
        
        // Add default zones with Demands (MW)
        zones.add(new CityZone("Central Hospital 🏥", true, 30));
        zones.add(new CityZone("Government Office 🏢", true, 20));
        zones.add(new CityZone("Residential Area 🏠", false, 40));
        zones.add(new CityZone("Shopping Mall 🏪", false, 35));
        zones.add(new CityZone("Factory Zone 🏭", false, 50));
    }

    public static GridManager getInstance() {
        if (instance == null) {
            instance = new GridManager();
        }
        return instance;
    }

    public List<CityZone> getZones() {
        return zones;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public int getTotalDemand() {
        int sum = 0;
        for(CityZone z : zones) sum += z.getDemand();
        return sum;
    }

    public void applyStrategy(DistributionStrategy strategy, int totalAvailablePower) {
        eventManager.notify("Applying Strategy: " + strategy.getStrategyName() + " | Grid Power: " + totalAvailablePower + " MW");
        
        // Reset allocations first
        for(CityZone z : zones) z.setAllocated(0);

        // Apply
        strategy.distributePower(totalAvailablePower, zones);
        
        // Notify about results based on strategy type
        if (strategy.getStrategyName().contains("Emergency")) {
            eventManager.notify("🚨 WARNING: Emergency protocol active. Power cut to non-essential zones.");
        } else if (strategy.getStrategyName().contains("Eco")) {
            eventManager.notify("🌿 ECO MODE: Non-critical power reduced by 30% to save energy.");
        }

        // Check if there was an overall shortage
        int totalAllocated = 0;
        for (CityZone z : zones) totalAllocated += z.getAllocated();
        
        if (totalAllocated < getTotalDemand()) {
            eventManager.notify("⚠️ DEFICIT: Not enough power to meet full city demand!");
        } else {
            eventManager.notify("✅ SUCCESS: Grid is stable.");
        }
        eventManager.notify("--------------------------------------------------");
    }
}
