package temporal;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.WorkflowInfo;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import org.slf4j.Logger;
import temporal.model.OrderUpdate;

import java.time.Duration;
import java.util.LinkedList;
import java.util.Queue;

public class OrderManagementWorkflowImpl implements OrderManagementWorkflow {
    private static final Logger logger = Workflow.getLogger(OrderManagementWorkflowImpl.class);
    private final OrderProcessingActivity orderProcessingActivity =
            Workflow.newActivityStub(
                    OrderProcessingActivity.class,
                    ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

    // instantiate your WorkflowQueue to prevent a race and add a capacity based
    // on your business logic
    private WorkflowQueue<OrderUpdate> signals = Workflow.newWorkflowQueue(Integer.MAX_VALUE);

    private io.temporal.workflow.WorkflowInfo info;

    private boolean exit = false;

    @Override
    public void addUpdate(OrderUpdate update) {
        // Add new order updates to the queue
        signals.put(update);
    }

    @Override
    public void exit() {
        logger.info("Exiting Order Management Workflow");
        exit = true;
    }


    // Entity workflow pattern with serialization of request
    // (ie. only one request is processed at a time)
    public void start() {

        /*
        for (OrderUpdate element: updates) {
            signals.offer(element);
        }*/

        //this.signals = updates;

        WorkflowInfo info = Workflow.getInfo();



        while(!info.isContinueAsNewSuggested()){
            long timeSinceStarted = Workflow.currentTimeMillis() - info.getRunStartedTimestampMillis();

            Duration duration = Duration.ofMillis(timeSinceStarted);
            Duration twentyFourHours = Duration.ofHours(24);

            boolean shouldProcessMore = Workflow.await(twentyFourHours.minus(duration), () ->  signals.poll() != null);

            if (shouldProcessMore){
                break;
            }

            OrderUpdate update = signals.poll();


            // Call the activity to process the Update
            orderProcessingActivity.processUpdate(update);

        }

        Workflow.continueAsNew(signals);

    }
}
