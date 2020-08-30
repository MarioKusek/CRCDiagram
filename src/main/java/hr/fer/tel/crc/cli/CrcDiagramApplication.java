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

import hr.fer.tel.crc.ToolVersion;
import hr.fer.tel.crc.generator.FileFormat;
import hr.fer.tel.crc.generator.IndentWriter;

public class CrcDiagramApplication {

  private final CrcDiagramConverter converter;
  private final PrintWriter writer;
  private Options options;

  // parsed values
  private String inputFile;
  private String outputFile;
  private FileFormat format;
  private String dotPath;
  private IndentWriter debugLogger;

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

    options.addOption(Option.builder("v")
        .desc("version")
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

    options.addOption(Option.builder("d")
        .desc("debug")
        .build());

  }

  public void parseInput(String... args) throws ParseException {

    try {
      final CommandLineParser parser = new DefaultParser();
      final CommandLine line = parser.parse(options, args);

      if (line.hasOption("h")) {
        printHelp();
        exitApp(0);
      } else if(line.hasOption("v")) {
        printVersion();
        exitApp(0);
      } else {
        fillInArguments(line);
      }
    } catch (MissingArgumentException e) {
      writer.println(e.getMessage());
      writer.println();
      printHelp();
      exitApp(100);
    }
  }

  private void fillInArguments(CommandLine line) {
    if (line.hasOption("i")) {
        inputFile = line.getOptionValue("i");
    } else {
      writer.println("-i is required option\n");
      printHelp();
      exitApp(1);
    }

    if (line.hasOption("o")) {
      outputFile = line.getOptionValue("o");
    } else {
      writer.println("-o is required option\n");
      printHelp();
      exitApp(2);
    }

    format = FileFormat.PNG; // default file format

    if (line.hasOption("f")) {
      try {
        format = FileFormat.valueOf(line.getOptionValue("f").toUpperCase());
      } catch (IllegalArgumentException e) {
        writer.println("Option -f need to have specific values.\n");
        printHelp();
        exitApp(3);
      }
    }

    if (line.hasOption("dotPath")) {
      dotPath = line.getOptionValue("dotPath");
    }

    if (line.hasOption("d")) {
      debugLogger = new IndentWriter(writer);
    }
  }

  void exitApp(int exitCode) {
    System.exit(exitCode);
  }

  public void convert() throws IOException, InterruptedException {
    converter.setDebugLogger(debugLogger);
    converter.convertToImage(inputFile, outputFile, format, dotPath);
  }

  void printHelp() {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(writer, formatter.getWidth(), "crcDiagram [OPTIONS]", "", options,
        formatter.getLeftPadding(), formatter.getDescPadding(),
        "\nTypical usage: crcDiagram -i input_file -f png -o output_file\n"
            + "If dot can not be found add parameter with path to it \ne.g. \"-dotPath /usr/local/bin\"");
  }

  private void printVersion() {
    writer.print("Version: ");
    writer.println(ToolVersion.VERSION);
    writer.print("Build time: ");
    writer.println(ToolVersion.BUILD_DATE);
    writer.print("Git commit: ");
    writer.println(ToolVersion.GIT_SHA);
    writer.print("Git commit time: ");
    writer.println(ToolVersion.GIT_DATE);
  }

  public static void main(String[] args) throws Exception {
    final CrcDiagramConverter converter = new CrcDiagramConverterImpl();

    final CrcDiagramApplication app = new CrcDiagramApplication(new PrintWriter(System.out, true), converter);
    app.parseInput(args);
    app.convert();
  }
}

