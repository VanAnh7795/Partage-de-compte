package upec.projetandroid20182019.model;

public class Plan {
    private int id;
    private String title;
    private String description;
    private String currency;

    public Plan() { }

    public Plan(int id, String title, String description, String currency) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.currency = currency;
    }

    public Plan(String title, String description, String currency) {
        this.title = title;
        this.description = description;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
