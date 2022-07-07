package uk.gov.dwp.health.fitnote.generator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CustomStringOverrideTest {

  private StringOverrideTestObject convertedJson;
  private ObjectMapper objectMapper;

  @Before
  public void setup() {
    objectMapper = new ObjectMapper();

    SimpleModule mod = new SimpleModule();
    mod.addDeserializer(String.class, new CustomStringOverride());
    objectMapper.registerModule(mod);
  }

  @Test
  public void testStringOverride_and_sign() throws IOException {
    String tesJson = "{\"test\":\"value &\"}";
    convertedJson = objectMapper.readValue(tesJson, StringOverrideTestObject.class);
    assertThat(convertedJson.getTest(), is(equalTo("value &#38;")));
  }

  @Test
  public void testStringOverride_greater_than_sign() throws IOException {
    String tesJson = "{\"test\":\"value >\"}";
    convertedJson = objectMapper.readValue(tesJson, StringOverrideTestObject.class);
    assertThat(convertedJson.getTest(), is(equalTo("value &#62;")));
  }

  @Test
  public void testStringOverride_less_than_sign() throws IOException {
    String tesJson = "{\"test\":\"value <\"}";
    convertedJson = objectMapper.readValue(tesJson, StringOverrideTestObject.class);
    assertThat(convertedJson.getTest(), is(equalTo("value &#60;")));
  }

  @Test
  public void testStringOverride_special_char() throws IOException {
    String tesJson = "{\"test\":\"value æ\"}";
    convertedJson = objectMapper.readValue(tesJson, StringOverrideTestObject.class);
    assertThat(convertedJson.getTest(), is(equalTo("value &#230;")));
  }
}
