package beyonddc.r420fancontroller.lib.config;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigurationManagerTest {

  @Test
  public void getFanSensorNames() {

    final IConfigurationManager configMgr = new ConfigurationManagerImpl();

    final List<String> fanSensorNames = configMgr.getFanSensorNames();

    System.out.println(fanSensorNames);

    Assertions.assertEquals(12, fanSensorNames.size());
  }
}
