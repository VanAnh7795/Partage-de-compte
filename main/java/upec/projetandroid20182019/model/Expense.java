package upec.projetandroid20182019.model;

public class Expense {
    private int id;
    private int idPlan;
    private String  nameParticipant;
    private String title;
    private double amount;
    private String date;

    public Expense() { }

    public Expense(int id, int idPlan, String nameParticipant, String title, double amount, String date) {
        this.id = id;
        this.idPlan = idPlan;
        this.nameParticipant = nameParticipant;
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    public Expense(int idPlan, String nameParticipant, String title, double amount, String date) {
        this.idPlan = idPlan;
        this.nameParticipant = nameParticipant;
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public String getNameParticipant() {
        return nameParticipant;
    }

    public void setNameParticipant(String nameParticipant) { this.nameParticipant = nameParticipant; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
