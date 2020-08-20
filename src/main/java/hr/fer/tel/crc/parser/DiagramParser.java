package hr.fer.tel.crc.parser;

import java.util.LinkedList;
import java.util.List;

import hr.fer.tel.crc.Class;
import hr.fer.tel.crc.Diagram;

public class DiagramParser {

  private final String text;
  private final List<Class> classes;

  public DiagramParser(String text) {
    this.text = text;
    classes = new LinkedList<>();
  }

  public void parseClasses() {
    StringRange classRange = StringExtractorUtil.extractClass(text, 0);

    while(!classRange.isEmptyRange()) {
      classes.add(new ClassParser(text, classRange).parse());
      classRange = StringExtractorUtil.extractClass(text, classRange.getEnd());
    }
  }

  public Diagram parse() {
    parseClasses();
    return new Diagram(classes);
  }
}
