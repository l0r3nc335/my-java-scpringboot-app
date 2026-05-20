package com.javacore.config;

import com.javacore.demo.entity.Item;
import com.javacore.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

	private final ItemRepository itemRepository;

	@Bean
	@Profile("default")
	CommandLineRunner seedItems() {
		return args -> {
			if (itemRepository.count() == 0) {
				itemRepository.save(Item.builder().name("Sample Item").description("Seeded data").build());
			}
		};
	}

}
