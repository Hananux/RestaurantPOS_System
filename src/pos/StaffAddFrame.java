package pos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StaffAddFrame extends CommonFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField nameField;
    private JComboBox<String> roleComboBox;
    private JCheckBox activeCheckBox;
    private DefaultTableModel tableModel;
    private JPasswordField passwordField;

    public StaffAddFrame(DefaultTableModel model) {
        super("Add Staff", 500, 400);
        this.tableModel = model;
    }

    @Override
    protected void initComponents() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 30, 100, 30);
        contentPane.add(lblName);

        nameField = new JTextField();
        nameField.setBounds(150, 30, 250, 30);
        contentPane.add(nameField);
        nameField.setColumns(10);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(50, 80, 100, 30);
        contentPane.add(lblRole);

        String[] roles = {"Chef", "Manager", "Waiter"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 80, 250, 30);
        contentPane.add(roleComboBox);

        JLabel lblStatus = new JLabel("Active:");
        lblStatus.setBounds(50, 183, 100, 30);
        contentPane.add(lblStatus);

        activeCheckBox = new JCheckBox();
        activeCheckBox.setBounds(150, 183, 30, 30);
        contentPane.add(activeCheckBox);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 129, 100, 30);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 129, 250, 34);
        contentPane.add(passwordField);

        ModernButton btnSubmit = new ModernButton("Add Staff");
        btnSubmit.setBounds(100, 250, 120, 40);
        btnSubmit.addActionListener(e -> submitStaff());
        contentPane.add(btnSubmit);

        ModernButton btnClose = new ModernButton("Close");
        btnClose.setBounds(260, 250, 120, 40);
        btnClose.addActionListener(e -> dispose());
        contentPane.add(btnClose);
    }

    private void submitStaff() {
        String name = nameField.getText();
        String role = (String) roleComboBox.getSelectedItem();
        String password = new String(passwordField.getPassword());
        boolean isActive = activeCheckBox.isSelected();

        if (name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(contentPane, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Staff newStaff = new Staff(name, role, isActive ? "Active" : "Inactive");
        try {
            StaffDatabase staffDatabase = new StaffDatabase();
            staffDatabase.addStaff(newStaff, password); // Call the addStaff method with the password
            if (tableModel != null) {
                tableModel.addRow(new Object[]{name, role, isActive ? "Active" : "Inactive"});
            }
            JOptionPane.showMessageDialog(contentPane, "Staff added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(contentPane, "Error adding staff: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
