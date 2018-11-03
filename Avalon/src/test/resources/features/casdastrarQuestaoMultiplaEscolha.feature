# language: pt
# ID.02
Funcionalidade: 
  Eu como professor
  Quero salvar questões de múltipla escolha
  Para usá-las na geração de provas

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas questões
    E deseje cadastrar uma nova questão

  Cenario: Cadastrar questao de múltipla escolha
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E preencher todas as alternativas da questão
    E definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao uma nova questao de múltipla escolha será cadastrada

  Cenario: Criticar questao múltipla escolha sem enunciado
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E não preencher o enunciado da questão
    E preencher todas as alternativas da questão
    E definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibido mensagem para enunciado obrigatório

  Cenario: Criticar questao múltipla escolha sem todas as alternativas
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E não preencher um das alternativas da questão
    E definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibido mensagem para alternativas obrigatórias

Cenario: Criticar questao múltipla escolha com enunciado maior que o permitido
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão com mais caracteres do que o permitido
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibido mensagem para enunciado da questão maior que o permitido

  Cenario: Criticar questao múltipla escolha com alternativas repetidas
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E preencher as alternativas iguais para a questao
    E definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibido mensagem para alternativas iguais

  Cenario: Criticar questao múltipla escolha sem resposta correta definida
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E preencher todas as alternativas da questão
    E nao definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibido mensagem para respota obrigatória
