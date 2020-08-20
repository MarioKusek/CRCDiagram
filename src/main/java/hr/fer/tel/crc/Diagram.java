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
      .forEach(c -> {
        c.getResponsibilities().stream().forEach(r -> {
          String collaborator = r.getCollaborator();
          if(collaborator != null && getClassByKey(collaborator) == null)
            throw new CollaboratorNotFoundException(c.getName(), collaborator);
          else if(collaborator != null && getClassByKey(collaborator) == c)
            throw new RuntimeException("Class with name " + c.getName() + " must not have collaborator that is this class");

        });
      });
  }

  public Class getClassByKey(String key) {
    return aliasses.get(key);
  }

  public List<Class> getClasses() {
    return classes;
  }
}
