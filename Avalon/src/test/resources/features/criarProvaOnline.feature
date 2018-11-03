# language: pt
# ID.21 - ID.22 - ID.25
Funcionalidade: 
  Eu como professor
  Quero criar uma prova online
  Para disponibilizar para meus alunos realizá-la

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de provas

  Cenário: Gerar nova prova do tipo verdadeiro ou falso
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova
    E selecionar o tipo verdadeiro ou falso no filtro de questões
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então uma nova prova do tipo verdadeiro ou falso sera criada

  Cenário: Gerar nova prova do tipo múltipla escolha
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova
    E selecionar o tipo múltipla escolha no filtro de questões
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então uma nova prova do tipo múltipla escolha sera criada

  Cenário: Criticar prova sem titulo
    Quando o professor clicar no botão nova prova
    E não preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova
    E clicar no botão pesquisar
    Então será exibida a mensagem "O título da prova é obrigatório."

  Cenário: Criticar prova sem data de início
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E não preencher a data de início da prova
    E preencher a data de término da prova
    E clicar no botão pesquisar
    Então será exibida a mensagem "A data e hora de início é obrigatória."

  Cenário: Criticar prova sem data de término
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E não preencher a data de término da prova
    E clicar no botão pesquisar
    Então será exibida a mensagem "A data e hora de término é obrigatória."

  Cenário: Criticar data de inicio da prova menor que a data atual
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova com uma data menor que a atual
    E preencher a data de término da prova
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então será exibida a mensagem "A data de início da prova não pode ser menor ou igual a data e hora atual."

  Cenário: Criticar data de inicio da prova maior que a data de término
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova com uma data maior que a data de termino
    E preencher a data de término da prova
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então será exibida a mensagem "A data de início da prova não pode ser maior que a data de término."

  Cenário: Criticar disponibilidade da prova menor que o permitido
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova com um intervalo menor que trinta minutos
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então será exibida a mensagem "A prova deve ficar disponível por ao menos 30 minutos."

  Cenário: Criticar disponibilidade da prova maior que o permitido
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova com um intervalo maior que cinco horas
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então será exibida a mensagem "A prova deve ficar disponível por no máximo 5 horas."

  Cenário: Criticar questões de prova sem peso
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova
    E clicar no botão pesquisar
    E selecionar questões para prova
    E não preencher o peso de uma questão
    E clicar no botão salvar nova prova
    Então será exibida a mensagem "O peso da questão é obrigatório."

  Cenário: Criticar prova sem nota máxima
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova
    E não preencher a nota máxima da prova
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então será exibida a mensagem "A nota máxima da prova é obrigatória."

  Cenário: Criticar prova com nota máxima inferior ao permitido
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova
    E preencher a nota máxima da prova com 0
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então será exibida a mensagem "O valor da nota deve ser maior ou igual a 1,0."

  Cenário: Criticar prova com nota máxima superior ao permitido
    Quando o professor clicar no botão nova prova
    E preencher o título da prova
    E preencher a data de início da prova
    E preencher a data de término da prova
    E preencher a nota máxima da prova com 11
    E clicar no botão pesquisar
    E selecionar questões para prova
    E clicar no botão salvar nova prova
    Então será exibida a mensagem "O valor da nota dever ser menor ou igual a 10,0."
