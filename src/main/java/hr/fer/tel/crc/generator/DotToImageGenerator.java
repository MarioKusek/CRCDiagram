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

    final String shell = System.getenv("SHELL");
    builder = new ProcessBuilder(shell, "-c", "dot -o" + outputFile + " -T " + fileFormat.getFormatText());

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
      System.out.println("Running Graphviz dot command: " + line);
      line = reader.readLine();
    }

    dotProcess.waitFor();

    if(dotProcess.exitValue() != 0)
      System.exit(dotProcess.exitValue());
  }

}
