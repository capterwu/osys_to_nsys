package org.wch.action;

import org.wch.bean.AgentBean;
import org.wch.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentAct {

    static PreparedStatement ps = null;
    static PreparedStatement ps2 = null;
    static PreparedStatement ps3 = null;
    static PreparedStatement ps4 = null;
    static PreparedStatement ps5 = null;

    static ResultSet rs = null;

    //根据地区名称找地区ID
    public static int getAreaId(String areaName, Connection mysql_conn){
        int area_id = 0;
        try {
            if(areaName.equals("")) {
                String sql = "SELECT id from area where cn_name like '%" + areaName + "%'";
                PreparedStatement arePre = mysql_conn.prepareStatement(sql);
                ResultSet areRs = arePre.executeQuery();
                while (areRs.next()) {
                    area_id = areRs.getInt(1);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return area_id;
    }

    //根据openid找上级代理ID
    public static int getAgentId(String openid,Connection mysql_conn){
        int agent_id = 0;
        try {
            String sql = "SELECT id from agent_relation where OPENID='"+openid+"'";
            PreparedStatement agentPre = mysql_conn.prepareStatement(sql);
            ResultSet agentRs = agentPre.executeQuery();
            while(agentRs.next()) {
                agent_id = agentRs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return agent_id;
    }

    //根据openid找代理商余额
    public static Double getAgentOldMoney(String openid,Connection sqlconn){
        double money = 0.00;
        try {
            String sql = "select  " +
                    "a.p_Account_TiCheng,a.p_Account_TiCheng_Tui,a.p_Account_Shop,a.p_Account_Shop_Tui from Plug_GX_User as a" +
                    " where a.p_OpenID=? ";
            ps = sqlconn.prepareStatement(sql);
            ps.setString(1,openid);
            rs = ps.executeQuery();
            while(rs.next()) {
                double p_Account_TiCheng = rs.getDouble("p_Account_TiCheng");
                double p_Account_TiCheng_Tui = rs.getDouble("p_Account_TiCheng_Tui");
                double p_Account_Shop = rs.getDouble("p_Account_Shop");
                double p_Account_Shop_Tui = rs.getDouble("p_Account_Shop_Tui");
                money = p_Account_Shop+p_Account_Shop_Tui+p_Account_TiCheng+p_Account_TiCheng_Tui;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return  money;
    }

    //更新代理用户分组
    public static void updateUserGroup(String openid,int group_id,Connection mysql_conn){
        try{
            String sql = "update member set m_group=? where openid=?";
            PreparedStatement updateGroup = mysql_conn.prepareStatement(sql);
            updateGroup.setInt(1,group_id);
            updateGroup.setString(2,openid);
            updateGroup.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
    }

    //更新代理商上级代理商ID
    public static void updateAgentRelation(String openid,int agentId,Connection mysql_conn){
        try{
            String sql = "update agent_relation a set a.agent_relation=? where a.openid=? ";
            PreparedStatement updateAgent = mysql_conn.prepareStatement(sql);
            updateAgent.setInt(1,agentId);
            updateAgent.setString(2,openid);
            updateAgent.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
    }


    //查询旧系统代理商数据
    public static List getOdbAgentData(Connection sqlconn){
        List<AgentBean> agentBeans = new ArrayList<>();
        try{
            String sql = "SELECT [AdminID]\n" +
                    "      ,[AdminName]\n" +
                    "      ,[AdminPass]\n" +
                    "      ,[p_Prov]\n" +
                    "      ,[p_City]\n" +
                    "      ,[p_Dist]\n" +
                    "      ,[GroupID]\n" +
                    "      ,[GroupName]\n" +
                    "      ,[PenName]\n" +
                    "      ,[LoginTimes]\n" +
                    "      ,[IsLock]\n" +
                    "      ,[IsAudio]\n" +
                    "      ,[LastLoginDate]\n" +
                    "      ,[LastLoginIP]\n" +
                    "      ,[Admin_OpenID]\n" +
                    "      ,[Superior_ID]\n" +
                    "      ,[Superior_OpenID]\n" +
                    "      ,[Superior_WxName]\n" +
                    "      ,[Superior_WxPic]\n" +
                    "      ,[myPrice]\n" +
                    "      ,[p_Name]\n" +
                    "      ,[p_Tel]\n" +
                    "      ,[AgentPath]\n" +
//                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Agent]\n" +
                    "  FROM [djx_nopublic_20180927].[dbo].[Plug_GX_Agent]\n" +
                    "  WHERE [AdminID] = 7";
            ps = sqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取旧的用户数据，总共有：");
            while(rs.next()){
                AgentBean bean = new AgentBean();
                bean.setId(rs.getInt("AdminID"));
                bean.setA_name(rs.getString("PenName"));
                bean.setA_password(rs.getString("AdminPass"));
                bean.setAccount(rs.getString("AdminName"));
                bean.setTemp_agent_oid(rs.getString("Superior_OpenID"));
                bean.setTemp_area(rs.getString("p_Dist"));
                bean.setContact_name(rs.getString("p_Name"));
                bean.setContact_phone(rs.getString("p_Tel"));
                bean.setIsblock(rs.getInt("IsLock"));
                bean.setLast_login(rs.getString("LastLoginDate"));
                bean.setLogin_nums(rs.getString("LoginTimes"));
                bean.setOid(rs.getString("Admin_OpenID"));
                String group =  rs.getString("GroupName") == null ?"":rs.getString("GroupName");
                if(group.equals("店铺管理")){
                    bean.setGroup(2);
                }else if(group.equals("业务员")){
                    bean.setGroup(3);
                }else if(group.equals("区域代理")){
                    bean.setGroup(4);
                }else if(group.equals("市级代理")){
                    bean.setGroup(5);
                }else if(group.equals("省级代理")){
                    bean.setGroup(6);
                }else if(group.equals("平台")){
                    bean.setGroup(7);
                }
                agentBeans.add(bean);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return agentBeans;
    }

    //导入旧系统代理商数据到新系统
    public  static void pushOldAgentToNew(Connection mssqlconn,Connection mysqlconn) throws SQLException{
        long startTime=System.currentTimeMillis();   //获取开始时间
        List<AgentBean> agentBeanList = getOdbAgentData(mssqlconn);
        try{
            String sql = "insert into agent_relation(ID,PLAT,OPENID,ACCOUNT,A_NAME,A_PASSWORD,AREA,ISBLOCK" +
                    ",REGISTTIME,CONTACT_NAME,CONTACT_PHONE,LOGIN_NUMS,LAST_LOGIN,M_GROUP,OPERATOR,ROLE)" +
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String sql2 = "insert into agent_wallet(agent_id,ACCRUAL,CURRENT,LAST_BALANCE,CHANGE_TIME) values(?,?,?,?,?)";   //添加用户钱包
            System.out.println("此次要处理的代理商数据记录总共有: "+agentBeanList.size()+" 条");
            ps2 = mysqlconn.prepareStatement(sql);
            ps3 = mysqlconn.prepareStatement(sql2);
            mysqlconn.setAutoCommit(false);
            mysqlconn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            for(int i = 0;i<agentBeanList.size();i++){
                AgentBean bean = agentBeanList.get(i);
                ps2.setInt(1,bean.getId());
                ps2.setInt(2,1);
                ps2.setString(3,bean.getOid());
                ps2.setString(4,bean.getAccount());
                ps2.setString(5,bean.getA_name());
                ps2.setString(6,bean.getA_password());
                String  area = bean.getTemp_area() == null ?"":bean.getTemp_area();
                if(area.equals("")){
                    ps2.setInt(7,440114);
                }else{
                    int area_id =  getAreaId(area,mysqlconn);
                    ps2.setInt(7,area_id);
                }

                ps2.setInt(8,bean.getIsblock());
                ps2.setString(9,bean.getRegisttime());
                ps2.setString(10,bean.getContact_name());
                ps2.setString(11,bean.getContact_phone());
                ps2.setString(12,bean.getLogin_nums());
                ps2.setString(13,bean.getLast_login());
                ps2.setInt(14,bean.getGroup());
                ps2.setInt(15,1);
                ps2.setInt(16,1);
                updateUserGroup(bean.getOid(),bean.getGroup(),mysqlconn);   //更新用户表代理商分组
                ps2.addBatch();

                //开始处理代理商agent_wallet数据
                double agent_moeny = getAgentOldMoney(bean.getOid(),mssqlconn);
                //System.out.printf("钱包余额:"+agent_moeny);
                ps3.setInt(1,bean.getId());
                ps3.setDouble(2,agent_moeny);
                ps3.setDouble(3,0.0);
                ps3.setDouble(4,agent_moeny);
                ps3.setString(5,setNowDate());
                ps3.addBatch();

            }
            System.out.println("代理商数据正在准备中......");
            ps2.executeBatch();
            System.out.printf("代理商钱包正在准备中......");
            ps3.executeBatch();
            System.out.println("准备处理数据，请耐心等待，开始处理中......");
            mysqlconn.commit();
            System.out.println("正在更新代理商数据中......");
            mysqlconn.setAutoCommit(true);
            for(int i = 0;i<agentBeanList.size();i++){
                AgentBean bean = agentBeanList.get(i);
                int agent_relation_id = getAgentId(bean.getTemp_agent_oid(),mysqlconn);    //查找用户上级代理商ID
                System.out.println("agent_relation_id:"+agent_relation_id);
                updateAgentRelation(bean.getOid(),agent_relation_id,mysqlconn);  //更新代理商上级代理商ID
            }
            System.out.println("代理商数据更新完毕!......");
            System.out.println("恭喜你，数据处理完毕。");
        }catch (SQLException e){
            mysqlconn.rollback();
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+((endTime-startTime)/1000)/60+"分钟");
    }


    public static String setNowDate() {
        SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = aDate.format(new Date());
        return nowDate;
    }

    public String beginOldAgentToNew(Connection sqlconn,Connection mysqlconn) throws SQLException {
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        pushOldAgentToNew(sqlconn, mysqlconn);
        return "代理商数据已经全部导入。";
    }
}
