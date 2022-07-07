package uk.gov.dwp.health.fitnote.generator.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.gov.dwp.health.fitnote.generator.domain.exception.InvalidJsonException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreatePdfFromImageItemTest {

  CreatePdfFromImageItem createPdfFromImageItem;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Before
  public void before() {
    createPdfFromImageItem = new CreatePdfFromImageItem();
  }

  @Test()
  public void validateJsonExceptionNinoNull() throws InvalidJsonException {
    exceptionRule.expect(InvalidJsonException.class);
    exceptionRule.expectMessage("Mandatory field exception : 'nino' is empty or null value in the JSON structure");
    createPdfFromImageItem.setApplicantNINO(null);
    createPdfFromImageItem.validateJson();
  }

  @Test()
  public void validateJsonExceptionNinoEmpty() throws InvalidJsonException {
    Nino nino = mock(Nino.class);
    when(nino.getNinoBody()).thenReturn("");
    exceptionRule.expect(InvalidJsonException.class);
    exceptionRule.expectMessage("Mandatory field exception : 'nino' is empty or null value in the JSON structure");
    createPdfFromImageItem.setApplicantNINO(nino);
    createPdfFromImageItem.validateJson();
  }

  @Test()
  public void validateJsonExceptionEncodedImageNull() throws InvalidJsonException {
    Nino mockNino = mock(Nino.class);
    when(mockNino.getNinoBody()).thenReturn("This is the body");
    createPdfFromImageItem.setApplicantNINO(mockNino);


    exceptionRule.expect(InvalidJsonException.class);
    exceptionRule.expectMessage("Mandatory field exception : 'image' is empty or null value in the JSON structure");
    createPdfFromImageItem.setEncodedImage(null);

    createPdfFromImageItem.validateJson();
  }

  @Test()
  public void validateJsonExceptionEncodedImageEmpty() throws InvalidJsonException {
    Nino mockNino = mock(Nino.class);
    when(mockNino.getNinoBody()).thenReturn("This is the body");
    createPdfFromImageItem.setApplicantNINO(mockNino);

    exceptionRule.expect(InvalidJsonException.class);
    exceptionRule.expectMessage("Mandatory field exception : 'image' is empty or null value in the JSON structure");
    createPdfFromImageItem.setEncodedImage("");

    createPdfFromImageItem.validateJson();
  }

  @Test()
  public void validateJsonExceptionClaimantAddressNull() throws InvalidJsonException {
    Nino mockNino = mock(Nino.class);
    when(mockNino.getNinoBody()).thenReturn("This is the body");
    createPdfFromImageItem.setApplicantNINO(mockNino);
    createPdfFromImageItem.setEncodedImage("Encoded Image");

    exceptionRule.expect(InvalidJsonException.class);
    exceptionRule.expectMessage("Mandatory field exception : 'claimantAddress' is empty or null value in the JSON structure");
    createPdfFromImageItem.setClaimantAddress(null);

    createPdfFromImageItem.validateJson();
  }

  @Test()
  public void validateJsonExceptionClaimantAddressNumNull() throws InvalidJsonException {
    Nino mockNino = mock(Nino.class);
    when(mockNino.getNinoBody()).thenReturn("This is the body");
    createPdfFromImageItem.setApplicantNINO(mockNino);
    createPdfFromImageItem.setEncodedImage("Encoded Image");
    Address address = mock(Address.class);
    when(address.getHouseNameOrNumber()).thenReturn(null);
    exceptionRule.expectMessage("Mandatory field exception : 'houseNameOrNumber' is empty or null value in the JSON structure");
    createPdfFromImageItem.setClaimantAddress(address);

    createPdfFromImageItem.validateJson();

  }

  @Test()
  public void validateJsonExceptionClaimantAddressEmpty() throws InvalidJsonException {
    Nino mockNino = mock(Nino.class);
    when(mockNino.getNinoBody()).thenReturn("This is the body");
    createPdfFromImageItem.setApplicantNINO(mockNino);
    createPdfFromImageItem.setEncodedImage("Encoded Image");
    Address address = mock(Address.class);

    exceptionRule.expect(InvalidJsonException.class);
    exceptionRule.expectMessage("Mandatory field exception : 'houseNameOrNumber' is empty or null value in the JSON structure");
    when(address.getHouseNameOrNumber()).thenReturn("");
    createPdfFromImageItem.setClaimantAddress(address);

    createPdfFromImageItem.validateJson();

  }

  @Test()
  public void validateJsonExceptionClaimantPostcodeNull() throws InvalidJsonException {
    Nino mockNino = mock(Nino.class);
    when(mockNino.getNinoBody()).thenReturn("This is the body");
    createPdfFromImageItem.setApplicantNINO(mockNino);
    createPdfFromImageItem.setEncodedImage("Encoded Image");
    Address address = mock(Address.class);

    exceptionRule.expect(InvalidJsonException.class);
    exceptionRule.expectMessage("Mandatory field exception : 'postcode' is empty or null value in the JSON structure");
    when(address.getHouseNameOrNumber()).thenReturn("10");
    when(address.getPostcode()).thenReturn(null);
    createPdfFromImageItem.setClaimantAddress(address);

    createPdfFromImageItem.validateJson();

  }

  @Test()
  public void validateJsonExceptionClaimantPostcodeEmpty() throws InvalidJsonException {
    Nino mockNino = mock(Nino.class);
    when(mockNino.getNinoBody()).thenReturn("This is the body");
    createPdfFromImageItem.setApplicantNINO(mockNino);
    createPdfFromImageItem.setEncodedImage("Encoded Image");
    Address address = mock(Address.class);

    exceptionRule.expect(InvalidJsonException.class);
    exceptionRule.expectMessage("Mandatory field exception : 'postcode' is empty or null value in the JSON structure");
    when(address.getHouseNameOrNumber()).thenReturn("10");
    when(address.getPostcode()).thenReturn("");
    createPdfFromImageItem.setClaimantAddress(address);

    createPdfFromImageItem.validateJson();

  }
}
