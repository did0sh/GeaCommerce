package geacommerce.domain.models.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.ProductManufacturer;

import java.math.BigDecimal;

public class ProductServiceModel {

    private String id;
    private String type;
    private String name;
    private ProductManufacturer manufacturer;
    private String status;
    private BigDecimal price;
    private Integer amount;
    private String country;
    private Cart cart;

    public ProductServiceModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductManufacturer getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(ProductManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
