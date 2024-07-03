package temporal;

import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import temporal.model.OrderInfo;

// Child Workflow Implementation
public class ChildWorkflowImpl implements ChildWorkflow {
  private static final Logger logger = LoggerFactory.getLogger(ChildWorkflowImpl.class);

  private boolean exit = false;

  @Override
  public void start() {
    logger.info("Starting Workflow for ChildWorkflow");
    Workflow.await(() -> exit);
  }

  @Override
  public void sendNotification(OrderInfo orderInfo) {
    logger.info(
        "Sending notification for customer from child workflow: " + orderInfo.getCustomerId());
    Workflow.sleep(Duration.ofSeconds(3));
    exit = true;
  }
}
