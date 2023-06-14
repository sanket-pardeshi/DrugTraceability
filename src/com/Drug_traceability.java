package com;


import java.util.*;

public class Drug_traceability {
    // ArrayList to store the blocks
    public static SortedMap<String, Block> blockList = new TreeMap<>();

    // Driver code
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String prevDate = "";
        int cnt = 0;
        boolean test = true;
        while (test) {

            System.out.println("1.Enter Data");
            System.out.println("2.Display");
            System.out.println("3.Search");
            System.out.println("4.Exit");
            System.out.println("Enter your choice : ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: {
                    System.out.println("Enter Date : ");
                    String date = sc.next();
                    System.out.println("Enter medicine name : ");
                    String medicineName = sc.next().toLowerCase();
                    System.out.println("Current Position : ");
                    String currentPath = sc.next();
//                    if(cnt==0){
//                        Block bk=new Block(date,currentPath,medicineName,"Manufacturer"+"->"+currentPath);
//                        blockList.put(date,bk);
//                        prevDate=date;
//                        cnt++;
//                    }else{
//
//                        Block bk=new Block(date,currentPath,medicineName,blockList.get(prevDate).totalPath+"->"+currentPath);
//                        blockList.put(date,bk);
//                        prevDate= date;
//                        cnt++;
//                    }

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

                    break;
                }
                case 2: {
                    if (blockList.isEmpty()) {
                        System.out.println("Blockchain is empty");
                    } else {
                        for (Map.Entry<String, Block> mp : blockList.entrySet()) {
                            System.out.println("Transaction Id : " + mp.getValue().transactionId);
                            mp.getValue().display_List();
                            System.out.println();
                        }
                    }
                    break;
                }
                case 3:{
                    System.out.println("Enter medicine name : ");
                    String medicine_Name = sc.next().toLowerCase();
                    String date_search = "";
                    System.out.println("Journey of Medicine "+medicine_Name);
                    int found=0;
                    for (Map.Entry<String, Block> block_list : blockList.entrySet()) {
                        if (block_list.getValue().dataList.containsKey(medicine_Name)) {
                            String day=block_list.getValue().transactionId;
                            System.out.println(day.substring(0,2)+"/"+day.substring(2,4)+"/"+day.substring(4,8)+" --> "+block_list.getValue().dataList.get(medicine_Name).currentPath);
                            date_search = block_list.getValue().transactionId;
                            found=1;
                        }
                    }
                    if(found==1) {
                        System.out.println(blockList.get(date_search).dataList.get(medicine_Name).totalPath);
                    } else{
                        System.out.println("No such medicine found");
                    }
                    break;
                }
                case 4: {
                    test = false;
                    break;
                }
                default: {
                    System.out.println("Enter Valid Choice");
                    break;
                }

            }
        }


    }
}
