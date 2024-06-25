package temporal.model;

public class OrderUpdate {
    enum UpdateType {
        PURCHASE, CANCEL, MODIFY
    }

    String orderID;
    UpdateType updateType;
}
