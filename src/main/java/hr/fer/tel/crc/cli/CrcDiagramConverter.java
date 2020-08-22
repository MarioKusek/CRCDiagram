package hr.fer.tel.crc.cli;

import java.io.IOException;

import hr.fer.tel.crc.generator.FileFormat;
import hr.fer.tel.crc.generator.IndentWriter;

public interface CrcDiagramConverter {

  void convertToImage(String inputFile, String outputFile, FileFormat format, String dotPath) throws IOException, InterruptedException;

  void setDebugLogger(IndentWriter debugLogger);

}
