package experiment.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @author liangchuan
 */

public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event: " + event.get());
    }
}
