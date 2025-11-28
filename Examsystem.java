package task;

import java.io.*;
import java.util.*;

class User {
    String username;
    String password;
    String email;
}

public class Examsystem {
    private static User currentUser = null;
    private static Map<Integer, String> answers = new HashMap<>();
    private static Timer timer;

    // login seetion here we use users.txt to store user data
    public static boolean login(String username, String password) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("users.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals(username) && parts[1].equals(password)) {
                currentUser = new User();
                currentUser.username = username;
                currentUser.password = password;
                currentUser.email = parts[2];
                return true;
            }
        }
        return false;
    }

    // Update email and pass 
    public static void updateProfile(String newEmail, String newPassword) throws IOException {
        File inputFile = new File("users.txt");
        File tempFile = new File("users_temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals(currentUser.username)) {
                writer.write(parts[0] + "," + newPassword + "," + newEmail + "\n");
                currentUser.password = newPassword;
                currentUser.email = newEmail;
            } else {
                writer.write(line + "\n");
            }
        }
        writer.close();
        reader.close();
        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    // MCQ Selection here we use question.txt to show que on console we can modify que on que.txt
    public static void takeExam() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("questions.txt"));
        String line;
        int qNo = 1;
        Scanner sc = new Scanner(System.in);

        while ((line = br.readLine()) != null) {
            System.out.println("Q" + qNo + ": " + line);
            String ans = sc.nextLine();
            answers.put(qNo, ans);
            qNo++;
        }
    }

    // TIMER & AUTO SUBMIT
    public static void startTimer(int seconds) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("\nTime up! Auto-submitting...");
                try {
                    submitAnswers();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logout();
            }
        }, seconds * 1000);
    }

    // SUBMIT ANSWERS ON RESULT.TXT
    public static void submitAnswers() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("results.txt", true));
        bw.write("User: " + currentUser.username + "\n");
        for (Map.Entry<Integer, String> entry : answers.entrySet()) {
            bw.write("Q" + entry.getKey() + ": " + entry.getValue() + "\n");
        }
        bw.write("----\n");
        bw.close();
    }

    // LOGOUT SECTION
    public static void logout() {
        currentUser = null;
        answers.clear();
        if (timer != null) timer.cancel();
        System.out.println("Session closed. Logged out.");
    }

    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username: ");
        String u = sc.nextLine();
        System.out.print("Enter password: ");
        String p = sc.nextLine();

        if (login(u, p)) {
            System.out.println("Login successful!");
            startTimer(60); // 1 minute timer
            takeExam();
            submitAnswers();
            logout();
        } else {
            System.out.println("Invalid credentials!");
        }
    }
}
