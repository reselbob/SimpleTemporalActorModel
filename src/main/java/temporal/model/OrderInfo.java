package temporal.model;

import java.util.UUID;

public class OrderUpdate {
    public enum UpdateType {
        PURCHASE, CANCEL, MODIFY
    }

    UUID orderID;
    UpdateType updateType;

    String message;

    public OrderUpdate(UUID orderID, UpdateType updateType, String message) {
        this.orderID = orderID;
        this.updateType = updateType;
        this.message = message;
    }
}
