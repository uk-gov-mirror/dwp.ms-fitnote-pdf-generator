package uk.gov.dwp.health.fitnote.generator.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dwp.health.fitnote.generator.domain.Address;
import uk.gov.dwp.health.fitnote.generator.domain.CreatePdfFromImageItem;
import uk.gov.dwp.health.fitnote.generator.domain.Nino;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.NINO_BODY;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.NINO_SUFFIX;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.IMAGE;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.IMAGE_PREFIX;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.MOBILE_NUMBER;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.MOBILE_NUMBER_TEXT;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.HOUSE_NAME_OR_NUMBER;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.STREET;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.CITY;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.POST_CODE;
import static uk.gov.dwp.health.fitnote.generator.constants.HTMLConstants.MOBILE_HTML_TEXT;


public class DataTransformationTest {

  private DataTransformation dataTransformation;

  private String encodedImage;

  final String JPG_IMAGE_PATH = "src/test/resources/fitnotes/FullPage_Portrait.jpg";

  @Mock
  private InputStream inputStream;

  @Before
  public void setup() throws IOException {
    dataTransformation = new DataTransformation();
  }

  @Test
  public void testTransformedHtmlWithAllFieldsPopulated() throws IOException {
    Map<String, String> transformData = dataTransformation.transformData(getRequestJson());
    assertThat(transformData.get(NINO_BODY), is(equalTo("AA370773")));
    assertThat(transformData.get(NINO_SUFFIX), is(equalTo("A")));
    assertThat(transformData.get(IMAGE), is(equalTo(IMAGE_PREFIX + encodedImage)));
    assertThat(transformData.get(MOBILE_NUMBER), is(equalTo("07865432345")));
      assertThat(transformData.get(MOBILE_NUMBER_TEXT), is(equalTo(MOBILE_HTML_TEXT)));
    assertThat(transformData.get(HOUSE_NAME_OR_NUMBER), is(equalTo("254")));
    assertThat(transformData.get(STREET), is(equalTo("Bakers Street")));
    assertThat(transformData.get(CITY), is(equalTo("London")));
    assertThat(transformData.get(POST_CODE), is(equalTo("NE129LG")));
  }

 @Test
  public void testTransformedHtmlWithBlankFields() throws IOException {
   CreatePdfFromImageItem requestJson = getRequestJson();
   requestJson.setMobileNumber(null);
   requestJson.getClaimantAddress().setStreet("  ");
   requestJson.getApplicantNINO().setNinoBody(null);
   requestJson.getApplicantNINO().setNinoSuffix("   ");
   requestJson.getClaimantAddress().setCity(null);
   requestJson.getClaimantAddress().setPostcode("     ");
   requestJson.getClaimantAddress().setHouseNameOrNumber(null);
    Map<String, String> transformData = dataTransformation.transformData(requestJson);
    assertThat(transformData.get(NINO_BODY), is(equalTo(" ")));
    assertThat(transformData.get(NINO_SUFFIX), is(equalTo(" ")));
    assertThat(transformData.get(IMAGE), is(equalTo(IMAGE_PREFIX + encodedImage)));
    assertThat(transformData.get(MOBILE_NUMBER), is(equalTo(" ")));
    assertThat(transformData.get(MOBILE_NUMBER_TEXT), is(equalTo(" ")));
    assertThat(transformData.get(HOUSE_NAME_OR_NUMBER), is(equalTo(" ")));
    assertThat(transformData.get(STREET), is(equalTo(" ")));
    assertThat(transformData.get(CITY), is(equalTo(" ")));
    assertThat(transformData.get(POST_CODE), is(equalTo(" ")));
  }

  private CreatePdfFromImageItem getRequestJson() throws IOException {
    CreatePdfFromImageItem requestJson = new CreatePdfFromImageItem();
    Nino applicantNINO;
    Address claimantAddress;
    encodedImage =
            Base64.encodeBase64String(FileUtils.readFileToByteArray(new File(JPG_IMAGE_PATH)));
    applicantNINO = new Nino();
    applicantNINO.setNinoBody("AA370773");
    applicantNINO.setNinoSuffix("A");
    requestJson.setApplicantNINO(applicantNINO);
    claimantAddress = new Address();
    claimantAddress.setHouseNameOrNumber("254");
    claimantAddress.setStreet("Bakers Street");
    claimantAddress.setCity("London");
    claimantAddress.setPostcode("NE12 9LG");
    requestJson.setClaimantAddress(claimantAddress);
    requestJson.setMobileNumber("07865432345");
    requestJson.setEncodedImage(encodedImage);
    return requestJson;
  }

}
