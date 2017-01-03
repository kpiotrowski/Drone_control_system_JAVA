package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Polozenie extends DataModel{
  @Getter @Setter private Float wspx;
  @Getter @Setter private Float wspy;
  @Getter @Setter private Float wspz;
  @Getter @Setter private Integer id;
}
