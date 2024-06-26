package temporal.helpers;

import java.util.UUID;
import temporal.model.OrderInfo;

public class DataHelper {
  public static String generateRandomString(int length) {
    String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder builder = new StringBuilder();
    while (length-- != 0) {
      int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
  }

  public static OrderInfo getRandomNewOrderInfo() {
    UUID orderId = UUID.randomUUID();
    UUID customerId = UUID.randomUUID();
    OrderInfo.UpdateType updateType = OrderInfo.UpdateType.PURCHASE;
    String message = generateRandomString(10);
    return new OrderInfo(orderId, customerId, updateType, message);
  }
}
