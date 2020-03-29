package beyonddc.r420fancontroller.lib.config;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigurationManagerImpl implements IConfigurationManager {

  private static final String FAN_SENSOR_NAMES_PROPERTY = "fan.sensor.names";

  private static final String R420_FAN_CONTROLLER_PROPERTIES = "r420fancontroller.properties";

  private Configuration configuration;

  /**
   * Default constructor.
   */
  public ConfigurationManagerImpl() {
    super();
    this.configuration = this.loadConfiguration();
  }

  public List<String> getFanSensorNames() {

    if (this.configuration != null) {

      final String[] fanSensorNames =
          this.configuration.getStringArray(FAN_SENSOR_NAMES_PROPERTY);

      if (fanSensorNames != null) {
        return Arrays.asList(fanSensorNames);
      }
    }

    return Collections.emptyList();
  }

  private Configuration loadConfiguration() {

    Configuration config = null;

    final File properties = new File(R420_FAN_CONTROLLER_PROPERTIES);

    final FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
        .configure((new Parameters()).fileBased().setFile(properties));

    try {

      config = builder.getConfiguration();

    } catch (ConfigurationException e) {

      e.printStackTrace();
    }

    return config;
  }
}
