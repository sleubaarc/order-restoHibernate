package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.CustomerService;
import ch.hearc.ig.orderresto.service.OrderService;
import ch.hearc.ig.orderresto.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderCLI extends AbstractCLI {

    private final OrderService orderService = new OrderService();
    private final CustomerService customerService = new CustomerService();

    public Order createNewOrder() {
        System.out.println("======================================================");

        Restaurant restaurant = (new RestaurantCLI()).displayRestaurant();

        Product product = (new ProductCLI()).displayRestaurantProduct(restaurant);

        this.ln("Voulez-vous commander à emporter ou sur place (E/P)?");
        String takeAway = this.readChoicesFromUser(new String[]{"E", "P"}).equals("E") ? "O" : "N";


        System.out.println("======================================================");
        System.out.println("0. Annuler");
        System.out.println("1. Je suis un client existant");
        System.out.println("2. Je suis un nouveau client");

        int userChoice = readIntFromUser(2);
        if (userChoice == 0) {
            throw new NullPointerException("Annulation de la commande, retour au menu principal");
        }

        CustomerCLI customerCLI = new CustomerCLI();
        Customer customer;
        if (userChoice == 1) {
            customer = customerCLI.displayExistingCustomer();
        } else {
            customer = customerCLI.enterNewCustomer();
        }

        Order order = orderService.createOrder(customer, restaurant, product, takeAway);

        orderService.insertOrder(order);

        System.out.println("Merci pour votre commande!");

        return order;
    }

    public Order selectOrder() {
        Customer customer = (new CustomerCLI()).displayExistingCustomer();

        List<Order> orders = orderService.getOrdersByCustomer(customer);

        if (orders.isEmpty()) {
            String customerEmail = customerService.getCustomerEmail(customer);
            System.out.println(String.format("Désolé, il n'y a aucune commande pour %s", customerEmail));
            return null;
        }

        System.out.println("Choisissez une commande:");
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println(String.format("%d. %s", i, orderService.getFormattedOrderInfo(order)));
        }

        int index = readIntFromUser(orders.size() - 1);
        return orders.get(index);
    }

    public void displayOrder(Order order) {
        String formattedDate = orderService.getOrderDate(order);
        String restaurantName = orderService.getOrderRestaurantName(order);
        BigDecimal totalAmount = orderService.getOrderTotalAmount(order);
        String takeAway = order.getTakeAway().equalsIgnoreCase("O") ? ", à emporter" : ", à consommer sur place";

        System.out.println(String.format("Commande %.2f, le %s chez %s:", totalAmount, formattedDate, restaurantName));

        List<String> productsInfo = orderService.getFormattedProductsInfo(order);
        for (String productInfo : productsInfo) {
            System.out.println(productInfo + takeAway);
        }
    }
}
