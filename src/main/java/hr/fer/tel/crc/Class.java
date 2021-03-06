package hr.fer.tel.crc;

import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class Class {

  private final String name;
  private String alias;

  @Singular
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

  public void add(Responsibility responsibility) {
    responsibilities.add(responsibility);
  }
}
