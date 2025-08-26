package br.com.gunthercloud.libraryapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gunthercloud.libraryapi.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

}
