package in.rajgrihisingh.adapter;

public class Fitting_Band {

    private String id="";
    private String type="";
    private boolean checked=false;

    public Fitting_Band(String id, String type, boolean checked) {
        this.id = id;
        this.type = type;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
