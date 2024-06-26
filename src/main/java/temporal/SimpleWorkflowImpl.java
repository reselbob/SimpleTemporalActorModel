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
import org.slf4j.LoggerFactory;
import temporal.model.OrderInfo;

public class SimpleWorkflowImpl implements SimpleWorkflow {

  private boolean exit = false;

  private static final Logger logger = LoggerFactory.getLogger(SimpleWorkflowImpl.class);

  private final List<OrderInfo> registeredOrderInfos = new LinkedList<>();

  private final OrderProcessingActivity orderProcessingActivity =
      Workflow.newActivityStub(
          OrderProcessingActivity.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

  private io.temporal.workflow.WorkflowInfo info;

  @Override
  public void update(OrderInfo orderInfo) {
    logger.info("Updating order in Workflow: " + orderInfo.toString());
    // Add new order updates to the queue
    orderProcessingActivity.update(orderInfo);
  }

  @Override
  public void register(OrderInfo orderInfo) {
    logger.info("Registering order in Workflow: " + orderInfo.toString());
    this.registeredOrderInfos.add(orderInfo);
  }

  @Override
  public void notifyCustomer(OrderInfo orderInfo) {

    logger.info("Notifying customer for order from parent workflow: " + orderInfo.toString());
    ChildWorkflowOptions childWorkflowOptions =
        ChildWorkflowOptions.newBuilder()
            .setWorkflowId("childWorkflow")
            .setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON)
            .build();
    ChildWorkflow childWorkflow =
        Workflow.newChildWorkflowStub(ChildWorkflow.class, childWorkflowOptions);

    Async.procedure(childWorkflow::sendNotification, orderInfo);
    Workflow.getWorkflowExecution(childWorkflow).get();
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
    logger.info("Adding order in Workflow: " + orderInfo.toString());
    orderProcessingActivity.add(orderInfo);
    this.registeredOrderInfos.add(orderInfo);
  }
}
