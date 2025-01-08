package com.example.outsourcingproject.domain.store.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.domain.store.dto.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.StoreCreateResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<StoreCreateResponseDto> createStore(
		@AuthenticationPrincipal UserDetails userDetails,
		@Valid @RequestBody StoreCreateRequestDto requestDto
	) {
		Long userId = Long.parseLong(userDetails.getUsername());
		StoreCreateResponseDto responseDto = storeService.createStore(userId, requestDto);
		return ResponseEntity.created(
			URI.create("/api/stores/" + responseDto.storeId())
		).body(responseDto);
	}
}
