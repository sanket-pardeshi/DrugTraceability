package com;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class DrugTraceabilityGUI1 extends JFrame implements ActionListener {
    private JTextField dateField, medicineField, currentPathField;
    private JButton enterButton, displayButton, searchButton;
    private JTextArea displayArea;
    private SortedMap<String, Block> blockList;
    private String prevDate;
    private int cnt;

    public DrugTraceabilityGUI1() {
        super("Drug Traceability");

        blockList = new TreeMap<>();
        prevDate = "";
        cnt = 0;

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Date:"));
        dateField = new JTextField();
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Medicine Name:"));
        medicineField = new JTextField();
        inputPanel.add(medicineField);
        inputPanel.add(new JLabel("Current Position:"));
        currentPathField = new JTextField();
        inputPanel.add(currentPathField);

        enterButton = new JButton("Enter");
        enterButton.addActionListener(this);
        inputPanel.add(enterButton);
        displayButton = new JButton("Display");
        displayButton.addActionListener(this);
        inputPanel.add(displayButton);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        inputPanel.add(searchButton);

        displayArea = new JTextArea(20, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == displayButton) {
            displayArea.setText("");
            for (Map.Entry<String, Block> blockList_entry : blockList.entrySet()) {
                displayArea.append("Block " + blockList_entry.getValue().transactionId + ":\n");
                for (Map.Entry<String, Data_Block> dataBlock_entry : blockList_entry.getValue().dataList.entrySet()) {
                    displayArea.append("Medicine: " + dataBlock_entry.getValue().medicineName + "\n");
                    displayArea.append("Current Position: " + dataBlock_entry.getValue().currentPath + "\n");
                    displayArea.append("Transaction Path: " + dataBlock_entry.getValue().totalPath + "\n");
                    displayArea.append("\n");
                }
            }
        } else if (source == searchButton) {
            String medicineName = medicineField.getText().toLowerCase();
            String date_search = "";
            for (Map.Entry<String, Block> block_list : blockList.entrySet()) {
                if (block_list.getValue().dataList.containsKey(medicineName)) {
                    date_search = block_list.getValue().transactionId;
                }
            }
            if (!date_search.equals("")) {
                displayArea.setText("Transaction ID: " + date_search + "\n");
                displayArea.append("Medicine: " + blockList.get(date_search).dataList.get(medicineName).medicineName + "\n");
                displayArea.append("Current Position: " + blockList.get(date_search).dataList.get(medicineName).currentPath + "\n");
                displayArea.append("Transaction Path: " + blockList.get(date_search).dataList.get(medicineName).totalPath + "\n");
            } else {
                displayArea.setText("Medicine not found!");
            }
        }
    }

    public static void main(String[] args) {
        DrugTraceabilityGUI1 gui = new DrugTraceabilityGUI1();
    }

//    static class StringUtil {
//        public static String applySha256(String input) {
//            try {
//                MessageDigest digest = MessageDigest.getInstance("SHA-256");
//                byte[] hash = digest.digest(input.getBytes("UTF-8"));
//                StringBuffer hexString = new StringBuffer();
//                for (int i = 0; i < hash.length; i++) {
//                    String hex = Integer.toHexString(0xff & hash[i]);
//                    if (hex.length() == 1)
//                        hexString.append('0');
//                    hexString.append(hex);
//                }
//                return hexString.toString();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}


