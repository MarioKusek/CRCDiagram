package hr.fer.tel.crc.parser;

import hr.fer.tel.crc.Class;

public class ClassParser {

  public static Class parse(String classText) {

    int bodyStartIndex = classText.indexOf("{");

    String classDeclaration = classText.substring(0, bodyStartIndex).trim();

    String name = extractClassName(classDeclaration);
    return new Class(name);
  }

  private static String extractClassName(String classDeclaration) {
    String classNameDeclaration = classDeclaration.substring(6);

    if(classNameDeclaration.startsWith("\"")) {
      int classNameEndIndex = classNameDeclaration.indexOf('"', 1);
      return classNameDeclaration.substring(1, classNameEndIndex);
    }


    return classNameDeclaration;
  }
}
