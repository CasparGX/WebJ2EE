package com.zex.web;

import java.sql.*;

/**
 * Created by caspar on 16-5-18.
 */
public class Common {
    public static String driverName = "com.MySQL,jdbc.Driver";
    public static String username = "root";
    public static String password = "123456";
    public static String dbName = "webj2ee";

    public static String tableUser = "user";
    public static String tableGoods = "goods";
    public static String tableAction = "action";

    private Connection connection;
    private Statement statement;

    public ResultSet getResult() {
        return result;
    }

    private ResultSet result;

    //联结字符串
    String url = "jdbc:MySQL://localhost/" + dbName + "?user=" + username + "&password=" + password+"&useUnicode=true&characterEncoding=UTF-8";

    public Common() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            String setName = "SET NAMES utf8";
            statement.execute(setName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void query(String sql) {
        try {
            result = statement.executeQuery(sql);
            ResultSetMetaData rmeta = result.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insert(String sql) {
        ResultSet result = null;
        int id = -1;
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.executeUpdate();
            result = pstmt.getGeneratedKeys();
            if (result.next()) {
                id = result.getInt(1);
                //System.out.println("数据主键：" + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int update(String sql) {
        ResultSet result = null;
        int id = -1;
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            id = pstmt.executeUpdate();
            result = pstmt.getGeneratedKeys();
            if (result.next()) {
                id = result.getInt(1);
                //System.out.println("数据主键：" + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void close() {
        try {
            this.connection.close();
            this.statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
