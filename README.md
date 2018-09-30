# Exercício #3 - RSS parte 2

A ideia deste exercício é aplicar os conceitos de `Service`, `BroadcastReceiver`, `SQLite`, `RecyclerView`, entre outros. 

Caso você não tenha feito a [parte 1 do exercício](https://github.com/if710/exercicio-rss) use o projeto disponível [neste link](https://github.com/if1001/exercicio2-rss) como referência. Reforçando que esta referência está escrita em Java. Portanto, caso já tenha feito a parte 1, sugiro que você continue a partir da sua resolução. 
A aplicação RSS disponível no [repositório de referência](https://github.com/if1001/exercicio2-rss) é uma versão atualizada da que foi passada como exercício anterior, mas *ainda incompleta*. Observe os passos listados abaixo. 
Esta versão está usando a classe `SQLiteRSSHelper` para gerenciar o banco de dados `SQLite` como forma de persistir os dados. 
Isto é, após o download e parsing do RSS, a lista de itens do feed está sendo armazenada no banco, ao invés de exibida diretamente na tela. 
Existem dois objetos `AsyncTask`. Um é responsável por carregar o XML da internet e salvar no banco. Se tudo correr bem, outro AsyncTask executa para carregar as notícias do banco de dados e exibir na tela. 

Siga os passos na ordem sugerida e marque mais abaixo, na sua resposta, quais os passos completados. 
Para entregar o exercício, responda o [formulário de entrega](https://docs.google.com/forms/d/e/1FAIpQLSeP0D2VaDDtG16w4OCc_ttU43QGCwcMq9b1GM8RdWyxUypSyg/viewform) até 30/09/2018, às 23h59.

  9. Modifique a aplicação para que passe a carregar o endereço do feed a partir de uma `SharedPreferences` com a chave `rssfeed`. Inclua a possibilidade de alterar a `SharedPreference` (`rssfeed`) incluindo um botão na `ActionBar` da aplicação. Ao clicar no botão, uma `Activity` deve ser exibida com base em uma `PreferenceScreen` gerada automaticamente por meio de um `Fragment` que estende a classe `PreferenceFragment`, como visto em sala. Defina um arquivo em `res/xml/preferencias.xml` para estruturar a tela.
  10. A classe `SQLiteRSSHelper` já tem toda a configuração do banco. No entanto, ainda é necessário implementar os métodos de manipulação do banco de dados (da linha 73 em diante), que estão em aberto ainda. A implementação do método `getItems` deve retornar apenas os itens não lidos;
  11. Modifique a aplicação de forma que ao clicar em um item RSS, o link seja aberto no navegador e a notícia seja marcada como lida no banco;
  12. Altere a aplicação de forma a usar um `Service` para fazer o download e persistência dos itens do feed no banco. Ou seja, a ideia aqui é mover o código que atualmente está no `AsyncTask` ou `doAsync` (dependendo da solução adotada por você no exercício anterior) que carrega o feed a partir da internet para um `Service`. Dica: use `IntentService`;
  13. Ao finalizar a tarefa, o `Service` deve enviar um `broadcast` avisando que terminou;
  14. Use um `BroadcastReceiver` registrado dinamicamente, para quando o usuário estiver com o app em primeiro plano, a atualização da lista de itens ser feita de forma automática;
  15. Se o usuário não estiver com o app em primeiro plano, um outro `BroadcastReceiver` registrado estaticamente deve exibir uma notificação, apenas se houver alguma notícia nova;

---

# Orientações

  - Comente o código que você desenvolver, explicando o que cada parte faz.
  - Entregue o exercício *mesmo que não tenha completado todos os itens* listados. Marque abaixo apenas o que completou.

----

# Status

| Passo | Completou? |
| ------ | ------ |
| 9 | **sim** |
| 10 | **sim** |
| 11 | **sim** |
| 12 | **sim** |
| 13 | **sim** |
| 14 | **sim** |
| 15 | **sim** |
