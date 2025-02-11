package pos;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.sql.SQLException;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StaffFrame extends CommonFrame {
    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;

    public StaffFrame() {
        super("Staff Management", 600, 400);
    }

    @Override
    protected void initComponents() {
        String[] columnNames = {"Name", "Role", "Status"};
        tableModel = new DefaultTableModel(null, columnNames);
        JTable staffTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(staffTable);

        ModernButton addButton = new ModernButton("Add Staff");
        ModernButton removeButton = new ModernButton("Remove Staff");
        ModernButton logoutButton = new ModernButton("Close");

        addButton.addActionListener(e -> openAddStaff());
        removeButton.addActionListener(e -> removeStaff(staffTable));
        logoutButton.addActionListener(e -> logout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(logoutButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        loadStaffIntoTable();
    }
    
    // Remaining methods (openAddStaff, removeStaff, loadStaffIntoTable, logout) remain unchanged


    private void openAddStaff() {
        StaffAddFrame addStaffForm = new StaffAddFrame(tableModel);
        addStaffForm.setVisible(true);
    }

    private void removeStaff(JTable staffTable) {
        int selectedRow = staffTable.getSelectedRow();

        if (selectedRow >= 0) {
          
            String name = (String) tableModel.getValueAt(selectedRow, 0); 
            String role = (String) tableModel.getValueAt(selectedRow, 1);    

            
            StaffDatabase staffDatabase = new StaffDatabase();
            try {
                staffDatabase.deleteStaff(name, role); 
                tableModel.removeRow(selectedRow);        
                JOptionPane.showMessageDialog(this, "Staff deleted successfully!", "Remove Staff", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete staff: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a staff to delete.", "Remove Staff", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadStaffIntoTable() {
        StaffDatabase staffDatabase = new StaffDatabase();

        try {
            List<Staff> staffList = staffDatabase.getAllStaff(); 
            for (Staff staff : staffList) {
                tableModel.addRow(new Object[]{staff.getName(), staff.getRole(), staff.getStatus()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        dispose();
        
    }
}



