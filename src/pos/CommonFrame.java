package pos;

import javax.swing.JFrame;

public abstract class CommonFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public CommonFrame(String title, int width, int height) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, width, height);
        initComponents();
    }

    protected abstract void initComponents(); // Abstract method to be implemented by subclasses
}
