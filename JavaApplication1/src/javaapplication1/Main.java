package javaapplication1;

import javaapplication1.cau2.StudentService;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StudentService studentService = new StudentService();

        try {
            // Thêm sinh viên
            studentService.addStudent("Nguyen Van A", "SV001", "CNTT", "Anh", 3.5f);
            System.out.println(">>> Thêm sinh viên thành công");

            // Lấy danh sách sinh viên
            List<Student> students = studentService.getAllStudents();
            for (Student student : students) {
                System.out.println(student);
            }

            // Tìm sinh viên
            Student student = studentService.findStudentByCode("SV001");
            if (student != null) {
                System.out.println("Tìm thấy sinh viên: " + student);
            } else {
                System.out.println("Không tìm thấy sinh viên");
            }

            // Sửa sinh viên
            studentService.updateStudent("SV001", "Nguyen Van B", "KTPM", "Anh, Pháp", 3.8f);
            System.out.println(">>> Sua sinh vien thanh cong");

            // Xóa sinh viên
            studentService.deleteStudent("SV001");
            System.out.println(">>> Xoa sinh vien thanh cong");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
