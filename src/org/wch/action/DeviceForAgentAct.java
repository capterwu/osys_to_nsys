package org.wch.action;

import org.wch.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceForAgentAct {

    static PreparedStatement ps = null;
    static PreparedStatement ps2 = null;
    static PreparedStatement ps3 = null;
    static PreparedStatement ps4 = null;
    static PreparedStatement ps5 = null;

    static ResultSet rs = null;
    static ResultSet rs2 = null;

    public static void AddDeviceToShop(Connection conn){
        try{
            String sql = "SELECT qrcode_store.ID FROM qrcode_store ";
            String sql2 = "INSERT into shop_equip(code,REGISTTIME,QRCODE_STORE,OPERATOR) VALUES (?,?,?,?)";
            String sql3 = "INSERT into agent_equip(agent_id,REGISTTIME,QRCODE_STORE,OPERATOR) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps2 = conn.prepareStatement(sql2);
            ps3 = conn.prepareStatement(sql3);
            rs = ps.executeQuery();
            while (rs.next()){
                ps2.setString(1,"8888");
                ps2.setString(2,"2018-09-01 18:18");
                ps2.setInt(3,rs.getInt(1));
                ps2.setInt(4,1);
                ps3.setInt(1,3);
                ps3.setString(2,"2018-09-01 18:18");
                ps3.setInt(3,rs.getInt(1));
                ps3.setInt(4,1);
                ps2.addBatch();
                ps3.addBatch();
            }
            ps2.executeBatch();
            ps3.executeBatch();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
    }

    public String beginDeviceShopAct(Connection conn){
        conn= DbConnection.getMysqlConnection();
        AddDeviceToShop(conn);
        return "执行完毕";
    }
}
