import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // MySQL Database Details
    private static final String URL =
            "jdbc:mysql://localhost:3306/campus_event_volunteer"
                    + "?useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=Asia/Kolkata";

    private static final String USER = "campus_app";

    // Apna MySQL application user password yahan change kar sakte ho
    private static final String PASSWORD = "Campus@12345";

    // Database Connection Method
    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Connection Testing Method
    public static boolean testConnection() {

        try (Connection connection = getConnection()) {

            if (connection != null && connection.isValid(5)) {
                System.out.println("Database Connected Successfully!");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            System.out.println(e.getMessage());
        }

        return false;
    }
}