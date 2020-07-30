package hr.fer.tel.crc.cli;

import java.io.IOException;

import hr.fer.tel.crc.generator.FileFormat;

public interface CrcDiagramConverter {

  void convertToImage(String inputFile, String outputFile, FileFormat format, String dotPath) throws IOException, InterruptedException;

}
