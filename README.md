# Bank API

## Спецификации

### Краткое описание системы

Bank API - сервис для банковских операций.

В системе есть пользователи (клиенты), у каждого клиента есть строго один банковский аккаунт, в котором изначально лежит
какая-то сумма. Деньги можно переводить между клиентами. На средства также начисляются проценты.

<!-- TOC -->

* [Bank API](#bank-api)
    * [Спецификации](#спецификации)
        * [Краткое описание системы](#краткое-описание-системы)
    * [Технологии](#технологии)
    * [Структура проекта Bank API](#структура-проекта-bank-api)
        * [bank](#bank)
            * [src/main/resources](#srcmainresources)
    * [Дизайны](#дизайны)
        * [Дизайн приложения Bank API](#дизайн-приложения-bank-api)
            * [Архитектура](#архитектура)
            * [Модель данных](#модель-данных)
            * [Безопасность](#безопасность)
        * [Дизайн API поиска](#дизайн-api-поиска)
            * [Допущения/ограничения](#допущенияограничения)
            * [Реализация](#реализация)
            * [Описание структуры обмена данными](#описание-структуры-обмена-данными)
            * [Пример запроса](#пример-запроса)
            * [Пример ответа](#пример-ответа)
        * [Дизайн увеличения баланса всех клиентов](#дизайн-увеличения-баланса-всех-клиентов)
            * [Допущения/ограничения](#допущенияограничения-1)
            * [Реализация](#реализация-1)
        * [Дизайн перевода денег между клиентами](#дизайн-перевода-денег-между-клиентами)
            * [Допущения/ограничения](#допущенияограничения-2)
            * [Реализация](#реализация-2)
            * [Тестирование](#тестирование)
        * [Дизайн удаления телефона (аналогично для почты)](#дизайн-удаления-телефона-аналогично-для-почты)
            * [Допущения/ограничения](#допущенияограничения-3)
            * [Реализация](#реализация-3)

<!-- TOC -->

## Технологии

* Java 17
* Spring Boot 3
* PostgreSQL
* Flyway
* Maven
* Swagger
* Slf4j
* JWT Authentication
* JUnit 5

## Структура проекта Bank API

### bank

Рассмотрим структуру проекта bank.

![struct](https://github.com/rxvl-gxbbx/bank_api/assets/156305432/1b599db2-e23d-4541-997f-c3fd063cea34)

Зеленые папки на картинке - это слои приложения.

* adapter - содержит классы, являющиеся адаптерами. Через адаптеры приложение взаимодействует с внешним миром:
    * persistence - адаптер для взаимодействия с базой данных с помощью ORM Hibernate
    * rest - адаптер, через который пользователь получает данные из приложения по REST
* app - содержит классы с логикой приложения
    * api - интерфейсы, которые предоставляет приложение. Здесь не должно быть реализации, только Java-интерфейсы
    * impl - реализация интерфейсов из api. Разделение необходимо для того, чтобы разработчики, использующие сервисы, не
      завязывались на их реализацию. Это облегчит дальнейшие доработки кода.
* domain - содержит сущности, с которыми пользователь работает в данном приложении, например "Пользователь", "Телефон"
* framework - конфигурация Spring и прочих фреймворков, используемых в приложении. Здесь нет прикладной логики
  приложения, только вспомогательные технические штуки, такие как настройка Spring Security.
* staging - содержит общие компоненты, которые потенциально можно выделить в отдельные библиотеки

Кроме того, в приложении можно увидеть примерно такой набор папок и файлов:

![struct_root](https://github.com/rxvl-gxbbx/bank_api/assets/156305432/e42ac0c5-caf2-429f-93c6-c2747043ee52)

##### src/main/resources

Папка для ресурсных файлов: картинок, property-файлов, скриптов и т.д.

Ниже перечислены различные ресурсные папки, файлы и их назначения

* [db/migration](https://github.com/rxvl-gxbbx/bank_api/tree/master/src/main/resources/db/migration) - папка для
  скриптов,исполняемых миграционной системой Flyway

## Дизайны

### Дизайн приложения Bank API

#### Архитектура

Приложение состоит из "Бэкенд" части.

Взаимодействие с бэкендом происходит через REST API по протоколу HTTP. Бэкенд - логика и данные приложения. Данные
хранятся в БД PostgreSQL. Логика представляет собой Java-приложение, которое работает в контейнере Spring Boot (внутри
которого поднимается Tomcat).

Бэкенд-приложение разбито на слои:

* Domain layer - слой бизнес-данных
* Application layer - слой операций над бизнес-данными
* Adapter layer - слой взаимодействия с внешними системами
* Framework layer - слой конфигурации фреймворков

![layers](https://github.com/rxvl-gxbbx/bank_api/assets/156305432/5f5b9864-1b29-404b-ae03-ebdea10789a8)

#### Модель данных

Данные приложения хранятся в базе данных PostgreSQL.

База данных наполняется стартовым набором таблиц и данных, необходимым для работы приложения, автоматически при запуске.
Если файл удалить - ничего страшного не произойдет, стартовый набор создастся заново. Для этого применяется Flyway -
технология для миграции базы данных.

Ниже представлена ER-диаграмма данных, используемых в приложении. Она отражает структуру базы данных.

![entities](https://github.com/rxvl-gxbbx/bank_api/assets/156305432/6b5ed82f-aa35-4108-9bcd-0534378dcc57)

| Сущность | Описание                     |
|----------|------------------------------|
| User     | Пользователь сервиса         |
| Phone    | Телефон пользователя         |
| Email    | Почта пользователя           |
| Account  | Банковский счет пользователя |

#### Безопасность

Аутентификация - проверка подлинности пользователя. В приложении Bank API проверка осуществляется через пару
Логин/Пароль. Введенная пользователем информация сравнивается с той, что хранится в базе данных.

Чтобы не выполнять аутентификацию при каждом запросе, в приложении используются JWT-токены. Это объекты, которые
генерируются на бэкенде при аутентификации и отправляются пользователю. Эти объекты хранят информацию о пользователе. В
дальнейшем при каждом запросе нужно отправлять JWT-токен на бэкенд, который проверяет его корректность по определенному
алгоритму шифрования. Если объект корректный - значит пользователю можно доверять. Чтобы избежать кражу токенов из
хранилища, им выставляется время жизни, после которого они перестают быть актуальными.

Ниже представлена UML диаграмма последовательностей процессов аутентификации.

![auth](https://github.com/rxvl-gxbbx/bank_api/assets/156305432/69f2e83b-dff5-4b4c-8337-bacaad8d6985)

### Дизайн API поиска

#### Допущения/ограничения

* Если передана дата рождения, то фильтр записей, где дата рождения больше чем переданный в запросе.
* Если передан телефон, то фильтр по 100% сходству.
* Если передано ФИО, то фильтр по like форматом ‘{text-from-request-param}%’
* Если передан email, то фильтр по 100% сходству.

#### Реализация

| Класс                                                                                                                                                                                                    | Описание                                                                                                                                                                                                                                            |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [com.rxvlvxr.bank.adapter.persistence.user.UserJpaRepository](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/user/UserJpaRepository.java)         | Интерфейс, наследующий `JpaRepository` и `JpaSpecificationExecutor`. `JpaSpecificationExecutor` нужен для использования JPA Specification.                                                                                                          |
| [com.rxvlvxr.bank.adapter.persistence.user.UserJpaSpecification](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/user/UserJpaSpecification.java)   | Класс, реализующий JPA Specification для поиска по фильтрам. В методе `toPredicate()` реализована логика фильтрации.                                                                                                                                |
| [com.rxvlvxr.bank.adapter.persistence.user.UserRepositoryAdapter](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/user/UserRepositoryAdapter.java) | Адаптер, реализующий `UserRepository`.                                                                                                                                                                                                              |
| [com.rxvlvxr.bank.adapter.rest.user.UserController](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/rest/user/UserController.java)                             | REST-контроллер. Метод `searchByFilters()` аннотирован `@PostMapping` со значением `"/search"`, которые обрабатывает POST-запрос по адресу `"/bank-api/users/search"`.                                                                              |
| [com.rxvlvxr.bank.app.api.user.UserRepository](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/api/user/UserRepository.java)                                       | Интерфейс, используется в UseCase-классах. Добавлен метод `findAll()`, который имеет параметры:<br/>  <ul><li>`Specification<User> specification` - отвечает за фильтрацию</li><li>`Pageable pageable` - отвечает за сортировку/пагинацию</li></ul> |
| [com.rxvlvxr.bank.app.api.user.FindUsersByFiltersInbound](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/api/user/FindUsersByFiltersInbound.java)                 | Inbound-интерфейс. Имеет единственный метод execute(), который возвращает `List<User>`. Используется в REST контроллере.                                                                                                                            |
| [com.rxvlvxr.bank.app.impl.user.FindUsersByFiltersUseCase](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/impl/user/FindUsersByFiltersUseCase.java)               | UseCase-класс, реализующий логику Inbound-интерфейса. Возвращает `List<User>`.                                                                                                                                                                      |

#### Описание структуры обмена данными

| Элемент             |              | Тип                       | Обязательность | Описание                                      |
|---------------------|--------------|---------------------------|----------------|-----------------------------------------------|
| `FilterDto`         |              | element                   |                | DTO объект запроса.                           |
|                     | `fullName`   | `String`                  | Н              | Часть (или полное) ФИО пользователя.          |
|                     | `birthDate`  | `LocalDate`               | Н              | Дата рождения пользователя.                   |
|                     | `phone`      | `String`                  | Н              | Номер телефона пользователя.                  |
|                     | `email`      | `String`                  | Н              | Почтовый адрес пользователя.                  |
|                     | `pagination` | `PageParamsDto`           | Н              | Параметры пагинации.                          |
|                     | `sort`       | `SortParamsDto`           | Н              | Параметры сортировки                          |
| `FilterResponseDto` |              | element                   |                | DTO объект ответа.                            |
|                     | `fullName`   | `String`                  | О              | ФИО пользователя.                             |
|                     | `birthDate`  | `LocalDate`               | О              | Дата рождения пользователя.                   |
|                     | `account`    | `AccountDto`              | О              | Данные банковского аккаунта пользователя.     |
|                     | `phones`     | `List<PhoneDto>`          | О              | Список телефонных номеров пользователя.       |
|                     | `emails`     | `List<EmailDto>`          | О              | Список почтовых адресов пользователя.         |
| `UsersDto`          |              | element                   |                | Обертка ответа.                               |
|                     | `users`      | `List<FilterResponseDto>` | О              | Список пользователей, ограниченных полями DTO |

#### Пример запроса

```json
{
  "phone": "79991234567",
  "email": "mail@mail.ru",
  "pagination": {
    "page_number": 1,
    "page_size": 1
  },
  "sort": {
    "field": "fullName",
    "direction": "asc"
  },
  "full_name": "Фам",
  "birth_date": "1999-05-20"
}
```

#### Пример ответа

```json
{
  "users": [
    {
      "fullName": "Фамилия Имя Отчество",
      "birthDate": "2000-05-20",
      "account": {
        "amount": 10500
      },
      "phones": [
        {
          "number": "79991234567"
        }
      ],
      "emails": [
        {
          "address": "mail@mail.ru"
        }
      ]
    }
  ]
}
```

### Дизайн увеличения баланса всех клиентов

#### Допущения/ограничения

* Раз в минуту баланс каждого клиента увеличиваются на 5% но не более 207% от начального депозита.

#### Реализация

| Класс                                                                                                                                                                                                                  | Описание                                                                                                                                                                                                                                                      |
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [com.rxvlvxr.bank.adapter.persistence.account.AccountJpaRepository](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/account/AccountJpaRepository.java)           | Интерфейс, наследующий `JpaRepository`. Метод `findById` аннотирован `@Lock` со значением `PESSIMISTIC_WRITE`. Доступ к данным блокируется на уровне базы данных, предотвращая одновременное изменение данных несколькими потоками.                           |
| [com.rxvlvxr.bank.adapter.persistence.account.AccountRepositoryAdapter](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/account/AccountRepositoryAdapter.java)   | Адаптер, реализующий `AccountRepository`.                                                                                                                                                                                                                     |
| [com.rxvlvxr.bank.app.api.account.AccountRepository](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/api/account/AccountRepository.java)                                         | Интерфейс. Используемые методы: <br/><ul><li>`findAll()` - возвращает `List<Account>`</li><li>`findById(Long id)` - возвращает `Optional<Account>`</li><li>`save(Account account)` - сохраняет запись в базе данных</li></ul> Используется в UseCase-классах. |
| [com.rxvlvxr.bank.app.api.account.IncreaseBalanceForAllAccountsInbound](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/api/account/IncreaseBalanceForAllAccountsInbound.java)   | Inbound-интерфейс. Имеет единственный метод `execute()`.                                                                                                                                                                                                      |
| [com.rxvlvxr.bank.app.impl.account.IncreaseBalanceForAllAccountsUseCase](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/impl/account/IncreaseBalanceForAllAccountsUseCase.java) | UseCase-класс, реализующий логику Inbound-интерфейса. Метод `execute()` аннотирован `@Scheduled` со значением `fixedRate = 60_000`. Метод является запланированным и выполняется каждую минуту (60000 миллисекунд).                                           |

### Дизайн перевода денег между клиентами

#### Допущения/ограничения

* Со счета аутентифицированного пользователя, на счёт другого пользователя.

#### Реализация

| Класс                                                                                                                                                                                                                | Описание                                                                                                                                                                                                                            |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [com.rxvlvxr.bank.adapter.persistence.account.AccountJpaRepository](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/account/AccountJpaRepository.java)         | Интерфейс, наследующий `JpaRepository`. Метод `findById` аннотирован `@Lock` со значением `PESSIMISTIC_WRITE`. Доступ к данным блокируется на уровне базы данных, предотвращая одновременное изменение данных несколькими потоками. |
| [com.rxvlvxr.bank.adapter.persistence.account.AccountRepositoryAdapter](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/account/AccountRepositoryAdapter.java) | Адаптер, реализующий `AccountRepository`.                                                                                                                                                                                           |
| [com.rxvlvxr.bank.app.api.account.AccountRepository](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/api/account/AccountRepository.java)                                       | Интерфейс. Используемые методы: <br/><ul><li>`findById(Long id)` - возвращает `Optional<Account>`</li><li>`save(Account account)` - сохраняет запись в базе данных</li></ul> Используется в UseCase-классах.                        |
| [com.rxvlvxr.bank.adapter.rest.account.AccountController](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/rest/account/AccountController.java)                             | REST-контроллер. Метод `transfer()` аннотирован `@PatchMapping` со значением `"/{id}/transfer"`, которые обрабатывает PATCH-запрос по адресу `"/bank-api/accounts/{id}/transfer"`.                                                  |
| [com.rxvlvxr.bank.app.api.account.TransferInbound](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/api/account/TransferInbound.java)                                           | Inbound-интерфейс. Имеет единственный метод `execute()`.                                                                                                                                                                            |
| [com.rxvlvxr.bank.app.impl.account.TransferUseCase](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/impl/account/TransferUseCase.java)                                         | UseCase-класс, реализующий логику Inbound-интерфейса. Метод `execute()` синхронизирован для потокобезопасности. Присутствуют валидации перевода денег.                                                                              |

#### Тестирование

Модульное тестирование:

* Проверить количество созданных аккаунтов с константным значением, объявленным в переменную
* Перевести деньги самому себе, проверить выбрасывается ли исключение
* Перевести больше денег, чем есть на счете и проверить выбрасывается ли исключение
* Перевести деньги и проверить корректные ли значения в таблице у обоих клиентов
* Имитировать множественные переводы денег из разных потоков используя многопоточность Java, проверить корректны ли
  данные после переводов

### Дизайн удаления телефона (аналогично для почты)

#### Допущения/ограничения

* Пользователь может удалить свои телефон и/или email. При этом нельзя удалить последний.

#### Реализация

| Класс                                                                                                                                                                                                        | Описание                                                                                                                                                                                                                               |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [com.rxvlvxr.bank.adapter.persistence.phone.PhoneJpaRepository](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/phone/PhoneJpaRepository.java)         | Интерфейс, наследующий `JpaRepository`.                                                                                                                                                                                                |
| [com.rxvlvxr.bank.adapter.persistence.phone.PhoneRepositoryAdapter](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/persistence/phone/PhoneRepositoryAdapter.java) | Адаптер, реализующий `PhoneRepository`.                                                                                                                                                                                                |
| [com.rxvlvxr.bank.adapter.rest.phone.PhonesController](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/adapter/rest/phone/PhonesController.java)                           | REST-контроллер. Метод `delete()` аннотирован `@DeleteMapping` со значением `"/{id}"`, которые обрабатывает DELETE-запрос по адресу `"/bank-api/phones/{id}"`.                                                                         |
| [com.rxvlvxr.bank.app.api.phone.PhoneRepository](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/api/phone/PhoneRepository.java)                                       | Интерфейс, используется в UseCase-классах. Используемые методы: <br/><ul><li>`findById(Long id)` - возвращает `Optional<Phone>`</li><li>`delete(Phone phone)` - удаляет запись в базе данных</li></ul> Используется в UseCase-классах. |
| [com.rxvlvxr.bank.app.api.phone.DeletePhoneInbound](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/api/phone/DeletePhoneInbound.java)                                 | Inbound-интерфейс. Имеет единственный метод `execute()`, который удаляет запись по ID. Используется в REST контроллере.                                                                                                                |
| [com.rxvlvxr.bank.app.impl.phone.DeletePhoneUseCase](https://github.com/rxvl-gxbbx/bank_api/blob/master/src/main/java/com/rxvlvxr/bank/app/impl/phone/DeletePhoneUseCase.java)                               | UseCase-класс, реализующий логику Inbound-интерфейса. Метод `validateDeletingPhone()` проверяет не является ли телефон последним добавленным.                                                                                          |