package geacommerce.domain.models.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class OrderViewModel {

    private String id;
    private String clientName;
    private String clientEmail;
    private BigDecimal orderPrice;
    private String status;
    private String deliveryAddress;
    private LocalDateTime orderDate;
    private Map<String, List<String>> formattedProducts;

    public OrderViewModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return this.clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public BigDecimal getOrderPrice() {
        return this.orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Map<String, List<String>> getFormattedProducts() {
        return this.formattedProducts;
    }

    public void setFormattedProducts(Map<String, List<String>> formattedProducts) {
        this.formattedProducts = formattedProducts;
    }
}
