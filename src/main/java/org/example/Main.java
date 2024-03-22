package org.example;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Scanner;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataBaseSetting setting = new DataBaseSetting();
        Connection conn = null;
        while (true) {
            log.info("请输入指令：");
            String s = scanner.nextLine();
            if (s.equals("switch")) {
                log.info("切换数据库");
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        log.error("关闭连接失败", e);
                    }
                }
                switchDataBase(setting);
                conn = getConnection(conn, setting);
            } else if (s.equals("init")) {
                conn = getConnection(conn, setting);
            } else if (s.equals("exit")) {
                log.info("退出");
                break;
            } else if (s.contains("select") || s.contains("SELECT")) {
                log.info("执行查询指令:" + s);
                try {
                    printResult(conn, s);
                } catch (Exception e) {
                    log.error("查询失败", e);
                }
            } else {
                log.info("无效指令");
            }
        }
    }

    private static Connection getConnection(Connection conn, DataBaseSetting setting) {
        try {
            conn = OracleJdbcUtil.getConnection(setting.getUrl(), setting.getUsername(), setting.getPassword());
        } catch (Exception e) {
            log.error("获取数据库连接失败", e);
        }
        return conn;
    }

    private static void switchDataBase(DataBaseSetting setting) {
        Scanner scanner = new Scanner(System.in);
        log.info("请输入数据库地址：");
        String url = scanner.nextLine();
        log.info("数据库地址：{}", url);
        log.info("请输入用户名：");
        String username = scanner.nextLine();
        log.info("用户名：{}", username);
        log.info("请输入密码：");
        String password = scanner.nextLine();

        setting.setUsername(username);
        setting.setUrl(url);
        setting.setPassword(password);
    }

    public static void printResult(Connection conn, String query) throws SQLException {
        //3.创建Statement对象(可以执行sql的对象)
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        //4.获取结果集
        ResultSet resultSet = preparedStatement.executeQuery();
        //5.对数据进行处理
        ResultSetMetaData metaData = resultSet.getMetaData();
        //获取列的数量
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 1; i <= columnCount; i++) {
                // 获取列的名称
                String columnName = metaData.getColumnName(i);
                // 获取列的值
                Object columnValue = resultSet.getObject(i);
                jsonObject.put(columnName, columnValue);
            }
            log.info("{}", jsonObject);
        }
    }
}