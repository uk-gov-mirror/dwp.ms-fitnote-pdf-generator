package uk.gov.dwp.health.fitnote.generator.util;

import org.apache.commons.lang3.StringUtils;
import uk.gov.dwp.health.fitnote.generator.domain.CreatePdfFromImageItem;
import java.util.HashMap;
import java.util.Map;

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

@SuppressWarnings("deprecation")
public class DataTransformation {

  public Map<String, String> transformData(CreatePdfFromImageItem requestJson) {
    Map<String, String> valuesMap = new HashMap<>();
    valuesMap.put(NINO_BODY, getBlankIfNotSet(requestJson.getApplicantNINO().getNinoBody()));
    valuesMap.put(NINO_SUFFIX, getBlankIfNotSet(requestJson.getApplicantNINO().getNinoSuffix()));
    valuesMap.put(IMAGE, IMAGE_PREFIX +  getBlankIfNotSet(requestJson.getEncodedImage()));
    valuesMap.putAll(populateMobleDetails(requestJson));
    valuesMap.put(HOUSE_NAME_OR_NUMBER, getBlankIfNotSet(requestJson.getClaimantAddress()
            .getHouseNameOrNumber()));
    valuesMap.put(STREET, getBlankIfNotSet(requestJson.getClaimantAddress().getStreet()));
    valuesMap.put(CITY, getBlankIfNotSet(requestJson.getClaimantAddress().getCity()));
    valuesMap.put(POST_CODE, getBlankIfNotSet(requestJson.getClaimantAddress()
            .getPostcode()
            .toUpperCase()
            .replace(" ", "")));
    return valuesMap;
  }

  private Map<String, String> populateMobleDetails(CreatePdfFromImageItem requestJson) {
    Map<String, String> localValuesMap = new HashMap<>();
    String mobileNumber = getBlankIfNotSet(requestJson.getMobileNumber());
    localValuesMap.put(MOBILE_NUMBER, mobileNumber);
    if (mobileNumber.trim().isEmpty()) {
      localValuesMap.put(MOBILE_NUMBER_TEXT, " ");
    } else {
      localValuesMap.put(MOBILE_NUMBER_TEXT, MOBILE_HTML_TEXT);
    }
    return localValuesMap;
  }


  private String getBlankIfNotSet(String value) {
    return StringUtils.isBlank(value) ? " " : value;
  }
}
