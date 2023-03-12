package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Ingredient extends Model {
    private static final Finder<Long, User> find = new Finder<>(User.class);
    @Id
    private Long id;

    private String name;

    @ManyToOne
    @JsonIgnore
    private Recipe parentRecipe;

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
}
