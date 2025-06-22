# intershop

Spring-приложение интернет магазин на реактивном стеке

Инструкции для запуска в Docker:
1) Клонировать проект через git clone
2) Выполнить mvn clean package
3) Выполнить docker compose up

docker run -d -p 8085:8080 --name keycloak -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.1.3 start-dev