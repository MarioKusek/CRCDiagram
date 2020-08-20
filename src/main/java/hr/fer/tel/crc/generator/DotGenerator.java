package hr.fer.tel.crc.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
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
  private IndentWriter indentWriter;

  private int indent;
  private Map<String, Integer> classMapNameToIndex = new HashMap<>();

  public DotGenerator(Diagram diagram, Writer writer) {
    this.diagram = diagram;
    this.writer = writer;
    this.indentWriter = new IndentWriter(writer);
  }

  public void writeDiagram() throws IOException {
    printPrefix();
    printClasses();
    printConnections();

    printSuffix();
  }

  private void printPrefix() throws IOException {
    println("digraph structs {");
    increseIndent();
    println("node [shape=record];");
  }

  private void printClasses() throws IOException {
    int i = 0;
    for (Class cl: diagram.getClasses()) {
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
    print(escapingString(cl.getName()));
    print(" | {");
    printResponsibilities(cl);
    print(" | ");
    printCollaborators(cl);
    print("}}\"];\n");
  }

  private String escapingString(String string) {
    String escapedString = string.replace("\\", "\\\\");
    escapedString = escapedString.replace("\n", "\\l");
    escapedString = escapedString.replace("\"", "\\\"");

    return escapedString;
  }

  private void printResponsibilities(Class cl) throws IOException {
    print(cl.getResponsibilities().stream()
        .map(r -> "- " + escapingString(r.getText()))
        .collect(Collectors.joining("\\l")));
  }

  private void printCollaborators(Class cl) throws IOException {
    print(cl.getResponsibilities().stream()
        .map(this::createCollaboratorString)
        .collect(Collectors.joining("\\l")));
  }

  private String createCollaboratorString(Responsibility r) {
    String collaboratorString;

    if(r.getCollaborator() == null)
      collaboratorString = "";
    else
      collaboratorString = escapingString(r.getCollaborator());

    String linesString = "\\l".repeat((int)(r.getText().lines().count()) - 1);

    return collaboratorString + linesString;
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
              printBidirectionalConnection(printedConnections, cl, second);
            } else {
              printOneWayConnection(printedConnections, cl, second);
            }
          }
        }
      }
    }
  }

  private void printOneWayConnection(Set<Pair> printedConnections, Class classFrom, Class classTo) throws IOException {
    printIndent();
    print("cl");
    print(classMapNameToIndex.get(classFrom.getName()).toString());
    print(" -> cl");
    print(classMapNameToIndex.get(classTo.getName()).toString());
    println();
    printedConnections.add(new Pair(classFrom, classTo));
  }

  private void printBidirectionalConnection(Set<Pair> printedConnections, Class firstClass, Class secondClass)
      throws IOException {
    printedConnections.add(new Pair(firstClass, secondClass));
    printedConnections.add(new Pair(secondClass, firstClass));
    printIndent();
    print("cl");
    print(classMapNameToIndex.get(firstClass.getName()).toString());
    print(" -> cl");
    print(classMapNameToIndex.get(secondClass.getName()).toString());
    print(" [dir=both]");
    println();
  }

  private boolean isBidirectionalConnection(Class first, Class second) {
    Set<Class> firstCollaborators = extractCollaboratorsForClass(first);
    Set<Class> secondCollaborators = extractCollaboratorsForClass(second);
    return firstCollaborators.contains(second) && secondCollaborators.contains(first);
  }

  private Set<Class> extractCollaboratorsForClass(Class clazz) {
    return clazz.getResponsibilities().stream()
        .map(r -> r.getCollaborator())
        .filter(c -> c != null)
        .map(c -> diagram.getClassByKey(c))
        .collect(Collectors.toSet());
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

  private void println() throws IOException {
    writer.append("\n");
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
