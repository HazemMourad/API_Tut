package org.example;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import junit.framework.Assert;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class UserOperations {

    public JSONObject reqbody= new JSONObject();
    public static  String baseURL="https://petstore.swagger.io/v2";
@Test
    public void userCreation()
    {

        JSONObject reqbody= new JSONObject();
        reqbody.put("id",100);
        reqbody.put("username","user123");
        reqbody.put("lastName", "Hazem");
        reqbody.put("email", "hazem@lds.com");
        reqbody.put("password", "123!");
        reqbody.put("phone", "+0123456789");
        reqbody.put("userStatus", 100);
     Response serverResp=  given().body(reqbody.toJSONString()).contentType("application/json")
               .when().post(baseURL+"/user").then()
               .statusCode(200).log().body().extract().response();
        JsonPath jsonpatheval= serverResp.jsonPath();
        String messageVal= jsonpatheval.get("message");
        System.out.println("Message is "+ messageVal);
        Assert.assertEquals(messageVal,"100");
    }
    @Test
    public void getUserByusername()
    {
        given().pathParam("username" , "user123").when().
                get(baseURL+"/user/{username}").then().
                assertThat().statusCode(200).log().body();
    }
    @Test
    public void UpdateUser()
    {
        reqbody.put("id", 100);
        reqbody.put("username", "user123");
        reqbody.put("firstName", "Akram"); // Change "firstname" to "firstName"
        reqbody.put("lastName", "Hazem");
        reqbody.put("email", "hazem@lds.com");
        reqbody.put("password", "123!!");
        reqbody.put("phone", "+0123456989");
        reqbody.put("userStatus", 100);

        // Sending the request and updating user
        given().contentType("application/json")
                .pathParam("username", "user123")
                .body(reqbody.toString()) // Convert JSON object to string
                .when()
                .put(baseURL + "/user/{username}")
                .then()
                .assertThat().statusCode(200)
                .log().body();
    }
    @Test
    public void createMultipleUsers() {
        int numberOfUsers = 5; // Change this to the desired number of users
        String[] usernames = {"user1", "user2", "user3", "user4", "user5"}; // Example usernames
        String[] lastNames = {"Doe", "Smith", "Johnson", "Brown", "Davis"}; // Example last names
        String[] emails = {"user1@example.com", "user2@example.com", "user3@example.com", "user4@example.com", "user5@example.com"}; // Example emails
        String[] passwords = {"pass1", "pass2", "pass3", "pass4", "pass5"}; // Example passwords
        String[] phones = {"1234567890", "2345678901", "3456789012", "4567890123", "5678901234"}; // Example phones

        for (int i = 0; i < numberOfUsers; i++) {
            reqbody.put("id", i + 1);
            reqbody.put("username", usernames[i]);
            reqbody.put("lastName", lastNames[i]);
            reqbody.put("email", emails[i]);
            reqbody.put("password", passwords[i]);
            reqbody.put("phone", phones[i]);
            reqbody.put("userStatus", 100);

            Response serverResp = given().body(reqbody.toJSONString()).contentType("application/json")
                    .when().post(baseURL + "/user")
                    .then().statusCode(200)
                    .log().body().extract().response();

            JsonPath jsonPathEvaluator = serverResp.jsonPath();
            String messageVal = jsonPathEvaluator.get("message");
            System.out.println("Message for user " + (i + 1) + " is " + messageVal);
            Assert.assertEquals(messageVal, Integer.toString(i + 1));
        }
    }
    @Test
    public  void deleteUser()
    {
        given().pathParam("username","user5").when().
                delete(baseURL+"/user/{username}").then().assertThat().statusCode(200);
    }
}
