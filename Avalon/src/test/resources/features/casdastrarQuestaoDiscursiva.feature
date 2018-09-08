# language: pt
# ID.01
Funcionalidade: 
  Eu como professor
  Quero salvar questões discursivas
  Para usá-las na geração de provas

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina minhas questoes
    E deseje cadastrar uma nova questao

  Cenario: Cadastrar questao discursiva
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questao
    E clicar no botao salvar questao
    E confirmar o cadastro da questao
    Entao uma nova questao discursiva sera cadastrada

  Cenario: Criticar questao discursiva sem enunciado
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E nao preencher o enunciado da questao
    E clicar no botao salvar questao
    E confirmar o cadastro da questao
    Entao sera exibido mensagem para enunciado obrigatorio

  Cenario: Criticar questao discursiva com enunciado duplicado
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questao com um valor ja cadastrado
    E clicar no botao salvar questao
    E confirmar o cadastro da questao
    Entao sera exibido mensagem para questão duplicada

Cenario: Criticar questao discursiva com enunciado maior que o permitido
    Quando o professor selecionar o tipo discursiva
    E selecionar um componente curricular
    E preencher o enunciado da questao com mais caracteres do que o permitido
    E clicar no botao salvar questao
    E confirmar o cadastro da questao
    Entao sera exibido mensagem para enunciado da questao maior que o permitido