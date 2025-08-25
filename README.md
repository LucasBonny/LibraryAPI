# LibraryAPI

## Sumário

- [Novos conhecimentos](#novos-conhecimentos)
- [Banco de dados PostgreSQL com Docker](#banco-de-dados-postgresql-com-docker)
	- [Explicação do comando](#explicação-do-comando)
- [PGAdmin4 Client com Docker](#pgadmin4-client-com-docker)
- [Comunicação entre containers](#comunicação-entre-containers)
	- [Criando a network](#criando-a-network)
	- [Criando containers com a network](#criando-containers-com-a-network)
- [Criando scripts para o banco de dados](#criando-scripts-para-o-banco-de-dados)
- [Configurando o datasource para o banco de dados](#configurando-o-datasource-para-o-banco-de-dados)
- [Criando pool de conexão com o banco de dados](#criando-pool-de-conexão-com-o-banco-de-dados)
- [Mapeamento das tabelas](#mapeamento-das-tabelas)
- [Criando tabelas no banco de dados automático](#criando-tabelas-no-banco-de-dados-automático)

## Novos conhecimentos

- [x] Docker básico
- [x] HikariCP básico

## Banco de dados PostgreSQL com Docker

Para utilizar o Docker para provisionar um banco de dados para nosso projeto, devemos passar o seguinte comando:
```bash
# COMANDO - PARAMETROS - NOME DA IMAGEM
docker run --name librarydb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=library -d postgres:16.3
```
### Explicação do comando
- docker run: `Comando para criar um novo container`
- --name: `Nome do container`
- -p: `Porta externa:Porta Interna`
- -e: `Variável de ambiente`
- -d: `Roda em stand-by`
- postgres:16.3: `Nome da imagem:Versão`

Após a criação do container, podemos acessar a base de dados.

## PGAdmin4 Client com Docker
```bash
# COMANDO - PARAMETROS - NOME DA IMAGEM
docker run --name pgadmin4 -p 15432:80 -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=admin dpage/pgadmin4:9.7.0
```
Após a execução do comando do `PGAdmin` podemos acessar em nosso navegador a seguinte URL: `http://localhost:15432` pois a porta foi passado no comando de criação do container.

Já dentro dele ao tentar acessar o banco de dados do Postgres, podemos observar que não é possível acessar diretamente pois cada container tem seu localhost e a gente apenas disponibiliza para o uso em nossa máquina e não entre containers.

## Comunicação entre containers
Para conseguirmos conexão entre os containers será necessário criar uma network fazendo que ambos a utilizem.

### Criando a network
```bash
docker network create library-network
```
E para isso será necessário a remoção dos containers criado para que possamos recriá-los com a mesma network. Basta usar o seguinte comando:
```bash
docker container rm pgadmin4
docker container rm librarydb
```
### Criando containers com a network
```bash
# COMANDO - PARAMETROS - NOME DA IMAGEM
docker run --name librarydb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=library --network library-network postgres:16.3

# COMANDO - PARAMETROS - NOME DA IMAGEM
docker run --name pgadmin4 -p 15432:80 -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=admin --network library-network dpage/pgadmin4:9.7.0
```
e se por acaso der erro de porta, devemos fazer uma checagem de portas se estão em uso:
```bash
netstat -aof | findstr 5432
```
## Criando scripts para o banco de dados

```sql
create table tb_autor(
	id uuid primary key,
	nome varchar(100) not null,
	data_nascimento date not null,
	nacionalidade varchar(50) not null
);

create table tb_livro (
	id uuid not null primary key,
	isbn varchar(20) not null,
	titulo varchar(150) not null,
	data_publicacao date not null,
	genero varchar(30) not null,
	preco numeric(18,2),
	id_autor uuid not null references tb_autor(id),
	constraint chk_genero check(genero in ('FICCAO', 'FANTASIA','MISTERIO', 'ROMANCE', 'BIOGRAFIA', 'CIENCIA'))
);

select * from tb_autor;

select * from tb_livro;
```
## Configurando o datasource para o banco de dados
Será necessário alterar o arquivo `application.yml` para inserir as informações do banco de dados dentro da aplicação.
```yml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/library
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
```
Com essa configuração a conexão foi estabilizada com o banco.

## Criando pool de conexão com o banco de dados

Para isso será necessário criar um bean para instanciar a conexão e com isso podemos gerenciar o que exatamente esse pool de conexão terá de permissão.

```java
@Value("${spring.datasource.url}")
String url;
@Value("${spring.datasource.username}")
String username;
@Value("${spring.datasource.password}")
String password;
@Value("${spring.datasource.driver-class-name}")
String driver;

@Bean
DataSource hikariDataSource() {
	HikariConfig config = new HikariConfig();
	config.setJdbcUrl(url);
	config.setUsername(username);
	config.setPassword(password);
	config.setDriverClassName(driver);

	config.setMaximumPoolSize(10); // Liberar no máximo 10 conexões
	config.setMinimumIdle(1); // Tamanho inicial com o pool de 1 até 10 conexões
	config.setPoolName("nome-da-db"); // (opcional) nome que irá aparecer no console
	config.setMaxLifetime(600000); // durar até 10 min a conexão
	config.setConnectionTimeout(150000);  // tempo para conseguir uma conexão
	config.setConnectionTestQuery("select 1"); // teste de conexão 

	return new HikariDataSource(config);
}
```
> [!IMPORTANT]
> Essa configuração é opcional, ao configurar os dados dentro do `application.yml` já irá estabelecer uma conexão com o banco de dados utilizando o HikariCP. 

Podemos ver a documentação do HikariCP [aqui](https://github.com/brettwooldridge/HikariCP).

## Mapeamento das tabelas

```java
@Table(
	schema = "public" // se caso tenha mais de 1 schema, podemos definir outro (opcional)
)
public class Livro {
	@Column(
		// Valores presentes na tabela
		precision = 18, // até 18 numeros
		scale = 2 // 2 numeros decimais
	)
	private BigDecimal preco;

	@Enumerated(EnumType.STRING) // necessária para informar o tipo de dado que será armazenado
	@Column(name = "genero", length = 30, nullable = false)
	private GeneroLivro genero;

	@ManyToOne // Pode ter muitos livros para 1 autor
	@JoinColumn(name = "id_autor") // criar uma na nova coluna
	private Autor autor; 
}

@Table(
	schema = "public" // se caso tenha mais de 1 schema, podemos definir outro (opcional)
)
public class Autor {
	@OneToManny(mappedBy = "autor")
	private List<Livro> livros; 
}

```

## Criando tabelas no banco de dados automático
Para fazer a criação das tabelas de acordo com o mapeamento feito dentro das classes, será necessário abrir o `application.yml` e colocar: 
```yml
spring:
  jpa:
    show-sql: true # Mostrar os SQL executados
    hibernate:
      ddl-auto: update # Gerar e manter as tabelas no DB
```
> [!IMPORTANT]
> Não devemos usar o DDL(Data Definiton Language) em produção pois podemos afetar o serviço em produção.