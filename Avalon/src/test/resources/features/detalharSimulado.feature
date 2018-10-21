# language: pt
# ID.17
Funcionalidade: 
  Eu como professor
  Quero poder visualizar os simulados por mim criados
  Para realizar a conferência dele.

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de simulados

  Cenário: Visualizar simulado de verdadeiro ou falso
    Quando o professor clicar no botão visualizar simulado em um simulado de verdadeiro ou falso
    Então será exibido os detalhes do simulado de verdadeiro ou falso

  Cenário: Visualizar simulado de múltipla escolha
    Quando o professor clicar no botão visualizar simulado em um simulado de múltipla escolha
    Então será exibido os detalhes do simulado de múltipla escolha
