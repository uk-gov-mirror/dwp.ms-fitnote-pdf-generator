package uk.gov.dwp.health.fitnote.generator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uk.gov.dwp.health.fitnote.generator.domain.exception.InvalidJsonException;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePdfFromImageItem {

  @JsonProperty("ninoObject")
  private Nino applicantNINO;

  @JsonProperty("image")
  private String encodedImage;

  @JsonProperty("mobileNumber")
  private String mobileNumber;

  @JsonProperty("claimantAddress")
  private Address claimantAddress;


  public void validateJson() throws InvalidJsonException {
    if (null == getApplicantNINO() || getApplicantNINO().getNinoBody().isEmpty()) {
      throw new InvalidJsonException(buildException("nino"));
    }
    if (null == getEncodedImage() || getEncodedImage().isEmpty()) {
      throw new InvalidJsonException(buildException("image"));
    }

    if (claimantAddress == null) {
      throw new InvalidJsonException(buildException("claimantAddress"));
    }

    if (claimantAddress.getHouseNameOrNumber() == null
        || "".equals(claimantAddress.getHouseNameOrNumber())) {
      throw new InvalidJsonException(buildException("houseNameOrNumber"));
    } else if (claimantAddress.getPostcode() == null || "".equals(claimantAddress.getPostcode())) {
      throw new InvalidJsonException(buildException("postcode"));
    }

  }

  private String buildException(String fieldName) {
    return
        String.format(
            "Mandatory field exception : '%s' is empty or null value in the JSON structure",
            fieldName);
  }

}
