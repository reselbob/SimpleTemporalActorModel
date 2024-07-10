package temporal;

import io.temporal.activity.ActivityInterface;
import temporal.model.OrderInfo;

@ActivityInterface
public interface OrderProcessingActivity {
  void add(OrderInfo orderInfo);

  void update(OrderInfo orderInfo);
}
