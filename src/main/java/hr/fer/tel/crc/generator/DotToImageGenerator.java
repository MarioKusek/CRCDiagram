package hr.fer.tel.crc.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DotToImageGenerator {

  private DotToImageGenerator() {
  }

  public static void generate(String graph, String outputFile, FileFormat fileFormat, String pathToDot)
      throws IOException, InterruptedException {
    ProcessBuilder builder;

    String[] graphvizArguments = generateShellCommand(outputFile, fileFormat);
    runGraphviz(graph, pathToDot, graphvizArguments);
  }

  private static void runGraphviz(String graph, String pathToDot, String[] graphvizArguments)
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

  private static String[] generateShellCommand(String outputFile, FileFormat fileFormat) {
    final String shell = System.getenv("SHELL");
    String graphvizArguments[] = new String[] {shell, "-c", "dot -o" + outputFile + " -T " + fileFormat.getFormatText()};
    // TODO this arguments should be in debug logger
    return graphvizArguments;
  }

}
