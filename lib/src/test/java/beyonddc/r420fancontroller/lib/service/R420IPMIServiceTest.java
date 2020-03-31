package beyonddc.r420fancontroller.lib.service;

import beyonddc.r420fancontroller.lib.config.ConfigurationManagerImpl;
import beyonddc.r420fancontroller.lib.model.IIPMIConnection;
import beyonddc.r420fancontroller.lib.model.IPMIConnectionImpl;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class R420IPMIServiceTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(R420IPMIServiceTest.class);

  @Test
  public void testGetSensorReadings() throws IOException {

    final IIPMIConnection ipmiConnection = IPMIConnectionImpl.newInstance(
        "192.168.8.97", "", "root");

    final IR420IPMIService service = new R420IPMIServiceImpl();

    Map<String, String> readings =
        service.getSensorReadings(ipmiConnection,
            (new ConfigurationManagerImpl()).getFanSensorNames());

    for(final String key : readings.keySet()) {
      LOGGER.debug(String.format("Sensor: '%s', Reading: '%s'", key, readings.get(key)));
    }
  }

  @Test
  public void testIntToHex() {

    final R420IPMIServiceImpl service = new R420IPMIServiceImpl();

    Assertions.assertEquals("0x05", service.intToHex(5));
    Assertions.assertEquals("0x0a", service.intToHex(10));
    Assertions.assertEquals("0x14", service.intToHex(20));
    Assertions.assertEquals("0x32", service.intToHex(50));
  }
}
