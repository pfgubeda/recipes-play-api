package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

@Entity
public class Ingredient extends Model {
    private static final Finder<Long, Recipe> find = new Finder<>(Recipe.class);
    @Id
    private Long id;


    private String name;
    private int quantity;

    @ManyToOne
    @JsonIgnore
    private Recipe parentRecipe;

    public Ingredient(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.quantity = ingredient.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Recipe getParentRecipe() {
        return parentRecipe;
    }
    public void setParentRecipe(Recipe parentRecipe) {
        this.parentRecipe = parentRecipe;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateIngredient( Ingredient ingredient) {
        this.name = ingredient.getName();
        this.quantity = ingredient.getQuantity();
    }
}
