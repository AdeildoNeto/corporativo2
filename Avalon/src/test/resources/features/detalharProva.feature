# language: pt
# ID.22
Funcionalidade: 
  Eu como professor
  Quero visualizar as provas que criei
  Para conferi-las

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de provas

  Cenário: Visualizar prova de verdadeiro ou falso
    Quando o professor clicar no botão visualizar prova em uma prova de verdadeiro ou falso
    Então será exibido os detalhes da prova de verdadeiro ou falso

  Cenário: Visualizar prova de múltipla escolha
    Quando o professor clicar no botão visualizar prova em uma prova de múltipla escolha
    Então será exibido os detalhes da prova de múltipla escolha
