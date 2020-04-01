package beyonddc.r420fancontroller.lib.service;

import beyonddc.r420fancontroller.lib.model.IFanControllerProfile;
import beyonddc.r420fancontroller.lib.model.IIPMIConnection;
import java.util.Objects;

public class TemperatureMonitorService implements ITemperatureMonitorService {

  private class TemperatureMonitorThread extends Thread {

    public TemperatureMonitorThread(
        final IIPMIConnection ipmiConnection,
        final IFanControllerProfile fanControllerProfile) {
      super();
    }

    public void run() {
      do {

      } while (TemperatureMonitorService.this.isStopping);
    }
  }

  private final IIPMIConnection ipmiConnection;
  private final IFanControllerProfile fanControllerProfile;
  private boolean isStopping;

  public TemperatureMonitorService(
      final IIPMIConnection ipmiConnection,
      final IFanControllerProfile fanControllerProfile) {
    Objects.requireNonNull(ipmiConnection);
    Objects.requireNonNull(fanControllerProfile);
    this.ipmiConnection = ipmiConnection;
    this.fanControllerProfile = fanControllerProfile;
    this.isStopping = false;
  }

  @Override
  public void start() {
  }

  @Override
  public void stop() {
    this.isStopping = true;
  }
}
