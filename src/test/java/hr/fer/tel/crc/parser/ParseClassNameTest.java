package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import hr.fer.tel.crc.Class;

class ParseClassNameTest {

  @Test
  void extractClassName() {
    Class c = new ClassParser("class name {}").parse();
    assertThat(c.getName()).isEqualTo("name");
  }

  @Test
  void extractClassNameFromRange() {
    String text = "class class name {}";
    Class c = new ClassParser(text, new StringRange(6, text.length()-1)).parse();
    assertThat(c.getName()).isEqualTo("name");
  }

  @Test
  void extractClassName2() {
    Class c = new ClassParser("class name2 {}").parse();
    assertThat(c.getName()).isEqualTo("name2");
  }

  @Test
  void extractClassNameWithSpaces() throws Exception {
    Class c = new ClassParser("class \"class name with spaces\" {}").parse();
    assertThat(c.getName()).isEqualTo("class name with spaces");
  }

  @Test
  void extractClassNameAndAlias() throws Exception {
    Class c = new ClassParser("class className as cn {}").parse();
    assertThat(c.getName()).isEqualTo("className");
    assertThat(c.getAlias()).isEqualTo("cn");
  }

  @Test
  void extractClassNameWithSpacesAndAlias() throws Exception {
    Class c = new ClassParser("class \"some class name\" as scm {}").parse();
    assertThat(c.getName()).isEqualTo("some class name");
    assertThat(c.getAlias()).isEqualTo("scm");
  }

  @Test
  void extractClassNameWithNoName_shouldThrowException() throws Exception {
    ParsingException e = assertThrows(ParsingException.class, () -> {
      new ClassParser("class {}").parse();
    });

    assertThat(e.getLine()).isEqualTo(1);
    assertThat(e.getColumn()).isEqualTo(6);
  }
}
