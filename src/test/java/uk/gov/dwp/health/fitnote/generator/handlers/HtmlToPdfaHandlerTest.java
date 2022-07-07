package uk.gov.dwp.health.fitnote.generator.handlers;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.dwp.health.fitnote.generator.application.PDFGeneratorConfiguration;
import uk.gov.dwp.tls.TLSConnectionBuilder;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HtmlToPdfaHandlerTest {

  private TLSConnectionBuilder connectionBuilder;

  @Mock
  PDFGeneratorConfiguration mockConfiguration;

  @Rule
  public WireMockRule htmlToPdfService = new WireMockRule(wireMockConfig().port(5678));

  @Before
  public void setup() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
    when(mockConfiguration.getHtmlToPdfServiceUrl()).thenReturn("http://localhost:5678/generatePdf");
    when(mockConfiguration.getHtmlToPdfConformanceLevel()).thenReturn("PDFA_1_A");

    connectionBuilder = new TLSConnectionBuilder(null, null);

    htmlToPdfService.start();
  }

  @Test
  public void testSuccessfulHtmlToPdf() throws IOException {
    HtmlToPdfaHandler instance = new HtmlToPdfaHandler(connectionBuilder, mockConfiguration);
    String htmlSubmission = "i-am-an-html-document";
    String returnValue = "i-am-a-pdf-document";

    htmlToPdfService.stubFor(post(urlEqualTo("/generatePdf"))
                                     .willReturn(aResponse()
                                                         .withBody(Base64.encodeBase64String(returnValue.getBytes()))
                                                         .withStatus(200)
                                     ));

    String response = instance.generateBase64PdfFromHtml(htmlSubmission);

    assertThat(response, is(equalTo(Base64.encodeBase64String(returnValue.getBytes()))));
    assertThat(Base64.decodeBase64(response), is(equalTo(returnValue.getBytes())));
  }

  @Test
  public void testFailure404WithHtmlToPdf() {
    HtmlToPdfaHandler instance = new HtmlToPdfaHandler(connectionBuilder, mockConfiguration);
    String htmlSubmission = "i-am-an-html-document";

    htmlToPdfService.stubFor(post(urlEqualTo("/generatePdf"))
                                     .willReturn(aResponse()
                                                         .withStatus(404)
                                     ));

    try {
      instance.generateBase64PdfFromHtml(htmlSubmission);
      fail("should throw exception");

    } catch (IOException e) {
      assertTrue(e.getMessage().startsWith("HtmlToPdf Failure with StatusCode 404"));
    }
  }

  @Test
  public void testFailure500WithHtmlToPdf() {
    HtmlToPdfaHandler instance = new HtmlToPdfaHandler(connectionBuilder, mockConfiguration);
    String htmlSubmission = "i-am-an-html-document";

    htmlToPdfService.stubFor(post(urlEqualTo("/generatePdf"))
                                     .willReturn(aResponse()
                                                         .withStatus(500)
                                     ));

    try {
      instance.generateBase64PdfFromHtml(htmlSubmission);
      fail("should throw exception");

    } catch (IOException e) {
      assertTrue(e.getMessage().startsWith("HtmlToPdf Failure with StatusCode 500"));
    }
  }
}