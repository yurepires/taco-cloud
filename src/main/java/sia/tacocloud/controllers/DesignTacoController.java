package sia.tacocloud.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import sia.tacocloud.entities.Ingredient;
import sia.tacocloud.entities.Ingredient.Type;
import sia.tacocloud.entities.Taco;
import sia.tacocloud.entities.TacoOrder;
import sia.tacocloud.repositories.IngredientRepository;

import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder") // Armazena o objeto "tacoOrder" na sessão
public class DesignTacoController {

    @Autowired
    private IngredientRepository ingredientRepository;

    // ModelAttribute sem nome faz com que o métod seja executado automaticamente antes de qualquer handler(@GetMapping ou @PostMapping)
    // addIngredientsToModel cria uma lista de ingredientes e os agrupa por tipo, armazenando os no Model, permitindo que sejam acessados na view.
    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepository.findAll();
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

    // A anotação valid executa a validação no objeto Taco enviado
    // antes que o métod processTaco() seja chamado.
    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {

        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }

    // Filtra a lista de ingredientes e retorna apenas aqueles que pertencem ao tipo informado
    // É usado no addIngredientsToModel() para agrupar os ingredientes por tipo no Model.
    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
        return StreamSupport.stream(ingredients.spliterator(), false)
                .filter(ingredient -> ingredient.getType().equals(type))
                .toList();
    }
}
