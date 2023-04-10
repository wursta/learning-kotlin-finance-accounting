# Учебный проект по Kotlin (Финансовый учёт)

Учебный проект курса https://otus.ru/lessons/kotlin/ \
Поток курса 2023-02.

## Описание

В данном учебном проекте реализуется один из микросервисов для сервиса/мобильного приложения по учёту финансов (доходы/расходы) и их категоризации 
в автоматическом и ручном режиме.

Общие задачи приложения (общая идея): 
 - Интеграция с разными банками для автоматического учёта доходов/расходов в приложении (предполагаем, что банки распологают API для оповещения приложения о совершаемых транзакциях)
 - Категоризация доходов/расходов из разных источников (банковские карты, наличные)
 - Аналитика расходов (за месяц, квартал, год...) 
 - Уведомления о регулярных платежах  

В учебном проекте будет реализован микросервис по:
 - CRUD для источников расходов (банковские карты)
 - CRUD для расходов из разных источников (банковские карты, наличные)
 - Аналитика расходов за промежуток времени

## Визуальное представление фронтенда

![Макет приложения](/docs/marketing/design-layout.png)

## Документация

1. Маркетинг
   1. [Заинтересанты](/docs/marketing/stakeholders.md)
   2. [Целевая аудитория](/docs/marketing/target-audience.md)
   3. [Конкурентный анализ](/docs/marketing/concurrency.md)
   4. [Анализ экономики](/docs/marketing/economy.md)
   5. [Пользовательские истории](/docs/marketing/user-stories.md)
2. DevOps
   1. [Схема инфраструктуры](/docs/devops/infrastruture.md)
   2. [Схема мониторинга](/docs/devops/monitoring.md)
## Структура проекта

| Название        | Описание                                                                                                             |
|-----------------|----------------------------------------------------------------------------------------------------------------------|
| m1l1-helloworld | Учебный модуль. Выводит площадь круга по введённому пользователем радиусу                                            |
| m1l3-oop        | Учебный модуль. Практика по созданию классов и интерфейсов. Unit-тесты.                                              |
| m1l3-dsl        | Учебный модуль. Практика по созданию DSL для Builder'а SQL запросов. Unit-тесты. [Описание DSL](/m1l4-dsl/README.md) |