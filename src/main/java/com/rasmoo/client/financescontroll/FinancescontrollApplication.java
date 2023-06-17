package com.rasmoo.client.financescontroll;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rasmoo.client.financescontroll.entity.Category;
import com.rasmoo.client.financescontroll.entity.Credential;
import com.rasmoo.client.financescontroll.entity.Entry;
import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.ICategoryRepository;
import com.rasmoo.client.financescontroll.repository.IEntryRepository;
import com.rasmoo.client.financescontroll.repository.IUserRepository;
import com.rasmoo.client.financescontroll.v1.constant.TypeEnum;

@SpringBootApplication
public class FinancescontrollApplication extends SpringBootServletInitializer implements CommandLineRunner{
	
	@Autowired
	private ICategoryRepository categoryRepository;
	
	@Autowired
	private IEntryRepository entryRepository;
	
	@Autowired
	IUserRepository userRepository;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FinancescontrollApplication.class);
	}
	

	
	public static void main(String[] args) {
		SpringApplication.run(FinancescontrollApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//System.err.println(new BCryptPasswordEncoder().encode("rasmoo"));
		
		Credential credential = new Credential();
		credential.setEmail("wellington@hotmail.com");
		credential.setSenha(new BCryptPasswordEncoder().encode("123456"));
		
		Credential credential2 = new Credential();
		credential2.setEmail("bia@hotmail.com");
		credential2.setSenha(new BCryptPasswordEncoder().encode("123456"));
		
		User user = new User();
		user.setCredencial(credential);
		user.setNome("Wellington Xavier");
		
		User user2 = new User();
		user2.setCredencial(credential2);
		user2.setNome("Bianca Dias");
		
		this.userRepository.saveAll(Arrays.asList(user,user2));
		
		
		Category c = new Category();
		c.setDescricao("Passeio ao cinema");
		c.setNome("Entretenimento");
		c.setUser(user);
		
		Category c1 = new Category();
		c1.setDescricao("Esádio de futebol");
		c1.setNome("Entretenimento");
		c1.setUser(user);
		
		Category c3 = new Category();
		c3.setDescricao("Curso de Maquiagem e unhas");
		c3.setNome("Curso de maquiagem");
		c3.setUser(user2);
		
		
		this.categoryRepository.saveAll(Arrays.asList(c,c1,c3));
		
		
		Entry entry = new Entry();
		entry.setCategoria(c);
		entry.setTipo(TypeEnum.DESPESA);
		entry.setValor(new BigDecimal(200));
		entry.setUser(user);
		
		Entry entry1 = new Entry();
		entry1.setCategoria(c1);
		entry1.setTipo(TypeEnum.DESPESA);
		entry1.setValor(new BigDecimal(250));
		entry1.setUser(user);
		
		this.entryRepository.saveAll(Arrays.asList(entry,entry1));
		
		
		
	}

}
