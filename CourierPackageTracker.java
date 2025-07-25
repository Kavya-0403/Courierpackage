import java.io.*;
import java.util.*;

public class CourierPackageTracker {

    static final String FILE_NAME = "couriers.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Courier Package Tracker System =====");
            System.out.println("1. Add New Package");
            System.out.println("2. View All Packages");
            System.out.println("3. Search Package by Tracking ID");
            System.out.println("4. Update Package Status");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addPackage(sc);
                case 2 -> viewAllPackages();
                case 3 -> searchPackage(sc);
                case 4 -> updateStatus(sc);
                case 5 -> System.out.println("Exiting system. Thank you!");
                default -> System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);

        sc.close();
    }

    // 1. Add new courier package
    static void addPackage(Scanner sc) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            System.out.print("Enter Tracking ID: ");
            String id = sc.nextLine();
            System.out.print("Enter Sender Name: ");
            String sender = sc.nextLine();
            System.out.print("Enter Receiver Name: ");
            String receiver = sc.nextLine();
            System.out.print("Enter Status (e.g., Shipped, In Transit, Delivered): ");
            String status = sc.nextLine();
            System.out.print("Enter Delivery Date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            String record = id + "|" + sender + "|" + receiver + "|" + status + "|" + date;
            bw.write(record);
            bw.newLine();
            System.out.println("Package added successfully!");

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // 2. View all courier packages
    static void viewAllPackages() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No package records found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("\nTrackingID\tSender\tReceiver\tStatus\t\tDelivery Date");
            System.out.println("-------------------------------------------------------------------");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                            parts[0], parts[1], parts[2], parts[3], parts[4]);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // 3. Search for a package by tracking ID
    static void searchPackage(Scanner sc) {
        System.out.print("Enter Tracking ID to search: ");
        String targetId = sc.nextLine();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5 && parts[0].equalsIgnoreCase(targetId)) {
                    System.out.println("\nPackage Found:");
                    System.out.println("Tracking ID : " + parts[0]);
                    System.out.println("Sender      : " + parts[1]);
                    System.out.println("Receiver    : " + parts[2]);
                    System.out.println("Status      : " + parts[3]);
                    System.out.println("Delivery Date: " + parts[4]);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Package with Tracking ID " + targetId + " not found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // 4. Update the status of an existing package
    static void updateStatus(Scanner sc) {
        System.out.print("Enter Tracking ID to update: ");
        String targetId = sc.nextLine();
        boolean updated = false;

        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 5 && parts[0].equalsIgnoreCase(targetId)) {
                    System.out.print("Enter New Status: ");
                    String newStatus = sc.nextLine();

                    String updatedLine = parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + newStatus + "|" + parts[4];
                    pw.println(updatedLine);
                    updated = true;
                } else {
                    pw.println(line);
                }
            }

            if (updated) {
                System.out.println("Package status updated successfully.");
            } else {
                System.out.println("Package not found.");
            }

        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
}