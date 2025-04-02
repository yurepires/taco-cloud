package sia.tacocloud.repositories;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.entities.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
