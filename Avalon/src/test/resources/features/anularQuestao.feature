# language: pt
Funcionalidade: 
  Eu como professor
  Quero anular uma questão por mim criada
  Para que a nota dos alunos não seja afetada pela mesma

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas questões

  Cenario: Anular questão
    Quando o professor clicar no botão anular questão
    E confirmar a anulação da questão
    Então a questão será anulada e a nota de todas as provas em que foi utilizada será recalculada
