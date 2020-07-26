package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MatchingCurlyBracesTests {

  @Test
  void onlyCurlyBraces() {
    assertThat(CurlyBracesMatcher.find("{}",0)).isEqualTo(1);
  }

  @Test
  void emptyString_shouldReturnMinusOne() throws Exception {
    assertThat(CurlyBracesMatcher.find("",0)).isEqualTo(-1);
  }

  @Test
  void noBracesOnInputIndex_shouldReturnMinusOne() throws Exception {
    assertThat(CurlyBracesMatcher.find(" ",0)).isEqualTo(-1);
  }

  @Test
  void noClosingBrace_shouldReturnMinusOne() throws Exception {
    assertThat(CurlyBracesMatcher.find("{",0)).isEqualTo(-1);
  }

  @Test
  void curlyBracesWithContent() throws Exception {
    assertThat(CurlyBracesMatcher.find("{ }",0)).isEqualTo(2);
  }
}
