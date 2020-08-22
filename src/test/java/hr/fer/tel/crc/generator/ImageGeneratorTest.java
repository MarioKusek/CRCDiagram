package hr.fer.tel.crc.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hr.fer.tel.approval.ImageDiffWebReporter;

@UseReporter({ImageDiffWebReporter.class})
class ImageGeneratorTest {

  private Path tempDir;
  private Path outputFile;
  private String exampleOfDotFormat;

  @BeforeEach
  void setup() throws Exception {
    tempDir = Files.createTempDirectory("dotDir");
    outputFile = Path.of(tempDir.toAbsolutePath().toString(), "test.png");
    exampleOfDotFormat = "digraph structs {\n" +
        "  node [shape=record];\n" +
        "  cl0 [label=\"{className1 | {- c1 resp1\\l- c1 resp2 | className2\\lclassName2}}\"];\n" +
        "  cl1 [label=\"{className2 | {- c2 resp1\\l- c2 resp2 | \\lclassName3}}\"];\n" +
        "  cl2 [label=\"{className3 | {- c3 resp1\\l- c3 resp2 | className1\\l}}\"];\n" +
        "\n" +
        "  cl0 -> cl1\n" +
        "  cl1 -> cl2\n" +
        "  cl2 -> cl0\n" +
        "}\n";
  }

  @Test
  void imageGeneration() throws Exception {
    new DotToImageGenerator().generate(
        exampleOfDotFormat,
        outputFile.toString(),
        FileFormat.PNG,
        "/usr/local/bin");

    Approvals.verify(outputFile.toFile());
  }

  // TODO add test with debug writer
  @Test
  void debugLoggingCommand() throws Exception {
    StringWriter sw = new StringWriter();
    IndentWriter iw = new IndentWriter(sw);

    new DotToImageGenerator(iw).generate(
        exampleOfDotFormat,
        outputFile.toString(),
        FileFormat.PNG,
        "/usr/local/bin");

    List<String> debugOutput = sw.toString().lines().collect(Collectors.toList());

    assertThat(debugOutput).hasSize(2);
    assertThat(debugOutput.get(0)).isEqualTo("DEBUG: Running following commandline:");
    assertThat(debugOutput.get(1)).startsWith("  /bin/zsh -c dot -o ");
    assertThat(debugOutput.get(1)).endsWith("test.png -T png");
  }


  public static void main(String[] args) throws IOException, InterruptedException {
    String inputFile = "test.dot";
    String graph = Files.readString(Path.of(inputFile), StandardCharsets.UTF_8);
    String outputFile = "test.png";
    FileFormat fileFormat = FileFormat.PNG;
    String pathToDot = "/usr/local/bin";

    new DotToImageGenerator().generate(graph, outputFile, fileFormat, pathToDot);
  }
}

