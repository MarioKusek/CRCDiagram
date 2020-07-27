package hr.fer.tel.crc;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Class {

  private final String name;
  private String alias;
  private List<Responsibility> responsibilities;


  public Class(String name) {
    if(name == null)
      throw new NullPointerException("Name can not be null.");
    this.name = name;
    this.responsibilities = new LinkedList<>();
  }

  public List<Responsibility> getResponsibilities() {
    return responsibilities;
  }
}
