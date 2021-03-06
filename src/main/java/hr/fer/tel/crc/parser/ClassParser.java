package hr.fer.tel.crc.parser;

import java.util.List;
import java.util.stream.Collectors;

import hr.fer.tel.crc.Class;
import hr.fer.tel.crc.Responsibility;

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
    final StringRange bodyRange = findBodyRange();
    final StringRange classDeclarationRange = new StringRange(textRange.getStart(), bodyRange.getStart() - 1);

    final StringRange nameRange = extractClassName(classDeclarationRange);
    final Class crcClass = new Class(unescapeClassName(nameRange.apply(text)));

    final StringRange aliasRange = extractAlias(new StringRange(nameRange.getEnd() + 1, classDeclarationRange.getEnd()));
    crcClass.setAlias(aliasRange.apply(text));

    extractBody(bodyRange).stream().forEach(crcClass::add);

    return crcClass;
  }

  private String unescapeClassName(String string) {
    String unescapedString = string.replace("\\\"", "\"");
    unescapedString = unescapedString.replace("\\n", "\n");
    unescapedString = unescapedString.replace("\\\\", "\\");

    return unescapedString;
  }

  private List<Responsibility> extractBody(StringRange bodyRange) {
    final StringRange bodyContentRange = new StringRange(bodyRange.getStart()+1, bodyRange.getEnd()-1);
    if(bodyContentRange.isEmptyRange())
      return List.of();

    return bodyContentRange.apply(text).lines()
      .filter(l -> !l.isBlank())
      .map(l -> extractResponsibility(l.trim()))
      .collect(Collectors.toList());
  }

  private Responsibility extractResponsibility(String line) {
    final int collaboratorDividerIndex = findCollaboratorDividerIndex(line);
    Responsibility responsibility;
    if(collaboratorDividerIndex == -1) {
      responsibility = new Responsibility(line);
    } else {
      responsibility = new Responsibility(unescapeResponsibility(line.substring(0, collaboratorDividerIndex).trim()));
      responsibility.setCollaborator(line.substring(collaboratorDividerIndex+1).trim());
    }
    return responsibility;
  }

  private String unescapeResponsibility(String string) {
    return string.replace("\\:", ":")
        .replace("\\\\", "\\")
        .replace("\\n", "\n");
  }

  private int findCollaboratorDividerIndex(String line) {
    int index = line.indexOf(':');

    while(index != -1 && line.charAt(index-1) == '\\')
        index = line.indexOf(':', index+1);

    return index;
  }

  private StringRange findBodyRange() {
    final int openCurlyBraceIndex = text.indexOf('{', textRange.getStart());
    if(openCurlyBraceIndex == -1)
      throw new ParsingException("Missing class body", text, textRange.getEnd()+1);

    return StringExtractorUtil.findMatchingCurlyBraceRange(text, openCurlyBraceIndex);
  }

  private StringRange extractAlias(StringRange range) {
    final int asIndex = text.indexOf("as ", range.getStart());
    if(asIndex == -1)
      return new StringRange(-1, -1);

    final StringRange aliasRange = new StringRange(asIndex + 3, text.indexOf(' ', asIndex + 3)-1);
    if(aliasRange.isEmptyRange())
      throw new ParsingException("Missing alias", text, aliasRange.getStart());

    return aliasRange;
  }

  private StringRange extractClassName(StringRange range) {
    if(range.size() < 6)
      throw new ParsingException("Class should start with class keyword", text, range.getStart());

    if(text.charAt(range.getStart() + 6) == '"') {
      final int classNameEndIndex = findEndOfClassName(range.getStart() + 7);
      return new StringRange(range.getStart() + 7, classNameEndIndex-1);
    }

    final StringRange nameRange = new StringRange(range.getStart() + 6, text.indexOf(' ', range.getStart() + 6)-1);
    if(nameRange.isEmptyRange())
      throw new ParsingException("Missing class name", text, range.getStart() + 6);
    return nameRange;
  }

  private int findEndOfClassName(int startIndex) {
    int index = text.indexOf('"', startIndex);

    while(text.charAt(index-1) == '\\')
      index = text.indexOf('"', index+1);

    return index;
  }

}
