package com.zex.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.ehcache.search.expression.Criteria;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import com.zex.web.HibernateUtil;
import org.omg.CORBA.COMM_FAILURE;

/**
 * Created by caspar on 16-5-18.
 */
public class Goods {
    private String tableName = "goods";

    public static int currentID = 1;

    public ResultSet getGoods() {
        String sql = "SELECT * FROM " + tableName;
        Common common = new Common();
        common.query(sql);
        //common.close();
        return common.getResult();
    }

    public String queryGoodsName(int id) {
        try {
        String sql = "SELECT * FROM " + tableName + " WHERE `id`=" + id;
        Common common = new Common();
        common.query(sql);
        //common.close();
        ResultSet result = common.getResult();

            while(result.next()){
                return result.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int insertGoods(int uid, String name, int stock) {
        String sql = "INSERT INTO " + tableName + "(name,stock)" + " VALUES(\"" + name + "\"," + stock + ")";
        Common common = new Common();
        int result = common.insert(sql);
        if (result != -1) {
            Action actionModel = new Action();
            actionModel.insertAction(result, uid, stock, 1);
        }
        return result;
    }

    public int updateGoods(int gid, int uid, int num, int action) {
        if (action == 0) {
            num = -num;
        }
        String sql = "UPDATE " + tableName + " SET `stock` = `stock`+" + num + " WHERE `id`=" + gid;
        Common common = new Common();
        int result = common.update(sql);
        //common.close();
        if (result != -1) {
            Action actionModel = new Action();
            actionModel.insertAction(gid, uid, num, action);
        }
        return result;
    }

    public int updateGoodsByHbm(int gid, int uid, int num, int action, boolean remote) {
        int stock = 0;
        GoodsModel goodsModel = null;
        if (action == 0) {
            num = -num;
        }
        final Session session = HibernateUtil.getSessionFactory().openSession();
        final Transaction tx = session.beginTransaction();

        List<GoodsModel> list = session.createQuery("select distinct g from GoodsModel g where g.id=:gid")
                .setInteger("gid", gid).list();
        for (GoodsModel goods : list) {
            goodsModel = goods;
            stock = goods.getStock();
        }
        session.clear();
        goodsModel.setId(gid);
        goodsModel.setStock(stock + num);
        session.saveOrUpdate(goodsModel);

        if (goodsModel.getId() != -1) {
            if (remote){
                Timer timer=new Timer();
                TimerTask task=new TimerTask(){
                    public void run(){
                        if (Common.isSuccess()){
                            tx.commit();
                            session.close();
                            System.out.println("计时任务:" + Common.isSuccess());
                        } else {
                            tx.rollback();
                            session.clear();
                            tx.commit();
                            session.close();
                            System.out.println("计时任务:" + Common.isSuccess());
                        }
                    }
                };

                //Goods.success = true;
                timer.schedule(task,10000);
            } else {
                tx.commit();
                session.close();
            }

            Action action1 = new Action();
            int result = action1.insertActionByHbm(gid, uid, num, action, 0);
            if (result == -1) {
                System.out.println("updateGoodsByHbm：更新商品后插入action记录失败");
                tx.rollback();
                return -1;
            } else {
                return goodsModel.getId();
            }
        } else {
            return -1;
        }
    }

    public int updateOutGoodsByHbm(String goodsName, int uid, int num, int action, int source) {
        int stock = 0;
        GoodsModel goodsModel = null;
        if (action == 0) {
            num = -num;
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<GoodsModel> list = session.createQuery("select distinct g from GoodsModel g where g.name=:goodsName")
                .setString("goodsName", goodsName).list();
        for (GoodsModel goods : list) {
            goodsModel = goods;
            stock = goods.getStock();
        }
        session.clear();
        if (goodsModel == null) {
            goodsModel = new GoodsModel();
            goodsModel.setName(goodsName);
            goodsModel.setStock(stock + num);
            session.save(goodsModel);
            tx.commit();
        } else {
            goodsModel.setName(goodsName);
            goodsModel.setStock(stock + num);
            session.update(goodsModel);
            tx.commit();
        }

        session.close();
        if (goodsModel.getId() != -1) {
            Action action1 = new Action();
            int result = action1.insertActionByHbm(goodsModel.getId(), uid, num, action, source);
            if (result == -1) {
                System.out.println("updateGoodsByHbm：更新商品后插入action记录失败");
                tx.rollback();
                return -1;
            } else {
                return goodsModel.getId();
            }
        } else {
            return -1;
        }

    }

    public void insertGoodsByHbm(int uid, String name, int stock) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        GoodsModel goodsModel = new GoodsModel();
        goodsModel.setName(name);
        goodsModel.setStock(stock);
        session.save(goodsModel);

        tx.commit();

        session.close();
        if (goodsModel.getId() != -1) {

            Action actionModel = new Action();
            actionModel.insertAction(goodsModel.getId(), uid, stock, 1);
        }
    }
}
