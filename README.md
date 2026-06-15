# ES2_FluxoCompletoDeSoftware
Esse repositório é dedicado à atividade avaliativa da disciplina Engenharia de Software 2, centrada em recriar o processo de desenvolvimento de software do começo ao fim, desde o desenvolvimento de código até versionamento e automatização de testes, criando uma interface front-end que acessa um back-end com banco de dados completamente funcional.

Uma versão funcional do site está disponível [aqui](https://clinica-frontend-pb5y.onrender.com/) até 14/07: 

# Clínica Web - Material Educacional

Sistema de Clínica Web para demonstração do ciclo completo de desenvolvimento de software.

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17 + Spring Boot 3.2 |
| Frontend | React 18 + React Router |
| Banco de Dados | PostgreSQL 15 |
| Build Backend | Maven |
| Build Frontend | Node.js 20 + npm |
| Versionamento | Git + GitHub |
| Containers | Docker + Docker Compose |
| Produção | Render |

## Estrutura do Projeto

```
agenda-web/
├── backend/           # API REST (Java/Spring Boot)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
├── frontend/          # UI (React)
│   ├── package.json
│   ├── Dockerfile
│   └── src/
└── docker-compose.yml
```

## Como Executar (Desenvolvimento)

```bash
# Usando Docker Compose
docker-compose up -d

# Backend disponível em: http://localhost:8080
# Frontend disponível em: http://localhost:3000
```

## Como Executar Testes

```bash
# Backend (JUnit 5 + Mockito)
cd backend
mvn test

# Frontend (Jest)
cd frontend
npm test
```
