package hr.fer.tel.crc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hr.fer.tel.crc.Class;
import hr.fer.tel.crc.Diagram;

public class DiagramParserTest {
  String input;

  @BeforeEach
  void setup() {
    input = "\n" +
      "class name as n {\n" +
      "    responsibility : collaborator\n" +
      "    ...\n" +
      "}\n" +
      "\n" +
      "class name2 as n2 {\n" +
      "    responsibility2 : collaborator2\n" +
      "    ...\n" +
      "}\n";
  }

  @Test
  void parseDiagram() throws Exception {


    DiagramParser parser = new DiagramParser(input);
    Diagram diagram = parser.parse();

    assertThat(diagram.getClassByKey("n")).isNotNull();
    assertThat(diagram.getClassByKey("name")).isNotNull();
    assertThat(diagram.getClassByKey("n2")).isNotNull();
    assertThat(diagram.getClassByKey("name2")).isNotNull();
  }

  @Test
  void extractAllClasses() throws Exception {
    DiagramParser parser = new DiagramParser(input);
    Diagram diagram = parser.parse();

    List<Class> allClasses = diagram.getClasses();

    assertThat(allClasses).hasSize(2);
    assertThat(allClasses.get(0).getName()).isEqualTo("name");
    assertThat(allClasses.get(1).getName()).isEqualTo("name2");
  }

}
