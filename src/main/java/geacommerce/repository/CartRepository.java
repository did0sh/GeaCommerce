package geacommerce.repository;

import geacommerce.domain.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    @Modifying
    @Transactional
    @Query(value = "DELETE cp FROM carts_products cp " +
            "JOIN carts c " +
            "on c.id = cp.cart_id " +
            "WHERE cp.product_id = :productId", nativeQuery = true)
    void deleteCartProduct(@Param("productId") String id);

    @Query(value = "SELECT count(cp.product_id) FROM carts_products cp " +
            "WHERE cp.cart_id = :cartId", nativeQuery = true)
    Integer getCountOfProductsInCart(@Param("cartId") String cartId);
}
