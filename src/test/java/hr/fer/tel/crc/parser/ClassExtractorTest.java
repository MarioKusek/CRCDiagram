package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClassExtractorTest {

  @Test
  void extractOneClassFromInput() {
    String input = "\n" +
        "class name {\n" +
        "    responsibility : colleborator\n" +
        "    ...\n" +
        "}\n" +
        "";

    assertThat(ClassExtractor.extractClass(input, 0).apply(input)).isEqualTo(
        "class name {\n" +
            "    responsibility : colleborator\n" +
            "    ...\n" +
            "}");
  }

}
