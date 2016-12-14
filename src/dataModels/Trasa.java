package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Trasa extends DataModel {
  @Getter @Setter String nazwa;
  @Getter @Setter Long uzytkownik_id;
}
