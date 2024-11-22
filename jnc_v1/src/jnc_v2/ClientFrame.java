/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jnc_v2;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 *
 * @author manhnv343
 */
public class ClientFrame extends JFrame{
    private JTextField fullNameField, studentCodeField, dobField, avgScoreField;
    private JRadioButton maleButton, femaleButton;
    private JComboBox<String> languageBox;
    private JLabel statusLabel;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ClientFrame() {
        setTitle("Quản Lý Sinh Viên - 2024");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(8, 2));

        add(new JLabel("Họ và Tên:"));
        fullNameField = new JTextField();
        add(fullNameField);

        add(new JLabel("Mã sinh viên:"));
        studentCodeField = new JTextField();
        add(studentCodeField);

        add(new JLabel("Ngày sinh (YYYY-MM-DD):"));
        dobField = new JTextField();
        add(dobField);

        add(new JLabel("Giới tính:"));
        maleButton = new JRadioButton("Nam");
        femaleButton = new JRadioButton("Nữ");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        JPanel genderPanel = new JPanel();
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        add(genderPanel);

        add(new JLabel("Ngoại ngữ:"));
        languageBox = new JComboBox<>(new String[]{"Anh", "Phap", "TQ"});
        add(languageBox);

        add(new JLabel("Điểm TB:"));
        avgScoreField = new JTextField();
        add(avgScoreField);

        statusLabel = new JLabel("Chưa kết nối");
        add(statusLabel);

        JButton connectButton = new JButton("Kết nối");
        connectButton.addActionListener(e -> connectToServer());
        add(connectButton);

        JButton sendButton = new JButton("Gửi");
        sendButton.addActionListener(e -> sendStudentData());
        add(sendButton);

        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> closeConnection());
        add(closeButton);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            statusLabel.setText("Đã kết nối");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Kết nối thất bại");
        }
    }

    private void sendStudentData() {
        try {
            String gender = maleButton.isSelected() ? "Nam" : "Nu";
            String language = (String) languageBox.getSelectedItem();
            float avgScore = Float.parseFloat(avgScoreField.getText());
            Student student = new Student(fullNameField.getText(), studentCodeField.getText(), dobField.getText(), gender, language, avgScore);

            output.writeObject(student);
            output.flush();

            boolean response = input.readBoolean();
            JOptionPane.showMessageDialog(this, response ? "Cập nhật thành công" : "Cập nhật không thành công");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            statusLabel.setText("Chưa kết nối");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientFrame().setVisible(true));
    }
}
