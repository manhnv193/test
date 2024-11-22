package javaapplication1.cau2;

import java.sql.*;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/qlsv";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void main(String[] args) {
        try{
            Connection conn = getConnection();
            System.out.println("Ket noi thanh cong!");
        }catch(SQLException e){
            System.out.println("Ket noi that bai!");
        }
    }
}
