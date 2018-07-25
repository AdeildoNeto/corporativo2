# language: pt
Funcionalidade: 
  Eu como professor
  Quero salvar questões
  Para usá-las na geração de provas e simulados

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina minhas questoes
    E deseje cadastrar uma nova questao

  Cenario: Cadastrar questao discursiva
    Quando o professor selecionar o tipo discursiva
    E selecionar o componente curricular
    E informar o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao uma nova questao sera cadastrada

  #Entao uma nova questao sera adicionada a lista de questoes cadastradas
  Cenario: Criticar questao sem enunciado
    Quando o professor selecionar o tipo discursiva
    E selecionar o componente curricular
    E nao informar o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para enunciado obrigatorio

  Cenario: Criticar cadastro de questao com enunciado duplicado
    Quando o professor selecionar o tipo discursiva
    E selecionar o componente curricular
    E informar o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para questao duplicada
