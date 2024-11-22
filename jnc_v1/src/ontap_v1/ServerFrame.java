/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ontap_v1;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author manhnv343
 */
public class ServerFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private ExecutorService executor;

    public ServerFrame() {
        setTitle("Server Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Rong, Cao
        setSize(600, 400);

        //Su dung Border Layout => Chia bo cuc Tren, Duoi, Phai, Trai, Trung Tam
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{
            "PC Name",
            "IP",
            "Port",
            "Status",
            "Start Time",
            "End Time"
        }, 0);

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
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                String username = (String) ois.readObject();
                String password = (String) ois.readObject();

                boolean isValid = AccountDAO.validateLogin(username, password);

                oos.writeObject(isValid ? "Login Successfully" : "Login failed");

                if (isValid) {
                    String clientInfo = socket.getInetAddress().getHostName();
                    String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    SwingUtilities.invokeLater(() -> tableModel.addRow(new Object[]{
                        clientInfo,
                        socket.getInetAddress().getHostAddress(),
                        socket.getPort(),
                        "Connected",
                        startTime,
                        "Running"
                    }));

                    // Wait for disconnect signal
                    String disconnectSignal = (String) ois.readObject();
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

            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerFrame().setVisible(true));
    }

}
