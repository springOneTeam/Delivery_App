package com.example.outsourcingproject.domain.menu.entity;

import com.example.outsourcingproject.domain.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long menuId;

	// 양방향 관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(nullable = false)
	private String menuName;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private boolean isDeleted;

	@Builder
	public Menu(Store store, String menuName, int price, boolean isDeleted) {
		this.store = store;
		this.menuName = menuName;
		this.price = price;
		this.isDeleted = false;
	}

	public void update(String menuName, int price) {
		this.menuName = menuName;
		this.price = price;
	}

	public void delete() {
		this.isDeleted = true;
	}

}
