package pos;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class OrderFrame extends CommonFrame {
    private static final long serialVersionUID = 1L;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public OrderFrame() {
        super("Order Management", 600, 400);
    }

    @Override
    protected void initComponents() {
        String[] columnNames = {"Table Number", "Order", "Order Date"};
        tableModel = new DefaultTableModel(null, columnNames);
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);

        ModernButton removeButton = new ModernButton("Complete Order");
        ModernButton logoutButton = new ModernButton("Close");

        removeButton.addActionListener(e -> removeOrder(orderTable));
        logoutButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(removeButton);
        buttonPanel.add(logoutButton);

        ImageIcon originalIcon = new ImageIcon(OrderFrame.class.getResource("/images/caticon.png"));
        Image resizedImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel lblIcon = new JLabel();
        lblIcon.setIcon(resizedIcon);
        lblIcon.setVerticalAlignment(SwingConstants.BOTTOM);
        lblIcon.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonPanel.add(lblIcon);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        loadOrdersIntoTable();
    }

    private void loadOrdersIntoTable() {
        OrdersDatabase ordersDatabase = new OrdersDatabase();

        try {
            List<Order> orderList = ordersDatabase.getAllOrders();
            for (Order order : orderList) {
                tableModel.addRow(new Object[]{order.getTableNumber(), order.getMenuItem(), order.getOrderDate()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeOrder(JTable orderTable) {
        int selectedRow = orderTable.getSelectedRow();

        if (selectedRow >= 0) {
            int tableNumber = (int) tableModel.getValueAt(selectedRow, 0);
            String menuItem = (String) tableModel.getValueAt(selectedRow, 1);

            OrdersDatabase ordersDatabase = new OrdersDatabase();
            try {
                ordersDatabase.deleteOrder(tableNumber, menuItem);
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Order completed successfully!", "Remove Order", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to complete order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to complete.", "Remove Order", JOptionPane.WARNING_MESSAGE);
        }
    }
}
