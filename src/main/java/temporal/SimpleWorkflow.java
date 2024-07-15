package temporal;

import io.temporal.workflow.*;
import temporal.model.OrderInfo;

@WorkflowInterface
public interface SimpleWorkflow {

  @WorkflowMethod
  void start();

  @SignalMethod
  void add(OrderInfo orderInfo);

  @SignalMethod
  void update(OrderInfo orderInfo);

  @SignalMethod
  void notifyCustomer(OrderInfo orderInfo);

  @SignalMethod
  void exit();
}
