package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Recipe;
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

        Recipe recipeModel = recipeResource.toModel();
        recipeModel.save(); //Guardar
        ObjectNode jsonResult = Json.newObject();
        jsonResult.put("id", recipeModel.getId());
        return Results.created(jsonResult);
    }
    @Cached(key = "all-recipes-view", duration = 5)
    public Result retrieveAll(Http.Request req) {
        List<Recipe> recipes;
        Optional<Object> optRecipes = cache.get("all-recipes");
        if(optRecipes.isPresent()){
            recipes = (List<Recipe>) optRecipes.get();
        }else{
            recipes = Recipe.findAll();
            cache.set("all-recipes", recipes);
        }

        List<RecipeResource> recipeResources = recipes.stream().map(RecipeResource::new).collect(Collectors.toList());
        JsonNode jsonResult = Json.toJson(recipeResources);
        Result res = Results.ok(jsonResult);
        return res;

    }

    public Result retrieveByName(Http.Request req, String name) {
       Recipe recipe = Recipe.findByName(name);
        RecipeResource recipeResource = new RecipeResource(recipe);
        JsonNode jsonResult = Json.toJson(recipeResource);
        Result res = Results.ok(jsonResult);
        return res;
    }

    public Result updateByName(Http.Request req, String name) {
        Recipe recipe = Recipe.findByName(name);
        Form<RecipeResource> recipeForm = formFactory.form(RecipeResource.class).bindFromRequest(req);
        RecipeResource recipeResource;

        if (recipeForm.hasErrors()) {
            //gestion de errores
            return Results.badRequest(recipeForm.errorsAsJson());
        } else {
            recipeResource = recipeForm.get();
        }

        recipeResource.updateModel(recipe);
        recipe.update();
        return Results.ok();
    }

    public Result deleteByName(Http.Request req, String name) {
        Recipe recipe = Recipe.findByName(name);
        recipe.delete();
        return Results.ok();
    }
}