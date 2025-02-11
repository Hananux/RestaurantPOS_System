package pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdersDatabase {

	public void addOrder(int tableNumber, String combinedItems, double totalAmount) throws SQLException {
	    
	    String query = "INSERT INTO orders (table_number, menu_item, total_amount, order_date) VALUES (?, ?, ?, NOW())";

	    try (Connection conn = DatabaseConnection.connect();
	         PreparedStatement pstmt = conn.prepareStatement(query)) {

	        pstmt.setInt(1, tableNumber); 
	        pstmt.setString(2, combinedItems); 
	        pstmt.setDouble(3, totalAmount); 
	        pstmt.executeUpdate(); 

	        System.out.println("Order added successfully.");
	    } catch (SQLException e) {
	        System.err.println("Error adding order: " + e.getMessage());
	        throw e;
	    }
	}
	
	public List<Order> getAllOrders() throws SQLException {
	    List<Order> orders = new ArrayList<>();
	    String query = "SELECT table_number, menu_item, order_date FROM orders"; 

	    try (Connection conn = DatabaseConnection.connect();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        while (rs.next()) {
	            int tableNumber = rs.getInt("table_number");
	            String menuItem = rs.getString("menu_item");
	            String orderDate = rs.getString("order_date");

	            orders.add(new Order(tableNumber, menuItem, orderDate)); 
	        }
	    }

	    return orders;
	}
	
    public void deleteOrder(int tableNumber, String menuItem) throws SQLException {
        String query = "DELETE FROM orders WHERE table_number = ? AND menu_item = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, tableNumber);
            pstmt.setString(2, menuItem);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Order deleted successfully.");
            } else {
                System.out.println("No matching order found.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
            throw e;
        }
    }
}