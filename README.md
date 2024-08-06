# Краткое описание

Для отправки запросов я использовал Postman.
Отправка запроса по адресу 'localhost:8080/ektogod/translateText'. Вид запроса - POST.

Язык должен быть написан в коде ISO 639-1 (по типу ru, be, en, fr, de и тд).

Текст до 1000 символов (я в бд поставил такую длину текста).


## Пример запроса

```json
{
    "text": "я люблю тебя",
    "originalLanguage": "ru",
    "finalLanguage": "be"
}
```
## Ответ в случае корректного запроса
```json
{
    "translatedText": "я любоў вы"
}
```

## Ответ в случае ошибки:
```json
{
    "errorMessage": "Translation went wrong because something from parameters is null!",
    "status": 500
}
```
## Перевод

Перевод, скорее всего, будет супер кривым, тк переводится каждое слово, а не фраза, из-за чего теряется контекст.

## БД
БД имеет одно представление 'query' с атрибутами:

![image](https://github.com/user-attachments/assets/7daeb907-c6ce-4b91-9281-de6955a0ca57)

(если нужно будет, то в тестах есть запрос для создания моей таблицы)



