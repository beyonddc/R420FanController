package beyonddc.r420fancontroller.lib.service;

import beyonddc.r420fancontroller.lib.model.IIPMIConnection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessOutput;
import org.zeroturnaround.exec.ProcessResult;

public class R420IPMIServiceImpl implements IR420IPMIService {

  private static final String IPMITOOL = "ipmitool";
  private static final String LANPLUS = "lanplus";

  private static final Logger LOGGER = LoggerFactory.getLogger(R420IPMIServiceImpl.class);

  /**
   * ipmitool -I lanplus -H 192.168.8.97 -U root -f ./idrac_p sensor reading "Temp" "Fan1A" "Fan1B" "Fan2A" "Fan2B" "Fan3A" "Fan3B" "Fan4A" "Fan4B" "Fan5A" "Fan5B" "Fan6A" "Fan6B"
   */
  @Override
  public Map<String, String> getSensorReadings(
      final IIPMIConnection ipmiConnection,
      final List<String> sensorNames) {

    final List<String> cliInput = this.populateIPMIConnectionCLIInput(ipmiConnection);

    cliInput.add("sensor");
    cliInput.add("reading");
    cliInput.addAll(sensorNames);

    final HashMap<String, String> sensorReadings = new HashMap<>();

    try {

      final Future<ProcessResult> processResultFuture = new ProcessExecutor()
          .readOutput(true)
          .command(cliInput).start().getFuture();

      final ProcessResult processResult = processResultFuture.get();

      final int exitValue = processResult.getExitValue();

      if (exitValue == 0) {

        final ProcessOutput processOutput = processResult.getOutput();
        for (final String line : processOutput.getLinesAsUTF8()) {

          final StringTokenizer tokenizer = new StringTokenizer(line, "|");

          final String sensor = tokenizer.nextToken().replaceAll("\\s+","");
          final String reading = tokenizer.nextToken().replaceAll("\\s+","");

          sensorReadings.put(sensor, reading);
        }
      }

    } catch (IOException | InterruptedException | ExecutionException e) {

      LOGGER.error("Failed to get sensor readings", e);
    }

    return sensorReadings;
  }

  private List<String> populateIPMIConnectionCLIInput(
      final IIPMIConnection ipmiConnection) {

    final List<String> ipmiConnectionCLIInput = new ArrayList<>();

    ipmiConnectionCLIInput.add(IPMITOOL);
    ipmiConnectionCLIInput.add("-I");
    ipmiConnectionCLIInput.add(LANPLUS);
    ipmiConnectionCLIInput.add("-H");
    ipmiConnectionCLIInput.add(ipmiConnection.getIP());
    ipmiConnectionCLIInput.add("-U");
    ipmiConnectionCLIInput.add(ipmiConnection.getUsername());
    ipmiConnectionCLIInput.add("-P");
    ipmiConnectionCLIInput.add(ipmiConnection.getPassword());

    return ipmiConnectionCLIInput;
  }
}
