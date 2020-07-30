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
    app.parseInput(Arrays.array("-h"));

    assertThat(writer.toString()).isEqualTo(getHelpMessage());
    assertThat(exitCode).isEqualTo(0);
  }

  private String getHelpMessage() {
    StringWriter sw = new StringWriter();
    CrcDiagramApplication a = new CrcDiagramApplication(new PrintWriter(sw, true), null);
    a.printHelp();
    return sw.toString();
  }

  @Test
  void inputOptionMissing() throws Exception {
    app.parseInput(Arrays.array());

    String printedText = writer.toString();
    assertThat(printedText).startsWith("-i is required option\n");
    assertThat(printedText).endsWith(getHelpMessage());
    assertThat(exitCode).isEqualTo(1);

  }

  @Test
  void inputFileExtracted() throws Exception {
    app.parseInput(Arrays.array("-i", "someInputFile.crc"));
    app.convert();

    assertThat(inputFile).isEqualTo("someInputFile.crc");
    assertThat(exitCode).isEqualTo(0);
  }
}
