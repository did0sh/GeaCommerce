package geacommerce.domain.models.service;

import geacommerce.domain.entities.Product;
import geacommerce.domain.entities.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartServiceModel {

    private String id;
    private User user;
    private Map<String, Product> products;
    private BigDecimal totalPrice;

    public CartServiceModel() {
        this.products = new LinkedHashMap<>();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, Product> getProducts() {
        return this.products;
    }

    public void setProducts(Map<String, Product> products) {
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
