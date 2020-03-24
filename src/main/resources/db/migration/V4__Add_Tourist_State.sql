create table state
(
    user_id bigint not null,
    chat_id bigint not null,
    state   int    not null,
    primary key (user_id, chat_id)
);