package hr.fer.tel.crc.generator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.api.Test;

import hr.fer.tel.approval.ImageDiffWebReporter;

@UseReporter({ImageDiffWebReporter.class})
class ImageGeneratorTest {

  @Test
  void imageGeneration() throws Exception {
    Path tempDir = Files.createTempDirectory("dotDir");
    Path outputFile = Path.of(tempDir.toAbsolutePath().toString(), "test.png");
    new DotToImageGenerator().generate(
        "digraph structs {\n" +
        "  node [shape=record];\n" +
        "  cl0 [label=\"{className1 | {- c1 resp1\\l- c1 resp2 | className2\\lclassName2}}\"];\n" +
        "  cl1 [label=\"{className2 | {- c2 resp1\\l- c2 resp2 | \\lclassName3}}\"];\n" +
        "  cl2 [label=\"{className3 | {- c3 resp1\\l- c3 resp2 | className1\\l}}\"];\n" +
        "\n" +
        "  cl0 -> cl1\n" +
        "  cl1 -> cl2\n" +
        "  cl2 -> cl0\n" +
        "}\n",
        outputFile.toString(),
        FileFormat.PNG,
        "/usr/local/bin");

    Approvals.verify(outputFile.toFile());
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

