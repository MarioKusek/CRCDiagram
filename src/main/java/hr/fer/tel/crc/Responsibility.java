package hr.fer.tel.crc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Responsibility {
  private final String text;
  private String collaborator;

  public boolean hasCollaborator() {
    return collaborator != null;
  }
}
