package br.com.gunthercloud.libraryapi.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_autor", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "livros")
public class Autor {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "nacionalidade", length = 50, nullable = false)
	private String nacionalidade;
	
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;
	
	@OneToMany(mappedBy = "autor",cascade = CascadeType.ALL)
	private List<Livro> livros;
}
