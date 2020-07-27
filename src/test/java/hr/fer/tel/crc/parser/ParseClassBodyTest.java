package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

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

}
