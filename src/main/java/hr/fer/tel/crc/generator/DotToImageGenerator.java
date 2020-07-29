package hr.fer.tel.crc.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DotToImageGenerator {

  public static void generate(String graph, String outputFile, FileFormat fileFormat, String pathToDot)
      throws IOException, InterruptedException {
    ProcessBuilder builder;
  
    String shell = System.getenv("SHELL");
    builder = new ProcessBuilder(shell, "-c", "dot -o" + outputFile + " -T " + fileFormat.getFormatText());
  
    if(pathToDot != null) {
      Map<String, String> env = builder.environment();
      env.compute("PATH", (k, v) -> {
        return v + File.pathSeparator + pathToDot;
      });
    }
  
    builder.redirectErrorStream(true);
    Process dotProcess = builder.start();
  
    if(dotProcess.isAlive()) {
      OutputStream dotOutputStream = dotProcess.getOutputStream();
      dotOutputStream.write(graph.getBytes(StandardCharsets.UTF_8));
      dotOutputStream.close();
    }
  
    BufferedReader reader = new BufferedReader(new InputStreamReader(dotProcess.getInputStream()));
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
