package in.rajgrihisingh.model;

public class SalaryList {
     private String id,date,staff_name,amount;

    public SalaryList(String id, String date, String staff_name, String amount) {
        this.id = id;
        this.date = date;
        this.staff_name = staff_name;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
