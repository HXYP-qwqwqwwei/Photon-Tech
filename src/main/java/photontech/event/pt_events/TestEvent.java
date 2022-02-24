package photontech.event.pt_events;

import net.minecraftforge.eventbus.api.Event;

public class TestEvent extends Event {
    private final String msg;

    public TestEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
