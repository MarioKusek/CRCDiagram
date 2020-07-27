package hr.fer.tel.crc.parser;

import hr.fer.tel.crc.Class;

public class ClassParser {

  private String text;

  public ClassParser(String text) {
    this.text = text;
  }

  public Class parse() {
    StringRange bodyRange = findBodyRange(text);
    StringRange classDeclarationRange = new StringRange(0, bodyRange.getStart() - 1);
    
    StringRange nameRange = extractClassName(text, classDeclarationRange);
    Class crcClass = new Class(nameRange.apply(text));
    
    StringRange aliasRange = extractAlias(text, new StringRange(nameRange.getEnd() + 1, classDeclarationRange.getEnd()));
    crcClass.setAlias(aliasRange.apply(text));
    
    return crcClass;
  }

  private static StringRange findBodyRange(String classText) {
    return StringExtractorUtil.findMatchingCurlyBraceRange(classText, classText.indexOf('{'));
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
