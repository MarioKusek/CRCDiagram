package hr.fer.tel.crc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diagram {

  private List<Class> classes;
  private Map<String, Class> aliasses;

  public Diagram(List<Class> classes) {
    this.classes = classes;

    aliasses = new HashMap<>();
    classes.stream().forEach(c -> {
      if(aliasses.containsKey(c.getName()))
        throw new RuntimeException("Class with name " + c.getName() + " is already in diagram.");
      aliasses.put(c.getName(), c);

      if(aliasses.containsKey(c.getAlias()))
        throw new RuntimeException("Class with alias " + c.getName() + " is already in diagram.");
      if(c.getAlias() != null)
        aliasses.put(c.getAlias(), c);
    });
  }

  public Class getClassByKey(String key) {
    return aliasses.get(key);
  }

  public List<Class> getClasses() {
    return classes;
  }
}
