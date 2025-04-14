create table categories (
                            id bigserial primary key,
                            name varchar(255) not null,
                            parent_id bigint,
                            constraint fk_category_parent
                                foreign key (parent_id) references categories(id) on delete cascade
);