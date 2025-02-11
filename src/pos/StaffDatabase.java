package pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDatabase {

    public void addStaff(Staff staff, String password) throws SQLException {
        String query = "INSERT INTO staff (name, password, role, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, staff.getName());
            pstmt.setString(2, password);
            pstmt.setString(3, staff.getRole());
            pstmt.setString(4, staff.getStatus());
            pstmt.executeUpdate();
        }
    }

    public void deleteStaff(String name, String role) throws SQLException {
        String query = "DELETE FROM staff WHERE name = ? AND role = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, role);
            pstmt.executeUpdate();
        }
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT name, role, status FROM staff";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String role = rs.getString("role");
                String status = rs.getString("status");
                staffList.add(new Staff(name, role, status));
            }
        }

        return staffList;
    }
}
