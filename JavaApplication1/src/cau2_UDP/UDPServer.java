package cau2_UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javaapplication1.Student;
import javaapplication1.cau2.StudentDAO;
import java.sql.*;
import java.util.List;


public class UDPServer {
    private static final int PORT = 12345;
    private static final int BUFFER_SIZE = 65507; // Max UDP packet size

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("UDP Server is running on port " + PORT + "...");

            while (true) {
                // Nhận dữ liệu từ client
                byte[] receiveBuffer = new byte[BUFFER_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                // Xử lý yêu cầu trong một luồng riêng
                new Thread(() -> {
                    try {
                        // Chuyển đổi dữ liệu nhận được thành đối tượng
                        ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
                        ObjectInputStream ois = new ObjectInputStream(bais);

                        // Đọc lệnh từ client
                        String command = (String) ois.readObject();
                        
                        // Chuẩn bị luồng để gửi phản hồi
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);

                        // Xử lý các lệnh tương tự như TCP
                        switch (command) {
                            case "ADD":
                                Student newStudent = (Student) ois.readObject();
                                try {
                                    StudentDAO.addStudent(newStudent);
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

                            default:
                                oos.writeObject("Unknown command.");
                                break;
                        }

                        // Gửi phản hồi về client
                        byte[] sendBuffer = baos.toByteArray();
                        DatagramPacket sendPacket = new DatagramPacket(
                            sendBuffer, 
                            sendBuffer.length, 
                            receivePacket.getAddress(), 
                            receivePacket.getPort()
                        );
                        serverSocket.send(sendPacket);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
