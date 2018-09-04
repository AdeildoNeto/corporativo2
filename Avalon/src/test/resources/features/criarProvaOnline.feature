# language: pt
# ID.21 - ID.22 - ID.25
Funcionalidade: 
  Eu como professor
  Quero criar uma prova online
  Para disponibilizar para meus alunos realizá-la

  Contexto: 
    Dado que o usuario esta logado como professor
    E esteja na pagina de provas

  Cenário: Gerar nova prova do tipo verdadeiro ou falso
    Quando o professor clicar no botao nova prova
    E preencher o titulo da prova
    E clicar no botao gerar
    E preencher a data de inicio da prova
    E preencher a data de termino da prova
    E selecionar o tipo verdadeiro ou falso no filtro de questoes
    E clicar no botao pesquisar
    E selecionar questoes para prova
    E clicar no botao salvar nova prova
    Então uma nova prova do tipo verdadeiro ou falso sera criada

  Cenário: Gerar nova prova do tipo multipla escolha
    Quando o professor clicar no botao nova prova
    E preencher o titulo da prova
    E clicar no botao gerar
    E preencher a data de inicio da prova
    E preencher a data de termino da prova
    E selecionar o tipo multipla escolha no filtro de questoes
    E clicar no botao pesquisar
    E selecionar questoes para prova
    E clicar no botao salvar nova prova
    Então uma nova prova do tipo multipla escolha sera criada

  Cenário: Criticar prova sem titulo
    Quando o professor clicar no botao nova prova
    E nao preencher o titulo da prova
    E clicar no botao gerar
    Então sera exibido mensagem para titulo da prova obrigatorio

  Cenário: Criticar prova sem data de início
    Quando o professor clicar no botao nova prova
    E preencher o titulo da prova
    E clicar no botao gerar
    E nao preencher a data de inicio da prova
    E preencher a data de termino da prova
    E clicar no botao pesquisar
    Então sera exibido mensagem para data de inicio da prova obrigatorio

  Cenário: Criticar prova sem data de término
    Quando o professor clicar no botao nova prova
    E preencher o titulo da prova
    E clicar no botao gerar
    E preencher a data de inicio da prova
    E nao preencher a data de termino da prova
    E clicar no botao pesquisar
    Então sera exibido mensagem para data de termino da prova obrigatorio

  Cenário: Criticar data de inicio da prova menor que a data atual
    Quando o professor clicar no botao nova prova
    E preencher o titulo da prova
    E clicar no botao gerar
    E preencher a data de inicio da prova com uma data menor que a atual
    E preencher a data de termino da prova
    E clicar no botao pesquisar
    E selecionar questoes para prova
    E clicar no botao salvar nova prova
    Então sera exibido mensagem para data de inicio da prova menor que a data atual

  Cenário: Criticar data de inicio da prova maior que a data de termino
    Quando o professor clicar no botao nova prova
    E preencher o titulo da prova
    E clicar no botao gerar
    E preencher a data de inicio da prova com uma data maior que a data de termino
    E preencher a data de termino da prova
    E clicar no botao pesquisar
    E selecionar questoes para prova
    E clicar no botao salvar nova prova
    Então sera exibido mensagem para data de inicio da prova maior que a data de termino

  Cenário: Criticar disponibilidade da prova menor que trinta minutos
    Quando o professor clicar no botao nova prova
    E preencher o titulo da prova
    E clicar no botao gerar
    E preencher a data de inicio da prova
    E preencher a data de termino da prova com um intervalo menor que trinta minutos
    E clicar no botao pesquisar
    E selecionar questoes para prova
    E clicar no botao salvar nova prova
    Então sera exibido mensagem para disponibilidade minima da prova

  Cenário: Criticar disponibilidade da prova maior que cinco horas
    Quando o professor clicar no botao nova prova
    E preencher o titulo da prova
    E clicar no botao gerar
    E preencher a data de inicio da prova
    E preencher a data de termino da prova com um intervalo maior que cinco horas
    E clicar no botao pesquisar
    E selecionar questoes para prova
    E clicar no botao salvar nova prova
    Então sera exibido mensagem para disponibilidade maxima da prova
