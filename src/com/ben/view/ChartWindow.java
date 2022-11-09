package com.ben.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class ChartWindow extends JFrame implements ActionListener {
    private final Connection con;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private final JComboBox<String> optionSelect = new JComboBox<>(new String[]{"Category","Content Rating", "Installs"});

    private final JButton
        pieChartButton = new JButton("Pie chart"),
        barChartButton = new JButton("Bar chart"),
        ratingChartButton = new JButton("Avg rating");
    public ChartWindow(Connection con) {
        this.con = con;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        JPanel optionsPanel = new JPanel();


        optionsPanel.add(optionSelect);
        optionSelect.setSelectedIndex(0);

        pieChartButton.addActionListener(this);
        optionsPanel.add(pieChartButton);
        barChartButton.addActionListener(this);
        optionsPanel.add(barChartButton);
        ratingChartButton.addActionListener(this);
        optionsPanel.add(ratingChartButton);

        add(optionsPanel, BorderLayout.NORTH);

        pieChart();

        setTitle("Chart");
        setLocationRelativeTo(null);
    }

    private String[] basicSelector(String s) {
        String[] params = new String[2];
        //  Category
        if (optionSelect.getSelectedIndex() == 0) {
            params[0] = String.format(s, "category", "category", "category");
            params[1] = "Category";
            return params;
        }
        //  Content Rating
        else if (optionSelect.getSelectedIndex() == 1) {
            params[0] = String.format(s, "content_rating", "content_rating", "content_rating");
            params[1] = "Content Rating";
            return params;
        }
        //  Installs
        else if (optionSelect.getSelectedIndex() == 2) {
            params[0] = String.format(s, "REPLACE(installs, ',', '') install_count", "installs", "CAST(install_count AS UNSIGNED)");
            params[1] = "Installs";
            return params;
        }
        return params;
    }

    private void pieChart() {
        try {
            var ds = new DefaultPieDataset();
            String query = "SELECT %s, count(*) FROM android_apps GROUP BY %s ORDER BY %s";
            String[] params = basicSelector(query);
            var rs = con.createStatement().executeQuery(params[0]);
            while(rs.next()) {
                ds.setValue(rs.getString(1), rs.getInt(2));
            }
            chart = ChartFactory.createPieChart("App count by "+params[1],ds,true, true, false);
            showChart();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void barChart() {
        try {
            var ds = new DefaultCategoryDataset();
            String query = "SELECT %s, count(*) FROM android_apps GROUP BY %s ORDER BY %s";
            String[] params = basicSelector(query);
            var rs = con.createStatement().executeQuery(params[0]);
            while(rs.next()) {
                ds.setValue(rs.getInt(2), rs.getString(1), "");
            }
            chart = ChartFactory.createBarChart(
                    "Average rating by " + params[1],
                    params[1],
                    "Rating (1 - 5)",
                    ds,
                    PlotOrientation.VERTICAL,
                    true, true, false
            );
            showChart();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void ratingChart() {
        try {
            var ds = new DefaultCategoryDataset();
            String query = "SELECT  %s, AVG(rating) FROM android_apps GROUP BY %s ORDER BY %s";
            String[] params = basicSelector(query);
            var rs = con.createStatement().executeQuery(params[0]);
            while(rs.next()) {
                ds.setValue(rs.getDouble(2), rs.getString(1), "");
            }
            chart = ChartFactory.createBarChart(
                    "Average rating by " + params[1],
                    params[1],
                    "Rating (1 - 5)",
                    ds,
                    PlotOrientation.VERTICAL,
                    true, true, false
            );
            showChart();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void showChart() {
        if (chartPanel != null)
            remove(chartPanel);
        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object target = e.getSource();

        if (target == pieChartButton) pieChart();
        if (target == barChartButton) barChart();
        if (target == ratingChartButton) ratingChart();
    }
}
