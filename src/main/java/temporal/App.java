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

/**
 * App is the main class that demonstrates how to use Temporal to create a simple workflow that
 * processes orders. The workflow is defined in the SimpleWorkflow interface and implemented in the
 * SimpleWorkflowImpl class. The workflow is started by the main method, which creates a new
 * instance of the SimpleWorkflow interface and calls the start method to begin processing orders.
 *
 * <p>Also, the class as a method, startWorkerWithFactory, that creates and runs the WorkerFactory
 * and Worker instance that will receive both standard Temporal even messages and custom messages
 * created signals.
 */
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

    // Create the workflow
    SimpleWorkflow wf = client.newWorkflowStub(SimpleWorkflow.class, options);

    WorkflowClient.start(wf::start);

    // Create a new OrderInfo object from the DataHelper object
    OrderInfo newOrder = DataHelper.getRandomNewOrderInfo();

    // Run the workflow's add() method, which will create and send an add signal to the Worker
    wf.add(newOrder);

    // Create a new OrderInfo object that is an update the OrderInfo object
    // created using DataHelper.getRandomNewOrderInfo();
    OrderInfo updateOrder =
        new OrderInfo(
            newOrder.getOrderId(),
            newOrder.getCustomerId(),
            OrderInfo.UpdateType.MODIFY,
            newOrder.getMessage());

    // Run the workflow's update() method, which will create and send an update signal to the Worker
    wf.update(updateOrder);

    // Run the workflow's notifyCustomer() method, which will create and send a notifyCustomer
    // signal to the Worker
    wf.notifyCustomer(updateOrder);

    // Run the workflow's exit() method, which will create and send a exit signal to the Worker
    wf.exit();
  }

  private static WorkerFactory startWorkerWithFactory(WorkflowClient client) {
    // Create a worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Create a worker that listens on a task queue and hosts both workflow and activity
    // implementations.
    Worker worker = factory.newWorker(TASK_QUEUE);

    // Workflows are stateful. So you need a type to create instances.
    worker.registerWorkflowImplementationTypes(SimpleWorkflowImpl.class);

    // Register the activities that will be executed within the workflow
    worker.registerActivitiesImplementations(new OrderProcessingActivityImpl());

    // Start the worker created by this factory.
    factory.start();

    logger.info("Worker listening on task queue: {}.", TASK_QUEUE);

    return factory;
  }
}
