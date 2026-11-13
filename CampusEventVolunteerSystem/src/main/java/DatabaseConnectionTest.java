import java.sql.Connection;

public class DatabaseConnectionTest {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            System.out.println("================================");
            System.out.println("DATABASE CONNECTION SUCCESSFUL!");
            System.out.println("================================");

            System.out.println("Database: " + connection.getCatalog());
            System.out.println("Status: Connected");

        } catch (Exception e) {

            System.out.println("DATABASE CONNECTION FAILED!");
            System.out.println("Error: " + e.getMessage());

            e.printStackTrace();
        }
    }
}