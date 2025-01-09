package com.example.outsourcingproject.domain.store.entity;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.store.dto.StoreUpdateRequestDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;

	@Column(nullable = false)
	private String storeName;

	@Column(nullable = false)
	private String tel;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String openTime;  // LocalTime에서 String으로 변경

	@Column(nullable = false)
	private String closeTime;  // LocalTime에서 String으로 변경

	@Column(nullable = false)
	private int minOrderAmount;

	@Column(nullable = false)
	private boolean isOperating;

	// 양방향 관계
	@OneToMany(mappedBy = "store")
	private List<Menu> menus = new ArrayList<>();

	@OneToMany(mappedBy = "store")
	private List<Review> reviews = new ArrayList<>();

	@OneToMany(mappedBy = "store")
	private List<Order> orders = new ArrayList<>();


	@Builder
	public Store(User owner, String storeName, String tel, String address,
		String openTime, String closeTime, int minOrderAmount,
		boolean isOperating) {
		this.owner = owner;
		this.storeName = storeName;
		this.tel = tel;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minOrderAmount = minOrderAmount;
		this.isOperating = isOperating;
	}

	public void update(StoreUpdateRequestDto dto) {
		this.storeName = dto.storeName();
		this.tel = dto.tel();
		this.address = dto.address();
		this.openTime = dto.openTime();
		this.closeTime = dto.closeTime();
		this.minOrderAmount = dto.minOrderAmount();
		this.isOperating = dto.isOperating();
	}
}