package javaapplication1.cau1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javaapplication1.Student;

public class Client {
        public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\nCHUONG TRINH QLSV-2024");
                System.out.println("1. Them sinh vien");
                System.out.println("2. Sua");
                System.out.println("3. Xoa");
                System.out.println("4. Tim sinh vien theo ma sinh vien");
                System.out.println("5. Thoat");
                System.out.print("Chon chuc nang: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        oos.writeObject("ADD");
                        System.out.print("Nhap id: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nhap ho ten: ");
                        String fullName = scanner.nextLine();
                        System.out.print("Nhap ma sinh vien: ");
                        String studentCode = scanner.nextLine();
                        System.out.print("Nhap nganh: ");
                        String major = scanner.nextLine();
                        System.out.print("Nhap ngoai ngu: ");
                        String language = scanner.nextLine();
                        System.out.print("Nhap GPA: ");
                        float gpa = scanner.nextFloat();
                        Student newStudent = new Student(id, fullName, studentCode, major, language, gpa);
                        oos.writeObject(newStudent);
                        System.out.println((String) ois.readObject());
                        break;
                    case 2:
                        oos.writeObject("EDIT");
                        System.out.print("Nhap ma sinh vien can sua: ");
                        int editId = scanner.nextInt();
                        scanner.nextLine();
                        oos.writeObject(editId);
                        System.out.print("Nhap ho ten moi: ");
                        String editName = scanner.nextLine();
                        System.out.print("Nhap ma sinh vien moi: ");
                        String editCode = scanner.nextLine();
                        System.out.print("Nhap nganh moi: ");
                        String editMajor = scanner.nextLine();
                        System.out.print("Nhap ngoai ngu moi: ");
                        String editLanguage = scanner.nextLine();
                        System.out.print("Nhap GPA moi: ");
                        float editGpa = scanner.nextFloat();
                        Student updatedStudent = new Student(editId, editName, editCode, editMajor, editLanguage, editGpa);
                        oos.writeObject(updatedStudent);
                        System.out.println((String) ois.readObject());
                        break;
                    case 3:
                        oos.writeObject("DELETE");
                        System.out.print("Nhap ma sinh vien can xoa: ");
                        int deleteId = scanner.nextInt();
                        oos.writeObject(deleteId);
                        System.out.println((String) ois.readObject());
                        break;
                    case 4:
                        oos.writeObject("SEARCH");
                        System.out.print("Nhap ma sinh vien: ");
                        int searchId = scanner.nextInt();
                        oos.writeObject(searchId);
                        Object response = ois.readObject();
                        System.out.println(response instanceof Student ? response : "Student not found.");
                        break;
                    case 5:
                        oos.writeObject("EXIT");
                        return;
                    default:
                        System.out.println("Lua chon khong hop le!");
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
