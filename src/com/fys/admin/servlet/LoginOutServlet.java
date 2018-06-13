package com.fys.admin.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登陆（即清除session）
 */
public class LoginOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (null != req.getSession().getAttribute("name")) {
            req.getSession().removeAttribute("name");
            req.getRequestDispatcher("/WEB-INF/admin/admin.jsp").forward(req, resp);
        }
    }
}