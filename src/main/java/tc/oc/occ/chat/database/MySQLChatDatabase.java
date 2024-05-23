package tc.oc.occ.chat.database;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import tc.oc.occ.chat.ChatAudit;
import tc.oc.occ.chat.ChatAuditConfig;
import tc.oc.occ.chat.messages.ChatMessage;
import tc.oc.occ.database.Database;

public class MySQLChatDatabase implements ChatDatabase {

  private final ChatAuditConfig config;
  private final ExecutorService executor;
  private final Logger logger;

  private static final String CHAT_TABLE = "chat_audit";
  private static final String CHAT_FIELDS =
      "id BIGINT AUTO_INCREMENT PRIMARY KEY, timestamp DATETIME NOT NULL, playerId CHAR(36) NOT NULL, playerName VARCHAR(255) NOT NULL, message TEXT, partyName VARCHAR(255), server VARCHAR(255), nickname VARCHAR(255), channel VARCHAR(255)";

  private Integer batchTaskID;
  private List<ChatMessage> pendingMessages;

  public MySQLChatDatabase(Logger logger, ChatAuditConfig config) {
    this.config = config;
    this.executor = Executors.newFixedThreadPool(6);
    this.logger = logger;
  }

  @Override
  public void enable() {
    this.pendingMessages = Lists.newArrayList();

    createTable(CHAT_TABLE, CHAT_FIELDS);
    this.batchTaskID = scheduleBatchSave();
  }

  @Override
  public void disable() {
    if (batchTaskID != null) {
      Bukkit.getScheduler().cancelTask(batchTaskID);
    }

    if (!pendingMessages.isEmpty()) {
      batchSave(pendingMessages);
    }

    batchTaskID = null;
    pendingMessages = null;
  }

  @Override
  public void saveMessage(ChatMessage message) {
    pendingMessages.add(message);
  }

  private Connection getConnection() throws SQLException {
    return Database.get().getConnectionPool().getPool().getConnection();
  }

  private void createTable(String table, String schema) {
    CompletableFuture.runAsync(
        () -> {
          try (Connection connection = getConnection();
              Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + table + " (" + schema + ");";
            statement.execute(sql);
            logger.info("Created table: " + table);
          } catch (SQLException e) {
            logger.warning("Error creating table " + table + ": " + e.getMessage());
          }
        },
        executor);
  }

  private void batchSave(List<ChatMessage> messages) {
    try (Connection connection = getConnection()) {
      String sql =
          "INSERT INTO "
              + CHAT_TABLE
              + " (timestamp, playerId, playerName, message, partyName, server, nickname, channel) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        for (ChatMessage message : messages) {
          statement.setTimestamp(1, new Timestamp(message.getTimestamp().getTime()));
          statement.setString(2, message.getPlayerId().toString());
          statement.setString(3, message.getPlayerName());
          statement.setString(4, message.getMessage());
          statement.setString(5, message.getPartyName());
          statement.setString(6, message.getServer());
          statement.setString(7, message.getNickname());
          statement.setString(8, message.getChannel().name());

          statement.addBatch();
        }
        statement.executeBatch();
      }
    } catch (SQLException e) {
      logger.warning("Error saving messages: " + e.getMessage());
    }
  }

  private int scheduleBatchSave() {
    return Bukkit.getScheduler()
        .scheduleSyncRepeatingTask(
            ChatAudit.get(),
            () -> {
              List<ChatMessage> messagesToSave = new ArrayList<>(pendingMessages);
              pendingMessages.clear();

              if (!messagesToSave.isEmpty()) {
                Bukkit.getScheduler()
                    .runTaskAsynchronously(
                        ChatAudit.get(),
                        () -> {
                          batchSave(messagesToSave);
                        });
              }
            },
            0L,
            config.getSaveDelay() * 20L);
  }
}
