package hr.fer.tel.crc.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import hr.fer.tel.crc.Class;
import hr.fer.tel.crc.Diagram;
import hr.fer.tel.crc.Responsibility;
import lombok.Data;

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

  private void printCollaborators(Class cl) throws IOException {
    print(cl.getResponsibilities().stream()
        .map(r -> r.getCollaborator() == null ? "" : r.getCollaborator())
        .collect(Collectors.joining("\\l")));
  }

  @Data
  private static class Pair {
    private final Class first;
    private final Class second;
  }

  private void printConnections() throws IOException {
    print("\n");

    Set<Pair> printedConnections = new HashSet<>();

    for(Class cl: diagram.getClasses()) {
      for(Responsibility r: cl.getResponsibilities()) {
        String collaborator = r.getCollaborator();
        if(collaborator != null) {
          Class second = diagram.getClassByKey(collaborator);
          if(!isPrintedConnection(printedConnections, cl, second)) {
            if(isBidirectionalConnection(cl, second)) {
              println("cl" + classMapNameToIndex.get(cl.getName()) + " <--> cl" + classMapNameToIndex.get(second.getName()));
            } else {
              println("cl" + classMapNameToIndex.get(cl.getName()) + " --> cl" + classMapNameToIndex.get(second.getName()));
            }
          }
        }
      }
    }
  }

  private boolean isBidirectionalConnection(Class first, Class second) {
    return false; // TODO fix this
  }

  private boolean isPrintedConnection(Set<Pair> printedConnections, Class cl, Class second) {
    return printedConnections.contains(new Pair(cl, second));
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
