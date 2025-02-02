package tw.brad.stest5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.brad.stest5.model.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long>{

}
