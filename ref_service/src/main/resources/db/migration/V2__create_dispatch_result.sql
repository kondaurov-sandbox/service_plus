CREATE TABLE public.dispatch_result (
    id uuid,
    status varchar not null,
    delivered_at timestamp without time zone,
    error_msg varchar,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES public.dispatch(id)
)
WITH (
    OIDS = FALSE
);