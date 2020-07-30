package hr.fer.tel.crc.cli;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
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

    options.addOption(Option.builder("i")
      .desc("input file in CRC format")
      .hasArg()
      .argName("file")
      .build());

    options.addOption(Option.builder("o")
        .desc("output file - generated image")
        .hasArg()
        .argName("file")
        .build());

    options.addOption(Option.builder("f")
        .desc("image format - png (default) or svg")
        .hasArg()
        .argName("format")
        .build());

    options.addOption(Option.builder("dotPath")
        .desc("path to dot command")
        .hasArg()
        .argName("path")
        .build());

  }

  public void parseInput(String[] args) throws ParseException, IOException, InterruptedException {

    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine line = parser.parse(options, args);

      if (line.hasOption("h")) {
        printHelp();
      } else {
        if (!line.hasOption("i")) {
          writer.println("-i is required option\n");
          printHelp();
          exitApp(1);
          return;
        } else {
            inputFile = line.getOptionValue("i");
        }

        if (!line.hasOption("o")) {
          writer.println("-o is required option\n");
          printHelp();
          exitApp(2);
          return;
        } else {
          outputFile = line.getOptionValue("o");
        }

        format = FileFormat.PNG; // default file format

        if (line.hasOption("f")) {
          try {
            format = FileFormat.valueOf(line.getOptionValue("f").toUpperCase());
          } catch (IllegalArgumentException e) {
            writer.println("Option -f need to have specific values.\n");
            printHelp();
            exitApp(3);
            return;
          }
        }

      }
    } catch (MissingArgumentException e) {
      writer.println(e.getMessage());
      writer.println();
      printHelp();
      exitApp(100);
    }
  }

  void exitApp(int exitCode) {
    System.exit(exitCode);
  }

  public void convert() throws IOException, InterruptedException {
    converter.convertToImage(inputFile, outputFile, format, dotPath);
  }

  void printHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(writer, formatter.getWidth(), "crcDiagram [OPTIONS]", "", options,
        formatter.getLeftPadding(), formatter.getDescPadding(),
        "\nTypical usage: crcDiagram -i input_file -f png -o output_file\n"
            + "If dot can not be found add parameter with path to it \ne.g. \"-dotPath /usr/local/bin\"");
  }
}

