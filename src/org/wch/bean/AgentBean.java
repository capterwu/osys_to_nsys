package org.wch.bean;

import java.security.SecureRandom;

public class AgentBean {

    private int id;
    private String oid;
    private String account;
    private String a_name;
    private String a_password;
    private int agent_relation_id;
    private String temp_agent_oid;
    private int area;
    private String temp_area;
    private int isblock;
    private String registtime;
    private String contact_name;
    private String contact_phone;
    private String login_nums;
    private String last_login;
    private int group;
    private String temp_groupName;

    public String getTemp_groupName() {
        return temp_groupName;
    }

    public void setTemp_groupName(String temp_groupName) {
        this.temp_groupName = temp_groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public String getA_password() {
        return a_password;
    }

    public void setA_password(String a_password) {
        this.a_password = a_password;
    }

    public int getAgent_relation_id() {
        return agent_relation_id;
    }

    public void setAgent_relation_id(int agent_relation_id) {
        this.agent_relation_id = agent_relation_id;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getIsblock() {
        return isblock;
    }

    public void setIsblock(int isblock) {
        this.isblock = isblock;
    }

    public String getRegisttime() {
        return registtime;
    }

    public void setRegisttime(String registtime) {
        this.registtime = registtime;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getLogin_nums() {
        return login_nums;
    }

    public void setLogin_nums(String login_nums) {
        this.login_nums = login_nums;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    private int manager;
    private int operator;

    public String getTemp_agent_oid() {
        return temp_agent_oid;
    }

    public void setTemp_agent_oid(String temp_agent_oid) {
        this.temp_agent_oid = temp_agent_oid;
    }

    public String getTemp_area() {
        return temp_area;
    }

    public void setTemp_area(String temp_area) {
        this.temp_area = temp_area;
    }
}
