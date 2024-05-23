package tc.oc.occ.chat.database;

import tc.oc.occ.chat.messages.ChatMessage;

public interface ChatDatabase {

  void saveMessage(ChatMessage message);

  void enable();

  void disable();
}
