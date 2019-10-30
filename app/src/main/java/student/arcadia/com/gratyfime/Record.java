package student.arcadia.com.gratyfime;

public class Record {

    private int id;
    private String business_id;
    private String title;
    private String description;
    private String[] images;
    private String end;
    private int quantity;
    private String terms;
    private double[] location;
    private String address;
    private String Tel;
    private String Website;
    private String Email;
    private String Twitter;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public String getTerms() {
        return terms;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getBusiness_id() {
        return business_id;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String[] getImages() {
        return images;
    }

    public String getEnd() {
        return end;
    }

    public double[] getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return Tel;
    }

    public String getWebsite() {
        return Website;
    }

    public String getEmail() {
        return Email;
    }

    public String getTwitter() {
        return Twitter;
    }

    public int getId() {
        return id;
    }
}
