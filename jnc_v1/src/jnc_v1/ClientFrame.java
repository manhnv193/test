package jnc_v1;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ClientFrame() {
        setTitle("Client Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        add(loginButton);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            socket = new Socket("localhost", 12345);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeObject(username);
            output.writeObject(password);

            String response = (String) input.readObject();
            if ("Login successful".equals(response)) {
                JOptionPane.showMessageDialog(this, "Login successful");
                showMainScreen(username);
            } else {
                JOptionPane.showMessageDialog(this, "Login failed");
                closeConnection();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showMainScreen(String username) {
        JFrame mainFrame = new JFrame("Welcome, " + username);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300, 200);

        JLabel welcomeLabel = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        mainFrame.add(welcomeLabel, BorderLayout.CENTER);

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(e -> disconnect(mainFrame));
        mainFrame.add(disconnectButton, BorderLayout.SOUTH);

        this.dispose(); // Close the login window
        mainFrame.setVisible(true);
    }

    private void disconnect(JFrame mainFrame) {
        try {
            output.writeObject("Disconnect");
            closeConnection();
            mainFrame.dispose();
            JOptionPane.showMessageDialog(this, "Disconnected from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientFrame().setVisible(true));
    }
}
