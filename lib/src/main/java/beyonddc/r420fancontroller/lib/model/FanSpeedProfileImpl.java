package beyonddc.r420fancontroller.lib.model;

public class FanSpeedProfileImpl implements IFanSpeedProfile {

  public static FanSpeedProfileImpl newInstance(
      final int fanSpeedPercentage, final int temperature) {
    if (fanSpeedPercentage <= 0 || fanSpeedPercentage > 100) {
      throw new IllegalArgumentException("Invalid fan speed percentage: " + fanSpeedPercentage);
    } else if (temperature <= 0) {
      throw new IllegalArgumentException("Invalid temperature: " + temperature);
    }
    return new FanSpeedProfileImpl(fanSpeedPercentage, temperature);
  }

  private int fanSpeedPercentage;
  private int temperature;

  private FanSpeedProfileImpl() {
    super();
  }

  private FanSpeedProfileImpl(
      final int fanSpeedPercentage, final int temperature) {
    this();
    this.fanSpeedPercentage = fanSpeedPercentage;
    this.temperature = temperature;
  }

  @Override
  public int getFanSpeedPercentage() {
    return this.fanSpeedPercentage;
  }

  @Override
  public int getTemperature() { return this.temperature; }
}
