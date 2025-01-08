package com.example.outsourcingproject.domain.order.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.order.enums.OrderStatus;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="orders")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int cart; //음식 수량

	private int totalAmount;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime ordertime;

	public Order(User user, Store store, Menu menu, OrderStatus orderStatus, int cart){
		this.user=user;
		this.store=store;
		this.menu=menu;
		this.orderStatus=orderStatus;
		this.cart=cart;
		this.totalAmount= menu.getPrice();
	}
}
