package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hr.fer.tel.crc.Class;

class ClassExtractorTest {

  private String input;

  @BeforeEach
  void setup() {
    input = "\n" +
        "class name {\n" +
        "    responsibility : collaborator\n" +
        "    ...\n" +
        "}\n" +
        "\n" +
        "class name2 {\n" +
        "    responsibility2 : collaborator2\n" +
        "    ...\n" +
        "}\n";
  }

  @Test
  void extractFirstClassFromInput() {
    assertThat(StringExtractorUtil.extractClass(input, 0).apply(input)).isEqualTo(
        "class name {\n" +
            "    responsibility : collaborator\n" +
            "    ...\n" +
            "}");
  }

  @Test
  void extractSecondClassFromInput() {
    assertThat(StringExtractorUtil.extractClass(input, 58).apply(input)).isEqualTo(
        "class name2 {\n" +
            "    responsibility2 : collaborator2\n" +
            "    ...\n" +
        "}");
  }

  @Test
  void extractAllClasses() throws Exception {
    DiagramParser parser = new DiagramParser(input);
    List<Class> allClasses = parser.parse();

    assertThat(allClasses).hasSize(2);
    assertThat(allClasses.get(0).getName()).isEqualTo("name");
    assertThat(allClasses.get(1).getName()).isEqualTo("name2");
  }
}
