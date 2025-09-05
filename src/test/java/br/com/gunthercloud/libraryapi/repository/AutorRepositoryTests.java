package br.com.gunthercloud.libraryapi.repository;

import br.com.gunthercloud.libraryapi.model.Autor;
import br.com.gunthercloud.libraryapi.model.GeneroLivro;
import br.com.gunthercloud.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
public class AutorRepositoryTests {
	
	@Autowired
	AutorRepository repository;

	@Test
	void salvarRegistro() {
		Autor a = new Autor();
		a.setNome("Maria");
		a.setNacionalidade("Brasileira");
		a.setDataNascimento(LocalDate.of(2000, 5, 13));
		System.out.println("Autor salvo: " + repository.save(a));
	}
	
	@Test
	void atualizarTest() {
		UUID u = UUID.fromString("e78f992e-3f24-4355-a4f5-782d18fdde50");
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

    @Test
    void listarTest() {
        List<Autor> lista = repository.findAll();
        lista.forEach(System.out::println);
    }

    @Test
    void countTest() {
        System.out.println("Contagem: " + repository.count());
    }

    @Test
    void deletePorIdTest() {
        repository.deleteById(UUID.fromString("d9ac8763-07f0-420f-8f2c-9a3488a4876f"));
    }

    @Test
    void deleteTest() {
        Optional<Autor> autor = repository.findById(UUID.fromString("b9ac62b6-4646-48df-b2d0-8c147a5638ff"));
        autor.ifPresent(value -> repository.delete(value));
    }

    @Test
    void salvarAutorComLivrosTest() {
        Autor a = new Autor();
        a.setNome("Silvano");
        a.setNacionalidade("Estadunidense");
        a.setDataNascimento(LocalDate.of(1198, 12, 12));

        Livro livro = new Livro();
        livro.setIsbn("63473-23458");
        livro.setPreco(BigDecimal.valueOf(42));
        livro.setTitulo("Mushoku Tensei");
        livro.setDataPublicacao(LocalDate.of(2017,5,8));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setAutor(a);

        a.setLivros(new ArrayList<>());
        a.getLivros().add(livro);

        repository.save(a);

    }
}
