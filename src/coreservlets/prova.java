import java.sql.*;
import java.util.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VulnerableApp {

    // CWE-259 / CWE-798: Hardcoded credentials and API key
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password123";
    private static final String API_KEY = "sk-test-abc123xyz";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String inputUser = scanner.nextLine();

        System.out.print("Enter password: ");
        String inputPass = scanner.nextLine();

        if (authenticateUser(inputUser, inputPass)) {
            System.out.println("Login successful!");

            System.out.print("Enter filename to delete: ");
            String filename = scanner.nextLine();

            deleteFile(filename);

            // CWE-327: Insecure cryptographic algorithm (MD5)
            String hash = md5Hash(inputPass);
            System.out.println("MD5 password hash: " + hash);

            // CWE-331: Predictable token generation using insecure PRNG
            String token = generateToken();
            System.out.println("Generated token: " + token);

            // CWE-312: Storing sensitive info in plain text file
            storeInsecurely("user=" + inputUser + ",pass=" + inputPass);

        } else {
            System.out.println("Login failed!");
        }

        scanner.close();
    }

    public static boolean authenticateUser(String username, String password) {
        Connection conn = null;
        Statement stmt = null;

        try {
            // CWE-532: Logging sensitive data
            System.out.println("Authenticating user: " + username + " with password: " + password);

            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();

            // CWE-89: SQL Injection
            String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
            ResultSet rs = stmt.executeQuery(query);

            return rs.next();

        } catch (SQLException e) {
            // CWE-209: Exposing stack trace
            e.printStackTrace();
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }

    // CWE-78: Command injection
    public static void deleteFile(String filename) {
        try {
            String command = "rm " + filename;
            Runtime.getRuntime().exec(command);
            System.out.println("File deleted: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CWE-327: MD5 is insecure
    public static String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            return "Error hashing";
        }
    }

    // CWE-331: Predictable PRNG
    public static String generateToken() {
        Random random = new Random();
        int value = random.nextInt(100000);
        return "token-" + value;
    }

    // CWE-312: Store sensitive data in plaintext file
    public static void storeInsecurely(String data) {
        try (FileWriter fw = new FileWriter("log.txt", true)) {
            fw.write(data + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
