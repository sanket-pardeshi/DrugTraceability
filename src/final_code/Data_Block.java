package final_code;


public class Data_Block {
    public String currentPath;
    public String totalPath;
    public String medicineName;

    // Constructor for the block
    public Data_Block(String currentPath, String medicineName, String totalPath) {
        this.medicineName = medicineName;
        this.currentPath = currentPath;
        this.totalPath = totalPath;
    }

    public void display_data(){
        System.out.println("Medicine Name : "+medicineName);
        System.out.println("Current Path : "+currentPath);
        System.out.println("Total Path : "+totalPath);
    }
}
