# language: pt
# ID.04
Funcionalidade: 
  Eu como professor
  Quero selecionar questões que cadastrei
  Para imprimir uma prova

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina de provas
    E deseje imprimir uma prova

  Cenário: Imprimir prova discursiva
    Quando o professor selecionar o tipo discursiva no filtro
    E clicar no botao pesquisar
    E selecionar uma questao
    E clicar no botao imprimir
    Então sera exibido em uma nova aba a prova com as questoes discursivas selecionadas

  Cenário: Imprimir prova de verdadeiro ou falso
    Quando o professor optar pelo tipo verdadeiro ou falso no filtro de pesquisa
    E clicar no botao pesquisar
    E selecionar uma questao
    E clicar no botao imprimir
    Então sera exibido em uma nova aba a prova com as questoes de verdadeiro ou falso selecionadas

  Cenário: Imprimir prova de multipla escolha
    Quando o professor optar pelo tipo multipla escolha no filtro de pesquisa
    E clicar no botao pesquisar
    E selecionar uma questao
    E clicar no botao imprimir
    Então sera exibido em uma nova aba a prova com as questoes de multipla escolha selecionadas

