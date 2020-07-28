package hr.fer.tel.crc.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import hr.fer.tel.crc.Class;
import hr.fer.tel.crc.Diagram;

public class DotGenerator {

  private Diagram diagram;
  private Writer writer;

  private int indent = 0;
  private Map<String, Integer> classMapNameToIndex = new HashMap<>();

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

  private void printClasses() throws IOException {
    int i = 0;
    for (Iterator<Class> iterator = diagram.getClasses().iterator(); iterator.hasNext();) {
      Class cl = iterator.next();
      classMapNameToIndex.put(cl.getName(), i);
      classMapNameToIndex.put(cl.getAlias(), i);
      printClass(cl, i);
      i++;
    }
  }

  private void printClass(Class cl, Integer index) throws IOException {
    printIndent();
    print("cl");
    print(index.toString());
    print(" [label=\"{");
    print(cl.getName());
    print(" | {");
    printResponsibilities(cl);
    print("} | {");
    printCollaborators(cl);
    print("}}\"];\n");
  }

  private void printResponsibilities(Class cl) throws IOException {
    print(cl.getResponsibilities().stream()
        .map(r -> "- " + r.getText())
        .collect(Collectors.joining("\\l")));
  }

  private void printCollaborators(Class cl) {
    // TODO not implemented
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
    print(string);
    writer.append("\n");

  }

  private void print(String string) throws IOException {
    writer.append(string);
  }

  private void printIndent() throws IOException {
    for(int i=0; i < indent * 2; i++)
      writer.append(" ");
  }
}
