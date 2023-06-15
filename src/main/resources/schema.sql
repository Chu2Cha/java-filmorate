DROP TABLE IF EXISTS FILM_GENRE;
DROP TABLE IF EXISTS FILM_USER_RATING;
DROP TABLE IF EXISTS GENRE;
DROP TABLE IF EXISTS FILM;
DROP TABLE IF EXISTS MPA;
DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS USERS;

-- we don't know how to generate root <with-no-name> (class Root) :(
CREATE TABLE IF NOT EXISTS GENRE (
                       GENRE_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
                       GENRE_NAME CHARACTER VARYING NOT NULL
);

create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER PRIMARY KEY AUTO_INCREMENT,
    MPA_NAME CHARACTER VARYING(5) NOT NULL
);

create table IF NOT EXISTS FILM
(
    FILM_ID           INTEGER auto_increment,
    FILM_NAME         CHARACTER VARYING(50) not null,
    DESCRIPTION       CHARACTER VARYING(200),
    FILM_RELEASE_DATE DATE                  not null,
    FILM_DURATION     INTEGER               not null,
    MPA_ID            INTEGER not null references MPA,
    constraint FILM_PK
        primary key (FILM_ID)
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null
        references FILM,
    GENRE_ID INTEGER not null
        references GENRE
);

create table IF NOT EXISTS USERS
(
    USER_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    EMAIL CHARACTER VARYING(200),
    LOGIN CHARACTER VARYING(200) not null,
    NAME CHARACTER VARYING(200),
    BIRTHDAY DATE not null
);

create table IF NOT EXISTS FILM_USER_RATING
(
    USER_ID INTEGER not null
        references USERS,
    FILM_ID  INTEGER not null
        references FILM
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID INTEGER not null
        references USERS,
    FRIEND_ID INTEGER not null
        references USERS,
    FRIEND_STATUS BOOLEAN
);