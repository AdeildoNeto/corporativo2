# language: pt
# ID.30
Funcionalidade: 
  Eu como professor
  Quero poder visualizar lista de notas de uma prova
  Para poder transcrevê-las para o Q-Acadêmico

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de provas

  Cenário: Visualizar notas dos alunos
    Quando o professor clicar no botão notas da prova selecionada
    Então será exibido a lista de notas dos alunos que realizaram esta prova
