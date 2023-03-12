package model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe extends Model {

    private static final Finder<Long, Recipe> find = new Finder<>(Recipe.class);
    @Id
    private Long id;

    private String Name;
    private int PreparationTime;
    private int Difficulty;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentRecipe")
    @JsonIgnore
    private List<Ingredient> ingredients;

    public static Recipe findByName(String name){
        return find.query().where().eq("name", name).findOne();
    }

    public static List<Recipe> findAll(){
        System.out.println("LLEGA AL FINDALL");
        return find.all();
    }





    //getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPreparationTime() {
        return PreparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        PreparationTime = preparationTime;
    }

    public int getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(int difficulty) {
        Difficulty = difficulty;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        if (this.ingredients == null){
            this.ingredients = new ArrayList<Ingredient>();
        }
        this.ingredients.add(ingredient);
    }
}
