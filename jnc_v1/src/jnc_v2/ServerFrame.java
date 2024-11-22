/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jnc_v2;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author manhnv343
 */
public class ServerFrame extends JFrame{
    private JTable table;
    private DefaultTableModel tableModel;
    private ExecutorService executor;

    public ServerFrame() {
        setTitle("Server Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"STT", "Họ và Tên", "Mã sinh viên", "Ngày sinh", "Giới tính", "Ngoại ngữ", "Điểm TB", "Thời gian cập nhật"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        executor = Executors.newCachedThreadPool();
        startServer();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            executor.execute(() -> {
                int index = 1;
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        int localIndex = index++;
                        executor.execute(() -> handleClient(clientSocket, localIndex));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket, int index) {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            Student student = (Student) input.readObject();
            boolean isInserted = StudentDAO.insertStudent(student);

            output.writeBoolean(isInserted);
            output.flush();

            if (isInserted) {
                Timestamp updateTime = new Timestamp(System.currentTimeMillis());
                SwingUtilities.invokeLater(() -> tableModel.addRow(new Object[]{
                    index,
                    student.getFullName(),
                    student.getStudentCode(),
                    student.getDob(),
                    student.getGender(),
                    student.getLanguage(),
                    student.getAvgScore(),
                    updateTime.toString()
                }));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerFrame().setVisible(true));
    }
}
