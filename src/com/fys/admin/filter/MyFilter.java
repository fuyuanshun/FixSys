package com.fys.admin.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器，防止直接访问后台管理页面
 */
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init !");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String uri = req.getRequestURI();
        //如果是登陆页面，不需要过滤
        if (uri.contains("/admin/login")) {
            filterChain.doFilter(req, resp);
            return;
        }

        String name = (String) req.getSession().getAttribute("name");
        //查看session中是否有管理员数据,如果有数据的话放行
        if (null != name && !name.equals("")) {
            filterChain.doFilter(req, resp);
            //否则跳转到登陆页面
        } else {
            req.getRequestDispatcher("/admin/login").forward(req, resp);
        }

    }

    @Override
    public void destroy() {
        System.out.println("destroy!");
    }
}