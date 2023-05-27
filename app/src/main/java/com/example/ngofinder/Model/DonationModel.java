package com.example.ngofinder.Model;

public class DonationModel {
    String amount_don,remark_don;

    public DonationModel(String amount_don, String remark_don) {
        this.amount_don = amount_don;
        this.remark_don = remark_don;
    }

    public String getAmount_don() {
        return amount_don;
    }

    public void setAmount_don(String amount_don) {
        this.amount_don = amount_don;
    }

    public String getRemark_don() {
        return remark_don;
    }

    public void setRemark_don(String remark_don) {
        this.remark_don = remark_don;
    }
}


