package beyonddc.r420fancontroller.lib.config;

import beyonddc.r420fancontroller.lib.model.IIPMIConnection;
import beyonddc.r420fancontroller.lib.model.IPMIConnectionImpl;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationManagerImpl implements IConfigurationManager {

  private static final int DEFAULT_SENSORS_CHECK_INTERVAL = 5;

  private static final String R420_FAN_CONTROLLER_PROPERTIES = "r420fancontroller.properties";

  private static final String IPMI_IP = "ipmi.ip";

  private static final String IPMI_USERNAME = "ipmi_username";

  private static final String IPMI_PASSWORD = "ipmi_password";

  private static final String FAN_SENSOR_NAMES_PROPERTY = "fan.sensor.names";

  private static final String TEMP_SENSOR_NAMES_PROPERTY = "temp.sensor.names";

  private static final String SENSORS_CHECK_INTERVAL_PROPERTY = "sensors.check.interval";

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManagerImpl.class);

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

  public IIPMIConnection getIPMIConnection() {

    final String ip = this.configuration.getString(IPMI_IP);
    final String username = this.configuration.getString(IPMI_USERNAME);
    final String password = this.configuration.getString(IPMI_PASSWORD);

    return IPMIConnectionImpl.newInstance(ip, password, username);
  }

  public List<String> getFanSensorNames() {
    return this.getPropertyAsList(FAN_SENSOR_NAMES_PROPERTY);
  }

  public List<String> getTempSensorNames() {
    return this.getPropertyAsList(TEMP_SENSOR_NAMES_PROPERTY);
  }

  public int getSensorsCheckInterval() {
    return this.configuration.getInt(
        SENSORS_CHECK_INTERVAL_PROPERTY, DEFAULT_SENSORS_CHECK_INTERVAL);
  }

  public void saveIPMIConnection(IIPMIConnection ipmiConnection) {
    this.configuration.setProperty(IPMI_IP, ipmiConnection.getIP());
    this.configuration.setProperty(IPMI_USERNAME, ipmiConnection.getUsername());
    this.configuration.setProperty(IPMI_PASSWORD, ipmiConnection.getPassword());
    this.saveConfiguration(this.configuration, this.configurationBuilder);
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

      LOGGER.error("Failed to load configuration", e);
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

      LOGGER.error("Failed to save configuration", e);
    }
  }
}
