package uk.gov.dwp.health.fitnote.generator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Nino {

  @JsonProperty("ninoBody")
  private String ninoBody;

  @JsonProperty("ninoSuffix")
  private String ninoSuffix;

}
