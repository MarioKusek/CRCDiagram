package hr.fer.tel.crc.parser;

import lombok.Getter;

@Getter
public class ParsingException extends RuntimeException {
  private final long line;
  private final int column;

  public ParsingException(String message, long line, int column) {
    super(String.format("%s - in line %d and column %d", message, line, column));
    this.column = column;
    this.line = line;
  }

  public ParsingException(String message, String text, int offset) {
    this(message, findLine(text, offset), findColumn(text, offset));
  }

  private static int findColumn(String text, int offset) {
    return text.substring(0, offset).lines()
        .reduce((first, second) -> second)
          .orElseGet(() -> "")
          .length();
  }

  private static long findLine(String text, int offset) {
    return text.lines().count();
  }

}
