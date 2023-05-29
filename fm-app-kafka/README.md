# Модуль приложения основанный на Apache Kafka
 
- Запуск Apache Kafka 
```
docker compose -f .\deploy\docker-compose-kafka.yml up -d
```

- Запуск приложения (предварительно нужно установить переменную окружения)
```
set KAFKA_HOSTS=localhost:9001
gradlew.bat fm-app-kafka:run
```

Доступные переменные окружения:

| **Имя**                 | **Описание**                                            |
|-------------------------|---------------------------------------------------------|
| KAFKA_HOSTS             | Хосты на которых запущена Kafka                         |
| KAFKA_GROUP_ID          | ID группы                                               |
| KAFKA_CARD_TOPIC_IN     | Имя топика для входящих сообщений по банковским картам  |
| KAFKA_CARD_TOPIC_OUT    | Имя топика для исходящих сообщений по банковским картам |
| KAFKA_EXPENSE_TOPIC_IN  | Имя топика для входящих сообщений по тратам             |
| KAFKA_EXPENSE_TOPIC_OUT | Имя топика для исходящих сообщений по тратам            |



## Полезные команды

- Получить список топиков
  ```
  docker exec -it kafka /usr/bin/kafka-topics --list --bootstrap-server localhost:9091
  ```

- Отправить сообщение в топик
  ```
  docker exec -it kafka /usr/bin/kafka-console-producer --topic fm-card-in --bootstrap-server localhost:9091
  ```