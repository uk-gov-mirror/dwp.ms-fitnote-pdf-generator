package uk.gov.dwp.health.fitnote.generator.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StringOverrideTestObject {
  @JsonProperty("test")
  private String test;
}
