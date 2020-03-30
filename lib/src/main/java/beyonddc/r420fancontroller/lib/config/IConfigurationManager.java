package beyonddc.r420fancontroller.lib.config;

import beyonddc.r420fancontroller.lib.model.IIPMIConnection;
import java.util.List;

/**
 * Implementation of this class is responsible for managing
 * configuration.
 */
public interface IConfigurationManager {

  IIPMIConnection getIPMIConnection();

  List<String> getFanSensorNames();

  List<String> getTempSensorNames();

  void saveIPMIConnection(IIPMIConnection impiConnection);

  void saveFanSensorNames(List<String> fanSensorNames);

  void saveTempSensorNames(List<String> tempSensorNames);
}
