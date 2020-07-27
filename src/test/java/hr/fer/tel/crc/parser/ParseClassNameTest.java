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

}
