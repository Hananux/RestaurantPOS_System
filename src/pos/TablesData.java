package pos;

import javax.swing.DefaultListModel;

class TablesData {
	
    private final DefaultListModel<String> items;
    private double totalAmount;

    public TablesData() {
        items = new DefaultListModel<>();
        totalAmount = 0.0;
    }

    public void addItem(String item, double price) {
        items.addElement(item);
        totalAmount += price;
    }

    public void clear() {
        items.clear();
        totalAmount = 0.0;
    }

    public DefaultListModel<String> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}