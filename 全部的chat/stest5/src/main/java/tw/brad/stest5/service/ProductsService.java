package tw.brad.stest5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tw.brad.stest5.model.Products;
import tw.brad.stest5.repository.ProductRepository;

@Service
public class ProductsService {
	@Autowired
	private ProductRepository productRepository;
	
	public List<Products> getProducts(){
		Sort sort = Sort.by("Categories.categoryId").ascending();
		return productRepository.findAll(sort);
	}
}
