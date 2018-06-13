package com.fys.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("gbk");
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String floor = req.getParameter("floor");
        String room = req.getParameter("room");
        String phone = req.getParameter("phone");


        if (null != id && !id.equals("")) {
            req.getSession().setAttribute("id", id);
            req.getSession().setAttribute("name", name);
            req.getSession().setAttribute("floor", floor);
            req.getSession().setAttribute("room", room);
            req.getSession().setAttribute("phone", phone);
            req.getRequestDispatcher("/WEB-INF/jsp/infoForm.jsp").forward(req, resp);
        } else {
            resp.getWriter().write("id必须为数字!");
        }
    }
}