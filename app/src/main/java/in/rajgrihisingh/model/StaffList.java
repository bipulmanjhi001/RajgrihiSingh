package in.rajgrihisingh.model;

public class StaffList {
    private String name,mobile,address,designation,image;

    public StaffList(String name, String mobile,
                     String address, String designation, String image) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.designation = designation;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
