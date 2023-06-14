package com;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrugTraceabilityGUI extends JFrame implements ActionListener {
    private final JTextField medicineField;
    private final JComboBox<String> cb;
    private final String[] choices;
    private final JButton enterButton;
    //    private final_code JTextField dateField;
    private final JButton displayButton;
    private final JButton searchButton;
    private final JTextArea displayArea;
    private final SortedMap<String, Block> blockList;
    private final SortedMap<String, Integer> date_cnt;

    JComboBox<Integer> yearBox;
    JComboBox<String> monthBox;
    JComboBox<String> dayBox;

    public DrugTraceabilityGUI() {
        super("Drug Traceability");
        blockList = new TreeMap<>();
        date_cnt = new TreeMap<>();

        JPanel inputPanel_date = new JPanel(new GridLayout(1, 0));
        inputPanel_date.add(new JLabel("Date:"));
//        dateField = new JTextField();
//        inputPanel.add(dateField);

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
        contentPane.add(inputPanel_date);
        contentPane.add(inputPanel);
        contentPane.add(inputPanel1);
        contentPane.add(scrollPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new FlowLayout());
        pack();
        setSize(600, 400);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == enterButton) {
//            if(!dateField.getText().equals("")&&!medicineField.getText().equals("")) {
            if (!medicineField.getText().equals("")) {
//                String date = dateField.getText();
                int k = monthBox.getSelectedIndex() + 1;
                String date = "";
                if (k < 10) {
                    date = dayBox.getSelectedItem() + "0" + (k) + "" + yearBox.getSelectedItem();
                } else {
                    date = dayBox.getSelectedItem() + "" + (k) + "" + yearBox.getSelectedItem();
                }
                String medicineName = medicineField.getText().toLowerCase();
//            String currentPath = currentPathField.getText();
                String currentPath = choices[cb.getSelectedIndex()];

                if (!date_cnt.containsKey(date)) {
                    date_cnt.put(date, 0);
                }

                String date_search = "";
                for (Map.Entry<String, Block> block_list : blockList.entrySet()) {
                    if (block_list.getValue().dataList.containsKey(medicineName)) {
                        date_search = block_list.getValue().transactionId;
                    }
                }
                String date_store = date + "_" + date_cnt.get(date);

                if (!blockList.containsKey(date_store)) {
                    if (date_search.equals("")) {
                        Block bk = new Block(date_store, currentPath, medicineName, currentPath);
                        blockList.put(date_store, bk);
                    } else {
                        Block bk = new Block(date_store, currentPath, medicineName, blockList.get(date_search).dataList.get(medicineName).totalPath + "->" + currentPath);
                        blockList.put(date_store, bk);
                    }
                } else {
                    if (blockList.get(date_store).dataList.size() == 5) {
                        date_cnt.put(date, date_cnt.get(date) + 1);
                        date_store = date + "_" + date_cnt.get(date);
                        if (date_search.equals("")) {
                            Block bk = new Block(date_store, currentPath, medicineName, currentPath);
                            blockList.put(date_store, bk);
                        } else {
                            Block bk = new Block(date_store, currentPath, medicineName, blockList.get(date_search).dataList.get(medicineName).totalPath + "->" + currentPath);
                            blockList.put(date_store, bk);
                        }
                    } else {
                        if (!blockList.get(date_store).dataList.containsKey(medicineName)) {
                            if (date_search.equals("")) {
                                Data_Block db = new Data_Block(currentPath, medicineName, currentPath);
                                blockList.get(date_store).dataList.put(medicineName, db);
                            } else {
                                Data_Block db = new Data_Block(currentPath, medicineName, blockList.get(date_search).dataList.get(medicineName).totalPath + "->" + currentPath);
                                blockList.get(date_store).dataList.put(medicineName, db);
                            }

                        } else {
                            Data_Block db = new Data_Block(currentPath, medicineName, blockList.get(date_store).dataList.get(medicineName).totalPath + "->" + currentPath);
                            blockList.get(date_store).dataList.put(medicineName, db);
                        }
                    }
                }
                // clear the input fields
//                dateField.setText("");
                medicineField.setText("");
//            currentPathField.setText("");
                cb.setSelectedIndex(0);

                // update display area with success message
                displayArea.setText("Data added successfully.\n");
            } else {
                displayArea.setText("*Fill in the data");
            }
        } else if (source == displayButton) {
            // display all data in the blockList
            if (blockList.isEmpty()) {
                displayArea.setText("No data available.");
            } else {
                String display = "";
                for (Map.Entry<String, Block> block_list : blockList.entrySet()) {
                    String day = block_list.getKey();
                    display += "Date: " + day.substring(0, 2) + "/" + day.substring(2, 4) + "/" + day.substring(4, 8) + "\n\n";
//                    display += "Date: " + day + "\n\n";
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
                searchResult += "Medicine Name: " + medicineName + "\n\n";
                for (Map.Entry<String, Block> block_list : blockList.entrySet()) {
                    if (block_list.getValue().dataList.containsKey(medicineName.toLowerCase())) {
                        String day = block_list.getKey();
                        searchResult += "Date: " + day.substring(0, 2) + "/" + day.substring(2, 4) + "/" + day.substring(4, 8);
                        searchResult += "Current Position: " + block_list.getValue().dataList.get(medicineName.toLowerCase()).currentPath + "\n";
                        searchResult += "Total Path: " + block_list.getValue().dataList.get(medicineName.toLowerCase()).totalPath + "\n";
                        searchResult += "\n";
                    }
                }
                String temp="Medicine Name: " + medicineName + "\n\n";
                if (searchResult.equals(temp)) {
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
        new DrugTraceabilityGUI();
    }
}



