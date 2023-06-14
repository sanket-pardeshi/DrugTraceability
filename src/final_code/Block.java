package final_code;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Block {

    public String transactionId;
    public String previousHash;
    public String data;
    public long timeStamp;
    HashMap<String, Data_Block> dataList = new LinkedHashMap<>();

    public Block(String transactionId, String currentPath, String data, String totalPath, String previousHash) {
        this.previousHash = previousHash;
        this.data = transactionId;
        this.timeStamp = new Date().getTime();
        if(transactionId.length()<=20) {
            this.transactionId = calculateHash();
        }else{
            this.transactionId=transactionId;
        }
        Data_Block db = new Data_Block(currentPath, data, totalPath);
        dataList.put(data, db);
    }

    // Function to calculate the hash
    public String calculateHash() {
        // Calling the "crypt" class
        // to calculate the hash
        // by using the previous hash,
        // timestamp and the data
        String calculatedhash
                = crypt.sha256(
                previousHash
                        + Long.toString(timeStamp)
                        + data);
        return calculatedhash;
    }

    public void display_List() {
        for (Map.Entry<String, Data_Block> db_mp : dataList.entrySet()) {
            db_mp.getValue().display_data();
            System.out.println();
        }
    }
}
