package uk.gov.dwp.health.fitnote.generator.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.dropwizard.testing.junit.DropwizardAppRule;
import uk.gov.dwp.health.fitnote.generator.application.PDFGeneratorApplication;
import uk.gov.dwp.health.fitnote.generator.application.PDFGeneratorConfiguration;

import org.junit.ClassRule;
import org.junit.runner.RunWith;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

@RunWith(Cucumber.class)
@SuppressWarnings({"squid:S2187"})
@CucumberOptions(plugin = "json:target/cucumber-report.json")
public class RunCukesTest {
	private static final String CONFIG_FILE = "test.yml";

	@ClassRule
	public static final DropwizardAppRule<PDFGeneratorConfiguration> RULE =
		new DropwizardAppRule<>(PDFGeneratorApplication.class, resourceFilePath(CONFIG_FILE));

}
