package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.ProductService;
import ch.hearc.ig.orderresto.service.RestaurantService;

import java.util.List;

public class ProductCLI extends AbstractCLI {

    private final ProductService productService = new ProductService();
    private final RestaurantService restaurantService = new RestaurantService();

    public Product displayRestaurantProduct(Restaurant restaurant) {
        String restaurantName = restaurantService.getRestaurantName(restaurant);
        this.ln(String.format("Bienvenue chez %s. Choisissez un de nos produits:", restaurantName));

        List<Product> products = productService.getProductsByRestaurant(restaurant);
        if (products == null || products.isEmpty()) {
            this.ln("Aucun produit disponible pour ce restaurant.");
            return null;
        }

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            this.ln(String.format("%d. %s", i, product.getName()));
        }

        int index = this.readIntFromUser(products.size() - 1);
        return products.get(index);
    }
}
