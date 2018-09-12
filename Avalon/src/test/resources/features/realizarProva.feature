# language: pt
# ID.23
Funcionalidade: 
  Eu como aluno
  Quero realizar um prova online
  Para ser avaliado em seu componente curricular

  Contexto: 
    Dado que o usuário está logado como aluno
    E esteja na página de provas disponíveis

  Cenário: Realizar prova de verdadeiro ou falso
    Quando o aluno iniciar uma prova de verdadeiro ou falso
    E clicar no botão iniciar do modal de instruções da prova
    E responder a primeira questão como verdadeiro
    E passar para a próxima questão de verdadeiro ou falso
    E responder a segunda questão como falso
    E clicar no botão finalizar prova
    E confirmar o fim da prova
    Então a prova será finalizada

  Cenário: Realizar prova de múltipla escolha
    Quando o aluno iniciar uma prova de múltipla escolha
    E clicar no botão iniciar do modal de instruções da prova
    E responder a primeira questão com a terceira alternativa
    E passar para a próxima questão de múltipla escolha
    E responder a segunda questão com a quinta alternativa
    E clicar no botão finalizar prova
    E confirmar o fim da prova
    Então a prova será finalizada

  Cenário: Criticar prova com questões em branco
    Quando o aluno iniciar uma prova de verdadeiro ou falso
    E clicar no botão iniciar do modal de instruções da prova
    E responder a primeira questão como verdadeiro
    E clicar no botão finalizar prova sem responder todas as questões
    Então será exibido mensagem para prova sem todas as questões respondidas

  Cenário: Exibir informações do aluno durante a prova
    Quando o aluno iniciar uma prova
    E clicar no botão iniciar do modal de instruções da prova
    Então o nome e o email do aluno serão exibidos na prova
