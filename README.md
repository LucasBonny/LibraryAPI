# LibraryAPI

## Novos conhecimentos

- [ ] Docker básico

## Banco de dados PostgreSQL com Docker

Para utilizar o Docker para provisionar um banco de dados para nosso projeto, devemos passar o seguinte comando:
```bash
docker run --name librarydb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=library postgres:16.3
```
### Explicação do comando
- docker run: `Comando para criar um novo container`
- --name: `Nome do container`
- -p: `Porta externa:Porta Interna`
- -e: `Variável de ambiente`
- postgres:16.3: `Nome da imagem:Versão`

Após a criação do container, podemos acessar a base de dados.

## PGAdmin4 Client com Docker
```bash
docker run --name pgadmin4 -p 15432:80 -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=admin dpage/pgadmin4:9.7.0
```
Após a execução do comando do `PGAdmin` podemos acessar em nosso navegador a seguinte URL: `http://localhost:15432` pois a porta foi passado no comando de criação do container.

Já dentro dele ao tentar acessar o banco de dados do Postgres, podemos observar que não é possível acessar diretamente pois cada container tem seu localhost e a gente apenas disponibiliza para o uso em nossa máquina e não entre containers.