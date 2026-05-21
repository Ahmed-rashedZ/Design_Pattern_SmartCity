package com.smartgrid.ui;

import com.smartgrid.manager.GridManager;
import com.smartgrid.model.CityZone;
import com.smartgrid.observer.GridObserver;
import com.smartgrid.strategy.EcoStrategy;
import com.smartgrid.strategy.EmergencyStrategy;
import com.smartgrid.strategy.NormalStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        
        // --- 1. Top Panel (Power Slider + Strategies) ---
        JPanel topContainer = new JPanel(new BorderLayout());

        // Slider for Total Power
        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        sliderPanel.setOpaque(false);
        int totalDemand = manager.getTotalDemand(); // Should be 175
        powerLabel = new JLabel("Available Grid Power: " + totalDemand + " MW");
        powerLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        powerSlider = new JSlider(0, 200, totalDemand);
        powerSlider.setPreferredSize(new Dimension(300, 40));
        powerSlider.setMajorTickSpacing(50);
        powerSlider.setMinorTickSpacing(10);
        powerSlider.setPaintTicks(true);
        powerSlider.setPaintLabels(true);
        powerSlider.addChangeListener(e -> powerLabel.setText("Available Grid Power: " + powerSlider.getValue() + " MW"));

        sliderPanel.add(new JLabel("Low Power "));
        sliderPanel.add(powerSlider);
        sliderPanel.add(powerLabel);

        // Strategy Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        JButton normalBtn = createButton("Normal Day", new Color(41, 128, 185));
        normalBtn.addActionListener(e -> applyStrategy(new NormalStrategy()));

        JButton ecoBtn = createButton("Eco-Friendly", new Color(39, 174, 96));
        ecoBtn.addActionListener(e -> applyStrategy(new EcoStrategy()));

        JButton emergencyBtn = createButton("Emergency", new Color(192, 57, 43));
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
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setEnabled(false); // Make it read-only
        
        // Default Center Renderer for regular columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Status Column Renderer (Colored and Centered)
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                String status = value.toString();
                if (status.equals("Fully Powered")) {
                    c.setForeground(new Color(39, 174, 96)); // Green
                } else if (status.equals("Partial Power")) {
                    c.setForeground(new Color(211, 84, 0)); // Orange
                } else {
                    c.setForeground(new Color(192, 57, 43)); // Red
                }
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 4) { // Status column index is 4
                table.getColumnModel().getColumn(i).setCellRenderer(statusRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // --- 3. Bottom Panel (Event Log) ---
        logArea = new JTextArea(8, 40);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        logArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("System Event Log (Observer Pattern)"));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(170, 45));
        return btn;
    }

    private void applyStrategy(com.smartgrid.strategy.DistributionStrategy strategy) {
        int power = powerSlider.getValue();
        manager.setStrategy(strategy);
        manager.executeStrategy(power);
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
