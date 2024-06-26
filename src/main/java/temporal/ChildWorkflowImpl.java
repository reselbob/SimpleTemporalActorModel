package temporal;

import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import temporal.model.OrderInfo;

// Child Workflow Implementation
public class ChildWorkflowImpl implements ChildWorkflow {
  private static final Logger logger = LoggerFactory.getLogger(ChildWorkflowImpl.class);

  @Override
  public void sendNotification(OrderInfo orderInfo) {
    logger.info("Sending notification for customer: " + orderInfo.getCustomerId());
    Workflow.sleep(Duration.ofSeconds(3));
  }
}
