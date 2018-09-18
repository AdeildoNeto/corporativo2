# language: pt
# ID.104
Funcionalidade: 
  Eu como professor
  Quero reagendar uma prova que cadastrei
  Para dar mais tempo aos alunos para realizá-la

  Contexto: 
    Dado que o usuário está logado como professor
    E esteja na página de provas

  Cenário: Reagendar prova futura
    Quando o professor selecionar uma prova para reagendamento
    E alterar a data de início da prova
    E alterar a data fim da prova
    E clicar no botão reagendar
    Então a disponibilidade da prova será alterada

  Cenário: Reagendar prova em andamento
    Quando o professor selecionar uma prova em andamento para reagendamento
    E alterar apenas a data fim de reagendamento
    E clicar no botão reagendar
    Então a disponibilidade da prova será alterada

  Cenário: Criticar alteração de data início para prova em andamento
    Quando o professor selecionar uma prova em andamento para reagendamento
    E alterar a data de início da prova
    E clicar no botão reagendar
    Então será exibido mensagem para alterar data de início de uma prova em andamento

  Cenário: Criticar reagendamento sem data de início
    Quando o professor selecionar uma prova para reagendamento
    E não preencher a data de início do reagendamento
    E alterar a data fim da prova
    E clicar no botão reagendar
    Então será exibido mensagem para data de início do reagendamento obrigatória

  Cenário: Criticar reagendamento sem data de término
    Quando o professor selecionar uma prova para reagendamento
    E alterar a data de início da prova
    E não preencher a data fim de reagendamento
    E clicar no botão reagendar
    Então será exibido mensagem para data fim do reagendamento obrigatória

  Cenário: Criticar data de início do agendamento maior que a data fim
    Quando o professor selecionar uma prova para reagendamento
    E alterar a data de início da prova
    E alterar a data fim da prova com um valor menor que a data de início
    E clicar no botão reagendar
    Então será exibido mensagem para data de início do reagendamento maior que a data fim

  Cenário: Criticar disponibilidade do reagendamento menor que trinta minutos
    Quando o professor selecionar uma prova para reagendamento
    E alterar a data de início da prova
    E alterar a data fim da prova com um intervalo menor do que trinta minutos
    E clicar no botão reagendar
    Então será exibido mensagem para disponibilidade mínima para o reagendamento

  Cenário: Criticar disponibilidade do reagendamento maior que cinco horas
    Quando o professor selecionar uma prova para reagendamento
    E alterar a data de início da prova
    E alterar a data fim da prova com um intervalo maior que cinco horas
    E clicar no botão reagendar
    Então será exibido mensagem para disponibilidade máxima para o reagendamento
