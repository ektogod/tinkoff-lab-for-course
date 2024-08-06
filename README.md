Для отправки запросов я использовал Postman.
Отправка запроса по адресу 'localhost:8080/ektogod/translateText'. Вид запроса - POST.
Пример json-файла запроса:
{
    "text": "я люблю тебя",
    "originalLanguage" : "ru",
    "finalLanguage" : "be"
} 

Ответ в случае корректного запроса возвращается в виде:
{
    "translatedText": "я любоў вы"
}

В случае ошибки:
{
    "errorMessage": "Translation went wrong because something from parameters is null!",
    "status": 500
}

Перевод, скорее всего, будет супер кривым, тк переводится каждое слово, а не фраза, из-за чего теряется контекст.

БД имеет одно представление 'query' с атрибутами:
ID: int AI PK
IP: varchar(45)
Original_Text: varchar(1000)
Original_Language: varchar(10)
Translated_Text: varchar(1000)
Target_Language: varchar(10)
Time: datetime
Status: int
Message: varchar(1000)
(если нужно будет, то в тестах есть запрос для создания моей таблицы)

