package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hr.fer.tel.crc.Class;

class ParseClassNameTest {

  @Test
  void extractClassName() {
    Class c = ClassParser.parse("class name {}");
    assertThat(c.getName()).isEqualTo("name");
  }

  @Test
  void extractClassName2() {
    Class c = ClassParser.parse("class name2 {}");
    assertThat(c.getName()).isEqualTo("name2");
  }

  @Test
  void extractClassNameWithSpaces() throws Exception {
    Class c = ClassParser.parse("class \"class name with spaces\" {}");
    assertThat(c.getName()).isEqualTo("class name with spaces");
  }

  @Test
  void extractClassNameAndAlias() throws Exception {
    Class c = ClassParser.parse("class className as cn {}");
    assertThat(c.getName()).isEqualTo("className");
    assertThat(c.getAlias()).isEqualTo("cn");
  }
}
