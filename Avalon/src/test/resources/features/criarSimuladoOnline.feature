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
    Então um novo simulado do tipo verdadeiro ou falso será criado

  Cenário: Gerar novo simulado do tipo múltipla escolha
    Quando o professor preencher o título do simulado
    E selecionar o tipo múltipla escolha no filtro de questões
    E clicar no botão pesquisar
    E selecionar questões para simulado
    E clicar no botão salvar novo simulado
    Então um novo simulado do tipo múltipla escolha sera criada

  Cenário: Criticar simulado sem título
    Quando o professor não preencher o título do simulado
    E clicar no botão pesquisar
    Então será exibida a mensagem "O título do simulado é obrigatório."

  Cenário: Criticar simulado com título duplicado
    Quando o professor preencher o título do simulado com um valor em uso
    E selecionar o tipo múltipla escolha no filtro de questões
    E clicar no botão pesquisar
    E selecionar questões para simulado
    E clicar no botão salvar novo simulado
    Então será exibida a mensagem "O título do simulado já está em uso."

  Cenário: Criticar título do simulado maior que o limite permitido
    Quando o professor preencher o título do simulado com mais caracteres que o permitido
    E selecionar o tipo múltipla escolha no filtro de questões
    E clicar no botão pesquisar
    E selecionar questões para simulado
    E clicar no botão salvar novo simulado
    Então será exibida a mensagem "O tamanho máximo do título é de 80 caracteres."
