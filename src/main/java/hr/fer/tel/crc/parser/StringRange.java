package hr.fer.tel.crc.parser;

public class StringRange {
  private int start;
  private int end;

  public StringRange(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public String apply(String text) {
    return text.substring(start, end);
  }
}
