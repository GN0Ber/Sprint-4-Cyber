package com.wise.buddy.wiseBuddy.util;

public class InputSanitizer {
  public static String sanitize(String s){
    if (s == null) return null;
    return s
      .replace("<","&lt;")
      .replace(">","&gt;")
      .replace("\"","&quot;")
      .replace("'","&#x27;");
  }
}
