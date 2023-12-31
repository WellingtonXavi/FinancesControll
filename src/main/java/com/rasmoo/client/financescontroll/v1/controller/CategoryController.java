package com.rasmoo.client.financescontroll.v1.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.client.financescontroll.entity.Category;
import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.ICategoryRepository;
import com.rasmoo.client.financescontroll.repository.IUserRepository;
import com.rasmoo.client.financescontroll.v1.dto.CategoryDTO;
import com.rasmoo.client.financescontroll.v1.vo.Response;

@RestController
@RequestMapping({"/v1/categoria", "/v2/categoria"} )
@CrossOrigin
//@PreAuthorize(value = "#oauth2.hasScope('cw_logado')")
public class CategoryController {

	@Autowired
	private ICategoryRepository categoryRepository;

	@Autowired
	private IUserRepository userRepository;

	private ModelMapper mapper = new ModelMapper();

	@PostMapping
	public ResponseEntity<Response<Category>> cadastrarCategoria(@RequestBody CategoryDTO categoria) {
		Response<Category> response = new Response<>();

		try {

			User user = findAuhtUSer();

			if (categoria != null && categoria.getId() == null) {
				Category categoriaEntity = mapper.map(categoria, Category.class);
				categoriaEntity.setUser(user);
				this.categoryRepository.save(categoriaEntity);
				response.setData(categoriaEntity);
				response.setStatusCode(HttpStatus.CREATED.value());
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
			throw new Exception();
		} catch (Exception e) {
			
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@PutMapping
	public ResponseEntity<Response<Category>> atualizarCategoria(@RequestBody CategoryDTO categoria) {
		Response<Category> response = new Response<>();
		try {
			
			User user = findAuhtUSer();
			if (categoria != null && categoria.getId() != null) {
				Optional<Category> category = this.categoryRepository.findById(categoria.getId());
				if (category.isPresent() && category.get().getUser().getId() == user.getId()) {
					
					Category categoriaEntity = mapper.map(categoria, Category.class);
					categoriaEntity.setUser(user);
					this.categoryRepository.save(categoriaEntity);
					response.setData(categoriaEntity);
					response.setStatusCode(HttpStatus.CREATED.value());
					return ResponseEntity.status(HttpStatus.CREATED).body(response);
				}
			}
			throw new Exception();
		} catch (Exception e) {
		
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@GetMapping
	//@PreAuthorize(value = "#oauth2.hasAnyScope('cc_logado','cw_logado')")
	public ResponseEntity<Response<List<Category>>> listarCategorias() {
		Response<List<Category>> response = new Response<>();
		try {
			
			User user = this.findAuhtUSer();
			
			response.setData(this.categoryRepository.findAllByUserId(user.getId()));
			response.setStatusCode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
		
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<Category>> consultarCategoria(@PathVariable Long id) {
		Response<Category> response = new Response<>();
		try {
			Optional<Category> catogory = this.categoryRepository.findById(id);
			if (catogory.isPresent()) {
				response.setData(catogory.get());
			}
			response.setStatusCode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Boolean>> excluirCategoria(@PathVariable Long id) {
		Response<Boolean> response = new Response<>();
		try {
			if (this.categoryRepository.findById(id).isPresent()) {
				this.categoryRepository.deleteById(id);
				response.setData(Boolean.TRUE);
			}
			response.setStatusCode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	private User findAuhtUSer() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.err.println("AUTENTICACAO " + authentication.getName());
		User user = this.userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new Exception());
		return user;
	}

}
