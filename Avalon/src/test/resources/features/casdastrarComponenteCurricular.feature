# language: pt
# ID.09
Funcionalidade: 
  Eu como professor
  Quero atribuir um componente curricular a uma questão
  Para gerar provas daquele componente curricular

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina minhas questoes
    E deseje cadastrar um novo componente curricular

  Cenario: Cadastrar componente curricular
    Quando o professor clicar no botao adicionar componente
    E preencher o nome do componente curricular
    E clicar no botao salvar componente
    Entao um novo componente sera cadastrado

  Cenario: Criticar nome do componente curricular duplicado
    Quando o professor clicar no botao adicionar componente
    E preencher o nome do componente curricular
    E clicar no botao salvar componente
    Entao sera exibido mensagem para componente duplicado

  Cenario: Criticar nome do componente obrigatorio
    Quando o professor clicar no botao adicionar componente
    E nao preencher o nome do componente curricular
    E clicar no botao salvar componente
    Entao sera exibido mensagem para nome do componente curricular obrigatorio

  Cenario: Criticar nome do componente maior que o permitido
    Quando o professor clicar no botao adicionar componente
    E preencher o nome do componente curricular com mais caracteres do que o permitido
    E clicar no botao salvar componente
    Entao sera exibido mensagem para nome do componente excedeu limite de caracteres
