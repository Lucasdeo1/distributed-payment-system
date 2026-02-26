# 💳 Sistema de Análise de Risco de Pagamento

API backend que simula o processamento de transações financeiras com
análise de risco antifraude.

Este projeto foi desenvolvido como parte do meu processo de aprendizado
em backend com Java e Spring Boot, simulando um cenário próximo ao
utilizado por bancos digitais e fintechs para autorizar transações.

------------------------------------------------------------------------

# 🎯 Visão Geral

O sistema representa um **motor de decisão para transações
financeiras**, responsável por:

-   Receber solicitações de pagamento\
-   Executar análise de risco baseada em regras\
-   Classificar a transação\
-   Permitir decisão automática ou manual\
-   Controlar o fluxo de estados da transação

O objetivo foi simular **os dois lados do processo**:

-   👤 Cliente → cria pagamentos\
-   🕵️ Analista → acompanha, analisa e decide

------------------------------------------------------------------------

# 🖥️ Interfaces Disponíveis

## 👤 Página do Cliente

http://localhost:8080/cliente

Permite criar novos pagamentos informando valor e descrição.

## 🕵️ Página do Analista

http://localhost:8080

Permite visualizar transações, acompanhar logs, executar análise manual
e aprovar ou rejeitar pagamentos.

Obs: Fiz desta maneira porque inicialmente estava tudo no arquivo index.hmtl porém precisei simular um cliente/usuário fazendo uma transação e o analista com o papel de verificar os valores entrando no sistema e posteriormente realizar a decisão de aprovar ou rejeitar(dependendo da análise dele) tem um terceiro botão de decisão automática caso seja valores menores(nada suspeito) porém futuramente pretendo automatizar essa posição do analista( o que seria ideal em um cenário de transação onde tudo precisa ser rápido e validado sem interferência humana)
------------------------------------------------------------------------

# 🏦 Cenário Simulado

O sistema simula o fluxo interno de autorização de uma transação
financeira, como:

-   PIX\
-   Transferência bancária\
-   Pagamento online

### Fluxo

1.  Cliente cria uma transação\
2.  O sistema calcula um score de risco\
3.  Se o risco for alto, a transação vai para revisão\
4.  O analista pode aprovar ou rejeitar\
5.  Se o risco for baixo, a aprovação pode ser automática

------------------------------------------------------------------------

# 🧠 Regras de Análise de Risco

  Regra                            Pontuação
  -------------------------------- -----------
  Valor maior que 5000             +50
  Transação entre 22h e 6h         +40
  Palavra "urgente" na descrição   +20

### Decisão automática

-   Se score \>= 70 → REJECTED\
-   Caso contrário → APPROVED

------------------------------------------------------------------------

# 🔄 Fluxo de Estados

-   PENDING → transação criada\
-   UNDER_REVIEW → análise executada\
-   APPROVED → autorizada\
-   REJECTED → bloqueada

------------------------------------------------------------------------

# 🏗️ Arquitetura

controller → service → repository → domain → dto

-   Controller → Camada HTTP\
-   Service → Regras de negócio\
-   Repository → Persistência\
-   Domain → Entidades e enums\
-   DTO → Comunicação externa

------------------------------------------------------------------------

# 🚀 Tecnologias Utilizadas

-   Java 17\
-   Spring Boot\
-   Spring Data JPA\
-   H2 Database\
-   Maven\
-   JUnit 5\
-   Lombok

------------------------------------------------------------------------

# ▶️ Como Executar

## Pré-requisitos

-   Java 17\
-   Maven

## Clonar o repositório

git clone https://github.com/Lucasdeo1/distributed-payment-system.git\
cd distributed-payment-system/payment-service-java

## Executar

Windows: mvnw.cmd spring-boot:run

Linux/Mac: ./mvnw spring-boot:run

Aplicação disponível em:

http://localhost:8080

------------------------------------------------------------------------

# 🧪 Executar Testes

mvn test

------------------------------------------------------------------------

# 🎓 Propósito do Projeto

Este projeto foi desenvolvido com o objetivo de:

-   Consolidar conhecimentos em Spring Boot\
-   Estruturar aplicações backend em camadas\
-   Modelar workflow de estados\
-   Implementar regras de negócio\
-   Escrever testes automatizados\
-   Simular um cenário próximo ao mercado financeiro

Projeto criado com foco em portfólio para vaga de desenvolvedor backend
júnior.

------------------------------------------------------------------------

# 📌 Melhorias Futuras

-   Dockerização\
-   Swagger\
-   Spring Security\
-   PostgreSQL\
-   CI/CD

------------------------------------------------------------------------

# 👨‍💻 Autor

Lucas Deodato\
https://github.com/Lucasdeo1
