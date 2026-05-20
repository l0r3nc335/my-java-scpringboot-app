package com.javacore.demo.service;

import com.javacore.common.exception.ResourceNotFoundException;
import com.javacore.demo.dto.ItemRequest;
import com.javacore.demo.dto.ItemResponse;
import com.javacore.demo.entity.Item;
import com.javacore.demo.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

	private static final Logger log = LoggerFactory.getLogger(ItemService.class);

	private final ItemRepository itemRepository;

	@Transactional(readOnly = true)
	public List<ItemResponse> findAll() {
		return itemRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public ItemResponse findById(Long id) {
		return toResponse(getItem(id));
	}

	@Transactional
	public ItemResponse create(ItemRequest request) {
		var item = Item.builder().name(request.name()).description(request.description()).build();
		var saved = itemRepository.save(item);
		log.info("Created item id={}", saved.getId());
		return toResponse(saved);
	}

	@Transactional
	public ItemResponse update(Long id, ItemRequest request) {
		var item = getItem(id);
		item.setName(request.name());
		item.setDescription(request.description());
		return toResponse(itemRepository.save(item));
	}

	@Transactional
	public void delete(Long id) {
		if (!itemRepository.existsById(id)) {
			throw new ResourceNotFoundException("Item not found: " + id);
		}
		itemRepository.deleteById(id);
		log.info("Deleted item id={}", id);
	}

	private Item getItem(Long id) {
		return itemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));
	}

	private ItemResponse toResponse(Item item) {
		return new ItemResponse(item.getId(), item.getName(), item.getDescription());
	}

}
