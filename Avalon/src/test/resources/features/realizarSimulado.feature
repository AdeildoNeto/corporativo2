# language: pt
# ID.23
Funcionalidade: 
  Eu como aluno
  Quero poder responder os simulados disponibilizados pelos professores.
  Para medir meus conhecimentos

  Contexto: 
    Dado que o usuário está logado como aluno
    E esteja na página de simulados disponíveis

  Cenário: Realizar simulado de verdadeiro ou falso
    Quando o aluno pesquisar os simulados disponíveis
    E iniciar um simulado de verdadeiro ou falso
    E responder todas as questões de verdadeiro ou falso
    E finalizar o simulado
    Então será exibido o resultado do aluno no simulado

  Cenário: Realizar simulado de múltipla escolha
    Quando o aluno pesquisar os simulados disponíveis
    E iniciar um simulado de múltipla escolha
    E responder todas as questões de múltipla escolha
    E finalizar o simulado
    Então será exibido o resultado do aluno no simulado

  Cenário: Criticar simulado com questões sem resposta
    Quando o aluno pesquisar os simulados disponíveis
    E iniciar um simulado
    E clicar no botão finalizar simulado sem responder todas as questões
    Então será exibido mensagem para simulado sem todas as questões respondidas
