package com.fys.admin.servlet;

import com.fys.util.MysqlUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 管理员登陆的处理
 */
public class DealServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        String sql = "select * from f_admin where name = ? and password = ?";
        String name = req.getParameter("name");
        String password = req.getParameter("password");

        Connection connection = MysqlUtil.getConn("fixsys", "root", "root");
        PreparedStatement preparedStatement = MysqlUtil.getPreparedStatement(connection, sql);
        try {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            //如果没有在数据库查询到记录
            if (!resultSet.next()) {
                resp.getWriter().write("error");
                return;
            }
            req.getSession().setAttribute("name", name);
            resp.getWriter().write("success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}