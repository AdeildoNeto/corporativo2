# language: pt
# ID.03
Funcionalidade: 
  Eu como professor
  Quero salvar questões de verdadeiro ou falso
  Para usá-las na geração de provas

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina minhas questoes
    E deseje cadastrar uma nova questao

  Cenario: Cadastrar questao de verdadeiro ou falso
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E preencher o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao uma nova questao sera cadastrada

  Cenario: Criticar questao verdadeiro ou falso sem enunciado
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E nao preencher o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para enunciado obrigatorio

  Cenario: Criticar questao verdadeiro ou falso com enunciado duplicado
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E preencher o enunciado da questao
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para questão duplicada

  Cenario: Criticar questao verdadeiro ou falso com enunciado maior que o permitido
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E preencher o enunciado da questao com mais caracteres do que o permitido
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para enunciado da questao maior que o permitido
