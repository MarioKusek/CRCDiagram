package hr.fer.tel.crc.parser;

import hr.fer.tel.crc.Class;

public class ClassParser {

  public static Class parse(String classText) {

    StringRange bodyRange = StringExtractorUtil.findMatchingCurlyBraceRange(classText, classText.indexOf('{'));

    StringRange classDeclarationRange = new StringRange(0, bodyRange.getStart() - 1);

    StringRange nameRange = extractClassName(classText, classDeclarationRange);
    Class crcClass = new Class(nameRange.apply(classText));

    StringRange aliasRange = extractAlias(classText, new StringRange(nameRange.getEnd() + 1, classDeclarationRange.getEnd()));
    crcClass.setAlias(aliasRange.apply(classText));

    return crcClass;
  }

  private static StringRange extractAlias(String text, StringRange range) {
    int asIndex = text.indexOf("as ", range.getStart());
    return new StringRange(asIndex + 3, text.indexOf(' ', asIndex + 3));
  }

  private static StringRange extractClassName(String text, StringRange range) {
    if(text.charAt(range.getStart() + 6) == '"') {
      int classNameEndIndex = text.indexOf('"', range.getStart() + 7);
      return new StringRange(range.getStart() + 7, classNameEndIndex);
    }

    return new StringRange(range.getStart() + 6, text.indexOf(' ', range.getStart() + 6));
  }
}
