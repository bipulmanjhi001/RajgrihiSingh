package in.rajgrihisingh.model;

public class Account {
    private String id,date,site_id,amount;
    private String supervisor_name,site_name;

    public Account(String id, String date, String amount, String supervisor_name, String site_name) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.supervisor_name = supervisor_name;
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

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSupervisor_name() {
        return supervisor_name;
    }

    public void setSupervisor_name(String supervisor_name) {
        this.supervisor_name = supervisor_name;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }
}
