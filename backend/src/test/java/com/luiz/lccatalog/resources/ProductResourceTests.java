package com.luiz.lccatalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luiz.lccatalog.dto.ProductDTO;
import com.luiz.lccatalog.services.ProductService;
import com.luiz.lccatalog.services.execptions.DatabaseException;
import com.luiz.lccatalog.services.execptions.ResourceNotFoundException;
import com.luiz.lccatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistsId;
    private Long dependentId;
    private ProductDTO productDTO;
    protected PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistsId = 2L;
        dependentId = 3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistsId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistsId), any())).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(productDTO);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistsId);
        doThrow(DatabaseException.class).when(service).delete(dependentId);

    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(delete("/products/{id}", nonExistsId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenDependentId() throws Exception {
        mockMvc.perform(delete("/products/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void insertShouldReturnProductDtoCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {

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
    public void updateShouldReturnNotFoundProductDtoWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", nonExistsId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotIdExists() throws Exception {
        mockMvc.perform(get("/products/{id}", nonExistsId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
