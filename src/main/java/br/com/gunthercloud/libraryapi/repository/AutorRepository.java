package br.com.gunthercloud.libraryapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gunthercloud.libraryapi.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

}
