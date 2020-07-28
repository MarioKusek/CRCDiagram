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
      new Diagram(List.of(cl1, cl2));
    });
  }

}
