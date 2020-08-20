package hr.fer.tel.crc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diagram {

  private final List<Class> classes;
  private final Map<String, Class> aliasses;

  public Diagram(List<Class> classes) {
    this.classes = classes;

    aliasses = new HashMap<>();
    classes.stream().forEach(c -> {
      if(aliasses.containsKey(c.getName()))
        throw ClassAlreadyInDiagramException.withName(c.getName());
      aliasses.put(c.getName(), c);

      if(aliasses.containsKey(c.getAlias()))
        throw ClassAlreadyInDiagramException.withAlias(c.getName());
      if(c.getAlias() != null)
        aliasses.put(c.getAlias(), c);
    });

    checkCollaborators();
  }

  private void checkCollaborators() {
    classes.stream()
      .forEach(clazz ->
        clazz.getResponsibilities().stream().forEach(responsibility -> {
          final String collaboratorName = responsibility.getCollaborator();
          if(collaboratorName != null && getClassByKey(collaboratorName) == null)
            throw new CollaboratorNotFoundException(clazz.getName(), collaboratorName);
          else if(collaboratorName != null && getClassByKey(collaboratorName) == clazz)
            throw new SameClassCollaboratorException(clazz.getName());
        })
      );
  }

  public Class getClassByKey(String key) {
    return aliasses.get(key);
  }

  public List<Class> getClasses() {
    return classes;
  }
}
