package temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class Controller {
    static final String TASK_QUEUE ="SimpleSignalWorkflow";
    static final String WORKFLOW_ID = TASK_QUEUE + "-01";

    public static void main(String[] args) {
        System.out.println("Starting Controller");

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker(TASK_QUEUE);

        worker.registerWorkflowImplementationTypes(OrderManagementWorkflowImpl.class);

        // Create the workflow options
        WorkflowOptions workflowOptions =
                WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).setWorkflowId(WORKFLOW_ID).build();

        OrderManagementWorkflow workflow = client.newWorkflowStub(OrderManagementWorkflow.class, workflowOptions);

        factory.start();
    }
}
