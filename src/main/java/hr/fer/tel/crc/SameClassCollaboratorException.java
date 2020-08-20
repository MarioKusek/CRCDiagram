package hr.fer.tel.crc;

public class SameClassCollaboratorException extends RuntimeException {

  public SameClassCollaboratorException(String className) {
    super("Class with name " + className + " must not have collaborator that is the same class.");
  }

}
