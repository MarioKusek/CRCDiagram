package hr.fer.tel.crc.parser;

public class ClassExtractor {

  public static StringRange extractClass(String text, int startIndex) {
    int startClassIndex = text.indexOf("class", startIndex);
    int endClassIndex = StringExtractorUtil.findMatchingCurlyBrace(text, text.indexOf("{", startClassIndex));

    return new StringRange(startClassIndex, endClassIndex+1);
  }

}
