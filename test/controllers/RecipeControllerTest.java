package controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import play.test.*;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;


public class RecipeControllerTest extends WithApplication{
    Application fakeAppWithMemoryDb = fakeApplication(inMemoryDatabase("testeando"));

    @Before
    public void setUp() {
        start(fakeAppWithMemoryDb);
    }

    @After
    public void tearDown() {
        stop(fakeAppWithMemoryDb);
    }
    @Test
    public void testCreate() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        Result result = route(app, request);
        System.out.println(contentAsString(result));
        assertEquals(CREATED, result.status());
    }

    @Test
    public void testCreateWithErrors() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void testCreateErrorAlreadyExists() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, request);
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void retrieveAllNoCacheXML() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveAllNoCached())
                .method("GET")
                .header("Accept", "application/xml");
        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertEquals("application/xml", result.contentType().get());
    }

    @Test
    public void retrieveAllNoCacheXMLNoExists() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveAllNoCached())
                .method("GET")
                .header("Accept", "application/xml");
        Result result = route(app, request);

        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void retrieveAllNoCacheJSON() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveAllNoCached())
                .method("GET")
                .header("Accept", "application/json");
        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertEquals("application/json", result.contentType().get());
    }
    @Test
    public void retrieveAllNoCacheJSONNoExists() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveAllNoCached())
                .method("GET");
        Result result = route(app, request);

        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void retrieveByNameXML() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveByName("Macarrones con Tomate"))
                .method("GET")
                .header("Accept", "application/xml");
        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertEquals("application/xml", result.contentType().get());
    }
    @Test
    public void retrieveByNameXMLNoExists() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveByName("Macarrones con Tomate"))
                .method("GET")
                .header("Accept", "application/xml");
        Result result = route(app, request);

        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void retrieveByNameJSON() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveByName("Macarrones con Tomate"))
                .method("GET")
                .header("Accept", "application/json");
        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertEquals("application/json", result.contentType().get());
    }
    @Test
    public void retrieveByNameJSONNoExists() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveByName("Macarrones con Tomate"))
                .method("GET")
                .header("Accept", "application/json");
        Result result = route(app, request);

        assertEquals(BAD_REQUEST, result.status());
        assertEquals("application/json", result.contentType().get());
    }

    @Test
    public void updateRecipeByName() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.updateRecipeByName("Macarrones con Tomate"))
                .method("PUT")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate sin Cebolla\",\n" +
                        "    \"time\" : 60,\n" +
                        "    \"difficulty\" : 6,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        Result result = route(app, request);
        assertEquals(OK, result.status());
        Http.RequestBuilder request2 = Helpers.fakeRequest(routes.RecipeController.retrieveByName("Macarrones con Tomate sin Cebolla"))
                .method("GET")
                .header("Accept", "application/json");
        Result result2 = route(app, request2);
        assertEquals(OK, result2.status());
        assertEquals("{\"name\":\"Macarrones con Tomate sin Cebolla\",\"time\":60,\"difficulty\":6,\"ingredients\":[{\"name\":\"gr macarrones\",\"quantity\":400},{\"name\":\"gr tomate triturado\",\"quantity\":800},{\"name\":\"cucharaditas de azucar\",\"quantity\":1},{\"name\":\"gr sal\",\"quantity\":5}]}", contentAsString(result2));
    }
    @Test
    public void updateRecipeByNameNoExists() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.updateRecipeByName("Macarrones con Tomate"))
                .method("PUT")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate sin Cebolla\",\n" +
                        "    \"time\" : 60,\n" +
                        "    \"difficulty\" : 6,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void updateRecipeByNameWithFormErrors() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.updateRecipeByName("Macarrones con Tomate"))
                .method("PUT")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"\",\n" +
                        "    \"time\" : 60,\n" +
                        "    \"difficulty\" : 6,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void deleteByName() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.deleteByName("Macarrones con Tomate"))
                .method("DELETE");
        Result result = route(app, request);
        assertEquals(OK, result.status());
        Http.RequestBuilder request2 = Helpers.fakeRequest(routes.RecipeController.retrieveByName("Macarrones con Tomate"))
                .method("GET")
                .header("Accept", "application/json");
        Result result2 = route(app, request2);
        assertEquals(BAD_REQUEST, result2.status());
    }
    @Test
    public void deleteByNameNoExists() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.deleteByName("Macarrones con Tomate"))
                .method("DELETE");
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void retrieveByDifficulty() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveByDifficulty(3))
                .method("GET")
                .header("Accept", "application/json");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
    @Test
    public void retrieveByDifficultyNoExists() throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveByDifficulty(3))
                .method("GET")
                .header("Accept", "application/json");
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }
    @Test
    public void retrieveByMaxPreparationTime()throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder create = Helpers.fakeRequest(routes.RecipeController.create())
                .method("POST")
                .bodyJson(Json.parse("{\n" +
                        "    \"name\" : \"Macarrones con Tomate\",\n" +
                        "    \"time\" : 30,\n" +
                        "    \"difficulty\" : 3,\n" +
                        "    \"ingredients\" : [\n" +
                        "        {\"name\":\"gr macarrones\",\n" +
                        "        \"quantity\":400},\n" +
                        "        {\"name\":\"cebolla\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr tomate triturado\",\n" +
                        "        \"quantity\": 800},\n" +
                        "        {\"name\":\"cucharaditas de azucar\",\n" +
                        "        \"quantity\": 1},\n" +
                        "        {\"name\":\"gr sal\",\n" +
                        "        \"quantity\": 5}\n" +
                        "    ]\n" +
                        "}"))
                .header("Content-Type", "application/json");
        route(app, create);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveByMaxPreparationTime(30))
                .method("GET")
                .header("Accept", "application/json");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
    @Test
    public void retrieveByMaxPreparationTimeNoExists()throws Exception{
        Http.RequestBuilder delete = Helpers.fakeRequest(routes.RecipeController.deleteAll());
        route(app, delete);
        Http.RequestBuilder request = Helpers.fakeRequest(routes.RecipeController.retrieveByMaxPreparationTime(30))
                .method("GET")
                .header("Accept", "application/json");
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }
}
