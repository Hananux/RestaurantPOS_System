package pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/restaurant_pos_system";
            String user = "root"; 
            String password = "Xy001"; 
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }
}
