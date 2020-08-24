package hr.fer.tel.crc.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class DotToImageGenerator {

  private IndentWriter debugLogger;

  public DotToImageGenerator() {
  }

  public DotToImageGenerator(IndentWriter debugLogger) {
    this.debugLogger = debugLogger;
  }

  public void generate(String graph, String outputFile, FileFormat fileFormat, String pathToDot)
      throws IOException, InterruptedException {
    final String[] graphvizArguments = generateShellCommand(outputFile, fileFormat);
    runGraphviz(graph, pathToDot, graphvizArguments);
  }

  private void runGraphviz(String graph, String pathToDot, String[] graphvizArguments)
      throws IOException, InterruptedException {
    ProcessBuilder builder;
    builder = new ProcessBuilder(graphvizArguments);

    if(pathToDot != null) {
      final Map<String, String> env = builder.environment();
      env.compute("PATH", (k, v) -> v + File.pathSeparator + pathToDot);
    }

    builder.redirectErrorStream(true);
    final Process dotProcess = builder.start();

    if(dotProcess.isAlive()) {
      final OutputStream dotOutputStream = dotProcess.getOutputStream();
      dotOutputStream.write(graph.getBytes(StandardCharsets.UTF_8));
      dotOutputStream.close();
    }

    final BufferedReader reader = new BufferedReader(new InputStreamReader(dotProcess.getInputStream()));
    String line = reader.readLine();
    while (line != null) {
      System.out.println("Graphviz dot command output: " + line);
      line = reader.readLine();
    }

    dotProcess.waitFor();

    if(dotProcess.exitValue() != 0)
      System.exit(dotProcess.exitValue());
  }

  private String[] generateShellCommand(String outputFile, FileFormat fileFormat) throws IOException {
    final String shell = System.getenv("SHELL");
    final String[] graphvizArguments = new String[] {shell, "-c", "dot -o " + outputFile + " -T " + fileFormat.getFormatText()};

    if(debugLogger != null) {
      debugLogger.println("DEBUG: Running following commandline:");
      debugLogger.increseIndent();
      debugLogger.println(Arrays.asList(graphvizArguments).stream().collect(Collectors.joining(" ")));
      debugLogger.decreseIndent();
    }

    return graphvizArguments;
  }

}
