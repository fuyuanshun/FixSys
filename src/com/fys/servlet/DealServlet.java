package com.fys.servlet;

import com.fys.util.CheckInfo;
import com.fys.util.MailUtil;
import com.fys.util.MysqlUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DealServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String floor = req.getParameter("floor");
        String room = req.getParameter("room");
        String phone = req.getParameter("phone");
        String info = req.getParameter("info");
        //获得当前的时间戳并转化为字符串
        String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        String[] infos = {id, name, floor, room, phone, info};
        if (!CheckInfo.isNull(infos)) {

            boolean isPhone = CheckInfo.checkPhone(phone);
            //判断手机格式是否正确
            if (!isPhone) {
                resp.getWriter().write("请按照要求扫描二维码,并且不要随意修改数据!");
                return;
            }

            String sql = "insert into uinfo (name, floor, room, phone, info, createDate) values (?, ?, ?, ?, ?, ?)";
            Connection connection = MysqlUtil.getConn("fixsys", "root", "root");

            PreparedStatement preparedStatement = MysqlUtil.getPreparedStatement(connection, sql);
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, floor);
                preparedStatement.setString(3, room);
                preparedStatement.setString(4, phone);
                preparedStatement.setString(5, info);
                preparedStatement.setString(6, createDate);
                if (1 == preparedStatement.executeUpdate()) {
                    resp.getWriter().write("提交成功!请等待维修部门与您取得联系!");
                    MailUtil.sendTo("有一个新用户提交了故障信息，请尽快查看处理.<a href='http://localhost:8080/admin/Manager'>去查看</a>", "fuyuanshun521@gmail.com");
                    return;
                }
                resp.getWriter().write("服务器内部好像出了点小问题~请稍后提交试试哦亲~");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            resp.getWriter().write("请填写故障信息!");
        }
    }
}