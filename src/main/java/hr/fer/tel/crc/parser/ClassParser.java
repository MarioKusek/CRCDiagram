package hr.fer.tel.crc.parser;

import hr.fer.tel.crc.Class;

public class ClassParser {

  private String text;
  private StringRange textRange;

  public ClassParser(String text) {
    this(text, new StringRange(0, text.length()-1));
  }

  public ClassParser(String text, StringRange textRange) {
    this.text = text;
    this.textRange = textRange;
  }

  public Class parse() {
    StringRange bodyRange = findBodyRange();
    StringRange classDeclarationRange = new StringRange(textRange.getStart(), bodyRange.getStart() - 1);

    StringRange nameRange = extractClassName(classDeclarationRange);
    Class crcClass = new Class(nameRange.apply(text));

    StringRange aliasRange = extractAlias(new StringRange(nameRange.getEnd() + 1, classDeclarationRange.getEnd()));
    crcClass.setAlias(aliasRange.apply(text));

    return crcClass;
  }

  private StringRange findBodyRange() {
    int openCurlyBraceIndex = text.indexOf('{', textRange.getStart());
    if(openCurlyBraceIndex == -1)
      throw new ParsingException("Missing class body", text, textRange.getEnd()+1);

    return StringExtractorUtil.findMatchingCurlyBraceRange(text, openCurlyBraceIndex);
  }

  private StringRange extractAlias(StringRange range) {
    int asIndex = text.indexOf("as ", range.getStart());
    StringRange aliasRange = new StringRange(asIndex + 3, text.indexOf(' ', asIndex + 3));
    if(aliasRange.isEmptyRange())
      throw new ParsingException("Missing alias", text, aliasRange.getStart());
    return aliasRange;
  }

  private StringRange extractClassName(StringRange range) {
    if(range.size() < 6)
      throw new ParsingException("Class should start with class keyword", text, range.getStart());

    if(text.charAt(range.getStart() + 6) == '"') {
      int classNameEndIndex = text.indexOf('"', range.getStart() + 7);
      return new StringRange(range.getStart() + 7, classNameEndIndex);
    }

    StringRange nameRange = new StringRange(range.getStart() + 6, text.indexOf(' ', range.getStart() + 6));
    if(nameRange.isEmptyRange())
      throw new ParsingException("Class name is missing", text, range.getStart() + 6);
    return nameRange;
  }

}
