package views;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Ingredient;
import models.Recipe;
import org.hibernate.validator.constraints.Range;
import play.libs.Json;
import play.data.validation.Constraints;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class RecipeResource {
    @JsonProperty("name")
    @Constraints.Required
    @NotBlank(message = "name empty")
    private String name;

    @JsonProperty("time")
    @Constraints.Required(message = "time empty")
    private Integer time;

    @JsonProperty("difficulty")
    @Constraints.Required(message = "difficulty empty")
    @Range(min = 1, max = 10, message = "difficulty must be between 1 and 10")
    private Integer difficulty;

    @JsonProperty("ingredients")
    @Constraints.Required
    private List<Ingredient> ingredients;

    public RecipeResource() {
    }
    public RecipeResource(Recipe recipe) {
        super();
        this.name = recipe.getName();
        this.time = recipe.getPreparationTime();
        this.difficulty = recipe.getDifficulty();
        this.ingredients = recipe.getIngredients();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getTime() {
        return time;
    }
    public void setTime(Integer time) {
        this.time = time;
    }
    public Integer getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public Recipe toModel(){
        Recipe r = new Recipe();
        r.setName(this.name);
        r.setPreparationTime(this.time);
        r.setDifficulty(this.difficulty);
        r.setIngredients(this.ingredients);
        return r;
    }

    public JsonNode toJson(){
        JsonNode json = Json.toJson(this);
        return json;
    }

    public void updateModel(Recipe recipe) {
        recipe.setName(this.name);
        recipe.setPreparationTime(this.time);
        recipe.setDifficulty(this.difficulty);
        recipe.setIngredients(this.ingredients);
    }

    public Ingredient getIngredientByName(String name) {
        for (Ingredient ingredient : this.ingredients) {
            if (ingredient.getName().equals(name)) {
                return ingredient;
            }
        }
        return null;
    }
}
