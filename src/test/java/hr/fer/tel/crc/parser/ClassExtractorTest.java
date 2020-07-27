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
        "";
  }

  @Test
  void extractFirstClassFromInput() {
    assertThat(ClassExtractor.extractClass(input, 0).apply(input)).isEqualTo(
        "class name {\n" +
            "    responsibility : colleborator\n" +
            "    ...\n" +
            "}");
  }

}
