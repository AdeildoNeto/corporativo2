# language: pt
# ID.11
Funcionalidade: 
  Eu como professor
  Quero visualizar os detalhes de uma questão
  Para usá-la em uma prova

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de provas
    E deseje imprimir uma prova

  Cenário: Exibir detalhe de uma questão discursiva
    Quando o professor selecionar o tipo discursiva no filtro
    E clicar no botão pesquisar
    E clicar no ícone de informação da questão
    Então será exibido um modal com as informações da questão discursiva

  Cenário: Exibir detalhe de uma questão de verdadeiro ou falso
    Quando o professor optar pelo tipo verdadeiro ou falso no filtro de pesquisa
    E clicar no botão pesquisar
    E clicar no ícone de informação da questão
    Então será exibido um modal com as informações da questão de verdadeiro ou falso

  Cenário: Exibir detalhe de uma questão de múltipla escolha
    Quando o professor optar pelo tipo múltipla escolha no filtro de pesquisa
    E clicar no botão pesquisar
    E clicar no ícone de informação da questão
    Então será exibido um modal com as informações da questão de múltipla escolha

