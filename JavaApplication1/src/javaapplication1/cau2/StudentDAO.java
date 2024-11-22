package javaapplication1.cau2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javaapplication1.Student;


public class StudentDAO {
    public static void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (full_name, student_code, major, language, gpa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFullName());
            stmt.setString(2, student.getStudentCode());
            stmt.setString(3, student.getMajor());
            stmt.setString(4, student.getLanguage());
            stmt.setFloat(5, student.getGpa());
            stmt.executeUpdate();
        }
    }

    public static void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET full_name = ?, major = ?, language = ?, gpa = ? WHERE student_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFullName());
            stmt.setString(2, student.getMajor());
            stmt.setString(3, student.getLanguage());
            stmt.setFloat(4, student.getGpa());
            stmt.setString(5, student.getStudentCode());
            stmt.executeUpdate();
        }
    }

    public static void deleteStudent(String studentCode) throws SQLException {
        String sql = "DELETE FROM students WHERE student_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentCode);
            stmt.executeUpdate();
        }
    }

    public static Student getStudentByCode(String studentCode) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("student_code"),
                            rs.getString("major"),
                            rs.getString("language"),
                            rs.getFloat("gpa")
                    );
                }
            }
        }
        return null;
    }

    public static List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("student_code"),
                        rs.getString("major"),
                        rs.getString("language"),
                        rs.getFloat("gpa")
                ));
            }
        }
        return students;
    }
}
