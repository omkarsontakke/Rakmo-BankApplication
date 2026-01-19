package in.sp.main.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransferRequest {


    private int fromAccountId;


    private int toAccountId;

    private BigDecimal amount;

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.UP);
    }
}
