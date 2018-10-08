package org.wch.bean;

public class DepositBean {

    private int mid;
    private String moid;
    private double accrual;
    private double current;
    private double lastbalance;
    private String changeTime;

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

    public double getAccrual() {
        return accrual;
    }

    public void setAccrual(double accrual) {
        this.accrual = accrual;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getLastbalance() {
        return lastbalance;
    }

    public void setLastbalance(double lastbalance) {
        this.lastbalance = lastbalance;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }
}
