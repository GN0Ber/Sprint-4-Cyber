package com.wise.buddy.wiseBuddy.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;

@Converter
public class AesGcmConverter implements AttributeConverter<String, String> {
  private static final String ALG = "AES";
  private static final String TRANS = "AES/GCM/NoPadding";
  private static final int GCM_TAG_LENGTH = 128;
  private static final SecureRandom RNG = new SecureRandom();

  // 16 bytes (128 bits) em Base64 em PII_AES_KEY (GitHub Secret/Env)
  private static final byte[] KEY = System.getenv("PII_AES_KEY") != null
      ? Base64.getDecoder().decode(System.getenv("PII_AES_KEY"))
      : new byte[16];

  @Override
  public String convertToDatabaseColumn(String plain) {
    if (plain == null) return null;
    try {
      byte[] iv = new byte[12]; RNG.nextBytes(iv);
      Cipher c = Cipher.getInstance(TRANS);
      c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, ALG), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
      byte[] enc = c.doFinal(plain.getBytes());
      byte[] out = new byte[iv.length + enc.length];
      System.arraycopy(iv,0,out,0,iv.length);
      System.arraycopy(enc,0,out,iv.length,enc.length);
      return Base64.getEncoder().encodeToString(out);
    } catch (Exception e) { throw new RuntimeException(e); }
  }

  @Override
  public String convertToEntityAttribute(String db) {
    if (db == null) return null;
    try {
      byte[] all = Base64.getDecoder().decode(db);
      byte[] iv = new byte[12]; System.arraycopy(all,0,iv,0,12);
      byte[] enc = new byte[all.length-12]; System.arraycopy(all,12,enc,0,enc.length);
      Cipher c = Cipher.getInstance(TRANS);
      c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, ALG), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
      return new String(c.doFinal(enc));
    } catch (Exception e) { throw new RuntimeException(e); }
  }
}
