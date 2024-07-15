package temporal;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import temporal.model.OrderInfo;

public class OrderProcessingActivityImpl implements OrderProcessingActivity {

  private static final Logger logger = Workflow.getLogger(OrderProcessingActivityImpl.class);
  ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void add(OrderInfo orderInfo) {
    try {
      //String json = objectMapper.writeValueAsString(orderInfo);
      logger.info("Order added in Activity: {}", orderInfo.toString());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void update(OrderInfo orderInfo) {
    try {
      //String json = objectMapper.writeValueAsString(orderInfo);
      logger.info("Order updated in Activity: {}", orderInfo.toString());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
