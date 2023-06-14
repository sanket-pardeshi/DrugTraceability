package com;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrugTraceability_SQL_connected extends JFrame implements ActionListener {
    private final JTextField medicineField;
    private final JComboBox<String> cb;
    private final String[] choices;
    private final JButton enterButton;
    private final JButton displayButton;
    private final JButton searchButton;
    private final JTextArea displayArea;
    private Connection conn;

    JComboBox<Integer> yearBox;
    JComboBox<String> monthBox;
    JComboBox<String> dayBox;

    // Database table name
    static final String TABLE_NAME = "drug_traceability_table";

    // Database column names
    static final String COLUMN_DATE = "date";
    static final String COLUMN_MEDICINE = "medicine";
    static final String COLUMN_PATH = "path";


    public DrugTraceability_SQL_connected() {
        super("Drug Traceability");
        conn=null;
        try
        {

            // MySQL database credentials
            String DB_URL = "jdbc:mysql://localhost:3306/drug_traceability";
            String USER = "root";
            String PASS = "saru12345";

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS drug_traceability_table " +
                    "(COLUMN_DATE VARCHAR(255), COLUMN_MEDICINE VARCHAR(255), COLUMN_PATH VARCHAR(255))";
            stmt.executeUpdate(sql);
            stmt.close();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        JPanel inputPanel_date = new JPanel(new GridLayout(1, 0));
        inputPanel_date.add(new JLabel("Date:"));

        yearBox = new JComboBox<>();
        monthBox = new JComboBox<>();
        dayBox = new JComboBox<>();

        // Add years to yearBox
        for (int i = 2000; i <= 2100; i++) {
            yearBox.addItem(i);
        }

        // Add months to monthBox
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthBox.addItem(month);
        }

        // Add days to dayBox
        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                String k = "0" + "" + i;
                dayBox.addItem(k);
            } else {
                dayBox.addItem(Integer.toString(i));
            }
        }

        // Add components to panel
        inputPanel_date.add(dayBox);
        inputPanel_date.add(monthBox);
        inputPanel_date.add(yearBox);

        JPanel inputPanel = new JPanel(new GridLayout(2, 0));
        inputPanel.add(new JLabel("Medicine Name:"));
        medicineField = new JTextField();
        inputPanel.add(medicineField);
        inputPanel.add(new JLabel("Current Position:"));
        choices = new String[]{"Manufacturer", "Distributor", "Pharmacist", "Patient"};
        cb = new JComboBox<String>(choices);
        cb.setVisible(true);
        inputPanel.add(cb);

        JPanel inputPanel1 = new JPanel(new GridLayout(1, 0));
        enterButton = new JButton("Enter");
        enterButton.addActionListener(this);
        Font font = enterButton.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        enterButton.setFont(boldFont);
        inputPanel1.add(enterButton);
        displayButton = new JButton("Display");
        displayButton.addActionListener(this);
        displayButton.setFont(boldFont);
        inputPanel1.add(displayButton);
        searchButton = new JButton("Search");
//        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchButton.setFont(boldFont);
        inputPanel1.add(searchButton);
        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(inputPanel_date, BorderLayout.NORTH);
        c.add(inputPanel, BorderLayout.CENTER);
        c.add(inputPanel1, BorderLayout.SOUTH);
        c.add(scrollPane, BorderLayout.EAST);

        setSize(800, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enterButton) {
            String date = yearBox.getSelectedItem().toString() + "-" + (monthBox.getSelectedIndex() + 1) + "-" + dayBox.getSelectedItem().toString();
            String medicine = medicineField.getText();
            String path = cb.getSelectedItem().toString();
            try {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_DATE + ", " + COLUMN_MEDICINE + ", " + COLUMN_PATH + ") VALUES ('" + date + "', '" + medicine + "', '" + path + "')";
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Data inserted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error inserting data: " + ex.getMessage());
            }
        } else if (e.getSource() == displayButton) {
            displayArea.setText("");
            try {
                Statement stmt = conn.createStatement();
                String sql = "SELECT * FROM " + TABLE_NAME;
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String date = rs.getString(COLUMN_DATE);
                    String medicine = rs.getString(COLUMN_MEDICINE);
                    String path = rs.getString(COLUMN_PATH);
                    displayArea.append(date + "\t" + medicine + "\t" + path + "\n");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error displaying data: " + ex.getMessage());
            }
        } else if (e.getSource() == searchButton) {
            String medicine = medicineField.getText();
            displayArea.setText("");
            try  {
                Statement stmt = conn.createStatement();
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MEDICINE + "='" + medicine + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String date = rs.getString(COLUMN_DATE);
                    String path = rs.getString(COLUMN_PATH);
                    displayArea.append(date + "\t" + medicine + "\t" + path + "\n");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error searching data: " + ex.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        new DrugTraceability_SQL_connected();
    }
}


