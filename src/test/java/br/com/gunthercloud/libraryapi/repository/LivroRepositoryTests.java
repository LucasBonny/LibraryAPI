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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class LivroRepositoryTests {

    @Autowired
    LivroRepository repository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTest() {
        Livro livro = new Livro();
        livro.setIsbn("42324-23451");
        livro.setPreco(BigDecimal.valueOf(12.1));
        livro.setTitulo("UFO");
        livro.setDataPublicacao(LocalDate.now());
        livro.setGenero(GeneroLivro.FICCAO);
        Optional<Autor> autor = autorRepository.findById(UUID.fromString("e78f992e-3f24-4355-a4f5-782d18fdde50"));
        autor.ifPresent(livro::setAutor);
        repository.save(livro);
    }

    @Test
    void salvarComAutorTest() {
        Livro livro = new Livro();
        livro.setIsbn("22222-11111");
        livro.setPreco(BigDecimal.valueOf(12.1));
        livro.setTitulo("Outro Livro");
        livro.setDataPublicacao(LocalDate.of(2011,1,1));
        livro.setGenero(GeneroLivro.FICCAO);

        Autor newUser = new Autor();
        newUser.setNome("Joao");
        newUser.setNacionalidade("Brasineiro");
        newUser.setDataNascimento(LocalDate.of(2000,1,1));

        autorRepository.save(newUser);

        livro.setAutor(newUser);

        repository.save(livro);
    }
    @Test
    void salvarCascadeTest() {
        Livro livro = new Livro();
        livro.setIsbn("22222-11111");
        livro.setPreco(BigDecimal.valueOf(12.1));
        livro.setTitulo("Outro Livro");
        livro.setDataPublicacao(LocalDate.of(2011,1,1));
        livro.setGenero(GeneroLivro.FICCAO);

        Autor newUser = new Autor();
        newUser.setNome("Joao");
        newUser.setNacionalidade("Brasileiro");
        newUser.setDataNascimento(LocalDate.of(2000,1,1));

        livro.setAutor(newUser);

        repository.save(livro);
    }

    @Test
    void livroAtualizarTest() {
        var livroParaAtualizar = repository.findById(UUID.fromString("ae0a4b85-8b29-4489-ae72-cf03658f4079"));
        if(livroParaAtualizar.isPresent()) {
            Autor nAutor = autorRepository.findById(UUID.fromString("a07199c1-8132-4ef8-b86d-ddeb64ce4358")).orElseThrow();
            Livro livro = livroParaAtualizar.get();
            livro.setAutor(nAutor);
            livro.setGenero(GeneroLivro.BIOGRAFIA);
            livro.setTitulo("Deu a louca na chapéuzinho");
            livro.setPreco(BigDecimal.valueOf(200));
            repository.save(livro);

        }
    }

    @Test
    void pesquisaPorTituloTest() {
        List<Livro> livros = repository.findByTituloContaining("Mushoku");
        livros.forEach(System.out::println);
    }

    @Test
    void pesquisaPorTituloEPrecoTest() {
        List<Livro> livros = repository.findByTituloAndPreco("Mushoku Tensei", BigDecimal.valueOf(42));
        livros.forEach(System.out::println);
    }

    @Test
    void listarTodosQueryTest() {
        List<Livro> livros = repository.listarTodosOrdenadoPorTitulo();
        livros.forEach(System.out::println);
    }

    @Test
    void listarTodosComLivroQueryTest() {
        var res = repository.listarAutoresDeLivros();
        res.forEach(System.out::println);
    }

    @Test
    void listarGenerosQueryTest() {
        var res = repository.listarGenerosBrasileiros();
        res.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryParamTest() {
        var res = repository.findByGenero(GeneroLivro.FICCAO, "titulo");
        res.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryPositionalParamTest() {
        var res = repository.findByGenero(GeneroLivro.MISTERIO, "preco");
        res.forEach(System.out::println);
    }
}
