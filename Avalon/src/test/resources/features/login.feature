# language: pt
Funcionalidade: Realizar login
  Eu como usuário
  Quero realizar login na aplicação
  Para utilizar a aplicação

  Contexto: 
    Dado que o usuário esteja na tela de login
    E o usuário clica no botão Acessar

  Cenário: Logar como professor
    Quando utilizar uma conta Google com o email institucional
    Então redireciona para a tela inicial do professor

  Cenário: Logar como aluno
    Quando utilizar uma conta Google com o email diferente do institucional
    Então redireciona para a tela inicial do aluno