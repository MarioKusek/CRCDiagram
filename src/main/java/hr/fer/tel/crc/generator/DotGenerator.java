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
  private IndentWriter writer;

  private Map<String, Integer> classMapNameToIndex = new HashMap<>();

  public DotGenerator(Diagram diagram, Writer writer) {
    this.diagram = diagram;
    this.writer = new IndentWriter(writer);
  }

  public void printDiagram() throws IOException {
    printPrefix();
    printClasses();
    printConnections();
    printSuffix();
  }

  private void printPrefix() throws IOException {
    writer.println("digraph structs {");
    writer.increseIndent();
    writer.println("node [shape=record];");
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
    writer.printIndent();
    writer.print("cl");
    writer.print(index.toString());
    writer.print(" [label=\"{");
    writer.print(escapingString(cl.getName()));
    writer.print(" | {");
    printResponsibilities(cl);
    writer.print(" | ");
    printCollaborators(cl);
    writer.print("}}\"];\n");
  }

  private String escapingString(String string) {
    String escapedString = string.replace("\\", "\\\\");
    escapedString = escapedString.replace("\n", "\\l");
    escapedString = escapedString.replace("\"", "\\\"");

    return escapedString;
  }

  private void printResponsibilities(Class cl) throws IOException {
    writer.print(cl.getResponsibilities().stream()
    .map(r -> "- " + escapingString(r.getText()))
    .collect(Collectors.joining("\\l")));
  }

  private void printCollaborators(Class cl) throws IOException {
    writer.print(cl.getResponsibilities().stream()
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
    writer.print("\n");

    Set<Pair> printedConnections = new HashSet<>();

    for(Class cl: diagram.getClasses()) {
      for(Responsibility r: cl.getResponsibilities()) {
        String collaborator = r.getCollaborator();
        if(collaborator != null) {
          Class second = diagram.getClassByKey(collaborator);
          if(!isPrintedConnection(printedConnections, cl, second)) {
            if(diagram.isBidirectionalConnection(cl, second)) {
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
    writer.printIndent();
    writer.print("cl");
    writer.print(classMapNameToIndex.get(classFrom.getName()).toString());
    writer.print(" -> cl");
    writer.print(classMapNameToIndex.get(classTo.getName()).toString());
    writer.println();
    printedConnections.add(new Pair(classFrom, classTo));
  }

  private void printBidirectionalConnection(Set<Pair> printedConnections, Class firstClass, Class secondClass)
      throws IOException {
    printedConnections.add(new Pair(firstClass, secondClass));
    printedConnections.add(new Pair(secondClass, firstClass));
    writer.printIndent();
    writer.print("cl");
    writer.print(classMapNameToIndex.get(firstClass.getName()).toString());
    writer.print(" -> cl");
    writer.print(classMapNameToIndex.get(secondClass.getName()).toString());
    writer.print(" [dir=both]");
    writer.println();
  }

  private boolean isPrintedConnection(Set<Pair> printedConnections, Class cl, Class second) {
    return printedConnections.contains(new Pair(cl, second));
  }

  private void printSuffix() throws IOException {
    writer.decreseIndent();
    writer.println("}");
  }
}
