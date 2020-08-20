package hr.fer.tel.crc.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  private TestExitException exitException;
  private StringWriter writer;
  private String[] parameters;

  class Converter implements CrcDiagramConverter {
    @Override
    public void convertToImage(String inputFile, String outputFile, FileFormat format, String dotPath)
        throws IOException, InterruptedException {
          ArgumentParsingTest.this.inputFile = inputFile;
          ArgumentParsingTest.this.outputFile = outputFile;
          ArgumentParsingTest.this.format = format;
          ArgumentParsingTest.this.dotPath = dotPath;
    }
  }

  class TestExitException extends RuntimeException {
    int exitCode = 0;

    public TestExitException(int exitCode) {
      this.exitCode = exitCode;
    }
  }

  @BeforeEach
  void setup() {
    converter = new Converter();
    writer = new StringWriter();
    app = new CrcDiagramApplication(new PrintWriter(writer, true), converter) {

      @Override
      void exitApp(int code) {
        throw new TestExitException(code);
      }
    };
  }

  @Test
  void help() throws Exception {
    app.parseInput(Arrays.array("-h"));

    assertThat(writer).hasToString(getHelpMessage());
  }

  private String getHelpMessage() {
    StringWriter sw = new StringWriter();
    CrcDiagramApplication a = new CrcDiagramApplication(new PrintWriter(sw, true), null);
    a.printHelp();
    return sw.toString();
  }

  @Test
  void inputFileOptionMissing() throws Exception {
    parameters = Arrays.array();

    exitException = assertThrows(TestExitException.class,  () -> app.parseInput(parameters));

    assertThat(writer).hasToString("-i is required option\n\n" + getHelpMessage());
    assertThat(exitException.exitCode).isEqualTo(1);
  }

  @Test
  void inputFileMissing() throws Exception {
    parameters = Arrays.array("-i");

    exitException = assertThrows(TestExitException.class, () -> app.parseInput(parameters));
    app.convert();

    assertThat(writer).hasToString("Missing argument for option: i\n\n" + getHelpMessage());
    assertThat(exitException.exitCode).isEqualTo(100);
  }

  @Test
  void inputFileExtractedButOutputIsMissing() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc");

    assertThrows(TestExitException.class, () -> app.parseInput(parameters));
    app.convert();

    assertThat(inputFile).isEqualTo("someInputFile.crc");
  }

  @Test
  void outputFileOptionMissing() throws Exception {
    parameters = Arrays.array("-i", "someFile");

    exitException = assertThrows(TestExitException.class, () -> app.parseInput(parameters));

    assertThat(writer).hasToString("-o is required option\n\n" + getHelpMessage());
    assertThat(exitException.exitCode).isEqualTo(2);
  }

  @Test
  void outputFileMissing() throws Exception {
    parameters = Arrays.array("-i", "someInput", "-o");

    exitException = assertThrows(TestExitException.class, () -> app.parseInput(parameters));
    app.convert();

    assertThat(writer).hasToString("Missing argument for option: o\n\n" + getHelpMessage());
    assertThat(exitException.exitCode).isEqualTo(100);
  }

  @Test
  void outputFileExtracted() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png");

    app.parseInput(parameters);
    app.convert();

    assertThat(outputFile).isEqualTo("someOutputFile.png");
  }

  @Test
  void defaultFileFormatIsPng() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png");

    app.parseInput(parameters);
    app.convert();

    assertThat(format).isEqualTo(FileFormat.PNG);
  }

  @Test
  void fileFormatMissingOption() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png", "-f");

    exitException = assertThrows(TestExitException.class, () -> app.parseInput(parameters));
    app.convert();

    assertThat(writer).hasToString("Missing argument for option: f\n\n" + getHelpMessage());
    assertThat(exitException.exitCode).isEqualTo(100);
  }

  @Test
  void fileFormatWrongOption() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png",
        "-f", "wrong");

    exitException = assertThrows(TestExitException.class, () -> app.parseInput(parameters));
    app.convert();

    assertThat(writer).hasToString("Option -f need to have specific values.\n\n" + getHelpMessage());
    assertThat(exitException.exitCode).isEqualTo(3);
  }

  @Test
  void caseInsensitiveFileFormatIsExtracted() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png",
        "-f", "svG");

    app.parseInput(parameters);
    app.convert();

    assertThat(format).isEqualTo(FileFormat.SVG);
  }

  @Test
  void defaultDotPathIsNull() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png");

    app.parseInput(parameters);
    app.convert();

    assertThat(dotPath).isNull();
  }

  @Test
  void dotPathMissingOption() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png", "-dotPath");

    exitException = assertThrows(TestExitException.class, () -> app.parseInput(parameters));
    app.convert();

    assertThat(writer).hasToString("Missing argument for option: dotPath\n\n" + getHelpMessage());
    assertThat(exitException.exitCode).isEqualTo(100);
  }

  @Test
  void dotPathIsExtracted() throws Exception {
    parameters = Arrays.array("-i", "someInputFile.crc", "-o", "someOutputFile.png",
        "-dotPath", "/some/path");

    app.parseInput(parameters);
    app.convert();

    assertThat(dotPath).isEqualTo("/some/path");
  }

}
