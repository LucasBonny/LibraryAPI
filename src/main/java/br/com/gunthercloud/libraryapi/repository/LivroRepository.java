package br.com.gunthercloud.libraryapi.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.com.gunthercloud.libraryapi.model.Autor;
import br.com.gunthercloud.libraryapi.model.GeneroLivro;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gunthercloud.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
*
* */

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

    public List<Livro> findByAutor(Autor autor);

    public List<Livro> findByTituloContaining(String titulo);

    public List<Livro> findByIsbn(String isbn);

    public List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    public List<Livro> findByTituloOrPreco(String titulo, BigDecimal preco);

    public List<Livro> findByDataPublicacaoBetween(LocalDate inicio, LocalDate fim);

    @Query(" select l from Livro l order by l.titulo")
    List<Livro> listarTodosOrdenadoPorTitulo();

    @Query("select a from Livro l join l.autor a")
    List<Autor> listarAutoresDeLivros();

    @Query("""
            select l.genero
            from Livro l
            join l.autor a
            where a.nacionalidade = 'Brasileira'
            order by l.genero
            """)
    List<String> listarGenerosBrasileiros();

    // named parameters ≥ parâmetros nomeados
    @Query("select l from Livro l where l.genero = :genero order by :paramOrdenacao")
    List<Livro> findByGenero(@Param("genero") GeneroLivro genero, @Param("paramOrdenacao") String nomePropriedade);

    // positional parameters ->
    @Query("select l from Livro l where l.genero = ?1 order by ?2")
    List<Livro> findByGeneroPositionalParameters(GeneroLivro genero, String nomePropriedade);

}
