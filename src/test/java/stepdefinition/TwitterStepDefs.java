package stepdefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.RestUtilities;
import common.ScenarioContext;
import constants.EndPoints;
import constants.Path;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

public class TwitterStepDefs {

    private RestUtilities restUtilities;
    private ScenarioContext scenarioContext;
    private ObjectMapper MAPPER = new ObjectMapper();

    public TwitterStepDefs(ScenarioContext scenarioContext, RestUtilities restUtilities) {
        this.scenarioContext = scenarioContext;
        this.restUtilities = restUtilities;
    }

    @Given("user is authenticated by Twitter API")
    public void userIsAuthenticatedByTwitterAPI() {
        scenarioContext.setRequestSpec(restUtilities.createRequestSpecification());
    }

    @And("twitter baseURL and basePath is set for statuses")
    public void endpointSetupForTwitterStatuses() {
        scenarioContext.getRequestSpec().baseUri(Path.BASE_URI);
        scenarioContext.getRequestSpec().basePath(Path.STATUSES);
    }

    //  POST request
    //  https://api.twitter.com/1.1/statuses/update.json?status=End%To%End%Tweeter%Workflow
    @When("^user sends a following tweet \"(.*)\" to the server$")
    public void userSendsAfollowingTweetToTheServer(String tweetContent) {
        Response response = restUtilities.makePostWithQueryParam(EndPoints.STATUSES_TWEET_POST, "status", tweetContent);
        scenarioContext.setResponse(response);
    }

    @And("tweet id is saved")
    public void saveTweetId() {
        JsonPath jsPath = new JsonPath(scenarioContext.responseAsString());
        scenarioContext.setTweetId(jsPath.get("id_str"));
    }

    //  Read request
    //  https://api.twitter.com/1.1/statuses/show.json?id=210462857140252672
    @Then("^user gets the tweet by Id$")
    public void userGetsTheTweetById() {
        Response response = restUtilities.makeGetWithQueryParam(EndPoints.STATUSES_TWEET_READ_SINGLE,
                "id", scenarioContext.getTweetId());
        scenarioContext.setResponse(response);
    }


    @Then("response contains elements with expected values")
    public void responseContainsElementsWithExpectedValues(Map<String, String> expectedElements) throws IOException {
        JsonNode jsonNode = MAPPER.readTree(scenarioContext.getResponse().getBody().asString());
        for (Map.Entry<String, String> element : expectedElements.entrySet()) {
            String currentValue = jsonNode.findValue(element.getKey()).toString();
            String expectedValue = element.getValue();
            assertThat(currentValue, notNullValue());
            assertThat("Expected: " + currentValue + " to be: " + expectedValue, currentValue, equalTo(expectedValue));
        }
    }

    @Then("^the response matches schema \"(.*)\"")
    public void theResponseMatchesSchema(String schemaName) {
        assertThat(scenarioContext.getResponse().getBody().asString(), matchesJsonSchemaInClasspath(schemaName));
    }

    @And("response is a list of elements with expected values")
    public void responseIsAlistOfElementsWithExpectedValues(List<Map<String, String>> expectedElements) throws IOException {
        JsonNode jsonNode = MAPPER.readTree(scenarioContext.responseAsString());
        assertThat("Request response is not a list.", jsonNode.isArray());
        assertThat("Expected: " + jsonNode.size() + " to be: " + expectedElements.size(), jsonNode.size(), equalTo(expectedElements.size()));
        for (int i = 0; i < expectedElements.size(); i++) {
            for (Map.Entry<String, String> element : expectedElements.get(i).entrySet()) {
                String currentValue = jsonNode.get(i).findValue(element.getKey()).toString();
                String expectedValue = element.getValue();
                assertThat(currentValue, notNullValue());
                assertThat("Expected: " + currentValue + " to be: " + expectedValue, currentValue, equalTo(expectedValue));
            }
        }
    }

    @Then("user will receive status code (\\d+)$")
    public void iWillReceiveStatusCode(int statusCode) {
        assertThat(scenarioContext.getResponse().getStatusCode(), equalTo(statusCode));

    }

    @And("response contains element \"(.*)\" with value \"(.*)\"")
    public void responseContainsElementWithValue(String element, String expectedValue) throws IOException {
        JsonNode jsonNode = MAPPER.readTree(scenarioContext.responseAsString());
        System.out.println("Pretty::: " + jsonNode.toString());
        String currentValue = jsonNode.get(element).toString();
        assertThat("Expected: " + currentValue + " to be: " + expectedValue, currentValue, equalTo(expectedValue));
    }

    @And("response contains element like \"(.*)\" in the response")
    public void responseContainsElement(String elementName) throws IOException {
        JsonNode jsonNode = MAPPER.readTree(scenarioContext.responseAsString());
        assertThat(jsonNode.has(elementName), is(true));
    }


    @And("^user deletes already created tweet$")
    public void userDeletesAlreadyCreatedTweet() {
        restUtilities.makePostWithPathParam(EndPoints.STATUSES_TWEET_DESTROY, "id", scenarioContext.getTweetId());
    }

    @When("^user gets a collection of the most recent tweets$")
    public void userGetsACollectionOfTheMostRecentTweets() {
        Response response = restUtilities.makeGet(EndPoints.STATUSES_USER_TIMELINE);
        scenarioContext.setResponse(response);
        System.out.println("Collection :: " + scenarioContext.getResponse().getBody().prettyPrint());
    }

    @And("tweet IDs are saved")
    public void saveTweetIds() {
        JsonPath jsPath = new JsonPath(scenarioContext.getResponse().getBody().asString());
        scenarioContext.setListOfTweetIds(jsPath.getList("id_str"));
        assertTrue("List of tweet Ids is empty!", !scenarioContext.getListOfTweetIds().isEmpty());

    }

    @And("^user deletes collection of already created tweets by tweet IDs$")
    public void userDeletesAlreadyCreatedTweets() {
        for (String id : scenarioContext.getListOfTweetIds()) {
            restUtilities.makePostWithPathParam(EndPoints.STATUSES_TWEET_DESTROY, "id", id);
            System.out.println("User deleted : " + id);
        }

    }

    @Then("in the response tweet number \"([^\"]*)\" contains following elements$")
    public void fsdfdsresponseContainsElementsWithExpectedValues(int index, Map<String, String> expectedElements) throws IOException {
        JsonNode jsonNode = MAPPER.readTree(scenarioContext.getResponse().getBody().asString());
        JsonNode tweetIndex = jsonNode.findValue("text").get(index);
        for (Map.Entry<String, String> element : expectedElements.entrySet()) {
            String currentValue = tweetIndex.findValue(element.getKey()).toString();
            String expectedValue = element.getValue();
            assertThat(currentValue, notNullValue());
            assertThat("Expected: " + currentValue + " to be: " + expectedValue, currentValue, equalTo(expectedValue));
        }
    }

}
