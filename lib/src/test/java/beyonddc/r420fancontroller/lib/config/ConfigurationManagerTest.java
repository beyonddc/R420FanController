package beyonddc.r420fancontroller.lib.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigurationManagerTest {

  @Test
  public void getFanSensorNames() {

    final IConfigurationManager configMgr = new ConfigurationManagerImpl();

    final List<String> fanSensorNames = configMgr.getFanSensorNames();

    Assertions.assertEquals(12, fanSensorNames.size());
  }

  @Test
  public void getTempSensorNames() {

    final IConfigurationManager configMgr = new ConfigurationManagerImpl();

    final List<String> tempSensorNames = configMgr.getTempSensorNames();

    Assertions.assertEquals(1, tempSensorNames.size());
  }

  @Test
  public void saveFanSensorNames() throws IOException {

    final String existingUserHome = System.getProperty("user.home");

    try {

      System.setProperty("user.home", "/tmp");

      final IConfigurationManager configMgr = new ConfigurationManagerImpl();

      configMgr.saveFanSensorNames(Arrays.asList(new String[]{"Fan1", "Fan2", "Fan3"}));

      final List<String> fanSensorNames = configMgr.getFanSensorNames();

      Assertions.assertEquals(3, fanSensorNames.size());

      // Reading it fome file again and compare it
      Assertions.assertEquals(3, (new ConfigurationManagerImpl()).getFanSensorNames().size());

    } finally {

      System.setProperty("user.home", existingUserHome);

      FileUtils.deleteDirectory(
          new File(
              String.format("%s/%s",
                  "/tmp",
                  ConfigurationManagerImpl.DEFAULT_DATA_DIR)));
    }
  }

  @Test
  public void saveTempSensorNames() throws IOException {

    final String existingUserHome = System.getProperty("user.home");

    try {

      System.setProperty("user.home", "/tmp");

      final IConfigurationManager configMgr = new ConfigurationManagerImpl();

      configMgr.saveTempSensorNames(Arrays.asList(new String[]{"Temp1", "Temp2"}));

      final List<String> tempSensorNames = configMgr.getTempSensorNames();

      Assertions.assertEquals(2, tempSensorNames.size());

      // Reading it fome file again and compare it
      Assertions.assertEquals(2, (new ConfigurationManagerImpl()).getTempSensorNames().size());

    } finally {

      System.setProperty("user.home", existingUserHome);

      FileUtils.deleteDirectory(
          new File(
              String.format("%s/%s",
                  "/tmp",
                  ConfigurationManagerImpl.DEFAULT_DATA_DIR)));
    }
  }
}
