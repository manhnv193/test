package javaapplication1.cau2;


import javaapplication1.cau2.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javaapplication1.Student;

public class StudentService {
    
    // Phương thức thêm sinh viên vào cơ sở dữ liệu
    public void addStudent(String fullName, String studentCode, String major, String language, float gpa) throws SQLException {
        String sql = "INSERT INTO students (full_name, student_code, major, language, gpa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, studentCode);
            ps.setString(3, major);
            ps.setString(4, language);
            ps.setFloat(5, gpa);
            ps.executeUpdate();
        }
    }

    // Phương thức sửa thông tin sinh viên dựa vào mã sinh viên
    public void updateStudent(String studentCode, String fullName, String major, String language, float gpa) throws SQLException {
        String sql = "UPDATE students SET full_name = ?, major = ?, language = ?, gpa = ? WHERE student_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, major);
            ps.setString(3, language);
            ps.setFloat(4, gpa);
            ps.setString(5, studentCode);
            ps.executeUpdate();
        }
    }

    // Phương thức xóa sinh viên dựa vào mã sinh viên
    public void deleteStudent(String studentCode) throws SQLException {
        String sql = "DELETE FROM students WHERE student_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentCode);
            ps.executeUpdate();
        }
    }

    // Phương thức tìm sinh viên dựa vào mã sinh viên
    public Student findStudentByCode(String studentCode) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentCode);
            try (ResultSet rs = ps.executeQuery()) {
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
        return null; // Không tìm thấy sinh viên
    }

    // Phương thức lấy toàn bộ danh sách sinh viên
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
