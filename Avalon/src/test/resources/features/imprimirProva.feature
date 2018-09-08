# language: pt
# ID.04
Funcionalidade: 
  Eu como professor
  Quero selecionar questões que cadastrei
  Para imprimir uma prova

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de provas
    E deseje imprimir uma prova

  Cenário: Imprimir prova discursiva
    Quando o professor selecionar o tipo discursiva no filtro
    E clicar no botão pesquisar
    E selecionar uma questão
    E clicar no botão imprimir
    Então será exibido em uma nova aba a prova com as questões discursivas selecionadas

  Cenário: Imprimir prova de verdadeiro ou falso
    Quando o professor optar pelo tipo verdadeiro ou falso no filtro de pesquisa
    E clicar no botão pesquisar
    E selecionar uma questão
    E clicar no botão imprimir
    Então será exibido em uma nova aba a prova com as questões de verdadeiro ou falso selecionadas

  Cenário: Imprimir prova de múltipla escolha
    Quando o professor optar pelo tipo múltipla escolha no filtro de pesquisa
    E clicar no botão pesquisar
    E selecionar uma questão
    E clicar no botão imprimir
    Então será exibido em uma nova aba a prova com as questões de múltipla escolha selecionadas

