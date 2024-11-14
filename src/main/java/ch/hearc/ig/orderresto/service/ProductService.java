package ch.hearc.ig.orderresto.service;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.ProductMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductMapper productMapper = new ProductMapper();

    public List<Product> getProductsByRestaurant(Restaurant restaurant) {
        List<Product> allProducts = new ArrayList<>();
        try (Connection connection = ConnectionDb.getInstance().getConnection()){
            allProducts = productMapper.findAllProducts(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des produits pour le restaurant " + restaurant.getName());
            return new ArrayList<>();
        }
        finally {
            ConnectionDb.getInstance().releaseConnection();}

        return allProducts.stream()
                .filter(product -> product.getRestaurant().getId().equals(restaurant.getId()))
                .collect(Collectors.toList());
    }
}
