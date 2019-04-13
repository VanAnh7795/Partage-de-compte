package upec.projetandroid20182019.model;

public class Paid {
    private String namePay;
    private String nameReceive;
    private double amount;

    public Paid () {}

    public Paid(String namePay, String nameReceive,  double amount) {
        this.namePay = namePay;
        this.nameReceive = nameReceive;
        this.amount = amount;
    }

    public String getNamePay() {
        return namePay;
    }

    public void setNamePay(String namePay) { this.namePay = namePay; }

    public String getNameReceive() {
        return nameReceive;
    }

    public void setNameReceive(String nameReceive) { this.nameReceive = nameReceive; }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
