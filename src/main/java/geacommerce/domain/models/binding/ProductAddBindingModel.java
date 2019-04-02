package geacommerce.domain.models.binding;

import geacommerce.domain.entities.ProductManufacturer;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ProductAddBindingModel {

    private String type;
    private String name;
    private ProductManufacturer manufacturer;
    private String status;
    private BigDecimal price;
    private Integer amount;
    private String country;

    public ProductAddBindingModel() {
    }

    @NotNull
    @NotEmpty
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 20)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    @NotNull
    public ProductManufacturer getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(ProductManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @NotNull
    @NotEmpty
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DecimalMin(value = "0.01")
    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull
    @Min(value = 1)
    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @NotEmpty
    @NotNull
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
