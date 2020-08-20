package hr.fer.tel.crc;

public class ClassAlreadyInDiagramException extends RuntimeException {

  private ClassAlreadyInDiagramException(String message) {
    super(message);
  }

  public static ClassAlreadyInDiagramException withName(String className) {
    return new ClassAlreadyInDiagramException("Class with name " + className + " is already in diagram.");
  }
}
