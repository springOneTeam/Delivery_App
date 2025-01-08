package com.example.outsourcingproject.domain.menu.service;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.store.service.StoreService;

@SpringBootTest
class MenuServiceTest {

	@Autowired
	MenuRepository menuRepository;

	@Autowired
	StoreService storeService;

	@Test
	public void test() throws Exception {
		//given

		//when

		//then
//		Assertions.assertThat().isEqualTo();
	}

}