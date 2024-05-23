package tc.oc.occ.chat.messages;

import tc.oc.occ.chat.ChatAuditConfig;
import tc.oc.occ.chat.database.ChatDatabase;
import tc.oc.pgm.util.event.ChannelMessageEvent;

public class ChatManager {

  private final ChatAuditConfig config;
  private final ChatDatabase database;

  public ChatManager(ChatAuditConfig config, ChatDatabase database) {
    this.config = config;
    this.database = database;
  }

  public void onChatMessage(ChannelMessageEvent event) {
    if (!config.isEnabled()) return;

    ChatMessage message = new ChatMessage(event);

    database.saveMessage(message);
  }
}
