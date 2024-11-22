package javaapplication1.cau1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javaapplication1.Student;


public class Server {
private static final String DATA_FILE = "Data.dat";
    private static ArrayList<Student> studentList = new ArrayList<>();

    public static void main(String[] args) {
        loadStudents(); // Load dữ liệu từ file
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            studentList = (ArrayList<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No data found. Starting with an empty list.");
        }
    }

    public static synchronized void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(studentList);
            System.out.println(">>> Save Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

                while (true) {
                    String command = (String) ois.readObject();
                    switch (command) {
                        case "ADD":
                            Student newStudent = (Student) ois.readObject();
                            studentList.add(newStudent);
                            saveStudents();
                            oos.writeObject("Student added successfully.");
                            break;
                        case "EDIT":
                            int editId = (int) ois.readObject();
                            Student updatedStudent = (Student) ois.readObject();
                            boolean found = false;
                            for (int i = 0; i < studentList.size(); i++) {
                                if (studentList.get(i).getId() == editId) {
                                    studentList.set(i, updatedStudent);
                                    saveStudents();
                                    oos.writeObject("Student updated successfully.");
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                oos.writeObject("Student not found.");
                            }
                            break;
                        case "DELETE":
                            int deleteId = (int) ois.readObject();
                            studentList.removeIf(s -> s.getId() == deleteId);
                            saveStudents();
                            oos.writeObject("Student deleted successfully.");
                            break;
                        case "LIST":
                            oos.writeObject(studentList);
                            break;
                        case "SEARCH":
                            int searchId = (int) ois.readObject();
                            Student result = null;
                            for (Student s : studentList) {
                                if (s.getId() == searchId) {
                                    result = s;
                                    break;
                                }
                            }
                            oos.writeObject(result != null ? result : "Student not found.");
                            break;
                        case "EXIT":
                            socket.close();
                            return;
                        default:
                            oos.writeObject("Unknown command.");
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
