package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.RestaurantMapper;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class RestaurantService {

    private final RestaurantMapper restaurantMapper = new RestaurantMapper();

    public List<Restaurant> getAllRestaurants() {
        try (Connection connection = ConnectionDb.getInstance().getConnection()) {
            Restaurant[] restaurants = restaurantMapper.findRestaurants(connection);
            return Arrays.asList(restaurants);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des restaurants.");
            return null;
        }
        finally {
            ConnectionDb.getInstance().releaseConnection();
        }
    }

        public String getRestaurantName(Restaurant restaurant) {
        return restaurant.getName();
    }

}
