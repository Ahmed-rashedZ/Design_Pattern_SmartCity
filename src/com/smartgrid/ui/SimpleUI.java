package com.smartgrid.ui;

import com.smartgrid.manager.GridManager;
import com.smartgrid.model.CityZone;
import com.smartgrid.observer.GridObserver;
import com.smartgrid.strategy.EcoStrategy;
import com.smartgrid.strategy.EmergencyStrategy;
import com.smartgrid.strategy.NormalStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Modern, clean, and simple GUI for the Smart Grid.
 * Also acts as an Observer.
 */
public class SimpleUI extends JFrame implements GridObserver {

    private GridManager manager;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    private JSlider powerSlider;
    private JLabel powerLabel;

    public SimpleUI() {
        manager = GridManager.getInstance();
        manager.getEventManager().subscribe(this); // Observer pattern

        setupWindow();
    }

    private void setupWindow() {
        setTitle("Smart Grid Controller - Interactive Edition");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.WHITE);

        // --- 1. Top Panel (Power Slider + Strategies) ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(245, 245, 250));

        // Slider for Total Power
        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        sliderPanel.setOpaque(false);
        int totalDemand = manager.getTotalDemand(); // Should be 175
        powerLabel = new JLabel("Available Grid Power: " + totalDemand + " MW");
        powerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        powerSlider = new JSlider(0, 200, totalDemand);
        powerSlider.setPreferredSize(new Dimension(300, 40));
        powerSlider.setMajorTickSpacing(50);
        powerSlider.setMinorTickSpacing(10);
        powerSlider.setPaintTicks(true);
        powerSlider.setPaintLabels(true);
        powerSlider.addChangeListener(e -> powerLabel.setText("Available Grid Power: " + powerSlider.getValue() + " MW"));

        sliderPanel.add(new JLabel("Low Power ⚡"));
        sliderPanel.add(powerSlider);
        sliderPanel.add(powerLabel);

        // Strategy Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        JButton normalBtn = createButton("Normal Day ☀️", new Color(52, 152, 219));
        normalBtn.addActionListener(e -> applyStrategy(new NormalStrategy()));

        JButton ecoBtn = createButton("Eco-Friendly 🌿", new Color(46, 204, 113));
        ecoBtn.addActionListener(e -> applyStrategy(new EcoStrategy()));

        JButton emergencyBtn = createButton("Emergency 🚨", new Color(231, 76, 60));
        emergencyBtn.addActionListener(e -> applyStrategy(new EmergencyStrategy()));

        btnPanel.add(normalBtn);
        btnPanel.add(ecoBtn);
        btnPanel.add(emergencyBtn);

        topContainer.add(sliderPanel, BorderLayout.NORTH);
        topContainer.add(btnPanel, BorderLayout.CENTER);

        // --- 2. Center Panel (Zones Table) ---
        String[] columns = {"Zone Name", "Type", "Demand (MW)", "Allocated (MW)", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setEnabled(false); // Make it read-only
        
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // --- 3. Bottom Panel (Event Log) ---
        logArea = new JTextArea(7, 40);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        logArea.setBackground(new Color(250, 250, 250));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("System Event Log (Observer Pattern)"));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(logScroll, BorderLayout.CENTER);

        // Add to Frame
        add(topContainer, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initial load
        applyStrategy(new NormalStrategy());
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 40));
        return btn;
    }

    private void applyStrategy(com.smartgrid.strategy.DistributionStrategy strategy) {
        int power = powerSlider.getValue();
        manager.applyStrategy(strategy, power);
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Clear table
        for (CityZone zone : manager.getZones()) {
            String type = zone.isCritical() ? "Critical" : "Standard";
            tableModel.addRow(new Object[]{
                zone.getName(), 
                type, 
                zone.getDemand(), 
                zone.getAllocated(), 
                zone.getStatus()
            });
        }
    }

    @Override
    public void onGridUpdate(String message) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logArea.append("[" + time + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
