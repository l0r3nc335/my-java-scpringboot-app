package com.javacore.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
		@NotBlank String name,
		String description) {
}
