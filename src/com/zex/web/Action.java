package com.zex.web;

import java.sql.ResultSet;

/**
 * Created by caspar on 16-5-18.
 */
public class Action {
    private String tableName = "action";

    public ResultSet getAction(){
        String sql="SELECT * FROM "+tableName+",user,goods where user.id=action.uid and goods.id=action.gid order by `time` desc";
        Common common = new Common();
        common.query(sql);
        //common.close();
        return common.getResult();
    }

    public ResultSet insertAction(int gid, int uid, int num, int action){
        String sql="INSERT INTO "+tableName+"(gid,uid,num,action)"+" VALUES("+gid+","+uid+","+num+","+action+")";
        Common common = new Common();
        common.insert(sql);
        //common.close();
        return common.getResult();
    }
}
