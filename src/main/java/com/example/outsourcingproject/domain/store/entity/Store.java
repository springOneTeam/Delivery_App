package com.example.outsourcingproject.domain.store.entity;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.user.entity.User;
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
	private LocalTime openTime;

	@Column(nullable = false)
	private LocalTime closeTime;

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
	private Store(
		User owner,
		String storeName,
		String tel,
		String address,
		LocalTime openTime,
		LocalTime closeTime,
		int minOrderAmount,
		boolean isOperating
	) {
		this.owner = owner;
		this.storeName = storeName;
		this.tel = tel;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minOrderAmount = minOrderAmount;
		this.isOperating = isOperating;
	}

	// 비즈니스 로직
	public void validateOwner(User user) {
		if (!this.owner.equals(user)) {
			throw new IllegalArgumentException("해당 가게의 사장님이 아닙니다.");
		}
	}

	public void closeStore() {
		this.isOperating = false;
	}
}