package hr.fer.tel.approval;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.approvaltests.reporters.ClipboardReporter;
import org.approvaltests.reporters.EnvironmentAwareReporter;
import org.approvaltests.reporters.GenericDiffReporter;

import com.spun.util.tests.TestUtils;

public class ImageDiffWebReporter implements EnvironmentAwareReporter
{
  public static final ImageDiffWebReporter INSTANCE = new ImageDiffWebReporter();
  @Override
  public void report(String received, String approved)
  {
    String text = "<html>\n" +
        "\n" +
        "<body>\n" +
        "  <center>\n" +
        "    <table border=1>\n" +
        "      <tr>\n" +
        "        <td><img src=\"file:///%s\"></td>\n" +
        "        <td><img src=\"file:///%s\"></td>\n" +
        "      </tr>\n" +
        "      <tr>\n" +
        "        <td>approved</td>\n" +
        "        <td>received</td>\n" +
        "      </tr>\n" +
        "      <tr>\n" +
        "        <td colspan=\"2\">\n" +
        "          <center>\n" +
        "            <img src=\"file:///%s\"><br />\n" +
        "            <div>difference</div>\n" +
        "          </center>\n" +
        "        </td>\n" +
        "      </tr>\n" +
        "    </table> \n" +
        "    <br />\n" +
        "    <b>to approve :</b> %s <br />\n" +
        "    <button onclick=\"copyCommand()\">copy to clipboard</button> folowing command <br />\n" +
        "    <font size=1><span id=\"copyCommand\">%s</span></font> <br /> \n" +
        "    and run in command window <br />\n" +
        "  </center>\n" +
        "</body>\n" +
        "<script >\n" +
        "  function copyCommand() {\n" +
        "\n" +
        "    var ta = document.createElement('textarea');\n" +
        "    var copyText = document.getElementById(\"copyCommand\");\n" +
        "    console.log(copyText.textContent);\n" +
        "    ta.value = copyText.textContent;\n" +
        "  \n" +
        "    document.body.appendChild(ta);\n" +
        "    ta.select();\n" +
        "    document.execCommand('copy');\n" +
        "    document.body.removeChild(ta);\n" +
        "\n" +
        "    alert(\"Copied the text: \" + copyText.textContent);\n" +
        "  }\n" +
        "</script>\n" +
        "</html>";

    String moveText = ClipboardReporter.getAcceptApprovalText(received, approved);

    File differenceFile = null;
    try {
      differenceFile = File.createTempFile("diffence", ".png");
      createDifferenceImage(received, approved, differenceFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    text = String.format(text, approved, received, differenceFile.getAbsolutePath(), received, moveText);
    TestUtils.displayHtml(text);
  }

  private void createDifferenceImage(String received, String approved, File differenceFile) throws IOException {
    BufferedImage imgReceived = ImageIO.read(new File(received));
    BufferedImage imgApproved = ImageIO.read(new File(approved));

    int receivedWidth = imgReceived.getWidth();
    int receivedHeight = imgReceived.getHeight();

    int approvedWidth = imgApproved.getWidth();
    int approvedHeight = imgApproved.getHeight();

    int width = Math.max(receivedWidth, approvedWidth);
    int height = Math.max(receivedHeight, approvedHeight);
    BufferedImage imgDifference= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    // fill with red
    for (int y = 0; y < height; y++)
    {
        for (int x = 0; x < width; x++)
        {
            int a = 255; //max 255
            int r = 255;
            int g = 0;
            int b = 0;

            int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel

            imgDifference.setRGB(x, y, p);
        }
    }

    // image comparison
    for (int y = 0; y < height; y++)
    {
        for (int x = 0; x < width; x++)
        {
          if(x >= receivedWidth || x >= approvedWidth ||
              y >= receivedHeight || y >= approvedHeight)
            continue;

            int rgbReceived = imgReceived.getRGB(x, y);
            int rgbApproved = imgApproved.getRGB(x, y);
            int redReceived = (rgbReceived >> 16) & 0xff;
            int greenReceived = (rgbReceived >> 8) & 0xff;
            int blueReceived = (rgbReceived) & 0xff;
            int redApproved = (rgbApproved >> 16) & 0xff;
            int greenApproved = (rgbApproved >> 8) & 0xff;
            int blueApproved = (rgbApproved) & 0xff;
            int difference = Math.abs(redReceived - redApproved);
            difference += Math.abs(greenReceived - greenApproved);
            difference += Math.abs(blueReceived - blueApproved);
            if(difference > 255)
              difference = 255;

            int p = (255<<24) | (difference<<16) | (difference<<8) | 0;
            imgDifference.setRGB(x, y, p);
        }
    }

    // write image to file
    ImageIO.write(imgDifference, "png", differenceFile);
  }


  /**
   * We assume any environment that is not headless will have a web browser to display the image in a web page.
   */
  @Override
  public boolean isWorkingInThisEnvironment(String forFile)
  {
    return !GraphicsEnvironment.isHeadless()
        && GenericDiffReporter.isFileExtensionValid(forFile, GenericDiffReporter.IMAGE_FILE_EXTENSIONS);
  }
}