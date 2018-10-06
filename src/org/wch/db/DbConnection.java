package org.wch.db;

import java.sql.*;

public class DbConnection {


//    public static String mysql_url = "jdbc:mysql://47.107.70.136:3306/";
//    public static String mysql_dbName = "ysaas";
//    public static String mysql_user = "root";
//    public static String mysql_pass = "123456";
//    public static String mysql_charset = "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
//   // public static String sql_url = "jdbc:sqlserver://47.99.73.127:1433;databaseName=";
////    public static String sql_url = "jdbc:sqlserver://119.23.208.171:1433;databaseName=";
//   public static String sql_url = "jdbc:sqlserver://47.107.70.136:1433;databaseName=";
//    public static String sql_dbName = "djx_nopublic";
//   // public static String sql_user = "fd";
//   // public static String sql_pass = "jaycnhappy";
////     public static String sql_dbName = "data_test";
////     public static String sql_user = "didatest";
////     public static String sql_pass = "1234";
//    //public static String sql_dbName = "data_test";
//     public static String sql_user = "xiaoyu";
//     public static String sql_pass = "xiaoyutest";


     //【嘀嗒测试服务器】开始_
//     public static String mysql_url = "jdbc:mysql://119.23.208.171:3306/";
//    public static String mysql_dbName = "ysaas";
//    public static String mysql_user = "root";
//    public static String mysql_pass = "123456";
//    public static String sql_url = "jdbc:sqlserver://119.23.208.171:1433;databaseName=";
//    public static String mysql_charset = "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
//    public static String sql_dbName = "djx_nopublic_20180927";
//    public static String sql_user = "user20180927";
//    public static String sql_pass = "user20180927";
    //【嘀嗒测试服务器】结束_

    //【本地服务器】开始_
    public static String mysql_url = "jdbc:mysql://localhost:3306/";
    public static String mysql_dbName = "ysaas";
    public static String mysql_user = "root";
    public static String mysql_pass = "8888";
    public static String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=";
    public static String mysql_charset = "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
    public static String sql_dbName = "djx_nopublic";
    public static String sql_user = "sa";
    public static String sql_pass = "1168";
    //【本地服务器】结束_

    static PreparedStatement ps = null;
    static ResultSet rs = null;
    static Connection conn = null;
    /**
     * 连接数据的三个类
     * 1.connection：负责java程序跟数据库之间的连接
     * 2.statement：执行SQL语句
     * 3.resultSet：负责存储执行了的select语句结果集
     * @throws SQLException
     */
    public static Connection getSqlConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try {
                conn = DriverManager.getConnection(sql_url+sql_dbName,sql_user,sql_pass);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }


    public static Connection getMysqlConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                conn = DriverManager.getConnection(mysql_url+mysql_dbName+mysql_charset,mysql_user,mysql_pass);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeDB() {
        if(rs != null ){
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
                System.out.println("Database connection terminated!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
