package com.fys.qrcode;

import com.fys.bean.User;
import com.fys.util.MysqlUtil;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成二维码
 */
public class CreateQRList {
    //数据库名称
    static String dbName = "fixsys";
    //数据库用户名
    static String username = "root";
    //数据库密码
    static String password = "root";
    //sql语句
    static String sql = "select * from user";

    public static void main(String[] args) {
        //生成的二维码内容
        String content="http://localhost:8080/send?";
        //生成二维码的log路径
        String logUri="F:/F.jpg";
        //输出的路径
        String outFileUri="F:/QRCodes/";

        List<User> users = getUserList();
        for (User u : users) {
            create(content+"id="+ u.getId() + "&floor=" + u.getFloor() + "&room=" + u.getRoom() + "&phone=" + u.getPhone(), logUri, outFileUri+u.getFloor() + "-" + u.getRoom() + "-" +u.getName()+".jpg");
        }
    }

    /**
     * 从数据库获得所有的用户列表,用于生成二维码图片
     */
    public static List<User> getUserList() {
        List<User> users = new ArrayList<>();
        Connection connection = MysqlUtil.getConn(dbName, username, password);
        PreparedStatement preparedStatement = MysqlUtil.getPreparedStatement(connection, sql);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = Integer.parseInt(resultSet.getString(1));
                String name = resultSet.getString(2);
                String floor = resultSet.getString(3);
                String room = resultSet.getString(4);
                String phone = resultSet.getString(5);
                User user = new User(id, name, floor, room, phone);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 生成
     * @param content
     * @param logUri
     * @param outFileUri
     */
    private static void create(String content, String logUri, String outFileUri) {
        int[]  size=new int[]{430,430};
        String format = "jpg";

        try {
            new QRCodeFactory().CreatQrImage(content, format, outFileUri, logUri,size);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}