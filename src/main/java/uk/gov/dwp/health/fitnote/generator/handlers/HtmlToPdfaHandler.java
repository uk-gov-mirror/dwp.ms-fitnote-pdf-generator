package uk.gov.dwp.health.fitnote.generator.handlers;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.health.fitnote.generator.application.PDFGeneratorConfiguration;
import uk.gov.dwp.tls.TLSConnectionBuilder;
import uk.gov.dwp.tls.TLSGeneralException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class HtmlToPdfaHandler {
  private static final String FULL_JSON = "{\"page_html\": \"%s\", \"conformance_level\": \"%s\"}";

  private static final String ERROR_MSG = "HtmlToPdf Failure with StatusCode %s and Message %s";
  private static final Logger LOG = LoggerFactory.getLogger(HtmlToPdfaHandler.class.getName());
  private final PDFGeneratorConfiguration configuration;
  private final TLSConnectionBuilder tlsBuilder;

  public HtmlToPdfaHandler(TLSConnectionBuilder tlsConnectionBuilder,
                           PDFGeneratorConfiguration config) {
    this.tlsBuilder = tlsConnectionBuilder;
    this.configuration = config;
  }

  public String generateBase64PdfFromHtml(String completedHtml) throws IOException {
    LOG.debug("base64 encoding html before calling htmlToPdf on {}",
        configuration.getHtmlToPdfServiceUrl());
    String base64HtmlSubmission = Base64.encodeBase64String(
        completedHtml.getBytes(StandardCharsets.UTF_8)
        );

    String base64PdfResult = null;

    HttpPost postMethod = new HttpPost(configuration.getHtmlToPdfServiceUrl());

    LOG.info("creating json request with base64 encoded html for pdfa at conformance level {}",
        configuration.getHtmlToPdfConformanceLevel());
    postMethod.setEntity(new StringEntity(String.format(FULL_JSON, base64HtmlSubmission,
        configuration.getHtmlToPdfConformanceLevel())));

    try {
      CloseableHttpResponse response = getTlsBuilder().configureSSLConnection().execute(postMethod);
      LOG.debug("received {} from {}", response.getStatusLine().getStatusCode(),
          configuration.getHtmlToPdfServiceUrl());

      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        LOG.info("successfully received base64 encoded pdfa document at conformance level {}",
            configuration.getHtmlToPdfConformanceLevel());
        base64PdfResult = EntityUtils.toString(response.getEntity());

      } else {
        throw new IOException(String.format(ERROR_MSG, response.getStatusLine().getStatusCode(),
            EntityUtils.toString(response.getEntity())));
      }

    } catch (TLSGeneralException
                     | NoSuchAlgorithmException
                     | KeyManagementException
                     | CertificateException
                     | KeyStoreException
                     | UnrecoverableKeyException
                     | ClientProtocolException e) {
      LOG.error(e.getMessage(), e);
    }

    return base64PdfResult;
  }

  private TLSConnectionBuilder getTlsBuilder() {
    return tlsBuilder;
  }
}
