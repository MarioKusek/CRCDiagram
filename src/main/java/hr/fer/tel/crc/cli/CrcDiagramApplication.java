package hr.fer.tel.crc.cli;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hr.fer.tel.crc.generator.FileFormat;

public class CrcDiagramApplication {

  private CrcDiagramConverter converter;
  private PrintWriter writer;
  private Options options;

  // parsed values
  private String inputFile;
  private String outputFile;
  private FileFormat format;
  private String dotPath;

  public CrcDiagramApplication(PrintWriter writer, CrcDiagramConverter converter) {
    this.writer = writer;
    this.converter = converter;
    createOptions();
  }

  private void createOptions() {
    options = new Options();

    options.addOption(Option.builder("h")
      .desc("help")
      .build());
  }

  public void parseInput(String[] args) throws ParseException, IOException, InterruptedException {

    CommandLineParser parser = new DefaultParser();
    CommandLine line = parser.parse(options, args);

    if (line.hasOption("h")) {
      printHelp();
    }

    // TODO
  }

  void exitApp(int exitCode) {
    System.exit(exitCode);
  }

  public void convert() throws IOException, InterruptedException {
    // TODO
  }

  void printHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(writer, formatter.getWidth(), "crcDiagram [OPTIONS]", "", options,
        formatter.getLeftPadding(), formatter.getDescPadding(),
        "\nTypical usage: crcDiagram -i input_file -f png -o output_file\n"
            + "If dot can not be found add parameter with path to it \ne.g. \"-dotPath /usr/local/bin\"");
  }
}

