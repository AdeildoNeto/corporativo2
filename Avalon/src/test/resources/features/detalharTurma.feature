# language: pt
Funcionalidade: 
  Eu como professor
  Quero detalhar uma turma por mim criada
  Para visualizar seus alunos

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas turmas

  Cenario: Detalhar turma
    Quando o professor clicar no botão detalhar turma
    Então será exibido a lista de alunos que pertecem a turma selecionada
