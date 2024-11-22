/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jnc_v2;

import java.sql.*;

/**
 *
 * @author manhnv343
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/StudentManagement";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
