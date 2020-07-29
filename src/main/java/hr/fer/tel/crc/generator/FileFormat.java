package hr.fer.tel.crc.generator;

public enum FileFormat {
  PNG("png"),
  SVG("svg");

  private final String formatText;

  private FileFormat(String text) {
    formatText = text;
  }

  public String getFormatText() {
    return formatText;
  }
}
