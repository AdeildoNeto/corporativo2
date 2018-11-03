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

  Cenario: Cadastrar questão de múltipla escolha
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E preencher todas as alternativas da questão
    E definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao uma nova questão de múltipla escolha será cadastrada

  Cenario: Criticar questão múltipla escolha sem todas as alternativas
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E não preencher um das alternativas da questão
    E definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibida a mensagem "O preenchimento das alternativas é obrigatório."

  Cenario: Criticar questão múltipla escolha com alternativas repetidas
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E preencher as alternativas iguais para a questão
    E definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibida a mensagem "Existem alternativas iguais. Favor revisar a questão."

  Cenario: Criticar questão múltipla escolha sem resposta correta definida
    Quando o professor selecionar o tipo múltipla escolha
    E selecionar um componente curricular
    E preencher o enunciado da questão
    E preencher todas as alternativas da questão
    E não definir a alternativa correta
    E clicar no botão salvar questão
    E confirmar o cadastro da questão
    Entao será exibida a mensagem "A resposta da questão deve ser selecionada."
