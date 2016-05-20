package com.zex.web;

import com.zex.web.Common;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * Created by caspar on 16-5-18.
 */
public class User {
    private String tableName = "user";

    public ResultSet getUser(){
        String sql="SELECT * FROM "+tableName;
        Common common = new Common();
        common.query(sql);
        //common.close();
        return common.getResult();
    }
}
