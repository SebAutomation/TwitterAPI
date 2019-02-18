package runner;


import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/features/twitter"},
        glue = {"src/test/java/automationframework/stepdefinition"},
//        tags = {"~@ignore"},
        plugin = {"json:target/report/cucumber.json"},
        snippets = SnippetType.CAMELCASE)
public class TestRunner {

}
