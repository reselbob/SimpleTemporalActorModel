package temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import temporal.helpers.DataHelper;
import temporal.model.OrderInfo;

public class App {
  static final String TASK_QUEUE = "SimpleWorkflow";

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {

    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    // Start the worker and hold onto the WorkerFactory for later use, if necessary.
    WorkerFactory factory = startWorkerWithFactory(client);

    // Declare the WORKFLOW_ID
    String WORKFLOW_ID = TASK_QUEUE + "-" + "01";

    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(TASK_QUEUE)
            .setWorkflowId(WORKFLOW_ID)
            // set the return options
            .setRetryOptions(
                RetryOptions.newBuilder()
                    .setInitialInterval(Duration.ofSeconds(1))
                    .setMaximumInterval(Duration.ofSeconds(10))
                    .build())
            .build();

    SimpleWorkflow wf = client.newWorkflowStub(SimpleWorkflow.class, options);

    WorkflowClient.start(wf::start);
    OrderInfo newOrder = DataHelper.getRandomNewOrderInfo();
    wf.add(newOrder);

    OrderInfo updateOrder =
        new OrderInfo(newOrder.getOrderID(), OrderInfo.UpdateType.MODIFY, newOrder.getMessage());
    wf.update(updateOrder);

    wf.exit();
  }

  private static WorkerFactory startWorkerWithFactory(WorkflowClient client) {
    // worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Worker that listens on a task queue and hosts both workflow and activity
    // implementations.
    Worker worker = factory.newWorker(TASK_QUEUE);

    // Workflows are stateful. So you need a type to create instances.
    worker.registerWorkflowImplementationTypes(SimpleWorkflowImpl.class);

    worker.registerActivitiesImplementations(new OrderProcessingActivityImpl());

    // Start the worker created by this factory.
    factory.start();

    logger.info("Worker listening on task queue: {}.", TASK_QUEUE);

    return factory;
  }
}
