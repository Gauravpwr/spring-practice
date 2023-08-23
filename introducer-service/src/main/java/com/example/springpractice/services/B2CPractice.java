package com.example.springpractice.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import com.microsoft.aad.msal4j.*;

import java.net.MalformedURLException;
import java.util.Collections;

public class B2CPractice {
    public static void main(String[] args) throws Exception {
        String accessToken = getAccessToken();


        // Define user attributes
        String username = "newuser@example.com";
        String password = "P@ssw0rd";
        String givenName = "John";
        String surname = "Doe";
        String displayName = givenName + " " + surname;

//        String userCreationPayload = "{" +
//                "\"accountEnabled\": true," +
//                "\"signInNames\": [{\"type\": \"emailAddress\", \"value\": \"" + username + "\"}]," +
//                "\"creationType\": \"LocalAccount\"," +
//                "\"passwordProfile\": {\"password\": \"" + password + "\", \"forceChangePasswordNextLogin\": false}," +
//                "\"displayName\": \"" + displayName + "\"," +
//                "\"givenName\": \"" + givenName + "\"," +
//                "\"surname\": \"" + surname + "\"" +
//                "}";
        String userCreationPayload = "{" +
                "  \"accountEnabled\": true,\n" +
                "  \"displayName\": \"Adele Vance\",\n" +
                "  \"mailNickname\": \"AdeleV\",\n" +
                "  \"userPrincipalName\": \"AdeleV@hdxcapitalapp.onmicrosoft.com\",\n" +
                "  \"passwordProfile\" : {\n" +
                "    \"forceChangePasswordNextSignIn\": true,\n" +
                "    \"password\": \"xWwvJ]6NMw+bWH-d\"\n" +
                "  }" +
                "}";

        HttpPost post = new HttpPost("https://graph.microsoft.com/v1.0/users");
        post.setHeader("Authorization", "Bearer " + accessToken);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(userCreationPayload));

        // Execute the HTTP request and handle the response
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(post);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 201) {
            System.out.println("User created successfully.");
        } else {
            System.err.println("Failed to create user. Status code: " + statusCode);
        }
    }

    private static String getAccessToken() throws MalformedURLException {
        // Replace with your Azure AD B2C configuration
        String tenantId = "c3fc0dea-e1c4-4ad9-9753-c5e1111cbb87";
        String clientId = "e96d09dd-1541-4b4c-a424-ac13720e38f9";
        String clientSecret = "yG48Q~5-g1gC~oN3TbnU0UYpcw3GufTRYfeilbHT";
//        String authority = "https://hdxcapitalapp.b2clogin.com/hdxcapitalapp.onmicrosoft.com/B2C_1A_signup_signin/";
        String authority = "https://login.microsoftonline.com/" + tenantId;
        // Create the MSAL application object
        ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                        clientId,
                        ClientCredentialFactory.createFromSecret(clientSecret))
                .authority(authority)
                .build();

        // Create the user
        IAuthenticationResult result = app.acquireToken(ClientCredentialParameters.builder(Collections.singleton("https://graph.microsoft.com/.default")).build()).join();
        String accessToken = result.accessToken();
        return accessToken;
    }
}

