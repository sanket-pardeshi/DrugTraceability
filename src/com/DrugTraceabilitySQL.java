package com;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class DrugTraceabilitySQL extends JFrame implements ActionListener {
    private final JTextField dateField;
    private final JTextField medicineField;
    private final JTextField currentPathField;
    private final JButton enterButton;
    private final JButton displayButton;
    private final JButton searchButton;
    private final JTextArea displayArea;
    private Connection conn;

    public DrugTraceabilitySQL() {
        super("Drug Traceability");

//        conn = null;
        try {
            // Connect to the MySQL database
            String url = "jdbc:mysql://localhost:3306/drug_traceability";
//            String url = "jdbc:mysql://127.0.0.1:3306/drug_traceability";
            String user = "root";
            String password = "saru12345";
            conn = DriverManager.getConnection(url, user, password);

            // Create the drug traceability table if it doesn't exist
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS drug_traceability_table " +
                    "(date VARCHAR(255), medicine_name VARCHAR(255), current_position VARCHAR(255), total_path VARCHAR(255))";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        JPanel inputPanel = new JPanel(new GridLayout(3, 0));
        inputPanel.add(new JLabel("Date:"));
        dateField = new JTextField();
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Medicine Name:"));
        medicineField = new JTextField();
        inputPanel.add(medicineField);
        inputPanel.add(new JLabel("Current Position:"));
        currentPathField = new JTextField();
        inputPanel.add(currentPathField);

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
        searchButton.addActionListener(this);
        searchButton.setFont(boldFont);
        inputPanel1.add(searchButton);

        displayArea = new JTextArea(20, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(inputPanel1, BorderLayout.CENTER);
        contentPane.add(scrollPane, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == enterButton) {
            String date = dateField.getText();
            String medicineName = medicineField.getText().toLowerCase();
            String currentPath = currentPathField.getText();
            try {
                // Insert data into the drug traceability table
                String sql = "INSERT INTO drug_traceability_table (date, medicine_name, current_position, total_path) " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, date);
                pstmt.setString(2, medicineName);
                pstmt.setString(3, currentPath);
                pstmt.setString(4, "");

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Data successfully entered!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to enter data. Please try again.");
                }

                pstmt.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else if (source == displayButton) {
            try {
                // Retrieve data from the drug traceability table
                String sql = "SELECT * FROM drug_traceability_table";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                // Display data in the text area
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("Date: ").append(rs.getString("date")).append("\n");
                    sb.append("Medicine Name: ").append(rs.getString("medicine_name")).append("\n");
                    sb.append("Current Position: ").append(rs.getString("current_position")).append("\n");
                    sb.append("Total Path: ").append(rs.getString("total_path")).append("\n\n");
                }
                displayArea.setText(sb.toString());

                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else if (source == searchButton) {
            String medicineName = JOptionPane.showInputDialog("Enter the medicine name to search for:");
            try {
                // Retrieve data from the drug traceability table based on medicine name
                String sql = "SELECT * FROM drug_traceability_table WHERE medicine_name=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, medicineName);
                ResultSet rs = pstmt.executeQuery();

                // Display data in the text area
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("Date: ").append(rs.getString("date")).append("\n");
                    sb.append("Medicine Name: ").append(rs.getString("medicine_name")).append("\n");
                    sb.append("Current Position: ").append(rs.getString("current_position")).append("\n");
                    sb.append("Total Path: ").append(rs.getString("total_path")).append("\n\n");
                }
                if (sb.length() > 0) {
                    displayArea.setText(sb.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "No data found for " + medicineName);
                }

                rs.close();
                pstmt.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        new DrugTraceabilitySQL();
    }
}
