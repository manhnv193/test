/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ontap_v1;

import java.awt.GridLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author manhnv343
 */
public class ClientFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientFrame() {
        setTitle("Client Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        // Dong, Cot
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password"));
        passwordField = new JPasswordField();
        add(passwordField);
        

    }

}
