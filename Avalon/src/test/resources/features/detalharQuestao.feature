# language: pt
# ID.11
Funcionalidade: 
  Eu como professor
  Quero visualizar os detalhes de uma questão
  Para usá-la em uma prova

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina de provas
    E deseje imprimir uma prova

  Cenário: Exibir detalhe de uma questao discursiva
    Quando o professor selecionar o tipo discursiva no filtro
    E clicar no botao pesquisar
    E clicar no icone de informacao da questao
    Então sera exibido um modal com as informacoes da questao discursiva

  Cenário: Exibir detalhe de uma questao de verdadeiro ou falso
    Quando o professor optar pelo tipo verdadeiro ou falso no filtro de pesquisa
    E clicar no botao pesquisar
    E clicar no icone de informacao da questao
    Então sera exibido um modal com as informacoes da questao de verdadeiro ou falso

  Cenário: Exibir detalhe de uma questao de multipla escolha
    Quando o professor optar pelo tipo multipla escolha no filtro de pesquisa
    E clicar no botao pesquisar
    E clicar no icone de informacao da questao
    Então sera exibido um modal com as informacoes da questao de multipla escolha

