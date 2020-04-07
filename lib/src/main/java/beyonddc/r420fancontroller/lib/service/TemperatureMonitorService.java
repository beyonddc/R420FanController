package beyonddc.r420fancontroller.lib.service;

import beyonddc.r420fancontroller.lib.config.IConfigurationManager;
import beyonddc.r420fancontroller.lib.model.IFanControllerProfile;
import beyonddc.r420fancontroller.lib.model.IIPMIConnection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TemperatureMonitorService implements ITemperatureMonitorService {

  private static class TemperatureMonitorThread extends Thread {

    private volatile boolean isStopping = false;

    private final IIPMIConnection ipmiConnection;
    private final IFanControllerProfile fanControllerProfile;
    private final IR420IPMIService ipmiService;
    private final IConfigurationManager configurationManager;

    public TemperatureMonitorThread(
        final IIPMIConnection ipmiConnection,
        final IFanControllerProfile fanControllerProfile,
        final IR420IPMIService ipmiService,
        final IConfigurationManager configurationManager) {
      super();
      this.ipmiConnection = ipmiConnection;
      this.fanControllerProfile = fanControllerProfile;
      this.ipmiService = ipmiService;
      this.configurationManager = configurationManager;
    }

    public void run() {

      try {

        final int sensorsCheckInterval =
            this.configurationManager.getSensorsCheckInterval() * 1000;

        do {

          final Map<String, String> tempReadings =
              this.ipmiService.getSensorReadings(
                  this.ipmiConnection,
                  this.configurationManager.getTempSensorNames());

          tempReadings.values();

          Thread.sleep(sensorsCheckInterval * 1000);

        } while (this.isStopping);

      } catch (final InterruptedException ex) {

      }
    }

    public void stopMonitoring() {
      this.isStopping = true;
    }
  }

  private final TemperatureMonitorThread monitorThread;

  public TemperatureMonitorService(
      final IIPMIConnection ipmiConnection,
      final IFanControllerProfile fanControllerProfile,
      final IR420IPMIService ipmiService,
      final IConfigurationManager configurationManager) {
    Objects.requireNonNull(ipmiConnection);
    Objects.requireNonNull(fanControllerProfile);
    this.monitorThread = new TemperatureMonitorThread(
        ipmiConnection, fanControllerProfile, ipmiService, configurationManager);
  }

  @Override
  public void start() {
    this.monitorThread.start();
  }

  @Override
  public void stop() {

    this.monitorThread.stopMonitoring();
    try {
      this.monitorThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
