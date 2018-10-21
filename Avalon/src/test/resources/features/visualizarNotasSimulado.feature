# language: pt
# ID.19
Funcionalidade: 
  Eu como aluno
  Quero visualizar a nota obtida no simulado por mim realizado
  Para saber meu nível de conhecimento

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de simulados

  Cenário: Visualizar notas dos alunos em um simulado
    Quando o professor clicar no botão notas do simulado selecionado
    Então será exibido a lista de notas dos alunos que realizaram este simulado
