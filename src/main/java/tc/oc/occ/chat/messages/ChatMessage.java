package tc.oc.occ.chat.messages;

import java.util.Date;
import java.util.UUID;
import tc.oc.occ.chat.EnvUtils;
import tc.oc.occ.chat.utils.PGMUtils;
import tc.oc.pgm.api.integration.Integration;
import tc.oc.pgm.util.channels.Channel;
import tc.oc.pgm.util.event.ChannelMessageEvent;

public class ChatMessage {

  private Long id;
  private Date timestamp;
  private UUID playerId;
  private String playerName;
  private String message;
  private String partyName;
  private String server;
  private String nickname;
  private Channel channel;

  public ChatMessage(ChannelMessageEvent event) {
    this(
        null,
        event.getSender().getUniqueId(),
        event.getSender().getName(),
        event.getMessage(),
        PGMUtils.getPartyName(event.getSender()),
        EnvUtils.getServerName(),
        Integration.getNick(event.getSender()),
        event.getChannel());
  }

  public ChatMessage(
      Long id,
      UUID playerId,
      String playerName,
      String message,
      String partyName,
      String server,
      String nickname,
      Channel channel) {
    this.id = id;
    this.timestamp = new Date();
    this.playerId = playerId;
    this.playerName = playerName;
    this.message = message;
    this.partyName = partyName;
    this.server = server;
    this.nickname = nickname;
    this.channel = channel;
  }

  public Long getId() {
    return id;
  }

  public void setLong(long id) {
    this.id = id;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public String getPlayerName() {
    return playerName;
  }

  public String getMessage() {
    return message;
  }

  public String getPartyName() {
    return partyName;
  }

  public String getServer() {
    return server;
  }

  public String getNickname() {
    return nickname;
  }

  public Channel getChannel() {
    return channel;
  }

  @Override
  public String toString() {
    return "ChatMessage [id="
        + id
        + ", timestamp="
        + timestamp
        + ", playerId="
        + playerId
        + ", playerName="
        + playerName
        + ", message="
        + message
        + ", partyName="
        + partyName
        + ", server="
        + server
        + ", nickname="
        + nickname
        + ", channel="
        + channel
        + "]";
  }
}
