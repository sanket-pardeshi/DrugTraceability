package com;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;



import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrugTraceabilityGUI_V1 extends JFrame implements ActionListener {
    private final JTextField dateField;
    private final JTextField medicineField;
    private final JComboBox<String> cb;
    private final String[] choices;
    private final JButton enterButton;
    private final JButton displayButton;
    private final JButton searchButton;
    private final JTextArea displayArea;
    private final SortedMap<String, Block> blockList;
    private final String prevDate;
    private final int cnt;

    public DrugTraceabilityGUI_V1() {
        super("Drug Traceability");
        blockList = new TreeMap<>();
        prevDate = "";
        cnt = 0;

        JPanel inputPanel = new JPanel(new GridLayout(3, 0));
        inputPanel.add(new JLabel("Date:"));
        dateField = new JTextField();
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Medicine Name:"));
        medicineField = new JTextField();
        inputPanel.add(medicineField);
        inputPanel.add(new JLabel("Current Position:"));
//        currentPathField = new JTextField();
//        inputPanel.add(currentPathField);
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
            if(!dateField.getText().equals("")&&!medicineField.getText().equals("")) {
                String date = dateField.getText();
                String medicineName = medicineField.getText().toLowerCase();
//            String currentPath = currentPathField.getText();
                String currentPath = choices[cb.getSelectedIndex()];



            String date_search = "";
            for (Map.Entry<String, Block> block_list : blockList.entrySet()) {
                if (block_list.getValue().dataList.containsKey(medicineName)) {
                    date_search = block_list.getValue().transactionId;
                }
            }
            if (!blockList.containsKey(date)) {
                if (date_search.equals("")) {
                    Block bk = new Block(date, currentPath, medicineName, "Manufacturer" + "->" + currentPath);
                    blockList.put(date, bk);
                } else {
                    Block bk = new Block(date, currentPath, medicineName, blockList.get(date_search).dataList.get(medicineName).totalPath + "->" + currentPath);
                    blockList.put(date, bk);
                }
            } else {
                if (!blockList.get(date).dataList.containsKey(medicineName)) {

                    if (date_search.equals("")) {
                        Data_Block db = new Data_Block(currentPath, medicineName, "Manufacturer" + "->" + currentPath);
                        blockList.get(date).dataList.put(medicineName, db);
                    } else {
                        Data_Block db = new Data_Block(currentPath, medicineName, blockList.get(date_search).dataList.get(medicineName).totalPath + "->" + currentPath);
                        blockList.get(date).dataList.put(medicineName, db);
                    }

                } else {
                    Data_Block db = new Data_Block(currentPath, medicineName, blockList.get(date).dataList.get(medicineName).totalPath + "->" + currentPath);
                    blockList.get(date).dataList.put(medicineName, db);
                }
            }
            // clear the input fields
            dateField.setText("");
            medicineField.setText("");
//            currentPathField.setText("");
            cb.setSelectedIndex(0);

            // update display area with success message
            displayArea.setText("Data added successfully.\n");
            }else{
                displayArea.setText("*Fill in the data");
            }
        }
            else if (source == displayButton) {
            // display all data in the blockList
            if (blockList.isEmpty()) {
                displayArea.setText("No data available.");
            } else {
                String display = "";
                for (Map.Entry<String, Block> block_list : blockList.entrySet()) {
                    display += "Date: " + block_list.getKey() + "\n";
                    for (Map.Entry<String, Data_Block> data_list : block_list.getValue().dataList.entrySet()) {
                        display += "Medicine Name: " + data_list.getKey() + "\n";
                        display += "Current Position: " + data_list.getValue().currentPath + "\n";
                        display += "Total Path: " + data_list.getValue().totalPath + "\n";
                        display += "\n";
                    }
                    display += "---------------------------------------\n";
                }
                displayArea.setText(display);
            }
        } else if (source == searchButton) {
            // search for data by medicine name
            String medicineName = JOptionPane.showInputDialog("Enter medicine name to search:");
            if (medicineName != null && !medicineName.isEmpty()) {
                String searchResult = "";
                for (Map.Entry<String, Block> block_list : blockList.entrySet()) {
                    if (block_list.getValue().dataList.containsKey(medicineName.toLowerCase())) {
                        searchResult += "Date: " + block_list.getKey() + "\n";
                        searchResult += "Medicine Name: " + medicineName + "\n";
                        searchResult += "Current Position: " + block_list.getValue().dataList.get(medicineName.toLowerCase()).currentPath + "\n";
                        searchResult += "Total Path: " + block_list.getValue().dataList.get(medicineName.toLowerCase()).totalPath + "\n";
                        searchResult += "\n";
                    }
                }
                if (searchResult.equals("")) {
                    displayArea.setText("No data found for medicine name '" + medicineName + "'.");
                } else {
                    displayArea.setText(searchResult);
                }
            } else {
                displayArea.setText("Invalid input.");
            }
        }
    }

    public static void main(String[] args) {
        new DrugTraceabilityGUI_V1();
    }
}



