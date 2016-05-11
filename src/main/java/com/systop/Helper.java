package com.systop;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Helper
{
  public static String padString(String src, char pc, int len, boolean rightDirc)
  {
    StringBuffer sb = new StringBuffer();
    if ((src != null) && (!src.equals(""))) {
      sb.append(src);
    }
    while (sb.length() < len) {
      if (rightDirc) {
        sb.append(pc);
      } else {
        sb.insert(0, pc);
      }
    }
    return sb.toString();
  }
  
  public static String nvl(Object obj)
  {
    return nvl(obj, "");
  }
  
  private static String nvl(Object obj, String sNullValue)
  {
    if (obj == null) {
      return sNullValue;
    }
    return obj.toString();
  }
  
  public static Date now()
  {
    return new Date(System.currentTimeMillis());
  }
    public static String encodeBASE64(String s)
  {
    if (s == null) {
      return null;
    }
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(s.getBytes());
  }
  
  public static void encodeBASE64(InputStream in, OutputStream out)
    throws IOException
  {
    if (in == null) {
      return;
    }
    BASE64Encoder encoder = new BASE64Encoder();
    encoder.encode(in, out);
  }
  
  public static String decodeBASE64(String s)
  {
    if (s == null) {
      return null;
    }
    BASE64Decoder decoder = new BASE64Decoder();
    try
    {
      byte[] b = decoder.decodeBuffer(s);
      return new String(b);
    }
    catch (Exception e) {}
    return null;
  }
  
  public static void decodeBASE64(InputStream in, OutputStream out)
    throws IOException
  {
    if (in == null) {
      return;
    }
    BASE64Decoder decoder = new BASE64Decoder();
    decoder.decodeBuffer(in, out);
  }
  
  public static void main(String[] args){
	  System.out.println(Helper.decodeBASE64("utOxsbmk0rW089Gn"));
  }
}

