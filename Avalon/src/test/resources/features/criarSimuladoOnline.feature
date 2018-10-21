# language: pt
# ID.15
Funcionalidade: 
  Eu como professor
  Quero montar simulados com questões previamente cadastradas
  Para disponibilizá-lo para os alunos.

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de simulados
    E deseje cadastrar um novo simulado

  Cenário: Gerar novo simulado do tipo verdadeiro ou falso
    Quando o professor preencher o título do simulado
    E selecionar o tipo verdadeiro ou falso no filtro de questões
    E clicar no botão pesquisar
    E selecionar questões para simulado
    E clicar no botão salvar novo simulado
    Então um novo simulado do tipo verdadeiro ou falso sera criada

  Cenário: Gerar novo simulado do tipo múltipla escolha
    Quando o professor preencher o título do simulado
    E selecionar o tipo múltipla escolha no filtro de questões
    E clicar no botão pesquisar
    E selecionar questões para simulado
    E clicar no botão salvar novo simulado
    Então um novo simulado do tipo múltipla escolha sera criada

  Cenário: Criticar simulado sem titulo
    Quando o professor não preencher o título do simulado
    E clicar no botão pesquisar
    Então será exibido mensagem para titulo do simulado obrigatório
