package hr.fer.tel.crc.parser;

public class CurlyBracesMatcher {

  public static int find(String text, int openCurlyBraceIndex) {
    if(text.length() == 0 || text.charAt(openCurlyBraceIndex) != '{')
      return -1;

    int closingCurlyBraceIndex = findClosingCurlyBraceFromIndex(text, openCurlyBraceIndex);
    return closingCurlyBraceIndex;
  }

  private static int findClosingCurlyBraceFromIndex(String text, int openCurlyBraceIndex) {
    int noOfOpenBraces = 0;

    for (int i = openCurlyBraceIndex; i < text.length(); i++) {
      char chacarcterAtIndex = text.charAt(i);
      if(chacarcterAtIndex == '{')
        noOfOpenBraces ++;
      else if (chacarcterAtIndex == '}')
        noOfOpenBraces--;

      if(noOfOpenBraces == 0)
        return i;
    }

    return -1;
  }
}
