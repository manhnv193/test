package javaapplication1;

import java.io.Serializable;


public class Student implements Serializable {
    
    private int id;
    private String fullName;
    private String studentCode;
    private String major;
    private String language;
    private float gpa;

    public Student() {
    }

    public Student(int id, String fullName, String studentCode, String major, String language, float gpa) {
        this.id = id;
        this.fullName = fullName;
        this.studentCode = studentCode;
        this.major = major;
        this.language = language;
        this.gpa = gpa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", fullName=" + fullName + ", studentCode=" + studentCode + ", major=" + major + ", language=" + language + ", gpa=" + gpa + '}';
    }
    
    
    
    
}
