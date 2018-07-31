# language: pt
Funcionalidade: 
  Eu como professor
  Quero salvar questões de verdadeiro ou falso
  Para usá-las na impressão de provas e na geração de provas e simulados

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina minhas questoes
    E deseje cadastrar uma nova questao

  Cenario: Cadastrar questao de verdadeiro ou falso
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E informar o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao uma nova questao sera cadastrada

  Cenario: Criticar questao verdadeiro ou falso sem enunciado
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E nao informar o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para enunciado obrigatorio

  Cenario: Criticar questao verdadeiro ou falso com enunciado duplicado
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E informar o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para questao duplicada
