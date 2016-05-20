package com.zex.web;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import com.zex.web.HibernateUtil;

/**
 * Created by caspar on 16-5-18.
 */
public class Goods {
    private String tableName = "goods";

    public ResultSet getGoods(){
        String sql="SELECT * FROM "+tableName;
        Common common = new Common();
        common.query(sql);
        //common.close();
        return common.getResult();
    }

    public int insertGoods(int uid, String name, int stock){
        String sql="INSERT INTO "+tableName+"(name,stock)"+" VALUES(\""+name+"\","+stock+")";
        Common common = new Common();
        int result = common.insert(sql);
        if (result!=-1) {
            Action actionModel = new Action();
            actionModel.insertAction(result,uid,stock,1);
        }
        return result;
    }
    public int updateGoods(int gid, int uid, int num, int action){
        if (action==0){
            num = -num;
        }
        String sql="UPDATE "+tableName+" SET `stock` = `stock`+"+num+" WHERE `id`="+gid;
        Common common = new Common();
        int result = common.update(sql);
        //common.close();
        if (result!=-1){
            Action actionModel = new Action();
            actionModel.insertAction(gid,uid,num,action);
        }
        return result;
    }

    public void insertGoodsByHbm(int uid, String name, int stock){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        GoodsModel goodsModel = new GoodsModel();
        goodsModel.setName(name);
        goodsModel.setStock(stock);
        session.save(goodsModel);

        tx.commit();
        if (goodsModel.getId()!=-1) {

            Action actionModel = new Action();
            actionModel.insertAction(goodsModel.getId(), uid, stock, 1);
        }
    }
}
