package ws18.model;

import java.util.UUID;

/**
 * @author Oliver Køppen, s175108
 */

public class Token {

    private String value;
    private String customerCpr;
    private boolean hasBeenUsed;

    public Token() {
        this.value = UUID.randomUUID().toString();
        this.hasBeenUsed = false;
    }

    public Token(String customerCpr) {
        this.customerCpr = customerCpr;
        this.value = UUID.randomUUID().toString();
        this.hasBeenUsed = false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isHasBeenUsed() {
        return hasBeenUsed;
    }

    public void setHasBeenUsed(boolean hasBeenUsed) {
        this.hasBeenUsed = hasBeenUsed;
    }

    public String getCustomerCpr() {
        return customerCpr;
    }

    public void setCustomerCpr(String customerCpr) {
        this.customerCpr = customerCpr;
    }
}
