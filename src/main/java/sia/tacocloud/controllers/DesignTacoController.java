package sia.tacocloud.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import sia.tacocloud.model.Ingredient;
import sia.tacocloud.model.Ingredient.Type;
import sia.tacocloud.model.Taco;
import sia.tacocloud.model.TacoOrder;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder") // Armazena o objeto "tacoOrder" na sessão
public class DesignTacoController {

    // ModelAttribute sem nome faz com que o métod seja executado automaticamente antes de qualquer handler(@GetMapping ou @PostMapping)
    // addIngredientsToModel cria uma lista de ingredientes e os agrupa por tipo, armazenando os no Model, permitindo que sejam acessados na view.
    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Salsa", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    // Cria um novo objeto TacoOrder e o coloca no modelo com o nome tacoOrder.
    // Como o @SessionAttributes("tacoOrder") está definido, esse objeto será mantido
    // na sessão do usuário enquanto ele estiver personalizando seu taco.
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    // Cria um objeto Taco e o adiciona ao modelo com o nome "taco",
    // mas esse não é mantido na sessão, apenas na requisição atual.
    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    // Como o Spring já preenche o Model com ingredientes, TacoOrder e Taco,
    // a página terá tudo que precisa para exibir o formulário de personalização do taco.
    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    // Filtra a lista de ingredientes e retorna apenas aqueles que pertencem ao tipo informado
    // É usado no addIngredientsToModel() para agrupar os ingredientes por tipo no Model.
    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getType().equals(type))
                .toList();
    }
}
