package in.rajgrihisingh.adapter;

public class ViewAttendanceList {
    private String id,name,count, mobile, designation;

    public ViewAttendanceList(String id, String name, String count, String mobile, String designation) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.mobile = mobile;
        this.designation = designation;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
