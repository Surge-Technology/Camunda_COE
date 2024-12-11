package com.surge.loanManagement.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DISBURSEMENT_DETAILS")
public class DisbursementDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISBURSEMENT_ID")
    private long disbursementId;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private CustomerDetails customerDetails;  // Assuming you want to link the disbursement to the customer

    @ManyToOne
    @JoinColumn(name = "LOAN_ID", nullable = false)
    private LoanDetails loanDetails;

    @Column(name = "DISBURSEMENT_AMOUNT")
    private String disbursementAmount;

    @Column(name = "DISBURSEMENT_DATE")
    private Date disbursementDate;

    @Column(name = "DISBURSEMENT_METHOD")
    private String disbursementMethod;

    @Column(name = "DISBURSEMENT_STATUS")
    private String disbursementStatus;

    @Column(name = "INTEREST_RATE")
    private int interestRate;

    @Column(name = "REPAYMENT_START_DATE")
    private Date repaymentStartDate;  // Added field for repayment start date

    @Column(name = "LOAN_TERM")
    private int loanTerm;  // Added field for loan term (in months or years, based on your context)

    @Column(name = "REPAYMENT_FREQUENCY")
    private String repaymentFrequency;  // Added field for repayment frequency (e.g., monthly, quarterly)

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    // Getters and Setters
    public long getDisbursementId() {
        return disbursementId;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public LoanDetails getLoanDetails() {
        return loanDetails;
    }

    public String getDisbursementAmount() {
        return disbursementAmount;
    }

    public Date getDisbursementDate() {
        return disbursementDate;
    }

    public String getDisbursementMethod() {
        return disbursementMethod;
    }

    public String getDisbursementStatus() {
        return disbursementStatus;
    }

    public int getInterestRate() {
        return interestRate;
    }

    public Date getRepaymentStartDate() {
        return repaymentStartDate;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public String getRepaymentFrequency() {
        return repaymentFrequency;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setDisbursementId(long disbursementId) {
        this.disbursementId = disbursementId;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public void setLoanDetails(LoanDetails loanDetails) {
        this.loanDetails = loanDetails;
    }

    public void setDisbursementAmount(String disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public void setDisbursementDate(Date disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public void setDisbursementMethod(String disbursementMethod) {
        this.disbursementMethod = disbursementMethod;
    }

    public void setDisbursementStatus(String disbursementStatus) {
        this.disbursementStatus = disbursementStatus;
    }

    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    public void setRepaymentStartDate(Date repaymentStartDate) {
        this.repaymentStartDate = repaymentStartDate;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public void setRepaymentFrequency(String repaymentFrequency) {
        this.repaymentFrequency = repaymentFrequency;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
