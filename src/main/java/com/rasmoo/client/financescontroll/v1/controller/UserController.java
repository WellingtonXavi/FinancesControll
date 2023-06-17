package com.rasmoo.client.financescontroll.v1.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.IUserRepository;
import com.rasmoo.client.financescontroll.v1.vo.Response;
import com.rasmoo.client.financescontroll.v1.vo.UserVO;

@RestController
@RequestMapping(value = "/v1/usuario")
@PreAuthorize(value = "#oauth2.hasScope('cc_logado')")
public class UserController {

	@Autowired
	private IUserRepository userRepository;

	@PostMapping
	public ResponseEntity<Response<User>> cadastrarUsuario(@RequestBody UserVO userVo) {
		Response<User> response = new Response<>();

		try {

			User user = new User();

			user.setNome(userVo.getNome());
			user.getCredencial().setEmail(userVo.getEmail());
			user.getCredencial().setSenha(new BCryptPasswordEncoder().encode(userVo.getPassword()));

			response.setData(this.userRepository.save(user));

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@PatchMapping
	public ResponseEntity<Response<User>> trocarSenha(@RequestBody UserVO userVo)
	{
		Response<User> response = new Response<>();
		
		try {
		
				Optional<User> user = 	this.userRepository.findByEmail(userVo.getEmail());
				
				if(user.isEmpty())
				{
					throw new Exception();
				}
				
				user.get().getCredencial().setSenha(userVo.getPassword());
				
				response.setData(this.userRepository.save(user.get()));
				
				return ResponseEntity.status(HttpStatus.OK).body(response);
		
		}
		catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
