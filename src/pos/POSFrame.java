package pos;

import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class POSFrame extends CommonFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<String> billListModel;
    private JLabel totalLabel;
    private JLabel tableLabel;
    private String currentTable = "None";
    private Map<String, TablesData> tablesData;
    private String[] roles;

    public POSFrame(String[] roles) {
        super("POS System", 923, 646);
        this.roles = roles;
        setCurrentTable("Table 1");
    }

    @Override
    protected void initComponents() {
        contentPane = new JPanel();
        contentPane.setBackground(new Color(212, 194, 181));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        tablesData = new HashMap<>();
        for (int i = 1; i <= 8; i++) {
            tablesData.put("Table " + i, new TablesData());
        }

        JPanel sidebar = createSidebarPanel();
        contentPane.add(sidebar);

        JPanel menuPanel = createMenuPanel();
        contentPane.add(menuPanel);

        JPanel billPanel = createBillPanel();
        contentPane.add(billPanel);
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setBounds(5, 5, 150, 594);
        sidebar.setBackground(new Color(212, 194, 181));
        sidebar.setLayout(null);

        ModernButton menuButton = new ModernButton("Menu");
        menuButton.setBounds(10, 44, 130, 102);
        ModernButton tablesButton = new ModernButton("Tables");
        tablesButton.setBounds(10, 156, 130, 102);
        ModernButton staffButton = new ModernButton("Staff");
        staffButton.setBounds(10, 268, 130, 102);
        ModernButton logoutButton = new ModernButton("Logout");
        logoutButton.setBounds(10, 492, 130, 102);
        ModernButton ordersButton = new ModernButton("Orders");
        ordersButton.setBounds(10, 380, 130, 102);

        staffButton.addActionListener(e -> openStaffScreen());
        tablesButton.addActionListener(e -> openTablesWindow());
        logoutButton.addActionListener(e -> logout());
        ordersButton.addActionListener(e -> openOrdersScreen());

        JLabel lblIcon = new JLabel("");
        lblIcon.setIcon(new ImageIcon(POSFrame.class.getResource("/images/catcafeicon.png")));
        lblIcon.setBounds(10, 0, 150, 34);
        sidebar.add(lblIcon);

        sidebar.add(menuButton);
        sidebar.add(tablesButton);
        sidebar.add(staffButton);
        sidebar.add(logoutButton);
        sidebar.add(ordersButton);

        return sidebar;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(165, 5, 504, 594);
        menuPanel.setBackground(new Color(212, 194, 181));
        menuPanel.setLayout(new GridLayout(4, 2, 10, 10));

        String[] menuItems = {"Burger", "Pizza", "Pasta", "Salad", "Soda", "Coffee", "Ice Cream", "Cake"};
        double[] prices = {170, 200, 150, 90, 40, 90, 130, 150};

        for (int i = 0; i < menuItems.length; i++) {
            final String itemName = menuItems[i];
            final double price = prices[i];
            ModernButton itemButton = new ModernButton(itemName);
            itemButton.addActionListener(e -> addToBill(itemName, price));
            menuPanel.add(itemButton);
        }

        return menuPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(165, 5, 504, 551);
        tablePanel.setBackground(new Color(212, 194, 181));
        tablePanel.setLayout(new GridLayout(4, 2, 10, 10));

        String[] tables = {"Table 1", "Table 2", "Table 3", "Table 4", "Table 5", "Table 6", "Table 7", "Table 8"};

        for (int i = 0; i < tables.length; i++) {
            final String tableName = tables[i];
            ModernButton tableButton = new ModernButton(tableName);

            
            tableButton.addActionListener(e -> setCurrentTable(tableName));

            tablePanel.add(tableButton);
        }

        return tablePanel;
    }

    private JPanel createBillPanel() {
        JPanel billPanel = new JPanel();
        billPanel.setBounds(679, 5, 220, 594);
        billPanel.setBackground(new Color(240, 240, 240));
        billPanel.setLayout(null);

        billListModel = new DefaultListModel<>();
        JList<String> billList = new JList<>(billListModel);
        JScrollPane billScrollPane = new JScrollPane(billList);
        billScrollPane.setBounds(0, 0, 200, 428);

        tableLabel = new JLabel("Table: " + currentTable);
        tableLabel.setBounds(10, 440, 190, 20);
        totalLabel = new JLabel("Total: ₺0.00");
        totalLabel.setBounds(10, 460, 190, 20);
        ModernButton clearButton = new ModernButton("Clear");
        clearButton.setBounds(10, 490, 180, 30);
        ModernButton checkoutButton = new ModernButton("Checkout");
        checkoutButton.setBounds(10, 530, 180, 30);

        clearButton.addActionListener(e -> clearBill());
        checkoutButton.addActionListener(e -> checkout());

        billPanel.add(billScrollPane);
        billPanel.add(tableLabel);
        billPanel.add(totalLabel);
        billPanel.add(clearButton);
        billPanel.add(checkoutButton);

        ImageIcon originalIcon = new ImageIcon(POSFrame.class.getResource("/images/catCheckout.jpg"));
        Image resizedImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel lblCheckoutCat = new JLabel("");
        lblCheckoutCat.setIcon(resizedIcon);
        lblCheckoutCat.setBounds(160, 558, 60, 36);
        billPanel.add(lblCheckoutCat);

        return billPanel;
    }

    private void addToBill(String item, double price) {
        TablesData tableData = tablesData.get(currentTable);
        tableData.addItem(item, price);
        updateBillDisplay();
    }

    private void clearBill() {
        TablesData tableData = tablesData.get(currentTable);
        tableData.clear();
        updateBillDisplay();
    }

    private void checkout() {
        TablesData tableData = tablesData.get(currentTable);
        ImageIcon icon = new ImageIcon("catcheckout.jpg");

        JOptionPane.showMessageDialog(this, "Total Amount for " + currentTable + ":\n ₺" 
                + String.format("%.2f", tableData.getTotalAmount()), "Checkout", JOptionPane.INFORMATION_MESSAGE, icon);

        try {
            OrdersDatabase dbManager = new OrdersDatabase();
            
            
            int tableNumber = Integer.parseInt(currentTable.replace("Table ", ""));
            
           
            List<String> itemsList = new ArrayList<>();
            for (int i = 0; i < tableData.getItems().size(); i++) {
                itemsList.add(tableData.getItems().getElementAt(i));
            }

            
            String itemsAsSingleString = String.join(", ", itemsList);

            
            dbManager.addOrder(tableNumber, itemsAsSingleString, tableData.getTotalAmount());
            JOptionPane.showMessageDialog(this, "Order saved to database!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        tableData.clear();
        updateBillDisplay();
    }

    private void openStaffScreen() {
        if (hasRole("manager")) {
            StaffFrame staffFrame = new StaffFrame();
            staffFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Access Denied! You don't have permission to access staff panel.", "Access Denied", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openOrdersScreen() {
        OrderFrame orderFrame = new OrderFrame();
        orderFrame.setVisible(true);
    }

    private void openTablesWindow() {
        if (hasRole("manager") || hasRole("waiter")) {
            contentPane.removeAll();
            JPanel sidebar = createSidebarPanel();
            contentPane.add(sidebar);
            JPanel tablePanel = createTablePanel();
            contentPane.add(tablePanel);
            contentPane.revalidate();
            contentPane.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Access Denied! You don't have permission to access the tables panel.", "Access Denied", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean hasRole(String roleToCheck) {
        for (String role : roles) {
            if (role.equalsIgnoreCase(roleToCheck)) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentTable(String tableName) {
        currentTable = tableName;
        updateBillDisplay();
        showMenuPanel();
    }

    private void showMenuPanel() {
        contentPane.removeAll();
        JPanel sidebar = createSidebarPanel();
        contentPane.add(sidebar);
        JPanel menuPanel = createMenuPanel();
        contentPane.add(menuPanel);
        JPanel billPanel = createBillPanel();
        contentPane.add(billPanel);
        contentPane.revalidate();
        contentPane.repaint();
    }

    private void updateBillDisplay() {
        TablesData tableData = tablesData.get(currentTable);
        billListModel.clear();

        for (int i = 0; i < tableData.getItems().size(); i++) {
            billListModel.addElement(tableData.getItems().getElementAt(i));
        }

        totalLabel.setText("Total: ₺" + String.format("%.2f", tableData.getTotalAmount()));
        tableLabel.setText("Table: " + currentTable);
    }

    private void logout() {
    	JOptionPane.showMessageDialog(this, "Logged out successfully!", "Logout", JOptionPane.INFORMATION_MESSAGE);
    	System.exit(0);
    
    }
    
}
    
