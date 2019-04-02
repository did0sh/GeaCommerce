package geacommerce.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
    private String town;
    private String password;
    private Role role;
    private Cart cart;

    public User() {
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "gender", nullable = false)
    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "phone_number", nullable = false)
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!phoneNumber.startsWith("+359")){
            this.phoneNumber = String.format("+359%s", phoneNumber);
        } else {
            this.phoneNumber = phoneNumber;
        }
    }

    @Column(name = "address", nullable = false)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "town", nullable = false)
    public String getTown() {
        return this.town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @OneToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
