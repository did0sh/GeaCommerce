package geacommerce.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    private User user;
    private Map<String, Product>  products;
    private BigDecimal totalPrice;

    public Cart() {
        this.products = new LinkedHashMap<>();
    }

    @OneToOne(mappedBy = "cart")
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToMany
    @JoinTable(name = "carts_products", joinColumns = {@JoinColumn(name = "cart_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    public Map<String, Product> getProducts() {
        return this.products;
    }

    public void setProducts(Map<String, Product>  products) {
        this.products = products;
    }

    @Column(name = "total_price", nullable = false)
    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
