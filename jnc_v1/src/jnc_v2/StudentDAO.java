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
public class StudentDAO {
    public static boolean insertStudent(Student student) {
        String query = "INSERT INTO Students (full_name, student_code, dob, gender, language, avg_score) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, student.getFullName());
            stmt.setString(2, student.getStudentCode());
            stmt.setString(3, student.getDob());
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getLanguage());
            stmt.setFloat(6, student.getAvgScore());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
