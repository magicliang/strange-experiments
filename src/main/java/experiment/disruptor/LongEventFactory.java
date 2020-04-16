package experiment.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author liangchuan
 */

public class LongEventFactory implements EventFactory<LongEvent> {
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
