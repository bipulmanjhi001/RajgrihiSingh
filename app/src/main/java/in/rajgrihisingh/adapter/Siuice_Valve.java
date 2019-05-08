package in.rajgrihisingh.adapter;

public class Siuice_Valve {

    private String id="";
    private String type="";
    private boolean checked=false;

    public Siuice_Valve(String title, String type, boolean checked) {
        this.id = title;
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
