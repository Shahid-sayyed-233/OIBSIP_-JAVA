import java.util.*;

class User {
    private String userId;
    private String pin;
    private double balance;
    private List<String> history;

    public User(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.history = new ArrayList<>();
    }

    public String getUserId() { return userId; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public List<String> getHistory() { return history; }
}

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        User user1 = new User("user1", "1234", 1000);
        User user2 = new User("user2", "5678", 2000);

        
        System.out.print("Enter User ID: ");
        String userId = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        User currentUser = null;
        if (userId.equals(user1.getUserId()) && pin.equals(user1.getPin())) {
            currentUser = user1;
        } else if (userId.equals(user2.getUserId()) && pin.equals(user2.getPin())) {
            currentUser = user2;
        }

        if (currentUser == null) {
            System.out.println(" Invalid login.");
            sc.close();
            return;
        }

        System.out.println(" Login successful!");

        int choice;
        do {
            System.out.println("\nATM Menu:");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println(" Transaction History:");
                    for (String record : currentUser.getHistory()) {
                        System.out.println(record);
                    }
                }
                case 2 -> {
                    System.out.print("Enter amount to withdraw: ");
                    double amt = sc.nextDouble();
                    if (amt <= currentUser.getBalance()) {
                        currentUser.setBalance(currentUser.getBalance() - amt);
                        currentUser.getHistory().add("Withdraw: " + amt);
                        System.out.println(" Withdrawal successful.");
                    } else {
                        System.out.println(" Insufficient balance.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter amount to deposit: ");
                    double amt = sc.nextDouble();
                    currentUser.setBalance(currentUser.getBalance() + amt);
                    currentUser.getHistory().add("Deposit: " + amt);
                    System.out.println(" Deposit successful.");
                }
                case 4 -> {
                    sc.nextLine(); // consume newline
                    System.out.print("Enter recipient User ID: ");
                    String toUserId = sc.nextLine();
                    System.out.print("Enter amount to transfer: ");
                    double amt = sc.nextDouble();

                    User recipient = null;
                    if (toUserId.equals(user1.getUserId())) recipient = user1;
                    else if (toUserId.equals(user2.getUserId())) recipient = user2;

                    if (recipient != null && amt <= currentUser.getBalance()) {
                        currentUser.setBalance(currentUser.getBalance() - amt);
                        recipient.setBalance(recipient.getBalance() + amt);
                        currentUser.getHistory().add("Transfer to " + recipient.getUserId() + ": " + amt);
                        recipient.getHistory().add("Transfer from " + currentUser.getUserId() + ": " + amt);
                        System.out.println(" Transfer successful.");
                    } else {
                        System.out.println(" Transfer failed.");
                    }
                }
                case 5 -> System.out.println(" Thank you for using the ATM!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);

        sc.close();
    }
}