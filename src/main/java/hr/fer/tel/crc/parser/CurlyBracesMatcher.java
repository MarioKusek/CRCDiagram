package hr.fer.tel.crc.parser;

public class CurlyBracesMatcher {

  public static int find(String text, int openCurlyBraceIndex) {
    if(text.length() == 0)
      return -1;

    if(text.charAt(openCurlyBraceIndex) != '{')
      return -1;

    return text.indexOf('}');
  }
}
