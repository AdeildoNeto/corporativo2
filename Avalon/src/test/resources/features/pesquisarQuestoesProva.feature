# language: pt
# ID.10 - (A história ID.07 foi substituida por esta.)
Funcionalidade: 
  Eu como professor
  Quero pesquisar questões
  Para utiliza-las na geração de uma prova

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de provas
    E deseje imprimir uma prova

  Cenário: Pesquisar questoes por enunciado
    Quando o professor preencher o filtro enunciado
    E clicar no botão pesquisar
    Então será exibido a lista de questoes onde o enunciado contenha o filtro informado

  Cenário: Pesquisar questoes por professor
    Quando o professor preencher o filtro nome do professor
    E clicar no botão pesquisar
    Então será exibido a lista de questoes onde o nome do autor contenha o filtro informado

  Cenário: Pesquisar questoes por componente curricular
    Quando o professor selecionar um componente curricular no filtro
    E clicar no botão pesquisar
    Então será exibido a lista de questoes do componente curricular informado

  Cenário: Pesquisar questoes por tipo discursiva
    Quando o professor selecionar o tipo discursiva no filtro
    E clicar no botão pesquisar
    Então será exibido a lista de questoes do tipo discursiva

  Cenário: Pesquisar questoes por tipo verdadeiro ou falso
    Quando o professor optar pelo tipo verdadeiro ou falso no filtro de pesquisa
    E clicar no botão pesquisar
    Então será exibido a lista de questoes do tipo verdadeiro ou falso

  Cenário: Pesquisar questoes por tipo múltipla escolha
    Quando o professor optar pelo tipo múltipla escolha no filtro de pesquisa
    E clicar no botão pesquisar
    Então será exibido a lista de questoes do tipo múltipla escolha

  Cenário: Pesquisar questoes nao cadastradas
    Quando o professor preencher o filtro enunciado com um enunciado nao existente
    E clicar no botão pesquisar
    Então será exibido a mensagem sem resultados para o filtro informado