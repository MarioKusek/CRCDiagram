package hr.fer.tel.crc.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
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
  private Set<ClassConnection> printedConnections;

  public DotGenerator(Diagram diagram, Writer writer) {
    this.diagram = diagram;
    this.writer = new IndentWriter(writer);
    this.printedConnections = new HashSet<>();
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
    for (final Class cl: diagram.getClasses()) {
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

    final String linesString = "\\l".repeat((int)(r.getText().lines().count()) - 1);

    return collaboratorString + linesString;
  }

  @Data
  private static class ClassConnection {
    private final Class primaryClass;
    private final Class collaboratorClass;

    public ClassConnection reverse() {
      return new ClassConnection(collaboratorClass, primaryClass);
    }
  }

  private void printConnections() throws IOException {
    printedConnections.clear();
    writer.print("\n");

    for(final ClassConnection connection: getAllConnections())
      if(connectionNotPrinted(connection)) {
        printConnection(connection);
      }
  }

  private Collection<ClassConnection> getAllConnections() {
    final Collection<ClassConnection> connections = new ArrayList<>();

    for(final Class cl: diagram.getClasses()) {
      for(final Responsibility r: cl.getResponsibilities()) {
        if(r.hasCollaborator()) {
          final String collaboratorName = r.getCollaborator();
          final Class collaboratorClass = diagram.getClassByKey(collaboratorName);
          connections.add(new ClassConnection(cl, collaboratorClass));
        }
      }
    }
    return connections;
  }

  private void printConnection(final ClassConnection connection)
      throws IOException {
    if(diagram.isBidirectionalConnection(connection.getPrimaryClass(), connection.getCollaboratorClass())) {
      printBidirectionalConnection(connection);
    } else {
      printOneWayConnection(connection);
    }
  }

  private void printOneWayConnection(ClassConnection connection) throws IOException {
    writer.printIndent();
    writer.print("cl");
    writer.print(classMapNameToIndex.get(connection.getPrimaryClass().getName()).toString());
    writer.print(" -> cl");
    writer.print(classMapNameToIndex.get(connection.getCollaboratorClass().getName()).toString());
    writer.println();
    printedConnections.add(connection);
  }

  private void printBidirectionalConnection(ClassConnection connection)
      throws IOException {
    printedConnections.add(connection);
    printedConnections.add(connection.reverse());
    writer.printIndent();
    writer.print("cl");
    writer.print(classMapNameToIndex.get(connection.getPrimaryClass().getName()).toString());
    writer.print(" -> cl");
    writer.print(classMapNameToIndex.get(connection.getCollaboratorClass().getName()).toString());
    writer.print(" [dir=both]");
    writer.println();
  }

  private boolean connectionNotPrinted(ClassConnection connection) {
    return !printedConnections.contains(connection);
  }

  private void printSuffix() throws IOException {
    writer.decreseIndent();
    writer.println("}");
  }
}
