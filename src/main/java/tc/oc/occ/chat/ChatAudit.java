package tc.oc.occ.chat;

import org.bukkit.plugin.java.JavaPlugin;
import tc.oc.occ.chat.database.ChatDatabase;
import tc.oc.occ.chat.database.DevChatDatabase;
import tc.oc.occ.chat.database.MySQLChatDatabase;
import tc.oc.occ.chat.listeners.ChatListener;
import tc.oc.occ.chat.messages.ChatManager;

public class ChatAudit extends JavaPlugin {

  private ChatManager manager;
  private ChatAuditConfig config;
  private ChatDatabase database;

  private static ChatAudit plugin;

  @Override
  public void onEnable() {
    plugin = this;
    this.saveDefaultConfig();
    this.reloadConfig();

    this.config = new ChatAuditConfig(getConfig());
    this.database =
        config.isProductionDatabase()
            ? new MySQLChatDatabase(getLogger(), config)
            : new DevChatDatabase(getLogger());
    database.enable();
    this.manager = new ChatManager(config, database);

    getServer().getPluginManager().registerEvents(new ChatListener(manager), this);
  }

  @Override
  public void onDisable() {
    if (database != null) {
      database.disable();
    }
  }

  public static ChatAudit get() {
    return plugin;
  }
}
