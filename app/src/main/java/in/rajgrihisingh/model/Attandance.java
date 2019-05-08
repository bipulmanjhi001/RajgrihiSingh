package in.rajgrihisingh.model;

public class Attandance {
    private String ida="";
    private String name="";
    private boolean checkeda=false;

    public Attandance(String ida, String name, boolean checkeda) {
        this.ida = ida;
        this.name = name;
        this.checkeda = checkeda;
    }

    public String getIda() {
        return ida;
    }

    public void setIda(String ida) {
        this.ida = ida;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheckeda() {
        return checkeda;
    }

    public void setCheckeda(boolean checkeda) {
        this.checkeda = checkeda;
    }
}
