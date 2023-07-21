package uk.gov.dwp.health.fitnote.generator.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import uk.gov.dwp.health.fitnote.generator.PDFGeneratorResource;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import uk.gov.dwp.health.fitnote.generator.handlers.HtmlToPdfaHandler;
import uk.gov.dwp.health.fitnote.generator.util.DataTransformation;
import uk.gov.dwp.health.version.HealthCheckResource;
import uk.gov.dwp.health.version.ServiceInfoResource;
import uk.gov.dwp.health.version.info.PropertyFileInfoProvider;
import uk.gov.dwp.tls.TLSConnectionBuilder;

public class PDFGeneratorApplication extends Application<PDFGeneratorConfiguration> {

  @Override
  protected void bootstrapLogging() {
    // to prevent dropwizard using its own standard logger
  }

  public static void main(String[] args) throws Exception {
    new PDFGeneratorApplication().run(args);
  }

  @Override
  public void run(PDFGeneratorConfiguration configuration, Environment environment)
      throws Exception {
    TLSConnectionBuilder connectionBuilder =
        new TLSConnectionBuilder(
            configuration.getHtmlToPdfTruststoreFile(),
            configuration.getHtmlToPdfTruststorePass(),
            configuration.getHtmlToPdfKeystoreFile(),
            configuration.getHtmlToPdfKeystorePass());

    final HtmlToPdfaHandler htmlToPdfaHandler =
        new HtmlToPdfaHandler(connectionBuilder, configuration);
    final DataTransformation dataTransformation = new DataTransformation();

    PDFGeneratorResource instance =
        new PDFGeneratorResource(htmlToPdfaHandler, dataTransformation, new ObjectMapper());
    environment.jersey().register(instance);

    environment.jersey().register(new HealthCheckResource());

    if (configuration.isApplicationInfoEnabled()) {
      environment
          .jersey()
          .register(new ServiceInfoResource(new PropertyFileInfoProvider("application.yml")));
    }
  }

  @Override
  public void initialize(Bootstrap<PDFGeneratorConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(
            bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
  }
}
