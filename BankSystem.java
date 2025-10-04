import java.util.*;
import java.io.*;

class BankSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== BANK ACCOUNT MANAGEMENT SYSTEM ===");
            System.out.println("1. Create New Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View All Accounts");
            System.out.println("5. Search Account by Number");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    BankOperations.createAccount();
                    break;
                case 2:
                    BankOperations.deposit();
                    break;
                case 3:
                    BankOperations.withdraw();
                    break;
                case 4:
                    BankOperations.viewAccounts();
                    break;
                case 5:
                    BankOperations.searchAccount();
                    break;
                case 6:
                    System.out.println("Exiting... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);
    }
}

class BankOperations {
    static final String FILE_NAME = "accounts.txt";

    static void createAccount() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Account Number: ");
            String accNo = sc.nextLine();
            System.out.print("Enter Account Holder Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Initial Balance: ");
            double balance = sc.nextDouble();
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true));
            bw.write(accNo + "," + name + "," + balance);
            bw.newLine();
            bw.close();
            System.out.println("✅ Account Created Successfully!");
        } catch (IOException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    static void deposit() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter Amount to Deposit: ");
        double amount = sc.nextDouble();
        updateBalance(accNo, amount, true);
    }

    static void withdraw() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter Amount to Withdraw: ");
        double amount = sc.nextDouble();
        updateBalance(accNo, amount, false);
    }

    private static void updateBalance(String accNo, double amount, boolean isDeposit) {
        File f = new File(FILE_NAME);
        File temp = new File("temp.txt");
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(f));
                BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accNo)) {
                    found = true;
                    double balance = Double.parseDouble(data[2]);
                    if (isDeposit) {
                        balance += amount;
                        System.out.println("✅ Deposited Successfully! New Balance: " + balance);
                    } else {
                        if (balance >= amount) {
                            balance -= amount;
                            System.out.println("✅ Withdrawn Successfully! New Balance: " + balance);
                        } else {
                            System.out.println("❌ Insufficient Balance!");
                        }
                    }
                    bw.write(data[0] + "," + data[1] + "," + balance);
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }

        if (found) {
            f.delete();
            temp.renameTo(f);
        } else {
            temp.delete();
            System.out.println("❌ Account Not Found!");
        }
    }

    static void viewAccounts() {
        try {
            File f = new File(FILE_NAME);
            if (!f.exists()) {
                System.out.println("No accounts found.");
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            System.out.println("\n--- Account Details ---");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                System.out.println("Acc No: " + data[0] + " | Name: " + data[1] + " | Balance: " + data[2]);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error viewing accounts: " + e.getMessage());
        }
    }

    static void searchAccount() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accNo)) {
                    System.out.println("✅ Account Found!");
                    System.out.println("Acc No: " + data[0] + " | Name: " + data[1] + " | Balance: " + data[2]);
                    found = true;
                    break;
                }
            }
            br.close();
            if (!found) {
                System.out.println("❌ Account Not Found!");
            }
        } catch (IOException e) {
            System.out.println("Error searching account: " + e.getMessage());
        }
    }
}
