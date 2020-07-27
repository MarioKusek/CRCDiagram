package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import hr.fer.tel.crc.Class;

class ParseClassBodyTest {

  @Test
  void noClassBody() {
    ParsingException e = assertThrows(ParsingException.class, () -> {
      new ClassParser("class x").parse();
    });

    assertThat(e.getMessage()).startsWith("Missing class body");
    assertThat(e.getLine()).isEqualTo(1);
    assertThat(e.getColumn()).isEqualTo(7);
  }

  @Test
  void emptyBody() throws Exception {
    Class c = new ClassParser("class x {}").parse();
    assertThat(c.getResponsibilities()).isEmpty();
  }

  @Test
  void bodyWithNoResponsibility() throws Exception {
    Class c = new ClassParser("class x {}").parse();
    assertThat(c.getResponsibilities()).isEmpty();
  }

  @Test
  void bodyWithOneResponsibilityWithoutColaborator() throws Exception {
    Class c = new ClassParser("class x {\n" +
        "    first responsibility  \n" +
        "}").parse();

    assertThat(c.getResponsibilities()).hasSize(1);
    assertThat(c.getResponsibilities().get(0).getText()).isEqualTo("first responsibility");
  }

  @Test
  void bodyWithTwoResponsibilitiesWithoutColaborator() throws Exception {
    Class c = new ClassParser("class x {\n" +
        "    first responsibility  \n" +
        "    second resp  \n" +
        "}").parse();

    assertThat(c.getResponsibilities()).hasSize(2);
    assertThat(c.getResponsibilities().get(0).getText()).isEqualTo("first responsibility");
    assertThat(c.getResponsibilities().get(1).getText()).isEqualTo("second resp");
  }

  @Test
  void bodyWithTwoResponsibilitiesAndEmptyLineWithoutColaborator() throws Exception {
    Class c = new ClassParser("class x {\n" +
        "    first responsibility  \n" +
        "\n" +
        "    second resp  \n" +
        "}").parse();

    assertThat(c.getResponsibilities()).hasSize(2);
    assertThat(c.getResponsibilities().get(0).getText()).isEqualTo("first responsibility");
    assertThat(c.getResponsibilities().get(1).getText()).isEqualTo("second resp");
  }
}
