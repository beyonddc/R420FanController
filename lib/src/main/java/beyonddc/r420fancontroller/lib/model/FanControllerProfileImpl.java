package beyonddc.r420fancontroller.lib.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FanControllerProfileImpl implements IFanControllerProfile {

  public static FanControllerProfileImpl newInstance(
      final List<IFanSpeedProfile> fanSpeedProfiles) {
    Objects.requireNonNull(fanSpeedProfiles);
    if (fanSpeedProfiles.isEmpty()) {
      throw new IllegalArgumentException("There is no fan speed profile provided");
    }
    return new FanControllerProfileImpl(fanSpeedProfiles);
  }

  private List<IFanSpeedProfile> fanSpeedProfiles;

  private FanControllerProfileImpl() {
    super();
  }

  private FanControllerProfileImpl(final List<IFanSpeedProfile> fanSpeedProfiles) {
    this();
    this.fanSpeedProfiles = fanSpeedProfiles;
  }

  @Override
  public List<IFanSpeedProfile> getFanSpeedProfiles() {
    return Collections.unmodifiableList(fanSpeedProfiles);
  }
}
