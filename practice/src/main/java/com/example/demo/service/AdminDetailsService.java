package com.example.demo.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.repository.Adminrepository;

@Service
public class AdminDetailsService implements UserDetailsService {

	@Autowired
	private Adminrepository adminRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Admin admin = adminRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("メールアドレスが見つかりません: " + email));

		return new User(
				admin.getEmail(),
				admin.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
	}
}
