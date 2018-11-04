# language: pt
Funcionalidade: 
  Eu como professor
  Quero cadastrar uma turma
  Para disponibilizá-la provas criadas por mim

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página minhas turmas

  Cenario: Cadastrar uma nova turma
    Quando o professor clicar no botão cadastrar turma
    E preencher o nome da turma
    E preencher o semestre e ano da turma
    E selecionar os alunos para a turma
    E clicar no botão salvar turma
    Então uma nova turma será cadastrada

  Cenario: Criticar turma sem nome
    Quando o professor clicar no botão cadastrar turma
    E não preencher o nome da turma
    E preencher o semestre e ano da turma
    E selecionar os alunos para a turma
    E clicar no botão salvar turma
    Então será exibida a mensagem "O nome da turma é obrigatório."

  Cenario: Criticar turma sem semestre e ano
    Quando o professor clicar no botão cadastrar turma
    E preencher o nome da turma
    E não preencher o semestre e ano da turma
    E selecionar os alunos para a turma
    E clicar no botão salvar turma
    Então será exibida a mensagem "O semestre/ano da turma é obrigatório."

  Cenario: Criticar turma sem alunos
    Quando o professor clicar no botão cadastrar turma
    E preencher o nome da turma
    E preencher o semestre e ano da turma
    E não selecionar os alunos para a turma
    E clicar no botão salvar turma
    Então será exibida a mensagem "É necessário selecionar ao menos um aluno."
