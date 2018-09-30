package org.wch.bean;

public class UserBean {

    private int id;
    private String openid;
    private String nickname;
    private String headPic;

    private int groupType;
    private int userType;
    private  String registime;
    private double lastMoney;
    private double depositMoney;
    private double rechargeMoney;
    private double agent_wallet;
    private int isTixian;
    private int sex;

    public int getIsTixian() {
        return isTixian;
    }

    public void setIsTixian(int isTixian) {
        this.isTixian = isTixian;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getRegistime() {
        return registime;
    }

    public void setRegistime(String registime) {
        this.registime = registime;
    }

    public double getLastMoney() {
        return lastMoney;
    }

    public void setLastMoney(double lastMoney) {
        this.lastMoney = lastMoney;
    }

    public double getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(double depositMoney) {
        this.depositMoney = depositMoney;
    }

    public double getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(double rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public double getAgent_wallet() {
        return agent_wallet;
    }

    public void setAgent_wallet(double agent_wallet) {
        this.agent_wallet = agent_wallet;
    }
}
