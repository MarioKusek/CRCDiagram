package hr.fer.tel.crc;

public class ClassAlreadyInDiagramException extends RuntimeException {

  public ClassAlreadyInDiagramException(String className) {
    super("Class with name " +className + " is already in diagram.");
  }
}
