/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jnc_v1;

/**
 *
 * @author manhnv343
 */
public class Account {

    private int id;
    private String username;
    private String password;

    public Account(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}


//CREATE DATABASE LoginSystem;
//
//USE LoginSystem;
//
//CREATE TABLE Account (
//    id INT AUTO_INCREMENT PRIMARY KEY,
//    username VARCHAR(50) NOT NULL UNIQUE,
//    password VARCHAR(50) NOT NULL
//);
//
//-- Dữ liệu mẫu
//INSERT INTO Account (username, password)
//VALUES
//    ('admin', '12345'),
//    ('user1', 'password1'),
//    ('user2', 'password2');
