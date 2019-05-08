package in.rajgrihisingh.model;

public class StockList {
    private String item_type_id,item_type,site_id,item_name,site_name,quantity;

    public StockList(String item_type_id, String item_type,
                     String site_id, String item_name, String site_name, String quantity) {
        this.item_type_id = item_type_id;
        this.item_type = item_type;
        this.site_id = site_id;
        this.item_name = item_name;
        this.site_name = site_name;
        this.quantity = quantity;
    }

    public String getItem_type_id() {
        return item_type_id;
    }

    public void setItem_type_id(String item_type_id) {
        this.item_type_id = item_type_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
