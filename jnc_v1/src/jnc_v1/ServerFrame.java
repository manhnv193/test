package jnc_v1;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.table.DefaultTableModel;

public class ServerFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ExecutorService executor;

    public ServerFrame() {
        setTitle("Server Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"PC Name", "IP", "Port", "Status", "Start Time", "End Time"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        executor = Executors.newCachedThreadPool();
        startServer();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            executor.execute(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        executor.execute(new ClientHandler(clientSocket));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream();
                 ObjectInputStream objectInput = new ObjectInputStream(input);
                 ObjectOutputStream objectOutput = new ObjectOutputStream(output)) {

                String username = (String) objectInput.readObject();
                String password = (String) objectInput.readObject();

                boolean isValid = AccountDAO.validateLogin(username, password);
                objectOutput.writeObject(isValid ? "Login successful" : "Login failed");

                if (isValid) {
                    String clientInfo = socket.getInetAddress().getHostName();
                    String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")); // Lấy thời gian thực tế

                    SwingUtilities.invokeLater(() -> tableModel.addRow(new Object[]{
                        clientInfo, 
                        socket.getInetAddress().getHostAddress(), 
                        socket.getPort(), 
                        "Connected", 
                        startTime, 
                        "Running"
                    }));
                    
                    // Wait for disconnect signal
                    String disconnectSignal = (String) objectInput.readObject();
                    if ("Disconnect".equals(disconnectSignal)) {
                        String endTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")); // Lấy thời gian ngắt kết nối

                        SwingUtilities.invokeLater(() -> {
                            int rowCount = tableModel.getRowCount();
                            for (int i = 0; i < rowCount; i++) {
                                if (tableModel.getValueAt(i, 1).equals(socket.getInetAddress().getHostAddress())) {
                                    tableModel.setValueAt("Disconnected", i, 3);
                                    tableModel.setValueAt(endTime, i, 5);
                                    break;
                                }
                            }
                        });
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerFrame().setVisible(true));
    }
}
