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

  public UUID getOrderId() {
    return orderId;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  UUID orderId;
  UUID customerId;
  UpdateType updateType;
  String message;

  public OrderInfo() {}

  public OrderInfo(UUID orderId, UUID customerId, UpdateType updateType, String message) {
    this.customerId = customerId;
    this.orderId = orderId;
    this.updateType = updateType;
    this.message = message;
  }
}
