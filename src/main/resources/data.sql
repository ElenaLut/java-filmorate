-- заполняем таблицу rate
insert into rate values ( 1,'G' );
insert into rate values ( 2,'PG' );
insert into rate values ( 3,'PG-13' );
insert into rate values ( 4,'R' );
insert into rate values ( 5,'NC-17' );

-- заполняем таблицу films
/*INSERT INTO films (FILM_NAME, description, release_date, duration, RATE_ID) VALUES ('Побег из Шоушенка', 'Молодого финансиста Энди Дюфрейна подозревают в убийстве, которого он не совершал. Несмотря на это его приговаривают к пожизненному заключению в тюрьме, из которой ещё никому не удавалось сбежать.', '2022-10-10', 100, 1);
INSERT INTO films (FILM_NAME, description, release_date, duration, RATE_ID) VALUES ('Крёстный отец', 'Первая часть криминальной саги о сицилийской мафиозной семье Корлеоне, которая обладает огромным авторитетом в Нью-Йорке.', '2022-10-11', 60, 2);
INSERT INTO films (FILM_NAME, description, release_date, duration, RATE_ID) VALUES ('Тёмный рыцарь', 'Городу в очередной раз требуется герой, и им традиционно становится Бэтмен, задача которого — ликвидировать преступника по кличке Джокер.', '2022-10-13', 60, 3);
*/
-- заполняем таблицу genre
INSERT INTO PUBLIC.genre (genre_name) VALUES ('Комедия');
INSERT INTO PUBLIC.genre (genre_name) VALUES ('Драма');
INSERT INTO PUBLIC.genre (genre_name) VALUES ('Мультфильм');
INSERT INTO PUBLIC.genre (genre_name) VALUES ('Триллер');
INSERT INTO PUBLIC.genre (genre_name) VALUES ('Документальный');
INSERT INTO PUBLIC.genre (genre_name) VALUES ('Боевик');

-- заполняем таблицу users
/*INSERT INTO users (email, login, user_name, birthday) values ('1@ya.ru', 'login1', 'Федорова Алина', '2021-10-20');
INSERT INTO users (email, login, user_name, birthday) values ('2@ya.ru', 'login2', 'Чернова Екатерина', '2021-11-21');
INSERT INTO users (email, login, user_name, birthday) values ('3@ya.ru', 'login3', 'Столяров Даниил', '2021-12-22');
*/