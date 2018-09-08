# language: pt
# ID.10 - (A história ID.07 foi substituida por esta.)
# ID.13
Funcionalidade: 
  Eu como professor
  Quero pesquisar questões
  Para utiliza-las na geração de uma prova

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de provas
    E deseje imprimir uma prova

  Cenário: Pesquisar questões por enunciado
    Quando o professor preencher o filtro enunciado
    E clicar no botão pesquisar
    Então será exibido a lista de questões onde o enunciado contenha o filtro informado

  Cenário: Pesquisar questões por professor
    Quando o professor preencher o filtro nome do professor
    E clicar no botão pesquisar
    Então será exibido a lista de questões onde o nome do autor contenha o filtro informado

  Cenário: Pesquisar questões por componente curricular
    Quando o professor selecionar um componente curricular no filtro
    E clicar no botão pesquisar
    Então será exibido a lista de questões do componente curricular informado

  Cenário: Pesquisar questões por tipo discursiva
    Quando o professor selecionar o tipo discursiva no filtro
    E clicar no botão pesquisar
    Então será exibido a lista de questões do tipo discursiva

  Cenário: Pesquisar questões por tipo verdadeiro ou falso
    Quando o professor optar pelo tipo verdadeiro ou falso no filtro de pesquisa
    E clicar no botão pesquisar
    Então será exibido a lista de questões do tipo verdadeiro ou falso

  Cenário: Pesquisar questões por tipo múltipla escolha
    Quando o professor optar pelo tipo múltipla escolha no filtro de pesquisa
    E clicar no botão pesquisar
    Então será exibido a lista de questões do tipo múltipla escolha

  Cenário: Pesquisar questões nao cadastradas
    Quando o professor preencher o filtro enunciado com um enunciado não existente
    E clicar no botão pesquisar
    Então será exibido a mensagem sem resultados para o filtro informado

  Cenário: Pesquisar questão compartilhada
    Quando o professor preencher o filtro com o enunciado de uma questão compartilhada
    E clicar no botão pesquisar
    Então será exibido a lista de questões compartilhadas em que o enunciado contenha o filtro informado

  Cenário: Pesquisar questão não compartilhada
    Quando o professor preencher o filtro com o enunciado de uma questão não compartilhada
    E clicar no botão pesquisar
    Então será exibido a mensagem sem resultados para o filtro informado
