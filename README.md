Проект UrlShortCut
--
Описание: Сервисы регистрируются на платформе отправляя название url своего сайта.
Взамен сервис получает логин и пароль который обменивает на индивидуальный
токен доступа. Далее регистрируют подсылки своего сервиса, сервис осуществляет перенапревление по
зарегистрированным производным URL'ам, перенаправление осуществляется по отдельно выданному уникальному коду.
У зарегестрированного сервиса имеется возможность получать информацию по своему сайту и активности на нём.

Имитация работы сервиса описывается ниже. Пример работы через Postman.

1. Выполняем запрос POST /urlshortcut/registration с телом JSON, пример ```{"site" : "https://stackoverflow.com/"}```
   В ответ получим примерно следующий JSON, повторная регистрация невозможна, вам возвращается просто статус false.

```{
      "registration": true,
      "login": "NWVkOGZmMjktYzVmOC00MGQ1LTk2MGUtYTYyMmRmNmJiNmYzYXNmYW1vajg5djk4c3Y5N2gxMHU5dnEzd2UyMzJmbjl3dnYzNzI4Mm52ODcybnY3OG4ydjM3Mjg=",
      "password": "M2Y2YmJhODUtYjQ4ZS00MWNhLTgzMWUtNjkyZGEzMjdkNjA0YXNmYW1vajg5djk4c3Y5N2gxMHU5dnEzd2UyMzJmbjl3dnYzNzI4Mm52ODcybnY3OG4ydjM3Mjg="
   }
```

2. Выполняем запрос POST /urlshortcut/authorization с телом JSON,
   ```{"login" : "выданный логин", "password": "выданный пароль"}```
   token = eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodHRwczovL3N0YWNrb3ZlcmZsb3cuY29tLyIsImV4cCI6MTY2ODcxODQxMn0.qtvng42LNUSLj14gY2XO5sof03Oox7yya4-A5vo3z8xzSJ5fYLzgGmHWPL1z-M7ZHyuYtgA-0V-BQfsxlWyEdA
   В замен мы получим токен доступа. Токен доступа имеет срок действия, чтобы получить новый токен, сделайте повторный запрос.

3. Далее выполняем запрос POST /urlshortcut/convert с телом JSON содержащим в хедерах токен доступа и производный url
   Пример JSON
   ```{ "url" : "https://stackoverflow.com/questions/70121462/use-a-secondary-h2-database-for-testing-an-api-in-spring-boot" }```
   Если саб-url совпадает с своим корнем, то получим уникальный код ```{"code": "00f4d6bc-d55c-4a10-94dd-fe5833584082"}```
   Если саб-url не совпадает с своим корнем, то получим статус BAD_REQUEST.
   Если саб-url уже зарегистрирован, то получите сообщение о том, что вам нужно использовать уже выданный код.

4. Для вызова саб-url использует endpoint GET urlshortcut/redirect/{code}, код в данном случае, тот что был вам выдан
   по конкретному url. В ответ вы будете перенаправлены на url который соответствует данному коду, каждый переход
   будет считаться в статистику.

5. Для получения статистики нужно выполнить запрос GET urlshortcut/statistic
   В ответ вы получите следующий JSON по всем связанным саб-url с вашим сайтом. Пример JSON:
```
   {
      "url": "https://stackoverflow.com/questions/70121462/use-a-secondary-h2-database-for-testing-an-api-in-spring-boot",
      "total": 5
   },
   {
      "url": "https://stackoverflow.com/questions/53002232/spring-boot-datajpatest-unit-test-reverting-to-h2-instead-of-mysql",
      "total": 1
   }
```   