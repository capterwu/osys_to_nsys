package org.wch.bean;

public class OrderBean {
    private int id;
    private String code;
    private int mid;
    private String ordertime;
    private int equip;
    private int order_status;
    private int pay_status;
    private int pay_type;
    private String pay_code;
    private String power_bi;
    private int bw_shop;
    private int bk_shop;
    private String bk_time;
    private int use_minute;
    private double bill;
    private double unit_price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public int getEquip() {
        return equip;
    }

    public void setEquip(int equip) {
        this.equip = equip;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getPower_bi() {
        return power_bi;
    }

    public void setPower_bi(String power_bi) {
        this.power_bi = power_bi;
    }

    public int getBw_shop() {
        return bw_shop;
    }

    public void setBw_shop(int bw_shop) {
        this.bw_shop = bw_shop;
    }

    public int getBk_shop() {
        return bk_shop;
    }

    public void setBk_shop(int bk_shop) {
        this.bk_shop = bk_shop;
    }

    public String getBk_time() {
        return bk_time;
    }

    public void setBk_time(String bk_time) {
        this.bk_time = bk_time;
    }

    public int getUse_minute() {
        return use_minute;
    }

    public void setUse_minute(int use_minute) {
        this.use_minute = use_minute;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }
}
