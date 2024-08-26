# Базовая настройка

## Запуск minikube

[Инструкция по установке](https://minikube.sigs.k8s.io/docs/start/)

```bash
minikube start
```


## Добавление токена авторизации GitHub

[Получение токена](https://github.com/settings/tokens/new)

```bash
kubectl create secret docker-registry ghcr --docker-server=https://ghcr.io --docker-username=<github_username> --docker-password=<github_token> -n default
```


## Установка API GW kusk

[Install Kusk CLI](https://docs.kusk.io/getting-started/install-kusk-cli)

```bash
kusk cluster install
```


## Настройка terraform

[Установите Terraform](https://yandex.cloud/ru/docs/tutorials/infrastructure-management/terraform-quickstart#install-terraform)


Создайте файл ~/.terraformrc

```hcl
provider_installation {
  network_mirror {
    url = "https://terraform-mirror.yandexcloud.net/"
    include = ["registry.terraform.io/*/*"]
  }
  direct {
    exclude = ["registry.terraform.io/*/*"]
  }
}
```

## Применяем terraform конфигурацию 

```bash
cd terraform
terraform apply
```

## Настройка API GW

```bash
kusk deploy -i api.yaml
```

## Проверяем работоспособность

```bash
kubectl port-forward svc/kusk-gateway-envoy-fleet -n kusk-system 8080:80
curl localhost:8080/hello
```


## Delete minikube

```bash
minikube delete
```

# Описание
## Архитектура As Is
### Функциональность
#### Управление отоплением:

* Пользователи могут удалённо включать/выключать отопление в своих домах.
* Пользователи могут устанавливать желаемую температуру.
* Система автоматически поддерживает заданную температуру, регулируя подачу тепла.
#### Мониторинг температуры:
* Система получает данные о температуре с датчиков, установленных в домах.
* Пользователи могут просматривать текущую температуру в своих домах через веб-интерфейс.

### Архитектура
* Язык программирования: **Java**
* База данных: **PostgreSQL**
* Архитектура: **Монолитная**, все компоненты системы (обработка запросов, бизнес-логика, работа с данными) находятся в рамках одного приложения.
* Взаимодействие: **Синхронное**, запросы обрабатываются последовательно.
* Масштабируемость: **Ограничена**, так как монолит сложно масштабировать по частям.
* Развертывание: Требует остановки всего приложения.
### Домены
* Управление отоплением
* Мониторинг температуры
### Диаграмма
[System Context diagram](./diagrams/Monolith_Context.puml)

## Архитектура To Be
### Архитектура
* Язык программирования: **Java**
* База данных: **PostgreSQL**
* Архитектура: **Микросервисная**, все компоненты системы (обработка запросов, бизнес-логика, работа с данными) разделены на отдельные приложения с собственной базой данных. 
Общение между сервисами происходит через обмен сообщениями в кафке
* Взаимодействие: **Асинхронное**, запросы обрабатываются асинхронно.
* Масштабируемость: **Не ограничена**, каждую часть системы можно масштабировать отдельно.
* Развертывание: Остановка всего приложения не требуется
### Описание микросервисов
* Gateway API - gateway, перенаправляет запросы на нужный сервис, аутентифицирует и авторизует запросы через auth-service. При успешной аутентификации добавляет заголовок userId
* Auth Service - сервис аутентификации и авторизации
* Device Management Service - сервис управления списком устройств пользователя. Использует [сагу для регистрации и удаления устройства](./smart-home-devices-management/src/main/java/ru/yandex/practicum/smarthome/devicesmanagement/out/messaging/DeviceSagas.java) Также разделены операции чтения и записи
* Automation Service - сервис автоматизации управления устройствами. Например, включение устройства по расписанию, либо по событию
* Telemetry History Service - сервис для хранения и получения истории показаний сенсоров
* Telemetry Service - сервис для получения данных сенсоров
* Heating System Service - сервис для управления системами отопления
* Lighting System Service - сервис для управления устройствами освещения
* Gate System Service - сервис для управления воротами
* CCTV System Service - сервис для управления видеонаблюдением

### Запуск
```shell
docker compose up -d
```
Api сервиса devices-management localhost:8000/devices

Api монолита localhost:8000/monolith

Пример добавления устройства:
```shell
curl --location 'http://localhost:8000/devices/24f07fd0-f95a-4519-b07f-d0f95ab51979' \
--header 'userId: a60ae84d-960f-49de-8ae8-4d960ff9de00' \
--header 'Content-Type: application/json' \
--data '{
    "homeId": "0a2675b2-f5a7-49f5-a675-b2f5a779f513",
    "serialNumber": "serialNumber",
    "deviceType": "HEATING_SYSTEM",
    "model": "model",
    "url": "url"
}'
```
Проверить, что устройство добавилось в монолит:
```shell
curl --location --request GET 'localhost:8000/monolith/heating/1'
```
### Остановка
```shell
docker compose down
```

### Диаграммы
[Container diagram](./diagrams/Microservices_Container.puml)

[Component diagram](./diagrams/Microservices_Component.puml)

[Telemetry Service ER diagram](./diagrams/Microservices_Telemetry_ER.puml)

[Devices Management ER diagram](./diagrams/Microservices_Devices_Management_ER.puml)

[Telemetry History Service ER diagram](./diagrams/Microservices_Telemetry_History_ER.puml)

### Описание API
[OpenApi Devices Management](./api/devices-management-openapi.yaml)

[AsyncApi Devices Management](./api/devices-management-asyncapi.yaml)

[AsyncApi Telemetry Service](./api/telemetry-service-asyncapi.yaml)

[AsyncApi Monolith](./api/monolith-asyncapi.yaml)