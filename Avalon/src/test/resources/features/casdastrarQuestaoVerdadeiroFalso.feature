# language: pt
# ID.03
Funcionalidade: 
  Eu como professor
  Quero salvar questões de verdadeiro ou falso
  Para usá-las na geração de provas

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas questões
    E deseje cadastrar uma nova questão

  Cenario: Cadastrar questao de verdadeiro ou falso
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao uma nova questao de verdadeiro ou falso será cadastrada

  Cenario: Criticar questao verdadeiro ou falso sem enunciado
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E nao preencher o enunciado da questão
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibido mensagem para enunciado obrigatório

  Cenario: Criticar questao verdadeiro ou falso com enunciado duplicado
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E preencher o enunciado da questão com um valor já cadastrado
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibido mensagem para questão duplicada

  Cenario: Criticar questao verdadeiro ou falso com enunciado maior que o permitido
    Quando o professor selecionar o tipo verdadeiro ou falso
    E selecionar um componente curricular
    E preencher o enunciado da questão com mais caracteres do que o permitido
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibido mensagem para enunciado da questão maior que o permitido
