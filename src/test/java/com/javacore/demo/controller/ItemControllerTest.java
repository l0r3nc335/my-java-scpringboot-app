package com.javacore.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacore.JavaCoreApplication;
import com.javacore.demo.dto.ItemRequest;
import com.javacore.demo.dto.ItemResponse;
import com.javacore.demo.service.ItemService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ItemController.class)
@Import(JavaCoreApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ItemService itemService;

	@Test
	void listReturnsItems() throws Exception {
		when(itemService.findAll()).thenReturn(List.of(new ItemResponse(1L, "Sample Item", "Seeded data")));

		mockMvc.perform(get("/api/v1/items"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Sample Item"));
	}

	@Test
	void createReturnsCreated() throws Exception {
		var request = new ItemRequest("New Item", "Description");
		when(itemService.create(any(ItemRequest.class))).thenReturn(new ItemResponse(2L, "New Item", "Description"));

		mockMvc.perform(post("/api/v1/items")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.name").value("New Item"));
	}

	@Test
	void getReturnsItem() throws Exception {
		when(itemService.findById(1L)).thenReturn(new ItemResponse(1L, "Sample Item", "Seeded data"));

		mockMvc.perform(get("/api/v1/items/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Sample Item"));
	}

}
