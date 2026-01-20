package in.sp.main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Date;

@Entity
@Table
@Service
public class TransactionDetails {

    @Id
    @Column
    String paymentTxtId;

    @Column
    String paymentTxtStatus;

    @Column
    String paymentFromName;

    @Column
    int paymentFromID;

    @Column
    int paymentToID;

    @Column
    Instant paymentTxtDate;

    @Column
    String paymentStatusReason;

    public Instant getPaymentTxtDate() {
        return paymentTxtDate;
    }

    public void setPaymentTxtDate(Instant paymentTxtDate) {
        this.paymentTxtDate = paymentTxtDate;
    }

    public String getPaymentStatusReason() {
        return paymentStatusReason;
    }

    public void setPaymentStatusReason(String paymentStatusReason) {
        this.paymentStatusReason = paymentStatusReason;
    }



    public int getPaymentFromID() {
        return paymentFromID;
    }

    public void setPaymentFromID(int paymentFromID) {
        this.paymentFromID = paymentFromID;
    }

    public int getPaymentToID() {
        return paymentToID;
    }

    public void setPaymentToID(int paymentToID) {
        this.paymentToID = paymentToID;
    }



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
