UC00619 — Desenvolvimento em Java — Sessão 11
Exercício 4 — Consultas (JPQL e SQL nativo) e transações
Projeto: library-jpa (continuação do Exercício 3) · Spring Boot 4.1.2 · Java 21 ·  Maven
================================================================

RESULTADO ESPERADO (depois das linhas do Exercício 3)
Books returned: 4
Available (JPQL):
  - Clean Code
  - Effective Java
  - Head First Java
  - The Clean Coder
Borrowed: Clean Code
Borrowed: The Clean Coder
Borrowed: Effective Java
WARN ... Loan failed: Book already borrowed: Clean Code
Available now:
  - Effective Java                ← voltou a estar disponível: rollback
  - Head First Java
Published before 2010 (native SQL): 2