package in.rajgrihisingh.model;

public class ExpenseList {

    private String id,expense_head_id,date,bill_no,supervisor,site_name;
    private String images,name,remarks,amount,site_id,expense_head;

    public ExpenseList(String id, String expense_head_id, String date, String bill_no, String supervisor,
                       String site_name, String image, String name, String remarks, String amount,
                       String site_id, String expense_head) {
        this.id = id;
        this.expense_head_id = expense_head_id;
        this.date = date;
        this.bill_no = bill_no;
        this.supervisor = supervisor;
        this.site_name = site_name;
        this.images = image;
        this.name = name;
        this.remarks = remarks;
        this.amount = amount;
        this.site_id = site_id;
        this.expense_head = expense_head;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpense_head_id() {
        return expense_head_id;
    }

    public void setExpense_head_id(String expense_head_id) {
        this.expense_head_id = expense_head_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getImage() {
        return images;
    }

    public void setImage(String image) {
        this.images = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getExpense_head() {
        return expense_head;
    }

    public void setExpense_head(String expense_head) {
        this.expense_head = expense_head;
    }
}
