package org.auladevsuperior.dscatalog.repositories;

import org.auladevsuperior.dscatalog.entities.Product;
import org.auladevsuperior.dscatalog.repository.ProductRepository;
import org.auladevsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

   @Autowired
   private ProductRepository repository;

   private long existingId;
   private long countTotalProducts;

   @BeforeEach
   void setUp() throws Exception{
      existingId = 1L;
      countTotalProducts = 25L;
   }

   @Test
   public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
      Product product = Factory.createProduct();
      product.setId(null);

      repository.save(product);

      Assertions.assertNotNull(product.getId());
      Assertions.assertEquals(countTotalProducts + 1, product.getId());
   }

   @Test
   public void deleteShouldDeleteObjectWhenIdExists(){

      repository.deleteById(1L);

      Optional<Product> result = repository.findById(existingId);
      Assertions.assertFalse(result.isPresent());
   }

   @Test
   public void findByIdShouldReturnNonEmptyOptionalProductWhenIdExists(){
      Optional<Product> result = repository.findById(existingId);
      Assertions.assertTrue(result.isPresent());
   }

}
