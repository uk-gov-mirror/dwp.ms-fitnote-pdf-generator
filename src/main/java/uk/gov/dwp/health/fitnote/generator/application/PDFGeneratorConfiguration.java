package uk.gov.dwp.health.fitnote.generator.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import uk.gov.dwp.crypto.SecureStrings;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PDFGeneratorConfiguration extends Configuration {

  private SecureStrings cipher = new SecureStrings();

  @JsonProperty("htmlToPdfServiceUrl")
  private String htmlToPdfServiceUrl;

  @JsonProperty("htmlToPdfConformanceLevel")
  private String htmlToPdfConformanceLevel;

  @JsonProperty("htmlToPdfTruststoreFile")
  private String htmlToPdfTruststoreFile;

  @JsonProperty("htmlToPdfTruststorePass")
  private SealedObject htmlToPdfTruststorePass;

  @JsonProperty("htmlToPdfKeystoreFile")
  private String htmlToPdfKeystoreFile;

  @JsonProperty("htmlToPdfKeystorePass")
  private SealedObject htmlToPdfKeystorePass;

  @JsonProperty("applicationInfoEnabled")
  private boolean applicationInfoEnabled;

  public PDFGeneratorConfiguration()
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
    // required for exception handling
  }

  public String getHtmlToPdfServiceUrl() {
    return htmlToPdfServiceUrl;
  }

  public String getHtmlToPdfTruststoreFile() {
    return htmlToPdfTruststoreFile;
  }

  public String getHtmlToPdfKeystoreFile() {
    return htmlToPdfKeystoreFile;
  }

  public String getHtmlToPdfTruststorePass() {
    return cipher.revealString(htmlToPdfTruststorePass);
  }

  public void setHtmlToPdfTruststorePass(String htmlToPdfTruststorePass)
      throws IllegalBlockSizeException, IOException {
    this.htmlToPdfTruststorePass = cipher.sealString(htmlToPdfTruststorePass);
  }

  public String getHtmlToPdfKeystorePass() {
    return cipher.revealString(htmlToPdfKeystorePass);
  }

  public void setHtmlToPdfKeystorePass(String htmlToPdfKeystorePass)
      throws IllegalBlockSizeException, IOException {
    this.htmlToPdfKeystorePass = cipher.sealString(htmlToPdfKeystorePass);
  }

  public String getHtmlToPdfConformanceLevel() {
    return htmlToPdfConformanceLevel;
  }

  public boolean isApplicationInfoEnabled() {
    return applicationInfoEnabled;
  }

}
