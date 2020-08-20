package hr.fer.tel.crc.generator;

import java.io.IOException;
import java.io.Writer;

public class IndentWriter {

  private Writer writer;
  private int indent;

  public IndentWriter(Writer writer) {
    this.writer = writer;
  }

  public void println() throws IOException {
    writer.append("\n");
  }

  public void println(String string) throws IOException {
    printIndent();
    print(string);
    writer.append("\n");
  }

  public void print(String string) throws IOException {
    writer.append(string);
  }

  public void printIndent() throws IOException {
    for(int i=0; i < indent * 2; i++)
      writer.append(" ");
  }

  public void decreseIndent() {
    indent--;
  }

  public void increseIndent() {
    indent++;
  }

}
