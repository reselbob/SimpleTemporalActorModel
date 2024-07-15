package temporal;

import com.fasterxml.jackson.databind.ObjectMapper;
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

  ObjectMapper objectMapper = new ObjectMapper();

  private final List<OrderInfo> registeredOrderInfos = new LinkedList<>();

  private final OrderProcessingActivity orderProcessingActivity =
      Workflow.newActivityStub(
          OrderProcessingActivity.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

  private io.temporal.workflow.WorkflowInfo info;

  @Override
  public void update(OrderInfo orderInfo) {
    try {
      String json = objectMapper.writeValueAsString(orderInfo);
      logger.info(String.format("Order updated in Workflow: %s", json));
      orderProcessingActivity.update(orderInfo);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public void notifyCustomer(OrderInfo orderInfo) {
    try {
      String json = objectMapper.writeValueAsString(orderInfo);
      logger.info(String.format("Notifying customer for order from parent workflow: %s", json));
      ChildWorkflowOptions childWorkflowOptions =
          ChildWorkflowOptions.newBuilder()
              .setWorkflowId("ChildWorkflow_01")
              .setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON)
              .build();
      ChildWorkflow childWorkflow =
          Workflow.newChildWorkflowStub(ChildWorkflow.class, childWorkflowOptions);
      Async.procedure(childWorkflow::start);
      Workflow.getWorkflowExecution(childWorkflow).get();
      Async.procedure(childWorkflow::sendNotification, orderInfo);
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
      String json = objectMapper.writeValueAsString(orderInfo);
      logger.info(String.format("Order added in Workflow: %s", json));
      orderProcessingActivity.add(orderInfo);
      this.registeredOrderInfos.add(orderInfo);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
