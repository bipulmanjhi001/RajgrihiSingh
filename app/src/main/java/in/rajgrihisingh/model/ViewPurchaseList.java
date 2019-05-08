package in.rajgrihisingh.model;

public class ViewPurchaseList {

    private String id,date,supplier_id,invoice_no,gross_amount,total_amount,supplier_name,site_name;

    public ViewPurchaseList(String id, String date, String supplier_id, String invoice_no, String gross_amount, String total_amount, String supplier_name, String site_name) {
        this.id = id;
        this.date = date;
        this.supplier_id = supplier_id;
        this.invoice_no = invoice_no;
        this.gross_amount = gross_amount;
        this.total_amount = total_amount;
        this.supplier_name = supplier_name;
        this.site_name = site_name;
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

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }
}
