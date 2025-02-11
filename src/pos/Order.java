package pos;

public class Order {
    private int tableNumber;
    private String menuItem;
    private String orderDate;

    public Order(int tableNumber, String menuItem, String orderDate) {
        this.tableNumber = tableNumber;
        this.menuItem = menuItem;
        this.orderDate = orderDate;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
  