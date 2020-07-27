package hr.fer.tel.crc.parser;

import hr.fer.tel.crc.Class;

public class ClassParser {

  public static Class parse(String classText) {

    int bodyStartIndex = classText.indexOf("{");

    String classDeclaration = classText.substring(0, bodyStartIndex).trim();

    StringRange nameRange = extractClassName(classDeclaration);
    return new Class(nameRange.apply(classText));
  }

  private static StringRange extractClassName(String classDeclaration) {
    if(classDeclaration.charAt(6) == '"') {
      int classNameEndIndex = classDeclaration.indexOf('"', 7);
      return new StringRange(7, classNameEndIndex);
    }

    return new StringRange(6, classDeclaration.length());
  }
}
