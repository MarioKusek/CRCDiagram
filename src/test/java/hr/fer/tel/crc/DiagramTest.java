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
    Class cl1 = new Class("cl1");
    Class cl2 = new Class("cl1");
    assertThrows(RuntimeException.class, () -> {
      List<Class> classList = List.of(cl1, cl2);
      new Diagram(classList);
    });
  }

  @Test
  void twoClassesWithSameAlias() throws Exception {
    Class cl1 = Class.builder().name("cl1").alias("a").build();
    Class cl2 = Class.builder().name("cl2").alias("a").build();
    assertThrows(RuntimeException.class, () -> new Diagram(List.of(cl1, cl2)));
  }

  @Test
  void twoClassesWithConflictingNameAndAlias() throws Exception {
    Class cl1 = Class.builder().name("cl1").alias("a").build();
    Class cl2 = Class.builder().name("a").build();
    assertThrows(RuntimeException.class, () -> new Diagram(List.of(cl1, cl2)));
  }

  @Test
  void classWithNonExistentCollaborator_shouldThrowException() throws Exception {
    Class cl1 = Class.builder()
        .name("cl1")
        .alias("a1")
        .responsibility(new Responsibility("r1", "cl3"))
        .build();
    Class cl2 = Class.builder().name("cl2").build();
    RuntimeException e = assertThrows(RuntimeException.class, () -> new Diagram(List.of(cl1, cl2)));

    assertThat(e.getMessage()).isEqualTo("Class with name cl1 has collaborator cl3 that can not be found.");
  }

  @Test
  void classesWithExistingCollaborators_shouldNotThrowException() throws Exception {
    Class cl1 = Class.builder()
        .name("cl1")
        .alias("a1")
        .responsibility(new Responsibility("r1", "cl2"))
        .build();
    Class cl2 = Class.builder()
        .name("cl2")
        .responsibility(new Responsibility("r2", "a1"))
        .build();

    new Diagram(List.of(cl1, cl2));
  }

  @Test
  void classWithSelfCollaboratorClass_shouldThrowException() throws Exception {
    Class cl1 = Class.builder()
        .name("cl1")
        .alias("a1")
        .responsibility(new Responsibility("r1", "cl1"))
        .build();

    RuntimeException e = assertThrows(RuntimeException.class, () -> new Diagram(List.of(cl1)));
  }

  @Test
  void classWithSelfCollaboratorAlias_shouldThrowException() throws Exception {
    Class cl1 = Class.builder()
        .name("cl1")
        .alias("a1")
        .responsibility(new Responsibility("r1", "a1"))
        .build();

    RuntimeException e = assertThrows(RuntimeException.class, () -> new Diagram(List.of(cl1)));
  }

}
