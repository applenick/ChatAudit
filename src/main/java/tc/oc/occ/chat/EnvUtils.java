package tc.oc.occ.chat;

import tc.oc.occ.environment.Environment;

public class EnvUtils {

  private static String SERVER_KEY = "server_name";

  public static String getServerName() {
    return Environment.get().getString(SERVER_KEY);
  }
}
