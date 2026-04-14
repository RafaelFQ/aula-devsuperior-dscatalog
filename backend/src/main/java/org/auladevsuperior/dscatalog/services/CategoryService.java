package org.auladevsuperior.dscatalog.services;

import org.auladevsuperior.dscatalog.dto.CategoryDTO;
import org.auladevsuperior.dscatalog.entities.Category;
import org.auladevsuperior.dscatalog.exception.ResourceNotFoundException;
import org.auladevsuperior.dscatalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

   @Autowired
   private CategoryRepository repository;

   @Transactional(readOnly = true)
   public List<CategoryDTO> findAll(){
      List<Category> list = repository.findAll();
      return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
   }

   @Transactional(readOnly = true)
   public CategoryDTO findById(Long id) {
      Optional<Category> obj = repository.findById(id);
      Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada."));
      return new CategoryDTO(entity);
   }
}
