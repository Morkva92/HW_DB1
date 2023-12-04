import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Main {

    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mydb?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "pass";

    static Connection conn;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            createClientsTable();
            createOrdersTable();
            generateRandomData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createClientsTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Clients (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "email VARCHAR(255)" +
                ")";
        try (PreparedStatement preparedStatement = conn.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void createOrdersTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Orders (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "client_id INT," +
                "product_name VARCHAR(255)," +
                "quantity INT," +
                "FOREIGN KEY (client_id) REFERENCES Clients(id)" +
                ")";
        try (PreparedStatement preparedStatement = conn.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void generateRandomData() throws SQLException {
        Random random = new Random();


        String insertClientSQL = "INSERT INTO Clients (name, email) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertClientSQL)) {
            for (int i = 0; i < 10; i++) {
                preparedStatement.setString(1, "Client" + i);
                preparedStatement.setString(2, "client" + i + "@Email.com");
                preparedStatement.executeUpdate();
            }
        }


        String insertOrderSQL = "INSERT INTO Orders (client_id, product_name, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertOrderSQL)) {
            for (int i = 0; i < 10; i++) {
                preparedStatement.setInt(1, random.nextInt(10) + 1);
                preparedStatement.setString(2, "Product" + i);
                preparedStatement.setInt(3, random.nextInt(5) + 1);
                preparedStatement.executeUpdate();
            }
        }
    }

}
