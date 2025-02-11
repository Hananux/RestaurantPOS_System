package pos;

import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginFrame() {
        
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/catcafeicon.png")));
        } catch (NullPointerException e) {
            System.err.println("Icon not found: /catcafeicon.png");
        }

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(29, 28, 95, 52);
        contentPane.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(135, 28, 239, 52);
        contentPane.add(usernameField);
        usernameField.setColumns(10);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(29, 107, 95, 52);
        contentPane.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(135, 110, 239, 46);
        contentPane.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(135, 180, 239, 46);
        loginButton.addActionListener(e -> login());
        contentPane.add(loginButton);
    }

    private void login() {
        String name = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()); 

        String role = validateLogin(name, password); 

        if (role != null) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Login", JOptionPane.INFORMATION_MESSAGE);
            dispose();   
            POSFrame posFrame = new POSFrame(role.split(",")); 
            posFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String validateLogin(String name, String password) {
        String query = "SELECT role FROM staff WHERE name = ? AND password = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password); 

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); 
            }
            return null; 
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null; 
        }
    }

}

