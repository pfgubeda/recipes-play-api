package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Ingredient;
import models.Recipe;
import play.cache.Cached;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.RecipeResource;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private MessagesApi nessagesApi;

    @Inject
    private SyncCacheApi cache;

    public Result create(Http.Request req) {


        Form<RecipeResource> recipeForm = formFactory.form(RecipeResource.class).bindFromRequest(req);
        RecipeResource recipeResource;

        if (recipeForm.hasErrors()) {
            //gestion de errores
            return Results.badRequest(recipeForm.errorsAsJson());
        } else {
            recipeResource = recipeForm.get();
        }
        //check if recipe exists already with that name
        Recipe recipe = Recipe.findByName(recipeResource.getName());
        if (recipe != null) {
            Messages messages = nessagesApi.preferred(req);
            ObjectNode jsonResult = Json.newObject();
            jsonResult.put("error", messages.at("recipe.exists"));
            return Results.badRequest(jsonResult);
        }
        Recipe recipeModel = recipeResource.toModel();
        recipeModel.save(); //Guardar
        return Results.created("Recipe : "+recipeModel.getName()+" created with id: "+recipeModel.getId());
    }
   @Cached(key = "all-recipes-view", duration = 5)
    public Result retrieveAll(Http.Request req) {
        List<Recipe> recipes;
        Optional<List<Recipe>> optRecipes = cache.get("all-recipes");
        if(optRecipes.isPresent()){
            recipes = optRecipes.get();
        }else{
            recipes = Recipe.findAll();
            cache.set("all-recipes", recipes);
        }
       if(recipes.isEmpty()){
           Messages messages = nessagesApi.preferred(req);
           ObjectNode jsonResult = Json.newObject();
           jsonResult.put("error", messages.at("recipe.empty"));
           return Results.badRequest(jsonResult);
       }
        if(req.accepts("application/xml")){
             return ok(views.xml.recipes.render(recipes));

        }else {
            List<RecipeResource> recipeResources = recipes.stream().map(RecipeResource::new).collect(Collectors.toList());
            JsonNode jsonResult = Json.toJson(recipeResources);
            Result res = Results.ok(jsonResult);
            return res;
        }

    }

    public Result retrieveAllNoCached(Http.Request req) {
        List<Recipe> recipes = Recipe.findAll();
        if(recipes.isEmpty()){
            Messages messages = nessagesApi.preferred(req);
            ObjectNode jsonResult = Json.newObject();
            jsonResult.put("error", messages.at("recipe.empty"));
            return Results.badRequest(jsonResult);
        }
        if(req.accepts("application/xml")){
            return ok(views.xml.recipes.render(recipes));

        }else {
            List<RecipeResource> recipeResources = recipes.stream().map(RecipeResource::new).collect(Collectors.toList());
            JsonNode jsonResult = Json.toJson(recipeResources);
            Result res = Results.ok(jsonResult);
            return res;
        }

    }

    public Result retrieveByName(Http.Request req, String name) {
       Recipe recipe = Recipe.findByName(name);
        if (recipe == null) {
            Messages messages = nessagesApi.preferred(req);
            ObjectNode jsonResult = Json.newObject();
            jsonResult.put("error", messages.at("recipe.not.exists"));
            return Results.badRequest(jsonResult);
        }
        List<Recipe> recipes = List.of(recipe);
        if(req.accepts("application/xml")){
            return ok(views.xml.recipes.render(recipes));
        }else {
            RecipeResource recipeResource = new RecipeResource(recipe);
            JsonNode jsonResult = Json.toJson(recipeResource);
            Result res = Results.ok(jsonResult);
            return res;
        }
    }

    public Result updateRecipeByName(Http.Request req, String name) {
        Recipe recipe = Recipe.findByName(name);
        if (recipe == null) {
            Messages messages = nessagesApi.preferred(req);
            ObjectNode jsonResult = Json.newObject();
            jsonResult.put("error", messages.at("recipe.not.exists"));
            return Results.badRequest(jsonResult);
        }
        Form<RecipeResource> recipeForm = formFactory.form(RecipeResource.class).bindFromRequest(req);
        RecipeResource recipeResource;

        if (recipeForm.hasErrors()) {
            //gestion de errores
            return Results.badRequest(recipeForm.errorsAsJson());
        } else {
            recipeResource = recipeForm.get();
        }
        //delete old ingredients
        recipe.getIngredients().forEach(Ingredient::delete);
        //update recipe
        recipe.setIngredients(recipeResource.getIngredients());
        recipe.setName(recipeResource.getName());
        recipe.setPreparationTime(recipeResource.getTime());
        recipe.setDifficulty(recipeResource.getDifficulty());
        recipe.update();
        return Results.ok("Recipe : "+recipe.getName()+" updated");
    }

    public Result deleteByName(Http.Request req, String name) {
        Recipe recipe = Recipe.findByName(name);
        if (recipe == null) {
            Messages messages = nessagesApi.preferred(req);
            ObjectNode jsonResult = Json.newObject();
            jsonResult.put("error", messages.at("recipe.not.exists"));
            return Results.badRequest(jsonResult);
        }
        recipe.delete();
        return Results.ok("Recipe : "+recipe.getName()+" deleted");
    }

    public Result retrieveByDifficulty(Http.Request req, int difficulty) {
        List<Recipe> recipes = Recipe.findByDifficulty(difficulty);
        if(recipes.isEmpty()){
            Messages messages = nessagesApi.preferred(req);
            ObjectNode jsonResult = Json.newObject();
            jsonResult.put("error", messages.at("recipe.empty"));
            return Results.badRequest(jsonResult);
        }
        List<RecipeResource> recipeResources = recipes.stream().map(RecipeResource::new).collect(Collectors.toList());
        JsonNode jsonResult = Json.toJson(recipeResources);
        Result res = Results.ok(jsonResult);
        return res;
    }

    public Result retrieveByMaxPreparationTime(Http.Request req, int maxPreparationTime) {
        List<Recipe> recipes = Recipe.findByMaxPreparationTime(maxPreparationTime);
        if(recipes.isEmpty()){
            Messages messages = nessagesApi.preferred(req);
            ObjectNode jsonResult = Json.newObject();
            jsonResult.put("error", messages.at("recipe.empty"));
            return Results.badRequest(jsonResult);
        }
        List<RecipeResource> recipeResources = recipes.stream().map(RecipeResource::new).collect(Collectors.toList());
        JsonNode jsonResult = Json.toJson(recipeResources);
        Result res = Results.ok(jsonResult);
        return res;
    }

    //Only for testing purposes
    public Result deleteAll(Http.Request req) {
        Recipe.deleteAll();
        return Results.ok("All recipes deleted");
    }

}