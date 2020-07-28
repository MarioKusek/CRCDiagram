package hr.fer.tel.crc.generator;

import java.io.IOException;
import java.io.Writer;

import hr.fer.tel.crc.Diagram;

public class DotGenerator {

  private Diagram diagram;
  private Writer writer;

  private int indent = 0;

  public DotGenerator(Diagram diagram, Writer writer) {
    this.diagram = diagram;
    this.writer = writer;
  }

  public void writeDiagram() throws IOException {
    printPrefix();
    printClasses();
    printConnections();

    printSuffix();

    this.writer = null;
  }

  private void printPrefix() throws IOException {
    println("digraph structs {");
    increseIndent();
    println("node [shape=record];");
  }

  private void printClasses() {
    // TODO print classes
  }

  private void printConnections() {
    // TODO print connections
  }

  private void decreseIndent() {
    indent--;
  }

  private void printSuffix() throws IOException {
    decreseIndent();
    println("}");
  }

  private void increseIndent() {
    indent++;
  }

  private void println(String string) throws IOException {
    printIndent();
    writer.append(string);
    writer.append("\n");

  }

  private void printIndent() throws IOException {
    for(int i=0; i < indent * 2; i++)
      writer.append(" ");
  }
}
