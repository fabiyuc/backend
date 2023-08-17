/* package com.guardias.backend.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        } else {
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String url = "jdbc:sqlserver://192.168.2.37:1433;databaseName=GuardiaDB?useSSL=false";
            String user = "sa";
            String password = "Sa123456.";

            try {

                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {

                e.printStackTrace();
            }
        }
        return connection;
    }
}
 */