package upec.projetandroid20182019.model;

public class Participant {
    private int id;
    private String name;
    private int planid;
    private double amount;

    public Participant(){}

    public Participant(String name, int planid) {
        this.name = name;
        this.planid = planid;
    }

    public Participant(int id, String name, int planid) {
        this.id = id;
        this.name = name;
        this.planid = planid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlanid() {
        return planid;
    }

    public void setPlanid(int planid) { this.planid = planid; }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
