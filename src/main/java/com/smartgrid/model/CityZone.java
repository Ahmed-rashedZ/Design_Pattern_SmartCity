package com.smartgrid.model;

/**
 * Data Model for a City Zone.
 * Now includes power demand and allocation for a more realistic but simple simulation.
 */
public class CityZone {
    private String name;
    private boolean isCritical; // e.g., Hospital = true
    private int demand;         // Required power in MW
    private int allocated;      // Power actually given in MW

    public CityZone(String name, boolean isCritical, int demand) {
        this.name = name;
        this.isCritical = isCritical;
        this.demand = demand;
        this.allocated = 0;
    }

    public String getName() { return name; }
    public boolean isCritical() { return isCritical; }
    public int getDemand() { return demand; }
    public int getAllocated() { return allocated; }
    public void setAllocated(int allocated) { this.allocated = allocated; }

    /**
     * Helper to get status text based on allocation vs demand.
     */
    public String getStatus() {
        if (allocated >= demand) return "Fully Powered";
        if (allocated > 0) return "Partial Power";
        return "Power Cut";
    }
}
