package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.RestaurantService;

import java.util.List;

public class RestaurantCLI extends AbstractCLI {

    private final RestaurantService restaurantService = new RestaurantService();

    public Restaurant displayRestaurant() {
        this.ln("Choisissez un restaurant:");

        List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();
        if (allRestaurants == null || allRestaurants.isEmpty()) {
            this.ln("Aucun restaurant disponible.");
            return null;
        }

        for (int i = 0; i < allRestaurants.size(); i++) {
            Restaurant restaurant = allRestaurants.get(i);
            this.ln(String.format("%d. %s.", i, restaurant.getName()));
        }

        int index = this.readIntFromUser(allRestaurants.size() - 1);
        return allRestaurants.get(index);
    }
}
