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

    return text.substring(start, end+1);
  }

  public boolean isEmptyRange() {
    return start < 0 || end < 0 || end < start;
  }

  public int size() {
    if(isEmptyRange())
      return 0;

    return end-start+1;
  }
}
