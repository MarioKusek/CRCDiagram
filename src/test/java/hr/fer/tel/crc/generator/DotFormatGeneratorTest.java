package hr.fer.tel.crc.generator;

import java.io.StringWriter;
import java.util.List;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import hr.fer.tel.crc.Class;
import hr.fer.tel.crc.Diagram;

class DotFormatGeneratorTest {

  private StringWriter writer = new StringWriter();
  private Diagram diagram;
  private DotGenerator generator;

  @Test
  void emptyDiagram() throws Exception {
    diagram = new Diagram(List.of());
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

  @Test
  void oneClassWithoutResponsibilities() throws Exception {
    diagram = new Diagram(List.of(new Class("className1")));
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

}