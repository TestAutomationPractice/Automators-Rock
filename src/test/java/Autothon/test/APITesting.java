package Autothon.test;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import org.testng.annotations.Test;

import AutomatorsRock.Test.Base;
import DTO.CommonTestDataDto;
import Miscellaneous.*;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.AddMoviePage;
import Pages.MoviePage;
import pages.Quote;

public class APITesting extends Base {
	
	public APITesting() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "API Testing", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-004")
	public void apiTesting() throws Throwable, Exception {
        String token = null, environment, TokenURL, domain, sSheet, username, password;
        RestTemplate restTemplate;
        HttpHeaders headers;
        MultiValueMap<String, String> map = null;
        HttpEntity<MultiValueMap<String, String>> requestEntity;
        ResponseEntity<Quote> quote;
        String jsonBody = "{\"username\" : \""+userName+"\",\"password\" : \"" + password + "\"}";
        try {
               restTemplate = new RestTemplate();
               headers = new HttpHeaders();
               headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
               HttpEntity<MultiValueMap<String, String>>
               requestEntity1 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
               quote = restTemplate.exchange(new URI("http://autothon-nagarro-backend-b04.azurewebsites.net/login"),
                            HttpMethod.POST, requestEntity1, Pages.Quote.class);
               token = quote.getBody().getToken();
        } catch (Exception e) {

        }
        
        static String token ="";
        public static void getToken(String[] args) {
               
               RestTemplate restTemplate;
               HttpHeaders headers;
               MultiValueMap<String, String> map = null;
               HttpEntity<MultiValueMap<String, String>> requestEntity;
               ResponseEntity<Quote> quote;
               String jsonBody = "{\"username\" : \"admin\",\"password\" : \"password\"}";
               try {
                      restTemplate = new RestTemplate();
                      headers = new HttpHeaders();
                      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                      HttpEntity<MultiValueMap<String, String>>
                      requestEntity1 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                      quote = restTemplate.exchange(new URI("http://autothon-nagarro-backend-b04.azurewebsites.net/login"),
                                   HttpMethod.POST, requestEntity1, Quote.class);
                      token = quote.getBody().getToken();
               } catch (Exception e) {

               }
        }
        
        
        public static void addMovie(String[] args) {

               RestTemplate restTemplate;
               HttpHeaders headers;
               MultiValueMap<String, String> map = null;
               HttpEntity<MultiValueMap<String, String>> requestEntity;
               ResponseEntity<Quote> quote;
               String jsonBody = "{\"movie\":\n" + 
                            "{\n" + 
                            "      \"rating\":5,\n" + 
                            "      \"title\":\"Test Test Test\",\n" + 
                            "      \"description\":\"Nice movie nice\",\n" + 
                            "      \"categories\":[\"Comedy\"],\n" + 
                            "      \"image\":\"http://autothon-nagarro-frontend-b04.azurewebsites.net/addMovie\",\n" + 
                            "      \"director\":\"Test Test Test\",\n" + 
                            "      \"reviews\":[],\n" + 
                            "      \"rented\":false\n" + 
                            "}\n" + 
                            "}";
               try {
                      map.add("Content-Type", "application/json");
                      map.add("user", token);
                      restTemplate = new RestTemplate();
                      headers = new HttpHeaders();
                      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                      HttpEntity<MultiValueMap<String, String>>
                      requestEntity1 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                      quote = restTemplate.exchange(new URI("http://autothon-nagarro-backend-b04.azurewebsites.net/login"),
                                   HttpMethod.POST, requestEntity1, Quote.class);
                      token = quote.getBody().getToken();
               } catch (Exception e) {

               }
        }

 }
}
