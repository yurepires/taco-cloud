package sia.tacocloud.repositories;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.entities.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder, String> {
}
