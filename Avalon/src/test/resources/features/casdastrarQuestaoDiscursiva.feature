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

  Cenario: Cadastrar questão discursiva
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então uma nova questão discursiva será cadastrada

  Cenario: Criticar questão sem enunciado
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E não preencher o enunciado da questão
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então será exibida a mensagem "O enunciado da questão é obrigatório."

  Cenario: Criticar questão discursiva com enunciado duplicado
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questão com um valor já cadastrado
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então será exibida a mensagem "Já existe uma questão com este enunciado."

  Cenario: Criticar questão com enunciado maior que o permitido
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questão com mais caracteres que o permitido
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então será exibida a mensagem "O tamanho máximo do enunciado é de 2000 caracteres."

Cenario: Criticar questão com solução maior que o permitido
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E preencher a solução da questão com mais caracteres que o permitido
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Então será exibida a mensagem "O tamanho máximo da solução é de 300 caractéres."