package javaapplication1.cau2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javaapplication1.Student;
import java.sql.*;
import java.util.List;


public class Server {
    public static void main(String[] args) {
        // Tạo một ServerSocket nghe tại cổng 12345 và tự động đóng khi kết thúc (try-with-resources)
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running on port 12345...");
            while (true) {
                // Chấp nhận kết nối từ client và trả về một đối tượng Socket
                Socket clientSocket = serverSocket.accept();
                
                // Tạo và khởi chạy một luồng mới (Thread) để xử lý yêu cầu từ client
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lớp con ClientHandler được sử dụng để xử lý các yêu cầu từ client, kế thừa từ Thread để chạy song song
    static class ClientHandler extends Thread {
        // Khai báo socket để liên lạc với client
        private Socket socket;

        // Hàm khởi tạo nhận socket từ client và gán cho biến socket của lớp
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // Lấy luồng nhập từ socket, cho phép server đọc dữ liệu được gửi từ client
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // Lấy luồng xuất từ socket, cho phép server gửi dữ liệu đến client.
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

                while (true) {
                    // Đọc lệnh từ client
                    System.out.println("Client - " + socket.getLocalAddress() + " - " + socket.getPort());
                    String command = (String) ois.readObject();
                    switch (command) {
                        case "ADD":
                            Student newStudent = (Student) ois.readObject();
                            try {
                                StudentDAO.addStudent(newStudent);
                                
                                // Gửi phản hồi về client
                                oos.writeObject("Student added successfully.");
                            } catch (SQLException e) {
                                oos.writeObject("Error adding student: " + e.getMessage());
                            }
                            break;

                        case "EDIT":
                            Student updatedStudent = (Student) ois.readObject();
                            try {
                                StudentDAO.updateStudent(updatedStudent);
                                oos.writeObject("Student updated successfully.");
                            } catch (SQLException e) {
                                oos.writeObject("Error updating student: " + e.getMessage());
                            }
                            break;

                        case "DELETE":
                            String studentCode = (String) ois.readObject();
                            try {
                                StudentDAO.deleteStudent(studentCode);
                                oos.writeObject("Student deleted successfully.");
                            } catch (SQLException e) {
                                oos.writeObject("Error deleting student: " + e.getMessage());
                            }
                            break;

                        case "SEARCH":
                            String searchCode = (String) ois.readObject();
                            try {
                                Student student = StudentDAO.getStudentByCode(searchCode);
                                oos.writeObject(student != null ? student : "Student not found.");
                            } catch (SQLException e) {
                                oos.writeObject("Error searching for student: " + e.getMessage());
                            }
                            break;

                        case "LIST":
                            try {
                                List<Student> students = StudentDAO.getAllStudents();
                                oos.writeObject(students);
                            } catch (SQLException e) {
                                oos.writeObject("Error fetching students: " + e.getMessage());
                            }
                            break;

                        case "EXIT":
                            socket.close();
                            return;

                        default:
                            oos.writeObject("Unknown command.");
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
