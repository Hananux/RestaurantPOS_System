package pos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.Border;


public class ModernButton extends JButton {

    private static final Color DEFAULT_COLOR = new Color(246, 237, 230);
    private static final Color HOVER_COLOR = new Color(194, 171, 155); // Light gray for hover
    private static final Color PRESSED_COLOR = new Color(191, 162, 140); // Dark gray for press
    

    public ModernButton(String text) {
        super(text); // Call superclass constructor
        setFont(new Font("Arial", Font.BOLD, 14)); // Set a modern font
        setForeground(new Color(103,71,52)); // Dark text for contrast against white background
        setFocusPainted(false); // Remove the focus border
        setContentAreaFilled(false); // Make content area transparent for the background to show
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover
        setOpaque(false); // Ensure transparency outside the rounded rectangle
        
        // Set rounded border
        setBorder(new RoundedBorder(30)); // Use custom border for rounded edges

        // Add MouseListener for hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_COLOR); // Change background color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(DEFAULT_COLOR); // Reset background color when hover ends
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(PRESSED_COLOR); // Change background color on press
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(HOVER_COLOR); // Reset background color when press ends
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Use Graphics2D for better quality and anti-aliasing
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill the button with a rounded rectangle
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Draw rounded background
        
        // Paint the text on top
        super.paintComponent(g2);
        g2.dispose();
    }

    private static class RoundedBorder implements Border {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 5, 5, 5); // Adjust this as necessary
        }

        @Override
        public boolean isBorderOpaque() {
            return false; // Border is not opaque
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(103,71,52)); // Set border color to dark gray
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Draw rounded border
            g2.dispose();
        }
    }
}