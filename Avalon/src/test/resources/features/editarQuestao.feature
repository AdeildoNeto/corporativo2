# language: pt
Funcionalidade: 
  Eu como professor
  Quero editar questões por mim criadas
  Para que eu possa corrigí-las

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas questões

  Cenario: Editar enunciado da questão
    Quando o professor selecionar uma questão para edição
    E alterar o enunciado da questão
    E clicar no botão salvar questão
    E confirmar a edição da questão
    Então o enunciado da questão será alterado

  Cenario: Criticar enunciado da questão duplicado para edição
    Quando o professor selecionar uma questão para edição
    E alterar o enunciado com um valor já existente para o tipo selecionado
    E clicar no botão salvar questão
    E confirmar a edição da questão
    Então será exibida a mensagem "Já existe uma questão com este enunciado."

  Cenario: Editar resposta da questão de verdadeiro ou falso
    Quando o professor selecionar uma questão de verdadeiro ou falso para edição
    E alterar a respota da questão
    E clicar no botão salvar questão
    E confirmar a edição da questão
    Então a respota da questão será alterada

  Cenario: Editar resposta da questão de múltipla escolha
    Quando o professor selecionar uma questão de múltipla escolha para edição
    E alterar a alternativa correta da questão
    E clicar no botão salvar questão
    E confirmar a edição da questão
    Então a alternativa correta da questão será alterada
