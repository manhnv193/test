package javaapplication1.cau2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import javaapplication1.Student;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ClientFrame extends javax.swing.JFrame {

    private JTextField fullNameField, studentCodeField, gpaField;
    private JComboBox<String> majorComboBox;
    private JRadioButton englishRadio, otherLanguageRadio;
    private JTable resultTable;

    // Action Panel
    private JPanel actionPanel = new JPanel(new GridLayout(1, 6));
    private JButton addButton = new JButton("Add");
    private JButton editButton = new JButton("Edit");
    private JButton deleteButton = new JButton("Delete");
    private JButton searchButton = new JButton("Search");
    private JButton listButton = new JButton("List");
    private JButton exitButton = new JButton("Exit");

    public ClientFrame() {
        setTitle("Student Management System");
        // Set kich thuoc cua so (rong, cao)
        setSize(800, 600);
        // Khi nguoi dung nhan dau x dong cua so => Giai phong toan bo tai nguyen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));

        fullNameField = new JTextField();
        studentCodeField = new JTextField();
        gpaField = new JTextField();
        majorComboBox = new JComboBox<>(new String[]{"CNTT", "KTPM", "KHMT"});
        englishRadio = new JRadioButton("English");
        otherLanguageRadio = new JRadioButton("Russian");
        ButtonGroup languageGroup = new ButtonGroup();
        languageGroup.add(englishRadio);
        languageGroup.add(otherLanguageRadio);

        inputPanel.add(new JLabel("Full Name:"));
        inputPanel.add(fullNameField);
        inputPanel.add(new JLabel("Student Code:"));
        inputPanel.add(studentCodeField);
        inputPanel.add(new JLabel("Major:"));
        inputPanel.add(majorComboBox);
        inputPanel.add(new JLabel("Language:"));
        JPanel languagePanel = new JPanel();
        languagePanel.add(englishRadio);
        languagePanel.add(otherLanguageRadio);
        inputPanel.add(languagePanel);
        inputPanel.add(new JLabel("GPA:"));
        inputPanel.add(gpaField);

        // Result Panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Result"));
        resultTable = new JTable();
        resultPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(searchButton);
        actionPanel.add(listButton);
        actionPanel.add(exitButton);

        // Add actions
        addButton.addActionListener(new AddAction());
        editButton.addActionListener(new EditAction());
        deleteButton.addActionListener(new DeleteAction());
        searchButton.addActionListener(new SearchAction());
        listButton.addActionListener(new ListAction());
        exitButton.addActionListener(new ExitAction());

        // Add Panels
        add(inputPanel, BorderLayout.WEST);
        add(resultPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        refreshTable();

        resultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = resultTable.getSelectedRow(); // Lấy dòng được chọn
                if (selectedRow >= 0) {
                    // Lấy dữ liệu từ JTable và gán vào các trường
                    fullNameField.setText(resultTable.getValueAt(selectedRow, 1).toString());
                    studentCodeField.setText(resultTable.getValueAt(selectedRow, 2).toString());
                    majorComboBox.setSelectedItem(resultTable.getValueAt(selectedRow, 3).toString());
                    String language = resultTable.getValueAt(selectedRow, 4).toString();
                    if (language.equals("English")) {
                        englishRadio.setSelected(true);
                    } else {
                        otherLanguageRadio.setSelected(true);
                    }
                    gpaField.setText(resultTable.getValueAt(selectedRow, 5).toString());
                }
            }
        });

    }

    class AddAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String fullName = fullNameField.getText();
                String studentCode = studentCodeField.getText();
                String major = (String) majorComboBox.getSelectedItem();
                String language = englishRadio.isSelected() ? "English" : "Russian";
                float gpa = Float.parseFloat(gpaField.getText());

                Student student = new Student(0, fullName, studentCode, major, language, gpa);

                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                oos.writeObject("ADD");
                oos.writeObject(student);

                String response = (String) ois.readObject();
                JOptionPane.showMessageDialog(ClientFrame.this, response);

                oos.close();
                ois.close();
                socket.close();

                refreshTable();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ClientFrame.this, "Error adding student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class ListAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                oos.writeObject("LIST");

                List<Student> students = (List<Student>) ois.readObject();

                String[][] data = new String[students.size()][6];
                for (int i = 0; i < students.size(); i++) {
                    Student s = students.get(i);
                    data[i][0] = String.valueOf(s.getId());
                    data[i][1] = s.getFullName();
                    data[i][2] = s.getStudentCode();
                    data[i][3] = s.getMajor();
                    data[i][4] = s.getLanguage();
                    data[i][5] = String.valueOf(s.getGpa());
                }

                String[] columnNames = {"ID", "Full Name", "Student Code", "Major", "Language", "GPA"};
                resultTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

                oos.close();
                ois.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ClientFrame.this, "Error fetching students.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//    class EditAction implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            try {
//                int selectedRow = resultTable.getSelectedRow();
//                if (selectedRow < 0) {
//                    JOptionPane.showMessageDialog(ClientFrame.this, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//
//                // Lấy dữ liệu từ form nhập liệu
//                String fullName = fullNameField.getText();
//                String studentCode = studentCodeField.getText();
//                String major = (String) majorComboBox.getSelectedItem();
//                String language = englishRadio.isSelected() ? "English" : "Russian";
//                float gpa = Float.parseFloat(gpaField.getText());
//
//                // Tạo đối tượng Student
//                Student student = new Student(0, fullName, studentCode, major, language, gpa);
//
//                // Kết nối tới server
//                Socket socket = new Socket("localhost", 12345);
//                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//
//                // Gửi lệnh "EDIT" và dữ liệu sinh viên
//                oos.writeObject("EDIT");
//                oos.writeObject(student);
//
//                // Nhận phản hồi từ server
//                String response = (String) ois.readObject();
//                JOptionPane.showMessageDialog(ClientFrame.this, response);
//
//                // Đóng kết nối
//                oos.close();
//                ois.close();
//                socket.close();
//
//                // Làm mới bảng
//                refreshTable();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(ClientFrame.this, "Error updating student.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
    class EditAction implements ActionListener {

        private boolean isUpdateMode = false; // Để xác định trạng thái "Sửa" hay "Cập nhật"

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isUpdateMode) {
                // Bước 1: Xử lý khi nhấn "Sửa"
                String studentCode = JOptionPane.showInputDialog(ClientFrame.this, "Nhập mã sinh viên cần sửa:");
                if (studentCode == null || studentCode.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(ClientFrame.this, "Mã sinh viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Gửi mã sinh viên tới server để tìm
                    Socket socket = new Socket("localhost", 12345);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    oos.writeObject("SEARCH");
                    oos.writeObject(studentCode);

                    Object response = ois.readObject();
                    if (response instanceof Student) {
                        Student student = (Student) response;

                        // Hiển thị thông tin sinh viên lên các trường nhập liệu
                        fullNameField.setText(student.getFullName());
                        studentCodeField.setText(student.getStudentCode());
                        majorComboBox.setSelectedItem(student.getMajor());
                        if ("English".equals(student.getLanguage())) {
                            englishRadio.setSelected(true);
                        } else {
                            otherLanguageRadio.setSelected(true);
                        }
                        gpaField.setText(String.valueOf(student.getGpa()));

                        // Đổi trạng thái nút "Sửa" thành "Cập nhật"
                        editButton.setText("Update");
                        isUpdateMode = true;
                    } else {
                        JOptionPane.showMessageDialog(ClientFrame.this, "Sinh viên không tồn tại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    }

                    oos.close();
                    ois.close();
                    socket.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ClientFrame.this, "Lỗi khi tìm kiếm sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Bước 2: Xử lý khi nhấn "Cập nhật"
                int confirm = JOptionPane.showConfirmDialog(ClientFrame.this, "Bạn có muốn cập nhật thay đổi?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Lấy thông tin mới từ các trường nhập liệu
                        String fullName = fullNameField.getText();
                        String studentCode = studentCodeField.getText();
                        String major = (String) majorComboBox.getSelectedItem();
                        String language = englishRadio.isSelected() ? "English" : "Russian";
                        float gpa = Float.parseFloat(gpaField.getText());

                        // Tạo đối tượng Student
                        Student student = new Student(0, fullName, studentCode, major, language, gpa);

                        // Gửi thông tin cập nhật tới server
                        Socket socket = new Socket("localhost", 12345);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                        oos.writeObject("EDIT");
                        oos.writeObject(student);

                        String response = (String) ois.readObject();
                        JOptionPane.showMessageDialog(ClientFrame.this, response);

                        oos.close();
                        ois.close();
                        socket.close();

                        // Làm mới bảng
                        refreshTable();

                        // Đổi lại nút "Cập nhật" thành "Sửa"
                        editButton.setText("Edit");
                        isUpdateMode = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(ClientFrame.this, "Lỗi khi cập nhật sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Người dùng chọn "No", không làm gì
                    editButton.setText("Edit");
                    isUpdateMode = false;
                }
            }
        }
    }

    class DeleteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(ClientFrame.this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Lấy mã sinh viên từ dòng được chọn
                String studentCode = resultTable.getValueAt(selectedRow, 2).toString();

                // Hiển thị hộp thoại xác nhận
                int confirm = JOptionPane.showConfirmDialog(ClientFrame.this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.NO_OPTION) {
                    return;
                }

                // Kết nối tới server để xóa sinh viên
                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                oos.writeObject("DELETE");
                oos.writeObject(studentCode);

                // Nhận phản hồi từ server
                String response = (String) ois.readObject();
                JOptionPane.showMessageDialog(ClientFrame.this, response);

                // Đóng kết nối
                oos.close();
                ois.close();
                socket.close();

                // Làm mới bảng
                refreshTable();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ClientFrame.this, "Error deleting student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Nhập mã sinh viên cần tìm
                String studentCode = JOptionPane.showInputDialog(ClientFrame.this, "Enter Student Code to Search:");
                if (studentCode == null || studentCode.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(ClientFrame.this, "Student Code cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kết nối tới server để tìm sinh viên
                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                oos.writeObject("SEARCH");
                oos.writeObject(studentCode);

                Object response = ois.readObject();
                if (response instanceof Student) {
                    Student student = (Student) response;

                    // Cập nhật dữ liệu cho JTable
                    String[][] data = {
                        {String.valueOf(student.getId()), student.getFullName(), student.getStudentCode(), student.getMajor(), student.getLanguage(), String.valueOf(student.getGpa())}
                    };
                    String[] columnNames = {"ID", "Full Name", "Student Code", "Major", "Language", "GPA"};
                    resultTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
                } else {
                    JOptionPane.showMessageDialog(ClientFrame.this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Đóng kết nối
                oos.close();
                ois.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ClientFrame.this, "Error searching for student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class ExitAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(ClientFrame.this, "Are you sure you want to exit the program?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Gửi lệnh EXIT tới server
                    Socket socket = new Socket("localhost", 12345);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject("EXIT");
                    oos.close();
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Thoát chương trình
                System.exit(0);
            }
        }
    }

    private void refreshTable() {
        try {
            // Kết nối tới server
            Socket socket = new Socket("localhost", 12345);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Gửi lệnh "LIST" để lấy danh sách sinh viên
            oos.writeObject("LIST");

            // Nhận danh sách sinh viên từ server
            List<Student> students = (List<Student>) ois.readObject();

            // Chuyển danh sách thành dữ liệu cho JTable
            String[][] data = new String[students.size()][6];
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                data[i][0] = String.valueOf(s.getId());
                data[i][1] = s.getFullName();
                data[i][2] = s.getStudentCode();
                data[i][3] = s.getMajor();
                data[i][4] = s.getLanguage();
                data[i][5] = String.valueOf(s.getGpa());
            }

            // Cột trong bảng
            String[] columnNames = {"ID", "Full Name", "Student Code", "Major", "Language", "GPA"};

            // Cập nhật mô hình của JTable
            resultTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

            // Đóng kết nối
            oos.close();
            ois.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error refreshing table.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
