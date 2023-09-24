## ТЕСТОВОЕ ЗАДАНИЕ https://nomia2.notion.site/2-Backend-developer-804cfb616b914788a640d107fb2853c8

## COMPILE AND RUN

    mvn clean package

Дальше запускаем джарник

    cd target/
    java -jar test-task-*.jar 

## **СТАРТ**

Билд докера

    docker build -t task:local .

Запуск контейнера

    docker run -p 8889:8889 task:local
