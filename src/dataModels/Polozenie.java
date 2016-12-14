package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Polozenie extends DataModel{
  @Getter @Setter private Long wspx;
  @Getter @Setter private Long wspy;
  @Getter @Setter private Long wspz;
  @Getter @Setter private Long id;
}
