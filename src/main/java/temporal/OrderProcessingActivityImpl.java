package temporal;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import temporal.model.OrderInfo;

public class OrderProcessingActivityImpl implements OrderProcessingActivity {

  private static final Logger logger = Workflow.getLogger(OrderProcessingActivityImpl.class);

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
