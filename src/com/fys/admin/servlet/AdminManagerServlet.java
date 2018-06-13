package com.fys.admin.servlet;

import com.fys.bean.UInfo;
import com.fys.util.MysqlUtil;
import com.fys.util.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 管理员页面的Servlet,用于控制页面跳转和逻辑处理
 */
public class AdminManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        //获得用户传进来的当前页参数，如果该参数为空，则使用默认的值1
        String strCurrentPage = req.getParameter("currentPage");
        if (null == strCurrentPage || "".equals(strCurrentPage)) {
            currentPage = 1;
            //使用正则表达式确保接收的参数为1个到多个数字
        } else {
            if (Pattern.matches("^\\d+$", strCurrentPage)) {
                currentPage = Integer.parseInt(strCurrentPage);
            }
        }

        int totalCount = getTotalCount();
        //初始化每页显示的条目数
        int pageSize = 10;

        Page page = new Page();
        page.init(totalCount, pageSize);

        //当前页不能大于总页数，如果当前页大于总页数的话，将总页数的值赋给当前页
        if (currentPage > page.getTotalPage()) {
            currentPage = page.getTotalPage();
        }

        if (currentPage <= 0) {
            currentPage = 1;
        }

        //初始化一个List集合，存放用户反馈的信息
        List<UInfo> infos = new ArrayList<>();
        ResultSet resultSet = null;
        //分页查询
        String sql = "select * from uinfo limit "+ ((currentPage-1) * pageSize) + ", "+ pageSize;
        Connection connection = MysqlUtil.getConn("fixsys", "root", "root");
        PreparedStatement preparedStatement = MysqlUtil.getPreparedStatement(connection, sql);
        try {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = Integer.parseInt(resultSet.getString(1));
                String name = resultSet.getString(2);
                String floor = resultSet.getString(3);
                String room = resultSet.getString(4);
                String phone = resultSet.getString(5);
                String info = resultSet.getString(6);
                String createDate = resultSet.getString(7);
                UInfo uInfo = new UInfo(id, name, floor, room, phone, info, createDate);
                infos.add(uInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            MysqlUtil.close(preparedStatement);
            MysqlUtil.close(connection);
        }
        req.getSession().setAttribute("infos", infos);
        req.getSession().setAttribute("page", page);
        req.getSession().setAttribute("currentPage", page.getCurrentPage());
        req.getRequestDispatcher("/WEB-INF/admin/manager.jsp").forward(req, resp);
    }


    private int getTotalCount() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "select count(id) from uinfo";
        int totalCount = 0;

        Connection connection = MysqlUtil.getConn("fixsys", "root", "root");
        try {
            preparedStatement = MysqlUtil.getPreparedStatement(connection, sql);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            totalCount = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            MysqlUtil.close(preparedStatement);
            MysqlUtil.close(connection);
        }
        return totalCount;
    }
}