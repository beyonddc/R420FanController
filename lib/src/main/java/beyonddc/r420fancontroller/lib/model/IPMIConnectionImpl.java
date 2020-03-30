package beyonddc.r420fancontroller.lib.model;

import java.util.Objects;

public class IPMIConnectionImpl implements IIPMIConnection {

  private final String ip;
  private final String password;
  private final String username;

  public static final IPMIConnectionImpl newInstance(
      final String ip,
      final String password,
      final String username) {

//    Objects.requireNonNull(ip, "IP cannot be null");
//    Objects.requireNonNull(password, "Password cannot be null");
//    Objects.requireNonNull(username, "Username cannot be null");
//
//    if (ip.isEmpty()) {
//      throw new IllegalArgumentException("IP is empty");
//    } else if (password.isEmpty()) {
//      throw new IllegalArgumentException("Password is empty");
//    } else if (username.isEmpty()) {
//      throw new IllegalArgumentException("Username is empty");
//    }

    return new IPMIConnectionImpl(ip, password, username);
  }

  private IPMIConnectionImpl(
      final String ip,
      final String password,
      final String username) {
    super();
    this.ip = ip;
    this.password = password;
    this.username = username;
  }

  @Override
  public String getIP() {
    return this.ip;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  public String getPasswordFilePath() {
    return null;
  }

  @Override
  public String getUsername() {
    return this.username;
  }
}
