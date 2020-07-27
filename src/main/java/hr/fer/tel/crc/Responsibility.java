package hr.fer.tel.crc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Responsibility {
  private final String text;
  private String collaborator;
}
