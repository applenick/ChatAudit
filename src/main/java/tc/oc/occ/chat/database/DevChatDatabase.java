package tc.oc.occ.chat.database;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.logging.Logger;
import tc.oc.occ.chat.messages.ChatMessage;

public class DevChatDatabase implements ChatDatabase {

  private final Logger logger;

  private List<ChatMessage> storage;

  public DevChatDatabase(Logger logger) {
    this.logger = logger;
    this.storage = Lists.newArrayList();
  }

  @Override
  public void enable() {}

  @Override
  public void disable() {}

  @Override
  public void saveMessage(ChatMessage message) {
    storage.add(message);
    logger.info("Saved message - " + message.getId());
  }
}
