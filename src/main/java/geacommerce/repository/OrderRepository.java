package geacommerce.repository;

import geacommerce.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {


    @Query(value = "SELECT o.id, p.name, op.product_amount " +
            "FROM orders_products op " +
            "JOIN orders o " +
            "ON o.id = op.order_id " +
            "JOIN products p " +
            "ON p.id = op.product_id", nativeQuery = true)
    Object[][] getOrderProductAmountAndProductName();
}
