package uk.gov.dwp.health.fitnote.generator.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

public class CustomStringOverride extends StdDeserializer<String> {

  public CustomStringOverride() {
    this(null);
  }

  public CustomStringOverride(Class<?> vc) {
    super(vc);
  }

  @Override
  public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    return HtmlUtils.htmlEscapeDecimal(jp.getValueAsString());
  }
}
