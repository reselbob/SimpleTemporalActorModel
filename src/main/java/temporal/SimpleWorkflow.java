package temporal;

import io.temporal.workflow.*;
import temporal.model.OrderInfo;
import java.util.Queue;

import java.util.List;
@WorkflowInterface
public interface OrderManagementWorkflow {

    @WorkflowMethod
    void start();

    @SignalMethod
    void add(OrderInfo orderInfo);

    @SignalMethod
    void update(OrderInfo orderInfo);

    @SignalMethod
    void save(OrderInfo orderInfo);


}
