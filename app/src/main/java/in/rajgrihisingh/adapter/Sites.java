package in.rajgrihisingh.adapter;

public class Sites {
    String id,name,address, description,supervisor_name,supervisor;

    public Sites(String id, String name, String address, String description, String supervisor_name, String supervisor) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.supervisor_name = supervisor_name;
        this.supervisor = supervisor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
