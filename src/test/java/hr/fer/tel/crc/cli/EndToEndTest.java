package hr.fer.tel.crc.cli;

import java.nio.file.Files;
import java.nio.file.Path;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.UseReporter;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;

import hr.fer.tel.approval.ImageDiffWebReporter;

@UseReporter(ImageDiffWebReporter.class)
class EndToEndTest {

  @Test
  void example() throws Exception {
    Path inputFile = Path.of("src", "test", "resources", "test.crc");
    Path destDir = Path.of("build", "endToEnd");
    Files.createDirectories(destDir);
    Path outputFile = destDir.resolve("test.png");

    CrcDiagramApplication.main(Arrays.array("-i", inputFile.toString(), "-o", outputFile.toString()));

    Approvals.verify(outputFile.toFile());
  }
}
