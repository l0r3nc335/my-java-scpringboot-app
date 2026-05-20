package com.javacore.demo.controller;

import com.javacore.demo.dto.ItemRequest;
import com.javacore.demo.dto.ItemResponse;
import com.javacore.demo.service.ItemService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping
	public List<ItemResponse> list() {
		return itemService.findAll();
	}

	@GetMapping("/{id}")
	public ItemResponse get(@PathVariable Long id) {
		return itemService.findById(id);
	}

	@PostMapping
	public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemRequest request) {
		var created = itemService.create(request);
		return ResponseEntity.created(URI.create("/api/v1/items/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public ItemResponse update(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
		return itemService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		itemService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
