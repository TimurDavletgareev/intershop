# intershop

Spring-приложение интернет магазин на реактивном стеке

Инструкции для запуска в Docker:
1) Клонировать проект через git clone
2) Запустить Docker
3) Запсутить контейнеры из docker-compose-keycloak.yml
4) Когда поднимется контейнер keycloak зайти в панель администратора http://localhost:8085
    с кредами admin/amin
5) Создать клиента с id "intershop" и настройками https://pictures.s3.yandex.net/resources/image_23_1743002431.png
6) На вкладке Credentials скопировать Client Secret и вставить в docker-compose.yml для переменной KEYCLOAK_CLIENT_SECRET
7) Выполнить mvn clean package
8) Выполнить docker compose up

