package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Recipe extends Model {

    private static final Finder<Long, Recipe> find = new Finder<>(Recipe.class);
    @Id
    private Long id;

    @Unique
    private String name;
    private int preparationTime;
    private int difficulty;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentRecipe")
    @JsonIgnore
    private List<Ingredient> ingredients;

    public static Recipe findByName(String name){
        return find.query().where().eq("name", name).findOne();
    }

    public static List<Recipe> findAll(){
        return find.all();
    }

    public static List<Recipe> findByDifficulty(int difficulty) {
        return find.query().where().eq("difficulty", difficulty).findList();
    }


    //getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name1) {
        name = name1;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime1) {
        preparationTime = preparationTime1;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty1) {
        difficulty = difficulty1;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void deleteIngredients() {
        this.ingredients.clear();
        System.out.println("Ingredients deleted");
    }

    public Ingredient getIngredientByName(String name) {
        for (Ingredient ingredient : this.ingredients) {
            if (ingredient.getName().equals(name)) {
                System.out.println("Ingredient found");
                return ingredient;
            }
        }
        return null;
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }
}
