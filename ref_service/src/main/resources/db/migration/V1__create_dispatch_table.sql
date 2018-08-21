CREATE TABLE public.dispatch
(
    id uuid,
    recipient varchar not null,
    channel varchar not null,
    content varchar not null,
    expiry_at timestamp without time zone not null,
    retries_count smallint,
    PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);