package temporal.model;

import java.util.UUID;

public class OrderInfo {
  public enum UpdateType {
    PURCHASE,
    CANCEL,
    MODIFY
  }

  public String getMessage() {
    return message;
  }

  public UpdateType getUpdateType() {
    return updateType;
  }

  public UUID getOrderID() {
    return orderID;
  }

  UUID orderID;
  UpdateType updateType;

  String message;

  public OrderInfo() {
  }

  public OrderInfo(UUID orderID, UpdateType updateType, String message) {
    this.orderID = orderID;
    this.updateType = updateType;
    this.message = message;
  }
}
