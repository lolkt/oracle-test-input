package org.example;


import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class OracleJdbcUtil {

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    // 加载注册驱动
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * 获取连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(String url, String user, String password) {
        Connection conn = threadLocal.get();
        try {
            if (conn == null || conn.isClosed()) {
                log.info("OracleJdbcUtil conn is null");
                conn = DriverManager.getConnection(url, user, password);
                log.info("conn get success{}",conn);
                threadLocal.set(conn);
            }
        } catch (SQLException e) {
            log.error("获取连接失败{}", e);
            throw new RuntimeException("获取连接失败！");
        }
        return conn;
    }

    /**
     * 释放资源
     *
     * @param rs
     * @param stmt
     * @param conn
     */
    public static void release(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
                log.info("close rs all");
            }
        } catch (SQLException e) {
            log.error("SQLException", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    log.info("close stmt all");
                }
            } catch (SQLException e) {
                log.error("SQLException", e);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                        threadLocal.remove();
                        log.info("close conn del threadLocal all");
                    }
                } catch (SQLException e) {
                    log.error("SQLException", e);
                }
            }
        }
    }

    /**
     * 释放资源
     *
     * @param stmt
     * @param rs
     */
    public static void release(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) {
                rs.close();
                log.info("close rs");
            }
        } catch (SQLException e) {
            log.error("SQLException", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    log.info("close stmt");
                }
            } catch (SQLException e) {
                log.error("SQLException", e);
            }
        }
    }
}
