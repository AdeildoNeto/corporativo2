# language: pt
# ID.01
Funcionalidade: 
  Eu como professor
  Quero salvar questões discursivas
  Para usá-las na geração de provas

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas questões
    E deseje cadastrar uma nova questão

  Cenario: Cadastrar questao discursiva
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então uma nova questão discursiva será cadastrada

  Cenario: Criticar questao discursiva sem enunciado
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E nao preencher o enunciado da questão
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então será exibido mensagem para enunciado obrigatório

  Cenario: Criticar questao discursiva com enunciado duplicado
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questão com um valor já cadastrado
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então será exibido mensagem para questão duplicada

Cenario: Criticar questao discursiva com enunciado maior que o permitido
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questão com mais caracteres do que o permitido
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então será exibido mensagem para enunciado da questão maior que o permitido