package hr.fer.tel.crc.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hr.fer.tel.crc.generator.FileFormat;

class ArgumentParsingTest {
  private CrcDiagramApplication app;
  private Converter converter;

  private String inputFile;
  private String outputFile;
  private FileFormat format;
  private String dotPath;
  private boolean convertCalled = false;

  private int exitCode = 0;
  private StringWriter writer;

  class Converter implements CrcDiagramConverter {
    @Override
    public void convertToImage(String inputFile, String outputFile, FileFormat format, String dotPath)
        throws IOException, InterruptedException {
          ArgumentParsingTest.this.inputFile = inputFile;
          ArgumentParsingTest.this.outputFile = outputFile;
          ArgumentParsingTest.this.format = format;
          ArgumentParsingTest.this.dotPath = dotPath;
          convertCalled = true;
    }
  }

  @BeforeEach
  void setup() {
    converter = new Converter();
    writer = new StringWriter();
    app = new CrcDiagramApplication(new PrintWriter(writer, true), converter) {

      @Override
      void exitApp(int code) {
        exitCode = code;
      }
    };
  }

  @Test
  void help() throws Exception {
    StringWriter sw = new StringWriter();
    CrcDiagramApplication a = new CrcDiagramApplication(new PrintWriter(sw, true), null);
    a.printHelp();

    app.parseInput(Arrays.array("-h"));

    assertThat(writer.toString()).isEqualTo(sw.toString());
  }

}
