package beyonddc.r420fancontroller.lib.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationUtils;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigurationManagerImpl implements IConfigurationManager {

  private static final String R420_FAN_CONTROLLER_PROPERTIES = "r420fancontroller.properties";

  private static final String FAN_SENSOR_NAMES_PROPERTY = "fan.sensor.names";

  private static final String TEMP_SENSOR_NAMES_PROPERTY = "temp.sensor.names";

  protected static final String DEFAULT_DATA_DIR = ".r420fancontroller";

  private final Configuration configuration;

  private final FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder;

  /**
   * Default constructor.
   */
  public ConfigurationManagerImpl() {

    super();

    this.configuration = this.loadConfiguration();

    final File savedProperties = new File(
        String.format("%s/%s",
            this.getConfigurationFilePath(),
            R420_FAN_CONTROLLER_PROPERTIES));

    if (!savedProperties.getParentFile().exists()) {
      savedProperties.getParentFile().mkdir();
    }

    this.configurationBuilder =
        this.getFileBasedConfigurationBuilder(savedProperties);
  }

  public List<String> getFanSensorNames() {
    return this.getPropertyAsList(FAN_SENSOR_NAMES_PROPERTY);
  }

  public List<String> getTempSensorNames() {
    return this.getPropertyAsList(TEMP_SENSOR_NAMES_PROPERTY);
  }

  public void saveFanSensorNames(final List<String> fanSensorNames) {
    this.configuration.setProperty(
        FAN_SENSOR_NAMES_PROPERTY, String.join(",", fanSensorNames));
    this.saveConfiguration(this.configuration, this.configurationBuilder);
  }

  public void saveTempSensorNames(final List<String> tempSensorNames) {
    this.configuration.setProperty(
        TEMP_SENSOR_NAMES_PROPERTY, String.join(",", tempSensorNames));
    this.saveConfiguration(this.configuration, this.configurationBuilder);
  }

  private File getConfigurationFile() {

    final File savedProperties = new File(
        String.format("%s/%s",
            this.getConfigurationFilePath(),
            R420_FAN_CONTROLLER_PROPERTIES));

    if (savedProperties.exists()) {

      return savedProperties;

    } else {

      return new File(R420_FAN_CONTROLLER_PROPERTIES);
    }
  }

  /**
   * Get configuration file path
   * @return
   */
  private String getConfigurationFilePath() {

    final String homeDir = System.getProperty("user.home");

    return String.format("%s/%s", homeDir, DEFAULT_DATA_DIR);
  }

  private FileBasedConfigurationBuilder<FileBasedConfiguration>
      getFileBasedConfigurationBuilder(final File properties) {

    final FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
            .configure((new Parameters())
                .fileBased()
                .setFile(properties)
                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

    return builder;
  }

  private List<String> getPropertyAsList(final String propertyName) {

    if (this.configuration != null) {

      final String[] properties =
          this.configuration.getStringArray(propertyName);

      if (properties != null) {
        return Arrays.asList(properties);
      }
    }

    return Collections.emptyList();
  }

  /**
   * Load configuration
   */
  private Configuration loadConfiguration() {

    Configuration config = null;

    final File properties = this.getConfigurationFile();

    final FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        this.getFileBasedConfigurationBuilder(properties);

    try {

      config = builder.getConfiguration();

    } catch (ConfigurationException e) {

      e.printStackTrace();
    }

    return config;
  }

  /**
   * Save configuration
   */
  private void saveConfiguration(
      final Configuration configToSave,
      final FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder) {

    try {

      if (!configurationBuilder.getFileHandler().getFile().exists()) {
        configurationBuilder.getFileHandler().getFile().createNewFile();
      }

      ConfigurationUtils.copy(
          configToSave, configurationBuilder.getConfiguration());

      configurationBuilder.save();

    } catch (final ConfigurationException | IOException e) {

      e.printStackTrace();
    }
  }
}
