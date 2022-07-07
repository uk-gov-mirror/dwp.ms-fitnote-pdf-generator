package uk.gov.dwp.health.fitnote.generator.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

  @JsonProperty("sessionId")
  private String sessionId;

  @JsonProperty("houseNameOrNumber")
  private String houseNameOrNumber;

  @JsonProperty("street")
  private String street;

  @JsonProperty("city")
  private String city;

  @JsonProperty("postcode")
  private String postcode;

}
