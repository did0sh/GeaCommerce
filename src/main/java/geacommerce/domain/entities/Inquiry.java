package geacommerce.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "inquiries")
public class Inquiry extends BaseEntity {

    private String userName;
    private String userEmail;
    private String message;

    public Inquiry() {
    }

    @Column(name = "user_name", nullable = false)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "user_email", nullable = false, unique = true)
    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Column(name = "message", nullable = false, length = 500)
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
