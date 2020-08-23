package hr.fer.tel.crc.generator;

import java.io.IOException;
import java.io.Writer;

public class IndentWriter {

  private final Writer writer;
  private int indent;

  public IndentWriter(Writer writer) {
    this.writer = writer;
  }

  public void println() throws IOException {
    print("\n");
  }

  public void println(String string) throws IOException {
    printIndent();
    print(string);
    println();
  }

  public void print(String string) throws IOException {
    writer.append(string);
    writer.flush();
  }

  public void printIndent() throws IOException {
    for(int i=0; i < indent * 2; i++)
      print(" ");
  }

  public void decreseIndent() {
    indent--;
  }

  public void increseIndent() {
    indent++;
  }

}
