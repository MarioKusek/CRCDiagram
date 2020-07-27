package hr.fer.tel.crc.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StringRange {
  private final int start;
  private final int end;

  public String apply(String text) {
    if(isEmptyRange())
      return null;

    return text.substring(start, end);
  }

  public boolean isEmptyRange() {
    return start < 0 || end < 0 || end < start;
  }
}
