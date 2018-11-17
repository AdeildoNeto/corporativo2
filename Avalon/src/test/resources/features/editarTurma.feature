# language: pt
Funcionalidade: 
  Eu como professor
  Quero editar uma turma por mim criada
  Para adicionar ou remover alunos

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas turmas

  Cenario: Editar turma
    Quando o professor clicar no botão editar turma
    E alterar o nome da turma
    E clicar no botão salvar edição da turma
    Então as alterações da turma serão efetivadas
