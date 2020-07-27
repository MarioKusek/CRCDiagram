package hr.fer.tel.crc.parser;

import java.util.LinkedList;
import java.util.List;

import hr.fer.tel.crc.Class;

public class DiagramParser {

  private String text;
  private List<Class> classes;

  public DiagramParser(String text) {
    this.text = text;
    classes = new LinkedList<>();
  }

  public List<Class> parse() {
    StringRange classRange = StringExtractorUtil.extractClass(text, 0);

    while(!classRange.isEmptyRange()) {
      classes.add(new ClassParser(text, classRange).parse());
      classRange = StringExtractorUtil.extractClass(text, classRange.getEnd());
    }

    return classes;
  }
}
