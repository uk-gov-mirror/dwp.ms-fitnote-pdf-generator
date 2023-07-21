package uk.gov.dwp.health.fitnote.generator;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.health.fitnote.generator.constants.HTMLTemplateFiles;
import uk.gov.dwp.health.fitnote.generator.domain.CreatePdfFromImageItem;
import uk.gov.dwp.health.fitnote.generator.domain.exception.InvalidJsonException;
import uk.gov.dwp.health.fitnote.generator.handlers.HtmlToPdfaHandler;
import uk.gov.dwp.health.fitnote.generator.util.CustomStringOverride;
import uk.gov.dwp.health.fitnote.generator.util.DataTransformation;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Path("/pdfGenerator")
@Produces(MediaType.APPLICATION_JSON)
public class PDFGeneratorResource {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(PDFGeneratorResource.class.getName());
  private static final String STANDARD_JSON_ERROR = "Payload contains invalid items (%s)";
  private final DataTransformation dataTransformation;
  private final HtmlToPdfaHandler htmlToPdfaHandler;
  private final ObjectMapper objectMapper;

  public PDFGeneratorResource(
      HtmlToPdfaHandler handler,
      DataTransformation dataTransformation,
      ObjectMapper objectMapper) {
    this.dataTransformation = dataTransformation;
    this.htmlToPdfaHandler = handler;
    this.objectMapper = objectMapper;
    SimpleModule mod = new SimpleModule();
    mod.addDeserializer(String.class, new CustomStringOverride());
    objectMapper.registerModule(mod);
  }

  @POST
  @Path("createPdfFromImage")
  public Response generatePdf(String jsonPayload) {
    Response response;
    if (null != jsonPayload) {
      try {
        CreatePdfFromImageItem imageItemClass =
            objectMapper.readValue(jsonPayload, CreatePdfFromImageItem.class);
        imageItemClass.validateJson();
        LOGGER.info("incoming structure successfully validated");

        String templateHtml =
            IOUtils.toString(
                Objects.requireNonNull(
                    getClass().getResourceAsStream(HTMLTemplateFiles.FITNOTE_TEMPLATE_HTML)),
                    StandardCharsets.UTF_8);

        StringSubstitutor substitutor =
            new StringSubstitutor(dataTransformation.transformData(imageItemClass));
        String resolvedHtml = substitutor.replace(templateHtml);
        LOGGER.debug("loaded html template, with substitute data({}})", resolvedHtml);
        String pdf = htmlToPdfaHandler.generateBase64PdfFromHtml(resolvedHtml);
        response = Response.ok().entity(Base64.decodeBase64(pdf)).build();
      } catch (UnrecognizedPropertyException | InvalidJsonException | JsonEOFException e) {
        LOGGER.debug("unable to parse input json");
        String errorMessage = String.format(STANDARD_JSON_ERROR, e.getMessage());
        response = Response.status(HttpStatus.SC_BAD_REQUEST).entity(errorMessage).build();
      } catch (Exception e) {
        response =
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        LOGGER.error("Exception Error - {}", e.getMessage());
      }
    } else {
      response = Response.status(Response.Status.BAD_REQUEST).build();
    }
    return response;
  }
}
