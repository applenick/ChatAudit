package tc.oc.occ.chat.utils;

import org.bukkit.entity.Player;
import tc.oc.pgm.api.PGM;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.player.MatchPlayer;

public class PGMUtils {

  public static MatchPlayer getMatchPlayer(Player player) {
    Match match = PGM.get().getMatchManager().getMatch(player);
    return match.getPlayer(player);
  }

  public static String getPartyName(Player player) {
    MatchPlayer mp = getMatchPlayer(player);
    if (mp == null) return "";

    return mp.getParty().getNameLegacy();
  }
}
