package org.auladevsuperior.dscatalog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.auladevsuperior.dscatalog.dto.ProductDTO;
import org.auladevsuperior.dscatalog.exception.DatabaseException;
import org.auladevsuperior.dscatalog.exception.ResourceNotFoundException;
import org.auladevsuperior.dscatalog.services.ProductService;
import org.auladevsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockitoBean
   private ProductService service;

   @Autowired
   private ObjectMapper objectMapper;

   private Long existingId;
   private Long nonExistingId;
   private Long dependentId;
   private ProductDTO productDTO;
   private PageImpl<ProductDTO> page;

   @BeforeEach
   void setUp() throws Exception{

      existingId = 1L;
      nonExistingId = 2L;
      dependentId = 3L;

      productDTO = Factory.createProductDTO();
      page = new PageImpl<>(List.of(productDTO));

      when(service.findAllPaged(any())).thenReturn(page);

      when(service.findById(existingId)).thenReturn(productDTO);
      when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

      when(service.update(eq(existingId), any())).thenReturn(productDTO);
      when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

      doNothing().when(service).delete(existingId);
      doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
      doThrow(DatabaseException.class).when(service).delete(dependentId);
   }

   @Test
   public void updateShouldReturnProductDTOWhenIdExists() throws Exception{

      String jsonBody = objectMapper.writeValueAsString(productDTO);

      mockMvc.perform(put("/products/{id}", existingId)
                  .content(jsonBody)
                  .contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.description").exists());
   }

   @Test
   public void updateShouldReturnProductDTOWhenIdDoesNotExists() throws Exception{
      String jsonBody = objectMapper.writeValueAsString(productDTO);

      mockMvc.perform(put("/products/{id}", nonExistingId)
                  .content(jsonBody)
                  .contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
   }

   @Test
   public void findAllShouldReturnPage() throws Exception{
      mockMvc.perform(get("/products")).andExpect(status().isOk());
   }

   @Test
   public void findByIdShouldReturnProductWhenIdExists() throws Exception{
      mockMvc.perform(get("/products/{id}", existingId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.description").exists());
   }

   @Test
   public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
      mockMvc.perform(get("/products/{id}", nonExistingId))
            .andExpect(status().isNotFound());
   }
}
