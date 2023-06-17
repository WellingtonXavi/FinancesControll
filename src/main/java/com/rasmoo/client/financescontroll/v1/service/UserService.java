package com.rasmoo.client.financescontroll.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rasmoo.client.financescontroll.core.impl.ResourceOwner;
import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.IUserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private IUserRepository  userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = this.userRepository.findByEmail(username)
				.orElseThrow( () -> new UsernameNotFoundException("Usuário ou senha incorretos."));
		
		
		
		return new ResourceOwner(user);
	}

}
