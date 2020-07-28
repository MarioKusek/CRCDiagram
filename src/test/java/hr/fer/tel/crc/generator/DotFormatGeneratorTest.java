package hr.fer.tel.crc.generator;

import java.io.StringWriter;
import java.util.List;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import hr.fer.tel.crc.Class;
import hr.fer.tel.crc.Diagram;
import hr.fer.tel.crc.Responsibility;

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

  @Test
  void twoClassesWithoutResponsibilities() throws Exception {
    diagram = new Diagram(List.of(new Class("className1"), new Class("secondClassName")));
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

  @Test
  void oneClassWithResponsibility() throws Exception {
    diagram = new Diagram(List.of(
        Class.builder()
          .name("className1")
          .responsibility(new Responsibility("c1 resp1"))
          .build()));
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

  @Test
  void twoClassesWithResponsibilities() throws Exception {
    diagram = new Diagram(List.of(
      Class.builder()
        .name("className1")
        .responsibility(new Responsibility("c1 resp1"))
        .build(),
      Class.builder()
        .name("className2")
        .responsibility(new Responsibility("c2 resp1"))
        .build()
    ));
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

  @Test
  void oneClasseWithTwoResponsibilities() throws Exception {
    diagram = new Diagram(List.of(
      Class.builder()
        .name("className1")
        .responsibility(new Responsibility("c1 resp1"))
        .responsibility(new Responsibility("c1 resp2"))
        .build()
    ));
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

  @Test
  void twoClassesWithTwoResponsibilities() throws Exception {
    diagram = new Diagram(List.of(
      Class.builder()
        .name("className1")
        .responsibility(new Responsibility("c1 resp1"))
        .responsibility(new Responsibility("c1 resp2"))
        .build(),
      Class.builder()
        .name("className2")
        .responsibility(new Responsibility("c2 resp1"))
        .responsibility(new Responsibility("c2 resp2"))
        .build()
    ));
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

  @Test
  void generatingUnidirectionalConnections() throws Exception {
    diagram = new Diagram(List.of(
        Class.builder()
          .name("className1")
          .responsibility(new Responsibility("c1 resp1", "className2"))
          .responsibility(new Responsibility("c1 resp2", "className2"))
          .build(),
        Class.builder()
          .name("className2")
          .responsibility(new Responsibility("c2 resp1"))
          .responsibility(new Responsibility("c2 resp2", "className3"))
          .build(),
        Class.builder()
          .name("className3")
          .responsibility(new Responsibility("c3 resp1", "className1"))
          .responsibility(new Responsibility("c3 resp2"))
          .build()
        ));
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

  @Test
  void generatingBidirectionalConnections() throws Exception {
    diagram = new Diagram(List.of(
        Class.builder()
          .name("className1")
          .alias("a1")
          .responsibility(new Responsibility("c1 resp1", "className2"))
          .responsibility(new Responsibility("c1 resp2"))
          .build(),
        Class.builder()
          .name("className2")
          .responsibility(new Responsibility("c2 resp1"))
          .responsibility(new Responsibility("c2 resp2", "a1"))
          .build()
        ));
    generator = new DotGenerator(diagram, writer);

    generator.writeDiagram();

    Approvals.verify(writer.toString());
  }

  // TODO escaping text in class name, responsibility and collaborators
  /*
   * \\ --> \\
   * \: --> :
   * \n --> \l
   * " --> \"
   */
}
