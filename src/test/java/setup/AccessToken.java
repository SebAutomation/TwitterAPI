package setup;

import static io.restassured.RestAssured.given;

public class AccessToken {

    private AccessToken(){}

    public static String get(String environmentUrl, String username, String password){
        return given()
                .auth().preemptive().basic(username, password)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("grant_type=client_credentials")
                .when().post(environmentUrl + "/security/oauth2/token").getBody().jsonPath().getString("access_token");

    }
}
