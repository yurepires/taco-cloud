package sia.tacocloud.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;

    @Size(min = 1, message = "You must choose at least 1 ingredient")
    @ManyToMany
    private List<Ingredient> ingredients = new ArrayList<>();

    private LocalDateTime createdAt;

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
}
