package hr.fer.tel.crc.cli;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import hr.fer.tel.crc.Diagram;
import hr.fer.tel.crc.generator.DotGenerator;
import hr.fer.tel.crc.generator.DotToImageGenerator;
import hr.fer.tel.crc.generator.FileFormat;
import hr.fer.tel.crc.generator.IndentWriter;
import hr.fer.tel.crc.parser.DiagramParser;

public class CrcDiagramConverterImpl implements CrcDiagramConverter {
  private IndentWriter debugLogger;

  @Override
  public void setDebugLogger(IndentWriter debugLogger) {
    this.debugLogger = debugLogger;
  }

  @Override
  public void convertToImage(String inputFile, String outputFile, FileFormat imageFormat, String dotPath) throws IOException, InterruptedException {
    final Diagram diagram = parseDiagramFromFile(inputFile);

    final StringWriter dotFormatWriter = new StringWriter();
    convertDiagramToGraphvizFormat(dotFormatWriter, diagram);

    final String diagramInDotFormat = dotFormatWriter.toString();
    new DotToImageGenerator().generate(diagramInDotFormat, outputFile, imageFormat, dotPath == null ? "/usr/local/bin" : dotPath);
  }

  private void convertDiagramToGraphvizFormat(final StringWriter sw, final Diagram diagram) throws IOException {
    new DotGenerator(diagram, sw, debugLogger).printDiagram();
  }

  private Diagram parseDiagramFromFile(String inputFile) throws IOException {
    final Diagram diagram = new DiagramParser(Files.readString(Path.of(inputFile))).parse();
    return diagram;
  }

}
