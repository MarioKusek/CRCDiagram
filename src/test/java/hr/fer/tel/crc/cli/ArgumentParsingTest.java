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
  void inputFileOptionMissing() throws Exception {
    app.parseInput(Arrays.array());

    String printedText = writer.toString();
    assertThat(printedText).isEqualTo("-i is required option\n\n" + getHelpMessage());
    assertThat(exitCode).isEqualTo(1);
  }

  @Test
  void inputFileMissing() throws Exception {
    app.parseInput(Arrays.array("-i"));
    app.convert();

    String printedText = writer.toString();
    assertThat(printedText).isEqualTo(
        "Missing argument for option: i\n\n" + getHelpMessage());
    assertThat(exitCode).isEqualTo(100);
  }

  @Test
  void inputFileExtractedButOutputIsMissing() throws Exception {
    app.parseInput(Arrays.array("-i", "someInputFile.crc"));
    app.convert();

    assertThat(inputFile).isEqualTo("someInputFile.crc");
    assertThat(exitCode).isNotEqualTo(0);
  }

  @Test
  void outputFileOptionMissing() throws Exception {
    app.parseInput(Arrays.array("-i", "someFile"));

    String printedText = writer.toString();
    assertThat(printedText).isEqualTo("-o is required option\n\n" + getHelpMessage());
    assertThat(exitCode).isEqualTo(2);
  }

  void outputFileMissing() throws Exception {
    app.parseInput(Arrays.array("-i", "someInput", "-o"));
    app.convert();

    String printedText = writer.toString();
    assertThat(printedText).isEqualTo(
        "Missing argument for option: o\n\n" + getHelpMessage());
    assertThat(exitCode).isEqualTo(100);
  }

  @Test
  void outputFileExtracted() throws Exception {
    app.parseInput(Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png"));
    app.convert();

    assertThat(outputFile).isEqualTo("someOutputFile.png");
    assertThat(exitCode).isEqualTo(0);
  }

  @Test
  void defaultFileFormatIsPng() throws Exception {
    app.parseInput(Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png"));
    app.convert();

    assertThat(format).isEqualTo(FileFormat.PNG);
    assertThat(exitCode).isEqualTo(0);
  }

  @Test
  void fileFormatMissingOption() throws Exception {
    app.parseInput(Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png", "-f"));
    app.convert();

    String printedText = writer.toString();
    assertThat(printedText).isEqualTo(
        "Missing argument for option: f\n\n" + getHelpMessage());
    assertThat(exitCode).isEqualTo(100);
  }

  @Test
  void fileFormatWrongOption() throws Exception {
    app.parseInput(Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png",
        "-f", "wrong"));
    app.convert();

    String printedText = writer.toString();
    assertThat(printedText).isEqualTo(
        "Option -f need to have specific values.\n\n" + getHelpMessage());
    assertThat(exitCode).isEqualTo(3);
  }

  @Test
  void defaultDotPathIsNull() throws Exception {
    app.parseInput(Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png"));
    app.convert();

    assertThat(dotPath).isNull();
    assertThat(exitCode).isEqualTo(0);
  }


}
