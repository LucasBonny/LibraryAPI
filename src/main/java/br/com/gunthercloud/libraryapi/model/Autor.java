package br.com.gunthercloud.libraryapi.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_autor", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "nacionalidade", length = 50, nullable = false)
	private LocalDate nacionalidade;
	
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;
	
	@OneToMany(mappedBy = "autor")
	private List<Livro> livros;
}
