package hr.fer.tel.crc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Class {

  private final String name;

  public Class(String name) {
    if(name == null)
      throw new NullPointerException("Name can not be null.");
    this.name = name;
  }

  private String alias;
}
