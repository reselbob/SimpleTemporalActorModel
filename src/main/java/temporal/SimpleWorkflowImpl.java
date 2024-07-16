package temporal;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import temporal.model.OrderInfo;

public class SimpleWorkflowImpl implements SimpleWorkflow {

  private boolean exit = false;
  private static final Logger logger = Workflow.getLogger(SimpleWorkflowImpl.class);

  private final List<OrderInfo> registeredOrderInfos = new LinkedList<>();

  private final OrderProcessingActivity orderProcessingActivity =
      Workflow.newActivityStub(
          OrderProcessingActivity.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

  @Override
  public void update(OrderInfo orderInfo) {
    try {
      logger.info("Order updated in Workflow: {}", orderInfo.toString());
      orderProcessingActivity.update(orderInfo);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void notifyCustomer(OrderInfo orderInfo) {
    try {
      logger.info("Notifying customer for order from parent workflow: {}", orderInfo.toString());
      ChildWorkflowOptions childWorkflowOptions =
          ChildWorkflowOptions.newBuilder()
              .setWorkflowId("ChildWorkflow_01")
              .setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON)
              .build();
      ChildWorkflow childWorkflow =
          Workflow.newChildWorkflowStub(ChildWorkflow.class, childWorkflowOptions);
      Async.procedure(childWorkflow::start);
      Workflow.getWorkflowExecution(childWorkflow).get();
      childWorkflow.sendNotification(orderInfo);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void exit() {
    logger.info("Exiting Workflow for SimpleWorkflow");
    exit = true;
  }

  public void start() {

    logger.info("Starting Workflow for SimpleWorkflow");
    Workflow.await(() -> exit);
  }

  @Override
  public void add(OrderInfo orderInfo) {
    try {
      logger.info("Order added in Workflow:{}", orderInfo.toString());
      orderProcessingActivity.add(orderInfo);
      this.registeredOrderInfos.add(orderInfo);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
