package hr.fer.tel.crc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

class DiagramTest {

  @Test
  void twoClassesWithoutAliases() throws Exception {
    Class cl1 = new Class("cl1");
    Class cl2 = new Class("cl2");
    Diagram diagram = new Diagram(List.of(cl1, cl2));

    assertThat(diagram.getClassByKey("cl1")).isEqualTo(cl1);
    assertThat(diagram.getClassByKey("cl2")).isEqualTo(cl2);
  }

  @Test
  void twoClassesWithSameName() throws Exception {
    List<Class> classList = List.of(
        new Class("cl1"),
        new Class("cl1")
    );

    assertThrows(RuntimeException.class, () -> new Diagram(classList));
  }

  @Test
  void twoClassesWithSameAlias() throws Exception {
    List<Class> classesList = List.of(
        Class.builder().name("cl1").alias("a").build(),
        Class.builder().name("cl2").alias("a").build()
    );

    assertThrows(RuntimeException.class, () -> new Diagram(classesList));
  }

  @Test
  void twoClassesWithConflictingNameAndAlias() throws Exception {
    List<Class> classesList = List.of(
        Class.builder().name("cl1").alias("a").build(),
        Class.builder().name("a").build()
    );

    assertThrows(RuntimeException.class, () -> new Diagram(classesList));
  }

  @Test
  void classWithNonExistentCollaborator_shouldThrowException() throws Exception {
    List<Class> classesList = List.of(
        Class.builder()
          .name("cl1")
          .alias("a1")
          .responsibility(new Responsibility("r1", "cl3"))
          .build(),
        Class.builder().name("cl2").build()
    );

    RuntimeException e = assertThrows(RuntimeException.class, () -> new Diagram(classesList));

    assertThat(e.getMessage()).isEqualTo("Class with name cl1 has collaborator cl3 that can not be found.");
  }

  @Test
  void classesWithExistingCollaborators_shouldNotThrowException() throws Exception {
    List<Class> classes = List.of(
      Class.builder()
        .name("cl1")
        .alias("a1")
        .responsibility(new Responsibility("r1", "cl2"))
        .build(),
      Class.builder()
        .name("cl2")
        .responsibility(new Responsibility("r2", "a1"))
        .build()
    );

    new Diagram(classes);
  }

  @Test
  void classWithSelfCollaboratorClass_shouldThrowException() throws Exception {
    List<Class> classes = List.of(
      Class.builder()
        .name("cl1")
        .alias("a1")
        .responsibility(new Responsibility("r1", "cl1"))
        .build()
      );

    assertThrows(RuntimeException.class, () -> new Diagram(classes));
  }

  @Test
  void classWithSelfCollaboratorAlias_shouldThrowException() throws Exception {
    List<Class> classes = List.of(
      Class.builder()
        .name("cl1")
        .alias("a1")
        .responsibility(new Responsibility("r1", "a1"))
        .build()
    );

    assertThrows(RuntimeException.class, () -> new Diagram(classes));
  }

}
