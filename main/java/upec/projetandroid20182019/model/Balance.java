package upec.projetandroid20182019.model;

public class Balance {
    private String name;
    private double amount;

    public Balance() {}

    public Balance(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public double getAmount() {

        return amount;
    }

    public void setAmount(double amount) {

        this.amount = amount;
    }
}
