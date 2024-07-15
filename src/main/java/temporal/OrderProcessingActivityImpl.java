package temporal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import temporal.model.OrderInfo;

public class OrderProcessingActivityImpl implements OrderProcessingActivity {

  private static final Logger logger = LoggerFactory.getLogger(OrderProcessingActivityImpl.class);
  ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void add(OrderInfo orderInfo) {
    try {
      String json = objectMapper.writeValueAsString(orderInfo);
      logger.info("Order added in Activity: {}", json);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void update(OrderInfo orderInfo) {
    try {
      String json = objectMapper.writeValueAsString(orderInfo);
      logger.info("Order updated in Activity: {}", json);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
