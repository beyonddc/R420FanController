package beyonddc.r420fancontroller.lib.service;

import beyonddc.r420fancontroller.lib.model.IIPMIConnection;
import java.util.List;
import java.util.Map;

public interface IR420IPMIService {

  /**
   * Gets a map of sensor readings where key is the name of the sensor and
   * value is the sensor reading.
   */
  Map<String, String> getSensorReadings(
      IIPMIConnection ipmiConnection, List<String> sensorNames);
}
