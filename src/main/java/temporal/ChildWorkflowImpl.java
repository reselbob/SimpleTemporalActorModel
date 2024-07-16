package temporal;

import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import temporal.model.OrderInfo;

// Child Workflow Implementation
public class ChildWorkflowImpl implements ChildWorkflow {
  private static final Logger logger = Workflow.getLogger(ChildWorkflowImpl.class);

  private boolean exit = false;

  @Override
  public void start() {
    logger.info("Starting Workflow for ChildWorkflow");
    Workflow.await(() -> exit);
  }

  @Override
  public void sendNotification(OrderInfo orderInfo) {
    try {
      logger.info(
          "Sending notification for customer from child workflow: {}", orderInfo.toString());
      Workflow.sleep(Duration.ofSeconds(3));
      exit = true;
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
