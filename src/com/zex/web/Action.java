package com.zex.web;

import com.sun.istack.internal.Nullable;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public int insertActionByHbm(int gid, int uid, int num, int action, int source) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.clear();

        ActionModel actionModel = new ActionModel();
        actionModel.setAction(action);
        actionModel.setGid(gid);
        actionModel.setUid(uid);
        actionModel.setNum(num);
        if (source!=0){
            actionModel.setSource("仓库"+source);
        }
        /*ResultSet resultSet = getAction();
        int id = 0;
        while(resultSet.next()){
            id = resultSet.getInt("id");
            break;
        }
        actionModel.setId(id+1);*/
        session.saveOrUpdate(actionModel);

        tx.commit();
        session.close();
        return actionModel.getId();
    }
}
