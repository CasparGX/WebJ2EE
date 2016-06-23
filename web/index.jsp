<%--
  Created by IntelliJ IDEA.
  User: caspar
  Date: 16-5-18
  Time: 上午11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ page import="com.mysql.jdbc.Driver" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.zex.web.*" %>
<%@ page import="com.zex.web.Message.send.Sender" %>
<%@ page import="sun.org.mozilla.javascript.json.JsonParser" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.JsonArray" %>
<%@ page import="com.google.gson.JsonObject" %>

<%

    request.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");

    ArrayList<GoodsModel> goodsInfo = new ArrayList<GoodsModel>();
    ArrayList<UserModel> userInfo = new ArrayList<UserModel>();

    int requestCode = 0;
    //requestCode = request.getParameter("action");
    if (request.getParameter("action") != null) {
        requestCode = Integer.parseInt(request.getParameter("action"));
    }
    System.out.println(requestCode);
    int num;
    int actionUserId;
    switch (requestCode) {
        case 1:
            String name = request.getParameter("goodsName");
            num = Integer.parseInt(request.getParameter("goodsNum"));
            actionUserId = Integer.parseInt(request.getParameter("actionUserId"));
            Goods goods = new Goods();
            int insertType = Integer.parseInt(request.getParameter("insertType"));
            if (insertType==1){
                int result = goods.insertGoods(actionUserId, name, num);
                if (result != -1) {
                    System.out.println("insert goods success");
                    response.sendRedirect("/index.jsp");
                }
            } else if (insertType ==0){

                goods.insertGoodsByHbm(actionUserId,name,num);
            }

            break;
        case 2:
            num = Integer.parseInt(request.getParameter("goodsNum"));
            int action = Integer.parseInt(request.getParameter("goodsAction"));
            int gid = Integer.parseInt(request.getParameter("goodsId"));
            actionUserId = Integer.parseInt(request.getParameter("actionUserId"));
            Goods goods1 = new Goods();
            int result1 = goods1.updateGoodsByHbm(gid, actionUserId, num, action);
            if (result1 != -1) {
                System.out.println("update goods success");
                response.sendRedirect("/index.jsp");
            }
            break;
        case 3:
            num = Integer.parseInt(request.getParameter("goodsNum"));
            int gidOut = Integer.parseInt(request.getParameter("goodsId"));
            int warehourseOut = Integer.parseInt(request.getParameter("warehourse"));
            actionUserId = Integer.parseInt(request.getParameter("actionUserId"));

            JsonObject data = new JsonObject();
            data.addProperty("gid",gidOut+"");
            data.addProperty("warehourse", warehourseOut+"");
            data.addProperty("actionUserId", actionUserId+"");
            data.addProperty("num", num+"");
            Gson gson = new Gson();
            String dataString = gson.toJson(data);
            Sender sender = new Sender(dataString);
            break;
    }
%>

<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>

    <script src="./js/jquery-2.1.3.min.js" type="text/javascript"></script>
    <script src="./js/bootstrap.min.js" type="text/javascript"></script>
    <script src="./js/material.min.js" type="text/javascript"></script>
    <script src="./js/ripples.min.js" type="text/javascript"></script>
    <link href="./css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="./css/roboto.min.css" type="text/css" rel="stylesheet"/>
    <link href="./css/material.min.css" type="text/css" rel="stylesheet"/>
    <link href="./css/ripples.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="./css/index.css" type="text/css"/>
    <style>
        .hidden {
            display: none;
        }
    </style>
</head>
<body>
<input class="hiddenInput" type="file" accept="image/jpeg" id="fileJpg" onchange="getImg(files);"/>
<input class="hiddenInput" type="file" accept="application/x-rar" id="fileRar" onchange="getRar(files);"/>

<div class="blank"></div>
<div class="col-md-8 col-md-offset-2">
    <div class="bs-component">
        <div class="panel panel-default">
            <div class="panel-heading">
                <p>
                    <b>企业级Web应用开发</b>
                </p>

                <p>组长：周恩旭（2013960837）</p>

                <p>组员：李好（2013960814） 张炜伦（2013960836） 周鑫（2013960838）</p>

                <p>班级：13计算机科学与技术（兴）</p>
            </div>
            <div class="panel-body form-group-info">

                <div class="center-block" style="margin-bottom: 10px;">
                    <button id="btnGoods" type="button" class="btn btn-info withripple left-block"
                            style="margin: 0px auto 0px auto;">商品管理
                    </button>
                    <button id="btnAction" type="button" class="btn btn-info withripple left-block"
                            style="margin: 0px auto 0px auto;">库存操作记录
                    </button>
                    <button id="btnUser" type="button" class="btn btn-info withripple left-block"
                            style="margin: 0px auto 0px auto;">用户管理
                    </button>
                </div>

                <div id="goods_table">

                    <button id="btnInsertGoods" type="button" class="btn btn-info withripple left-block">添加商品
                    </button>
                    <button id="btnUpdateGoods" type="button" class="btn btn-info withripple left-block">更新商品
                    </button>
                    <button id="btnOutGoods" type="button" class="btn btn-info withripple left-block">商品拨库
                    </button>
                    <table class="table table-striped ">
                        <tr>
                            <th>商品ID</th>
                            <th>商品名称</th>
                            <th>库存</th>
                        </tr>
                        <%
                            Goods goods = new Goods();
                            ResultSet result = goods.getGoods();
                            while (result.next()) {
                                GoodsModel goodsModel = new GoodsModel();
                                goodsModel.setId(result.getInt("id"));
                                goodsModel.setName(result.getString("name"));
                                goodsModel.setStock(result.getInt("stock"));
                                goodsInfo.add(goodsModel);
                                out.print("<tr><td>" + result.getString("id") + "</td>");
                                out.print("<td>" + result.getString("name") + "</td>");
                                out.print("<td>" + result.getString("stock") + "</td><tr/>");
                            }
                        %>
                    </table>
                </div>
                <table id="action_table" class="table table-striped hidden">
                    <tr>
                        <th>ID</th>
                        <th>商品ID</th>
                        <th>商品名称</th>
                        <th>用户ID</th>
                        <th>用户名称</th>
                        <th>操作</th>
                        <th>数量</th>
                        <th>时间</th>
                    </tr>
                    <%
                        Action action = new Action();
                        ResultSet actionResult = action.getAction();
                        while (actionResult.next()) {
                            out.print("<tr><td>" + actionResult.getString("action.id") + "</td>");
                            out.print("<td>" + actionResult.getString("goods.id") + "</td>");
                            out.print("<td>" + actionResult.getString("goods.name") + "</td>");
                            out.print("<td>" + actionResult.getString("user.id") + "</td>");
                            out.print("<td>" + actionResult.getString("user.name") + "</td>");
                            if (actionResult.getInt("action.action") == 1) {
                                out.print("<td style=\"color:green\">" + "入库" + "</td>");
                            } else {
                                out.print("<td style=\"color:red\">" + "出库" + "</td>");

                            }
                            out.print("<td>" + actionResult.getString("action.num") + "</td>");
                            out.print("<td>" + actionResult.getString("action.time") + "</td><tr/>");
                        }
                    %>
                </table>
                <table id="user_table" class="table table-striped hidden">
                    <tr>
                        <th>用户ID</th>
                        <th>用户名称</th>
                    </tr>
                    <%
                        User user = new User();
                        ResultSet userResult = user.getUser();
                        while (userResult.next()) {
                            UserModel userModel = new UserModel();
                            userModel.setId(userResult.getInt("id"));
                            userModel.setName(userResult.getString("name"));
                            userInfo.add(userModel);
                            out.print("<tr><td>" + userResult.getString("id") + "</td>");
                            out.print("<td>" + userResult.getString("name") + "</td><tr/>");
                        }
                    %>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="popwindow" class="hidden" style="position: fixed; z-index: 999; top:0%;left:0%; width: 100%;height: 100%;">
    <div id="dark" style="width: 100%;height: 100%; background: rgba(0, 0, 0, 0.5);"></div>
    <div id="insertGoods" class="col-md-4 col-md-offset-4 " style="position: absolute;top: 25%;">
        <div class="bs-component panel panel-default">
            <div class="panel-heading">
                <p>
                    <b>添加商品</b>
                </p>
            </div>
            <div class="panel-body form-group-info">
                <form method="post" action="index.jsp">
                    <input name="action" type="hidden" value="1"><%--1表示insertGoods 2表示updateGoods 3表示insertUser--%>
                    <input name="goodsAction" type="hidden" value="1">

                    <input name="insertType" type="radio" value="1">JDBC
                    <input name="insertType" type="radio" value="0" style="margin-left: 10px;">Hibernate
                    <br/>
                    操作用户：
                    <select name="actionUserId">
                        <%
                            for (UserModel item:userInfo){
                                out.print("<option value=\"" + item.getId() + "\">" + item.getId() + ":" + item.getName()  + "</option>");
                            }
                        %>
                    </select>
                    <br/>
                    商品名称：<input class="form-control" id="insertGoodsName" name="goodsName"
                                type="text"/>
                    库存数量：<input class="form-control" id="insertGoodsNum" name="goodsNum"
                                type="number"/>
                    <input class="btn btn-info withripple left-block" type="submit" value="确定">
                </form>
            </div>
        </div>
    </div>

    <%--更新商品的弹窗--%>
    <div id="updateGoods" class="col-md-4 col-md-offset-4 " style="position: absolute;top: 25%;">
        <div class="bs-component panel panel-default">

            <div class="panel-heading">
                <p>
                    <b>更新商品</b>
                </p>
            </div>
            <div class="panel-body form-group-info">
                <form method="post" action="index.jsp">
                    <input type="hidden" name="action" value="2"/>
                    操作用户：
                    <select name="actionUserId">
                        <%
                            for (UserModel item:userInfo){
                                out.print("<option value=\"" + item.getId() + "\">" + item.getId() + ":" + item.getName()  + "</option>");
                            }
                        %>
                    </select>
                    <br/>
                    商品名称：
                    <select name="goodsId" class="selectize-control">
                        <%
                            for (GoodsModel item : goodsInfo) {
                                out.print("<option value=\"" + item.getId() + "\">" + item.getId() + ":" + item.getName() + " 库存：" + item.getStock() + "</option>");
                            }
                        %>
                    </select>
                    <br/>
                    <input name="goodsAction" type="radio" value="1">入库
                    <input name="goodsAction" type="radio" value="0" style="margin-left: 10px;">出库
                    <br/>
                    操作数量：<input class="form-control" id="inputGoodsNum" name="goodsNum" type="number"/>
                    <input class="btn btn-info withripple left-block" type="submit" value="确定"/>
                </form>
            </div>
        </div>
    </div>

    <%--拨库商品的弹窗--%>
    <div id="outGoods" class="col-md-4 col-md-offset-4 " style="position: absolute;top: 25%;">
        <div class="bs-component panel panel-default">

            <div class="panel-heading">
                <p>
                    <b>调拨商品</b>
                </p>
            </div>
            <div class="panel-body form-group-info">
                <form method="post" action="index.jsp">
                    <input type="hidden" name="action" value="3"/>
                    操作用户：
                    <select name="actionUserId">
                        <%
                            for (UserModel item:userInfo){
                                out.print("<option value=\"" + item.getId() + "\">" + item.getId() + ":" + item.getName()  + "</option>");
                            }
                        %>
                    </select>
                    <br/>
                    商品名称：
                    <select name="goodsId" class="selectize-control">
                        <%
                            for (GoodsModel item : goodsInfo) {
                                out.print("<option value=\"" + item.getId() + "\">" + item.getId() + ":" + item.getName() + " 库存：" + item.getStock() + "</option>");
                            }
                        %>
                    </select>
                    <br/>
                    选择调拨到的仓库：
                    <select name="warehourse">
                        <option value="2">仓库二</option>
                        <option value="3">仓库三</option>
                    </select>
                    <br/>
                    操作数量：<input class="form-control" name="goodsNum" type="number"/>
                    <input class="btn btn-info withripple left-block" type="submit" value="确定"/>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $("#btnGoods").click(function () {
        $("#goods_table").removeClass("hidden");
        $("#action_table").addClass("hidden");
        $("#user_table").addClass("hidden");
    });
    $("#btnAction").click(function () {
        $("#goods_table").addClass("hidden");
        $("#action_table").removeClass("hidden");
        $("#user_table").addClass("hidden");
    });
    $("#btnUser").click(function () {
        $("#goods_table").addClass("hidden");
        $("#action_table").addClass("hidden");
        $("#user_table").removeClass("hidden");
    });

    $("#btnInsertGoods").click(function () {
        $("#popwindow").removeClass("hidden");
        $("#insertGoods").removeClass("hidden");
        $("#outGoods").addClass("hidden");
        $("#updateGoods").addClass("hidden");
    });
    $("#btnUpdateGoods").click(function () {
        $("#popwindow").removeClass("hidden");
        $("#insertGoods").addClass("hidden");
        $("#outGoods").addClass("hidden");
        $("#updateGoods").removeClass("hidden");
    });
    $("#btnOutGoods").click(function () {
        $("#popwindow").removeClass("hidden");
        $("#outGoods").removeClass("hidden");
        $("#insertGoods").addClass("hidden");
        $("#updateGoods").addClass("hidden");
    });
    $("#dark").click(function () {
        $("#popwindow").addClass("hidden");
    });
</script>
</body>
</html>
