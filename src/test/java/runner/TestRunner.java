package runner;


import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.testng.CucumberFeatureWrapper;
import org.testng.annotations.Test;

@CucumberOptions(
        features = {"src/test/features/twitter"},
        glue = {"stepdefinition"},
//        tags = {"~@ignore"},
        plugin = {"json:target/report/cucumber.json"},
        snippets = SnippetType.CAMELCASE,
        format = {"json:target/cucumber.json", "html:target/site/cucumber-pretty"})

public class TestRunner extends BaseRunner{

    @Test(groups = "cucumber", description = "Runs Cucumber All Feature", dataProvider = "features")
    public void runTests(CucumberFeatureWrapper cucumberFeatureWrapper) {

        testNGCucumberRunner.runCucumber(cucumberFeatureWrapper.getCucumberFeature());

    }
}