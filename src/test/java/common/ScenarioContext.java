package common;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class ScenarioContext {

    private Response response;
    private String tweetId;
    private List<String> listOfTweetIds;

    public List<String> getListOfTweetIds() {
        return listOfTweetIds;
    }

    public void setListOfTweetIds(List<String> listOfTweetIds) {
        this.listOfTweetIds = listOfTweetIds;
    }

    private RequestSpecification requestSpec;

    public RequestSpecification getRequestSpec() {
        return requestSpec;
    }

    public void setRequestSpec(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String responseAsString () {
        return  response.getBody().asString();
    }
}
