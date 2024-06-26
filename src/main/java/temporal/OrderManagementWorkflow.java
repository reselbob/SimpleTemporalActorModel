package temporal;

import io.temporal.workflow.*;
import temporal.model.OrderUpdate;
import java.util.Queue;

import java.util.List;
@WorkflowInterface
public interface OrderManagementWorkflow {

    @WorkflowMethod
    void start();

    @SignalMethod
    void addUpdate(OrderUpdate update);

    @SignalMethod
    void exit();

}
