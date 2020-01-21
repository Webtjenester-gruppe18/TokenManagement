package ws18.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Oliver Køppen, s175108
 */

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest implements Serializable {
    String fromAccountNumber;
    String cpr;
    String toAccountNumber;
    BigDecimal amount;
    String description;
    Token token;
}
