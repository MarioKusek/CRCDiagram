package hr.fer.tel.crc;

public class CollaboratorNotFoundException extends RuntimeException {

  public CollaboratorNotFoundException(String className, String collaborator) {
    super("Class with name " + className + " has collaborator " + collaborator + " that can not be found.");
  }
}
