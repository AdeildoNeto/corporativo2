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
    E preencher o titulo da prova
    E preencher a data de inicio da prova
    E preencher a data de termino da prova
    E selecionar o tipo verdadeiro ou falso no filtro de questoes
    E clicar no botão pesquisar
    E selecionar questoes para prova
    E clicar no botão salvar nova prova
    Então uma nova prova do tipo verdadeiro ou falso sera criada

  Cenário: Gerar nova prova do tipo múltipla escolha
    Quando o professor clicar no botão nova prova
    E preencher o titulo da prova
    E preencher a data de inicio da prova
    E preencher a data de termino da prova
    E selecionar o tipo múltipla escolha no filtro de questoes
    E clicar no botão pesquisar
    E selecionar questoes para prova
    E clicar no botão salvar nova prova
    Então uma nova prova do tipo múltipla escolha sera criada

  Cenário: Criticar prova sem titulo
    Quando o professor clicar no botão nova prova
    E nao preencher o titulo da prova
    E preencher a data de inicio da prova
    E preencher a data de termino da prova
    E clicar no botão pesquisar
    Então será exibido mensagem para titulo da prova obrigatório

  Cenário: Criticar prova sem data de início
    Quando o professor clicar no botão nova prova
    E preencher o titulo da prova
    E nao preencher a data de inicio da prova
    E preencher a data de termino da prova
    E clicar no botão pesquisar
    Então será exibido mensagem para data de inicio da prova obrigatório

  Cenário: Criticar prova sem data de término
    Quando o professor clicar no botão nova prova
    E preencher o titulo da prova
    E preencher a data de inicio da prova
    E nao preencher a data de termino da prova
    E clicar no botão pesquisar
    Então será exibido mensagem para data de termino da prova obrigatório

  Cenário: Criticar data de inicio da prova menor que a data atual
    Quando o professor clicar no botão nova prova
    E preencher o titulo da prova
    E preencher a data de inicio da prova com uma data menor que a atual
    E preencher a data de termino da prova
    E clicar no botão pesquisar
    E selecionar questoes para prova
    E clicar no botão salvar nova prova
    Então será exibido mensagem para data de inicio da prova menor que a data atual

  Cenário: Criticar data de inicio da prova maior que a data de termino
    Quando o professor clicar no botão nova prova
    E preencher o titulo da prova
    E preencher a data de inicio da prova com uma data maior que a data de termino
    E preencher a data de termino da prova
    E clicar no botão pesquisar
    E selecionar questoes para prova
    E clicar no botão salvar nova prova
    Então será exibido mensagem para data de inicio da prova maior que a data de termino

  Cenário: Criticar disponibilidade da prova menor que trinta minutos
    Quando o professor clicar no botão nova prova
    E preencher o titulo da prova
    E preencher a data de inicio da prova
    E preencher a data de termino da prova com um intervalo menor que trinta minutos
    E clicar no botão pesquisar
    E selecionar questoes para prova
    E clicar no botão salvar nova prova
    Então será exibido mensagem para disponibilidade minima da prova

  Cenário: Criticar disponibilidade da prova maior que cinco horas
    Quando o professor clicar no botão nova prova
    E preencher o titulo da prova
    E preencher a data de inicio da prova
    E preencher a data de termino da prova com um intervalo maior que cinco horas
    E clicar no botão pesquisar
    E selecionar questoes para prova
    E clicar no botão salvar nova prova
    Então será exibido mensagem para disponibilidade maxima da prova
