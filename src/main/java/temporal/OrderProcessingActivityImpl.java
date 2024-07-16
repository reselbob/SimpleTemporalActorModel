package temporal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import temporal.model.OrderInfo;

public class OrderProcessingActivityImpl implements OrderProcessingActivity {

  private static final Logger logger = LoggerFactory.getLogger(OrderProcessingActivityImpl.class);

  @Override
  public void add(OrderInfo orderInfo) {
    try {
      logger.info("Order added in Activity: {}", orderInfo.toString());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void update(OrderInfo orderInfo) {
    try {
      logger.info("Order updated in Activity: {}", orderInfo.toString());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
