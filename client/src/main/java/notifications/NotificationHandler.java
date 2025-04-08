package notifications;

import commands.*;
import messages.*;

public interface NotificationHandler {
    void notify(ServerMessage message);
}
