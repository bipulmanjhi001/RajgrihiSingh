package in.rajgrihisingh.pref;

public class ProfileUser {
    private String s_name,address;
    private String description, supervisor_name;

    public ProfileUser(String s_name, String address, String description, String supervisor_name) {
        this.s_name = s_name;
        this.address = address;
        this.description = description;
        this.supervisor_name = supervisor_name;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupervisor_name() {
        return supervisor_name;
    }

    public void setSupervisor_name(String supervisor_name) {
        this.supervisor_name = supervisor_name;
    }
}
