package ws18.token;

public class User {

    private String cprNumber;
    private String firstName;
    private String lastName;

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    public String getCprNumber() {
        return cprNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}
