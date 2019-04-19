package geacommerce.repository;

import geacommerce.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(value = "SELECT * FROM products p WHERE p.type = :productType", nativeQuery = true)
    Page<Product> findAll(Pageable pageable, @Param("productType") String productType);

    @Query(value = "SELECT * FROM products p WHERE p.name LIKE %:productName%", nativeQuery = true)
    Page<Product> findAllBySearchInput(Pageable pageable, @Param("productName") String productName);
}
