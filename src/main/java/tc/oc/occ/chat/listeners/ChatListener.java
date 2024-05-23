package tc.oc.occ.chat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tc.oc.occ.chat.messages.ChatManager;
import tc.oc.pgm.util.event.ChannelMessageEvent;

public class ChatListener implements Listener {

  private ChatManager manager;

  public ChatListener(ChatManager manager) {
    this.manager = manager;
  }

  @EventHandler
  public void onChat(ChannelMessageEvent event) {
    this.manager.onChatMessage(event);
  }
}
