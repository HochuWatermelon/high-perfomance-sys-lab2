# high-perfomance-sys-lab2
2 лабочка по Высокопроизводительным системам
# Запуск:
1. docker compose build
2. Запуск контейнеров в следующем порядке (ждем, пока полностю прогрузится, потом переходим к следующему):
   - database (перед этим создаем локально cоединение freelance на порту 5433, туда кладём базочки сервисов)
   - all migrations containers
   - config-server
   - eureka-server
   - gateway-server
   - auth-service (бдшка freelance-users)
   - worker-service (бдшка freelance-worker) и order-service(бдшка freelance-order)
3. В базочку данных freelance-users вбиваем следующую sql-ину
INSERT INTO public.users
(id, full_name, "password", confirmed, "blocked", "role")
VALUES('20f985c8-fae0-4e7c-a74c-bdd6afd25cdb'::uuid, 'I AM SUPERVISOR', '$2a$10$yretvD5tX91H1RAtfWTAqutmt3dtlY2eRcFR6D7GmO4hck7TcY/8.', true, false, 'SUPERVISOR');
Так мы создадим тестовые данные SUPERVISOR, босса этой качалки, ему подвластно клипать юзеров
С помощью POST запроса на http://localhost:8088/freelance-api/auth/tokens
с телом запроса
{
    "fullName": "I AM SUPERVISOR",
    "password": "SoFresh"
}
получаем access-токен супервайзора
4. Кайфуем от рабочей 2 лабочки
