package org.wch.bean;

public class RechargeBean {

    private int mid;
    private String moid;
    private String numbers;
    private String odd_numbers;
    private double accrual;
    private int payStatus;
    private String trade_time;
    private int trade_type;

    public double getAccrual() {
        return accrual;
    }

    public void setAccrual(double accrual) {
        this.accrual = accrual;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getMoid() {
        return moid;
    }

    public void setMoid(String moid) {
        this.moid = moid;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getOdd_numbers() {
        return odd_numbers;
    }

    public void setOdd_numbers(String odd_numbers) {
        this.odd_numbers = odd_numbers;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public int getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(int trade_type) {
        this.trade_type = trade_type;
    }
}
