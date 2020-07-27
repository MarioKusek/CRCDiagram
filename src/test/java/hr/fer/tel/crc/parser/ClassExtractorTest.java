package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassExtractorTest {

  private String input;

  @BeforeEach
  void setup() {
    input = "\n" +
        "class name {\n" +
        "    responsibility : colleborator\n" +
        "    ...\n" +
        "}\n" +
        "\n" +
        "class name2 {\n" +
        "    responsibility2 : colleborator2\n" +
        "    ...\n" +
        "}\n";
  }

  @Test
  void extractFirstClassFromInput() {
    assertThat(ClassExtractor.extractClass(input, 0).apply(input)).isEqualTo(
        "class name {\n" +
            "    responsibility : colleborator\n" +
            "    ...\n" +
            "}");
  }

  @Test
  void extractSecondClassFromInput() {
    assertThat(ClassExtractor.extractClass(input, 58).apply(input)).isEqualTo(
        "class name2 {\n" +
            "    responsibility2 : colleborator2\n" +
            "    ...\n" +
        "}");
  }

}
