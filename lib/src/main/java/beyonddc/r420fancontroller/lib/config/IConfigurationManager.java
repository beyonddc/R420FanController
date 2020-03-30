package beyonddc.r420fancontroller.lib.config;

import java.util.List;

/**
 * Implementation of this class is responsible for managing
 * configuration.
 */
public interface IConfigurationManager {

  List<String> getFanSensorNames();

  List<String> getTempSensorNames();

  void saveFanSensorNames(List<String> fanSensorNames);

  void saveTempSensorNames(List<String> tempSensorNames);
}
