# language: pt
Funcionalidade: 
  Eu como professor
  Quero excluir uma turma por mim criada
  Para que não seja possível associá-la a novas provas.

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas turmas

  Cenario: Excluir turma
    Quando o professor clicar no botão excluir turma
    E confirmar a exclusão da turma
    Então a turma será desativada e não poderá mais ser utilizada
