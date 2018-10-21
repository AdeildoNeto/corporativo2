# language: pt
# ID.28
Funcionalidade: 
  Eu como professor
  Quero salvar histórico dos simulado
  Para poder avaliar o desempenho da turma e dificuldade das questões.

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de simulados

  Cenário: Visualizar lista e notas de um simulado
    Quando o professor clicar no botão resultados do simulado selecionado
    Então será exibido a lista de notas dos alunos que realizaram este simulado

  Cenário: Visualizar histórico de um simulado
    Quando o professor clicar no botão resultados do simulado selecionado
    E selecionar um resultado do simulado
    Então será exibido o detalhe do simulado selecionado
