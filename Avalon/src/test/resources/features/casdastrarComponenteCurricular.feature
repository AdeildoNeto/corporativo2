# language: pt
# ID.09
Funcionalidade: 
  Eu como professor
  Quero atribuir um componente curricular a uma questão
  Para gerar provas daquele componente curricular

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas questões
    E deseje cadastrar um novo componente curricular

  Cenario: Cadastrar componente curricular
    Quando o professor clicar no botão adicionar componente
    E preencher o nome do componente curricular
    E clicar no botão salvar componente
    Então um novo componente sera cadastrado

  Cenario: Criticar nome do componente curricular duplicado
    Quando o professor clicar no botão adicionar componente
    E preencher o nome do componente curricular com um nome já cadastrado
    E clicar no botão salvar componente
    Então será exibido mensagem para componente duplicado

  Cenario: Criticar nome do componente obrigatório
    Quando o professor clicar no botão adicionar componente
    E nao preencher o nome do componente curricular
    E clicar no botão salvar componente
    Então será exibido mensagem para nome do componente curricular obrigatório

  Cenario: Criticar nome do componente maior que o permitido
    Quando o professor clicar no botão adicionar componente
    E preencher o nome do componente curricular com mais caracteres do que o permitido
    E clicar no botão salvar componente
    Então será exibido mensagem para nome do componente excedeu limite de caracteres
