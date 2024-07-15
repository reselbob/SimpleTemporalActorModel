package temporal.model;

import java.util.UUID;

/**
 * OrderInfo is a simple class that represents the information about an order. It contains fields
 * for orderId, customerId, updateType, and message.
 */
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

  @Override
  public String toString() {
    return "OrderInfo{"
        + "orderId="
        + orderId
        + ", customerId="
        + customerId
        + ", updateType="
        + updateType
        + ", message='"
        + message
        + '\''
        + '}';
  }
}
