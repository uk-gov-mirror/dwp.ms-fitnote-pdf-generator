package uk.gov.dwp.health.fitnote.generator.integration;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.Rule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GeneratorSteps {

    private static String FULLY_ENCODED_JSON_STRING_WITH_MOBILE_LANDSCAPE;
    private static String FULLY_ENCODED_JSON_STRING_WITH_MOBILE;
    private static String FULLY_ENCODED_JSON_STRING_NO_MOBILE;
    private static String FULLY_ENCODED_JSON_STRING_WITH_NEW_ADDRESS_LANDSCAPE;
    private static String FULLY_ENCODED_JSON_STRING_WITH_FULL_DETAILS_PORTRAIT;
    private static String FULLY_ENCODED_JSON_STRING_WITHOUT_NEW_ADDRESS;
    private static String FULLY_ENCODED_JSON_STRING_WITH_PARTIAL_ADDRESS;

    // set-up servers, clients and responses
    private HttpResponse generatedResponse;
    private HttpClient httpClient;

    @Before
    public void setup() throws IOException {
        String jpgImageLandscape = "src/test/resources/fitnotes/FullPage_Landscape.jpg";
        String encodedImageL = Base64.encodeBase64String(FileUtils.readFileToByteArray(new File(jpgImageLandscape)));
        String jpgImagePath = "src/test/resources/fitnotes/FullPage_Portrait.jpg";
        String encodedImage = Base64.encodeBase64String(FileUtils.readFileToByteArray(new File(jpgImagePath)));

        httpClient = HttpClients.createDefault();

        // test structures
        String clamaintAddressJson = "{\"houseNameOrNumber\" : \"254\", \"street\" : null," + "\"city\" : null, \"postcode\" : \"NE12 9PG\"}";
        FULLY_ENCODED_JSON_STRING_WITH_MOBILE_LANDSCAPE =
                String.format("{\"ninoObject\":{\"ninoBody\":\"AA370773\",\"ninoSuffix\":\"A\"},\"image\":\"%s\",\"mobileNumber\":\"01632960833\", \"claimantAddress\":" + clamaintAddressJson + "}", encodedImageL);
        FULLY_ENCODED_JSON_STRING_WITH_MOBILE =
                String.format("{\"ninoObject\":{\"ninoBody\":\"AA370773\",\"ninoSuffix\":\"A\"},\"image\":\"%s\",\"mobileNumber\":\"01632960833\", \"claimantAddress\":" + clamaintAddressJson + "}", encodedImage);
        FULLY_ENCODED_JSON_STRING_NO_MOBILE =
                String.format("{\"ninoObject\":{\"ninoBody\":\"AA370773\",\"ninoSuffix\":\"A\"},\"image\":\"%s\", \"claimantAddress\":" + clamaintAddressJson + "}", encodedImage);
        FULLY_ENCODED_JSON_STRING_WITH_NEW_ADDRESS_LANDSCAPE =
                String.format("{\"ninoObject\":{\"ninoBody\":\"AA370773\",\"ninoSuffix\":\"A\"},\"image\":\"%s\", \"claimantAddress\":" + clamaintAddressJson + "}", encodedImageL);
        FULLY_ENCODED_JSON_STRING_WITH_FULL_DETAILS_PORTRAIT =
                String.format("{\"ninoObject\":{\"ninoBody\":\"AA370773\",\"ninoSuffix\":\"A\"},\"image\":\"%s\", \"mobileNumber\":\"01632960833\", " +
                        "\"claimantAddress\" : { \"houseNameOrNumber\" : \"254\", \"street\" : \"Bakers Street\", \"city\" : \"London\", \"postcode\" : \"NE12 9LG\" }}", encodedImage);
        FULLY_ENCODED_JSON_STRING_WITH_PARTIAL_ADDRESS =
                String.format("{\"ninoObject\":{\"ninoBody\":\"AA370773\",\"ninoSuffix\":\"A\"},\"image\":\"%s\", \"mobileNumber\":\"01632960833\", " +
                        "\"claimantAddress\" : { \"houseNameOrNumber\" : \"254\", \"street\" : null, \"city\" : null, \"postcode\" : \"NE12 9LG\" }}", encodedImage);
        FULLY_ENCODED_JSON_STRING_WITHOUT_NEW_ADDRESS =
                String.format("{\"ninoObject\":{\"ninoBody\":\"AA370773\",\"ninoSuffix\":\"A\"},\"image\":\"%s\" ", encodedImage);
    }

    @Given("^the controller is up$")
    public void the_controller_is_up() throws Throwable {
        httpClient = HttpClientBuilder.create().build();
    }

    @When("^I hit the service url \"([^\"]*)\" with an invalid body$")
    public void iHitTheServiceUrlWithAnInvalidBody(String serviceUrl) throws Throwable {
        String COMPLETE_INVALID_JSON = "{\"bad_nino\":null,\"null_imgae\":null}";
        performHttpPostWithUriOf(serviceUrl, COMPLETE_INVALID_JSON);
    }

    @When("^I hit the service url \"([^\"]*)\" with valid JSON, no mobile$")
    public void iHitTheServiceUrlWithAValidBody_NO_MOBILE(String serviceUrl) throws Throwable {
        performHttpPostWithUriOf(serviceUrl, FULLY_ENCODED_JSON_STRING_NO_MOBILE);
    }

    @When("^I hit the service url \"([^\"]*)\" with valid JSON with mobile$")
    public void iHitTheServiceUrlWithAValidBody_WITH_MOBILE(String serviceUrl) throws Throwable {
        performHttpPostWithUriOf(serviceUrl, FULLY_ENCODED_JSON_STRING_WITH_MOBILE);
    }

    @When("^I hit the service url \"([^\"]*)\" with valid JSON and Landscape Image")
    public void iHitTheServiceUrlWithAValidBody_LANDSCAPE(String serviceUrl) throws Throwable {
        performHttpPostWithUriOf(serviceUrl, FULLY_ENCODED_JSON_STRING_WITH_MOBILE_LANDSCAPE);
    }

    @Then("^I get a http response of (\\d+)$")
    public void I_get_a_http_response_of(int statusCode) throws Throwable {
        assertThat(generatedResponse.getStatusLine().getStatusCode(), is(statusCode));
    }

    @And("^The PDF is written to \"([^\"]*)\"$")
    public void thePDFIsWrittenTo(String pdfFile) throws Throwable {
        writeStreamToPDF(pdfFile, generatedResponse.getEntity());
    }

    @When("^I hit the service url \"([^\"]*)\" with valid JSON with new address details")
    public void iHitTheServiceUrlWithAValidBody_WITH_NEW_ADDRESS(String serviceUrl) throws Throwable {
        performHttpPostWithUriOf(serviceUrl, FULLY_ENCODED_JSON_STRING_WITH_NEW_ADDRESS_LANDSCAPE);
    }


    @When("^I hit the service url \"([^\"]*)\" with valid JSON and missing new address details$")
    public void iHitTheServiceUrlWithAValidBodyAndMissingAddressDetails(String serviceUrl) throws Throwable {
        performHttpPostWithUriOf(serviceUrl, FULLY_ENCODED_JSON_STRING_WITHOUT_NEW_ADDRESS);
    }

    @When("^I hit the service url \"([^\"]*)\" with valid JSON containing all details$")
    public void iHitTheServiceUrlWithAValidBodyContainingAllDetails(String serviceUrl) throws Throwable{
        performHttpPostWithUriOf(serviceUrl, FULLY_ENCODED_JSON_STRING_WITH_FULL_DETAILS_PORTRAIT);
    }

    @When("I hit the service url \"([^\"]*)\" with valid JSON containing a partial address$")
    public void iHitTheServiceUrlWithAValidBodyContainingAPartialAddress(String serviceUrl) throws Throwable{
        performHttpPostWithUriOf(serviceUrl, FULLY_ENCODED_JSON_STRING_WITH_PARTIAL_ADDRESS);
    }

    /*
     * PRIVATE METHODS
     */
    private void performHttpPostWithUriOf(String uri, String body) throws IOException {
        HttpPost httpUriRequest = new HttpPost(uri);
        HttpEntity entity = new StringEntity(body);
        httpUriRequest.setEntity(entity);

        generatedResponse = httpClient.execute(httpUriRequest);
    }

    private void writeStreamToPDF(String pdfFilePath, HttpEntity returnBody) throws IOException {
        File pdfFile = new File(pdfFilePath);
        pdfFile.getParentFile().mkdirs();

        // delete any existing file
        if (pdfFile.exists()) {
            pdfFile.delete();
        }

        // create new pdf file from output
        FileOutputStream pdfOutput = new FileOutputStream(pdfFile);
        pdfOutput.write(IOUtils.toByteArray(returnBody.getContent()));
        pdfOutput.close();
    }
}
