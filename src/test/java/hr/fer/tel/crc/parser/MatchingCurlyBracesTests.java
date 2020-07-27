package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MatchingCurlyBracesTests {

  @Test
  void onlyCurlyBraces() {
    assertThat(StringExtractorUtil.findMatchingCurlyBrace("{}",0)).isEqualTo(1);
  }

  @Test
  void emptyString_shouldReturnMinusOne() throws Exception {
    assertThat(StringExtractorUtil.findMatchingCurlyBrace("",0)).isEqualTo(-1);
  }

  @Test
  void noBracesOnInputIndex_shouldReturnMinusOne() throws Exception {
    assertThat(StringExtractorUtil.findMatchingCurlyBrace(" ",0)).isEqualTo(-1);
  }

  @Test
  void noClosingBrace_shouldReturnMinusOne() throws Exception {
    assertThat(StringExtractorUtil.findMatchingCurlyBrace("{",0)).isEqualTo(-1);
  }

  @Test
  void noOpeningBrace_shouldReturnMinusOne() throws Exception {
    assertThat(StringExtractorUtil.findMatchingCurlyBrace("}",0)).isEqualTo(-1);
  }

  @Test
  void curlyBracesWithContent() throws Exception {
    assertThat(StringExtractorUtil.findMatchingCurlyBrace("{ }",0)).isEqualTo(2);
  }

  @Test
  void nestingCurlyBracesWithContent() throws Exception {
    assertThat(StringExtractorUtil.findMatchingCurlyBrace("{{ }}",0)).isEqualTo(4);
  }

  @Test
  void nestingCurlyBracesWithContent2() throws Exception {
    assertThat(StringExtractorUtil.findMatchingCurlyBrace("{a{b}c{d{e}f}g}",6)).isEqualTo(12);
  }
}
