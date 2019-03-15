package common;

import constants.Auth;
import constants.EndPoints;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestUtilities {

    private RequestSpecBuilder requestBuilder;
    private ScenarioContext scenarioContext;

    public RestUtilities(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    public RequestSpecification createRequestSpecification() {
        //We add authorization
        AuthenticationScheme authScheme =
                RestAssured.oauth(Auth.CONSUMER_KEY, Auth.CONSUMER_SECRET, Auth.ACCESS_TOKEN, Auth.ACCESS_SECRET);
        requestBuilder = new RequestSpecBuilder();
        requestBuilder.setAuth(authScheme);
        scenarioContext.getDefaultHeaders().put("Content-Type", "application/json");
        scenarioContext.getDefaultHeaders().put("Accept", "application/json");
        requestBuilder.addHeaders(scenarioContext.getDefaultHeaders());
        return requestBuilder.build();
    }

    public Response makePostWithQueryParam(String path, String parameterName, String parameterValue) {
        return given()
                .log()
                .all()
                .spec(scenarioContext.getRequestSpec())
                .queryParam(parameterName, parameterValue)
                .when()
                .post(path);
    }

    public Response makePostWithPathParam(String path, String parameterName, String parameterValue) {
        return given()
                .log()
                .all()
                .spec(scenarioContext.getRequestSpec())
                .pathParam(parameterName, parameterValue)
                .when()
                .post(path);
    }

    public Response makeGetWithQueryParam(String path, String parameterName, String parameterValue) {
        return given()
                .log()
                .all()
                .spec(scenarioContext.getRequestSpec())
                .queryParam(parameterName, parameterValue)
                .when()
                .get(path);
    }

    public Response makeGet(String path) {
        return given()
                .log()
                .all()
                .spec(scenarioContext.getRequestSpec())
                .when()
                .get(path);
    }

/*    public static ResponseSpecification getResponseSpecification() {
        responseBuilder = new ResponseSpecBuilder();
        responseBuilder.expectResponseTime(lessThan(3L), TimeUnit.SECONDS);
        responseSpec = responseBuilder.build();
        return responseSpec;
    }*/

    public void validateAgainstJsonSchema() {
        given()
                .contentType("application/json")
                .when()
                .get("http://myExample/users")
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath("example_schema.json"));
    }

    public static RequestSpecification createQueryParam(RequestSpecification rspec, String param, String value) {
        return rspec.queryParam(param, value);
    }

    public static RequestSpecification createQueryParam(RequestSpecification rspec, Map<String, String> queryMap) {
        return rspec.queryParams(queryMap);
    }

    public static RequestSpecification createPathParam(RequestSpecification rspec, String param, String value) {
        return rspec.pathParam(param, value);
    }

    public static JsonPath getJsonPath(Response res) {
        String jsPath = res.asString();
        return new JsonPath(jsPath);
    }

    public static XmlPath getXmlPath(Response res) {
        String xmlPath = res.asString();
        return new XmlPath(xmlPath);
    }

    public static void resetBasePath() {
        RestAssured.basePath = null;
    }

    public static void setContentType(ContentType type) {
        given().contentType(type);
    }
}
