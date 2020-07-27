package hr.fer.tel.crc.parser;

import hr.fer.tel.crc.Class;

public class ClassParser {

  public static Class parse(String classText) {

    int bodyStartIndex = classText.indexOf("{");

    String classDeclaration = classText.substring(0, bodyStartIndex);

    StringRange nameRange = extractClassName(classDeclaration);
    Class crcClass = new Class(nameRange.apply(classText));

    StringRange aliasRange = extractAlias(classDeclaration, new StringRange(nameRange.getEnd()+1, classDeclaration.length()));
    crcClass.setAlias(aliasRange.apply(classDeclaration));

    return crcClass;
  }

  private static StringRange extractAlias(String text, StringRange range) {
    int asIndex = text.indexOf("as ", range.getStart());
    return new StringRange(asIndex + 3, text.indexOf(' ', asIndex + 3));
  }

  private static StringRange extractClassName(String classDeclaration) {
    if(classDeclaration.charAt(6) == '"') {
      int classNameEndIndex = classDeclaration.indexOf('"', 7);
      return new StringRange(7, classNameEndIndex);
    }

    return new StringRange(6, classDeclaration.indexOf(' ', 6));
  }
}
