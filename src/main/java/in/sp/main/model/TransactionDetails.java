package in.sp.main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table
public class TransactionDetails {

    @Id
    @Column
    String paymentTxtId;

    @Column
    String paymentTxtStatus;

    @Column
    String paymentFromName;

    @Column
    String paymentToName;

    @Column
    BigDecimal paymentTransferAmount;


    public String getPaymentTxtId() {
        return paymentTxtId;
    }

    public void setPaymentTxtId(String paymentTxtId) {
        this.paymentTxtId = paymentTxtId;
    }

    public String getPaymentTxtStatus() {
        return paymentTxtStatus;
    }

    public void setPaymentTxtStatus(String paymentTxtStatus) {
        this.paymentTxtStatus = paymentTxtStatus;
    }

    public String getPaymentFromName() {
        return paymentFromName;
    }

    public void setPaymentFromName(String paymentFromName) {
        this.paymentFromName = paymentFromName;
    }

    public String getPaymentToName() {
        return paymentToName;
    }

    public void setPaymentToName(String paymentToName) {
        this.paymentToName = paymentToName;
    }

    public BigDecimal getPaymentTransferAmount() {
        return paymentTransferAmount;
    }

    public void setPaymentTransferAmount(BigDecimal paymentTransferAmount) {
        this.paymentTransferAmount = paymentTransferAmount.setScale(2, RoundingMode.UP);
    }
}
