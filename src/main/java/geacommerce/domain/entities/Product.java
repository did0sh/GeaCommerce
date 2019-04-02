package geacommerce.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String type;
    private String name;
    private ProductManufacturer manufacturer;
    private String status;
    private BigDecimal price;
    private Integer amount;
    private String country;
    private List<Cart> carts;
    private Integer cartAmount;

    public Product() {
        this.carts = new ArrayList<>();
    }

    @Column(name = "type", nullable = false)
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "manufacturer", nullable = false)
    @Enumerated(EnumType.STRING)
    public ProductManufacturer getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(ProductManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Column(name = "status", nullable = false)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "price", nullable = false, precision = 7, scale = 2, columnDefinition = "DECIMAL(7,2)")
    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "amount", nullable = false)
    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Column(name = "country", nullable = false)
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @ManyToMany(mappedBy = "products")
    public List<Cart> getCarts() {
        return this.carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    @Column(name = "cart_amount")
    public Integer getCartAmount() {
        return this.cartAmount;
    }

    public void setCartAmount(Integer cartAmount) {
        this.cartAmount = cartAmount;
    }
}
