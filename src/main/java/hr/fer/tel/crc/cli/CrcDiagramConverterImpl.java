package hr.fer.tel.crc.cli;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import hr.fer.tel.crc.Diagram;
import hr.fer.tel.crc.generator.DotGenerator;
import hr.fer.tel.crc.generator.DotToImageGenerator;
import hr.fer.tel.crc.generator.FileFormat;
import hr.fer.tel.crc.parser.DiagramParser;

public class CrcDiagramConverterImpl implements CrcDiagramConverter {

  @Override
  public void convertToImage(String inputFile, String outputFile, FileFormat format, String dotPath) throws IOException, InterruptedException {
    final StringWriter sw = new StringWriter();
    final Diagram diagram = new DiagramParser(Files.readString(Path.of(inputFile))).parse();
    new DotGenerator(diagram, sw).printDiagram();
    DotToImageGenerator.generate(sw.toString(), outputFile, format, dotPath == null ? "/usr/local/bin" : dotPath);
  }

}
