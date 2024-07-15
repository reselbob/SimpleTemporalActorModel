package temporal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import temporal.model.OrderInfo;

public class OrderProcessingActivityImpl implements OrderProcessingActivity {

  private static final Logger logger = LoggerFactory.getLogger(OrderProcessingActivityImpl.class);

  @Override
  public void add(OrderInfo orderInfo) {
    logger.info(String.format("Order added in Activity: %s", orderInfo.toString()));
  }

  @Override
  public void update(OrderInfo orderInfo) {
    logger.info(String.format("Order updated in Activity: %s", orderInfo.toString()));
  }
}
