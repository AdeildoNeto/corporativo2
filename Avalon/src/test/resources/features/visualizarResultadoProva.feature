# language: pt
# ID.24
Funcionalidade: 
  Eu como aluno
  Quero visualizar o resultado da prova que realizei
  Para conhecer o meu desempenho

  Contexto: 
    Dado que o usuário está logado como aluno
    E esteja na página de resultados da prova

  Cenário: Visualizar resultado de uma prova
    Quando o aluno selecionar a prova que deseja conhecer o resultado
    Então será exibido o resultado da prova
