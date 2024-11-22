/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jnc_v2;

import java.io.Serializable;

/**
 *
 * @author manhnv343
 */
public class Student implements Serializable{
    private int id;
    private String fullName;
    private String studentCode;
    private String dob;
    private String gender;
    private String language;
    private float avgScore;

    public Student(String fullName, String studentCode, String dob, String gender, String language, float avgScore) {
        this.fullName = fullName;
        this.studentCode = studentCode;
        this.dob = dob;
        this.gender = gender;
        this.language = language;
        this.avgScore = avgScore;
    }

    // Getters and setters
    public String getFullName() { return fullName; }
    public String getStudentCode() { return studentCode; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }
    public String getLanguage() { return language; }
    public float getAvgScore() { return avgScore; }
}


//CREATE DATABASE StudentManagement;
//
//USE StudentManagement;
//
//CREATE TABLE Students (
//    id INT AUTO_INCREMENT PRIMARY KEY,
//    full_name VARCHAR(100) NOT NULL,
//    student_code VARCHAR(20) NOT NULL UNIQUE,
//    dob DATE NOT NULL,
//    gender ENUM('Nam', 'Nu') NOT NULL,
//    language ENUM('Anh', 'Phap', 'TQ') NOT NULL,
//    avg_score FLOAT NOT NULL,
//    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
//);
//
//-- Dữ liệu mẫu
//INSERT INTO Students (full_name, student_code, dob, gender, language, avg_score)
//VALUES
//    ('Nguyen Van A', 'DHCN2024-1', '2004-12-12', 'Nam', 'Anh', 8.5),
//    ('Pham Thi B', 'DHCN2024-2', '2005-10-15', 'Nu', 'TQ', 6.5);
