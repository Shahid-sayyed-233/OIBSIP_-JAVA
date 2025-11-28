package jdnc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Task1 {
    static final int min = 1000;
    static final int max = 9999;

    public static class User {
        String username;
        String password;
        Scanner sc = new Scanner(System.in);

        public String getusername() {
            System.out.print("Enter Your Username: ");
            username = sc.nextLine();
            return username;
        }

        public String getuserpass() {
            System.out.print("Enter Your Password: ");
            password = sc.nextLine();
            return password;
        }
    }

    public static class pasrecord {
        int pno;
        String pasname;
        String trainno;
        String classtype;
        String jdate;
        String from;
        String to;

        Scanner sc = new Scanner(System.in);

        public int getno() {
            Random random = new Random();
            pno = random.nextInt(max - min + 1) + min;
            return pno;
        }

        public String getpasname() {
            System.out.print("Enter Passenger Name: ");
            pasname = sc.nextLine();
            return pasname;
        }

        public String gettrainno() {
            System.out.print("Enter The Train Number: ");
            trainno = sc.nextLine();
            return trainno;
        }

        public String getclasstype() {
            System.out.print("Enter The Class Type: ");
            classtype = sc.nextLine();
            return classtype;
        }

        public String getjdate() {
            System.out.print("Enter The Date (YYYY-MM-DD format recommended): ");
            jdate = sc.nextLine();
            return jdate;
        }

        public String getfrom() {
            System.out.print("Enter The Starting station: ");
            from = sc.nextLine();
            return from;
        }

        public String getto() {
            System.out.print("Enter The Ending station: ");
            to = sc.nextLine();
            return to;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        User user = new User();
        String enteredUser = user.getusername();
        String enteredPass = user.getuserpass();
        
        final String CORRECT_USER = "admin";
        final String CORRECT_PASS = "pass";

        if (!enteredUser.equals(CORRECT_USER) || !enteredPass.equals(CORRECT_PASS)) {
            System.out.println("\n❌ Login Failed! Invalid Username or Password.");
            sc.close();
            return;
        }
        
        System.out.println("\n✅ Login Successful! Welcome, " + CORRECT_USER + ".");
        
        Connection cn = null;
        Statement st = null;

        try {
            Class.forName("org.postgresql.Driver");
            cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
            System.out.println("connected to database.");
            st = cn.createStatement();

            System.out.println("\n--- Reservation Operations ---");
            System.out.println("Enter Number for Operation or Function");
            System.out.println("1. Insert Record (New Reservation)");
            System.out.println("2. Delete Record (Cancellation)");
            System.out.println("3. Show All Records");
            System.out.print("Your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Exiting.");
                return;
            }
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                pasrecord p1 = new pasrecord();
                int pno = p1.getno();
                String pname = p1.getpasname();
                String trainno = p1.gettrainno();
                String classtype = p1.getclasstype();
                String jdate = p1.getjdate();
                String from = p1.getfrom();
                String to = p1.getto();

                String in = "INSERT INTO reserv (pno, pname, trainno, classtype, jdate, start_station, end_station) VALUES ("
                          + pno + ", '" + pname + "', '" + trainno + "', '" + classtype + "', '" + jdate
                          + "', '" + from + "', '" + to + "')";

                int rowsAffected = st.executeUpdate(in);
                if (rowsAffected > 0) {
                    System.out.println("Value inserted. PNR: " + pno);
                } else {
                    System.out.println("Record not stored.");
                }

            } else if (choice == 2) {
                System.out.print("Enter the PNR (pno) of the record to delete: ");
                if (!sc.hasNextInt()) {
                    System.out.println("Invalid PNR input. Cannot delete.");
                    return;
                }
                int pnrToDelete = sc.nextInt();

                String de = "DELETE FROM reserv WHERE pno = " + pnrToDelete;
                int rowsAffected = st.executeUpdate(de);

                if (rowsAffected > 0) {
                    System.out.println("Record Deleted.");
                } else {
                    System.out.println("Record Not found with PNR " + pnrToDelete);
                }

            } else if (choice == 3) {
                ResultSet rs = st.executeQuery("SELECT * FROM reserv");
                boolean foundRecords = false;

                System.out.println("pno\tpname\t\ttrainno\t\tclasstype\tjdate\t\tfrom\t\tto");
                System.out.println("-----------------------------------------------------------------------------------------");

                while (rs.next()) {
                    foundRecords = true;
                    System.out.printf("%d\t%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n",
                                      rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                      rs.getString(5), rs.getString(6), rs.getString(7));
                }

                if (!foundRecords) {
                    System.out.println("No records found.");
                }
                rs.close();

            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            if (st != null) st.close();
            if (cn != null) cn.close();
            sc.close();
        }
    }
}
