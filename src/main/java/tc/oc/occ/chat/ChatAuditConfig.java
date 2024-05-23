package tc.oc.occ.chat;

import org.bukkit.configuration.Configuration;

public class ChatAuditConfig {

  // Plugin
  private boolean enabled;

  // Database
  private int saveDelay;
  private boolean productionDatabase;
  private int batchSize;

  public ChatAuditConfig(Configuration config) {
    reload(config);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public int getSaveDelay() {
    return saveDelay;
  }

  public int getBatchSize() {
    return batchSize;
  }

  public boolean isProductionDatabase() {
    return productionDatabase;
  }

  public void reload(Configuration config) {
    this.enabled = config.getBoolean("enabled");
    this.productionDatabase = config.getBoolean("database.enabled");
    this.batchSize = config.getInt("database.batch-size");
    this.saveDelay = config.getInt("database.save-delay");
  }
}
