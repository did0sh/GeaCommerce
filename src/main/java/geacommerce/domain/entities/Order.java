package geacommerce.domain.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private User buyer;
    private BigDecimal orderPrice;
    private String status;
    private String deliveryAddress;
    private LocalDateTime orderDate;
    private List<Product> boughtProducts;

    public Order() {
    }

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    public User getBuyer() {
        return this.buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @Column(name = "order_price", nullable = false, precision = 7, scale = 2, columnDefinition = "DECIMAL(7,2)")
    public BigDecimal getOrderPrice() {
        return this.orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    @Column(name = "status", nullable = false)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "delivery_address", nullable = false)
    public String getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Column(name = "order_date", nullable = false)
    public LocalDateTime getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @ManyToMany
    @JoinTable(name = "orders_products", joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
                    inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id"), @JoinColumn(name = "product_amount", referencedColumnName = "cart_amount")})
    public List<Product> getBoughtProducts() {
        return this.boughtProducts;
    }

    public void setBoughtProducts(List<Product> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }
}
