# language: pt
# ID.02
Funcionalidade: 
  Eu como professor
  Quero salvar questões de multipla escolha
  Para usá-las na geração de provas

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina minhas questoes
    E deseje cadastrar uma nova questao

  Cenario: Cadastrar questao de multipla escolha
    Quando o professor selecionar o tipo multipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questao
    E preencher todas as alternativas da questao
    E definir a alternativa correta
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao uma nova questao sera cadastrada

  Cenario: Criticar questao multipla escolha sem enunciado
    Quando o professor selecionar o tipo multipla escolha
    E selecionar um componente curricular
    E nao preencher o enunciado da questao
    E preencher todas as alternativas da questao
    E definir a alternativa correta
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para enunciado obrigatorio

  Cenario: Criticar questao multipla escolha sem todas as alternativas
    Quando o professor selecionar o tipo multipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questao
    E nao preencher um das alternativas da questao
    E definir a alternativa correta
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para alternativas obrigatorias

Cenario: Criticar questao multipla escolha com enunciado maior que o permitido
    Quando o professor selecionar o tipo multipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questao com mais caracteres do que o permitido
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para enunciado da questao maior que o permitido

  Cenario: Criticar questao multipla escolha com alternativas repetidas
    Quando o professor selecionar o tipo multipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questao
    E preencher as alternativas iguais para a questao
    E definir a alternativa correta
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para alternativas iguais

  Cenario: Criticar questao multipla escolha sem resposta correta definida
    Quando o professor selecionar o tipo multipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questao
    E preencher todas as alternativas da questao
    E nao definir a alternativa correta
    E clicar no botao salvar questao
    E confirmar cadastro da questao
    Entao sera exibido mensagem para respota obrigatoria
