drop table if exists likes cascade;
drop table if exists mpa cascade;
drop table if exists films cascade;
drop table if exists genres cascade;
drop table if exists film_genres cascade;
drop table if exists users cascade;
drop table if exists friendships cascade;


CREATE TABLE IF NOT EXISTS mpa
(
    id     SERIAL PRIMARY KEY,
    rating VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    release_date DATE NOT NULL,
    duration     INT not null,
    mpa_id       INT NOT NULL REFERENCES mpa (id)
);

CREATE TABLE IF NOT EXISTS genres
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  INT NOT NULL REFERENCES films (id),
    genre_id INT NOT NULL REFERENCES genres (id),
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(100) NOT NULL UNIQUE,
    login    VARCHAR(50)  NOT NULL UNIQUE,
    name     VARCHAR      NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friendships
(
    user1_id INT NOT NULL REFERENCES users (id),
    user2_id INT NOT NULL REFERENCES users (id),
    status   VARCHAR(15),
    PRIMARY KEY (user1_id, user2_id)
);

CREATE TABLE IF NOT EXISTS likes
(
    user_id INT NOT NULL REFERENCES users (id),
    film_id INT NOT NULL REFERENCES films (id),
    PRIMARY KEY (user_id, film_id)
)
