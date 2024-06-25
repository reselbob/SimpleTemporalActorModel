package temporal;

import io.temporal.activity.ActivityInterface;
import temporal.model.OrderUpdate;

@ActivityInterface
public interface OrderProcessingActivity {
    void processUpdate(OrderUpdate orderUpdate) ;
}
