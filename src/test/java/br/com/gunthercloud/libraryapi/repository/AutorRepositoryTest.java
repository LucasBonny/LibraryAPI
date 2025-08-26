package br.com.gunthercloud.libraryapi.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.gunthercloud.libraryapi.model.Autor;

@SpringBootTest
public class AutorRepositoryTest {
	
	@Autowired
	private AutorRepository repository;

	@Test
	void salvarRegistro() {
		Autor a = new Autor();
		a.setNome("Maria");
		a.setNacionalidade("Brasileira");
		a.setDataNascimento(LocalDate.of(2000, 05, 13));
		System.out.println(repository.save(a));
	}
	
	@Test
	void atualizarTest() {
		UUID u = UUID.fromString("78b72dd1-b77c-4e22-bd66-aac24a16c7e7");
		Optional<Autor> autor = repository.findById(u);
		
		if(autor.isPresent()) {
			Autor newUser = new Autor();
			newUser.setId(u);
			newUser.setNome("Marcos");
			newUser.setNacionalidade("Estrangeiro");
			newUser.setDataNascimento(LocalDate.now());
			
			var res = repository.save(newUser);
			
			System.out.println(res);
		}
	}
}
