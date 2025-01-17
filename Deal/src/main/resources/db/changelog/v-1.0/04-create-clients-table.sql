create table if not exists clients (
        client_id_uuid uuid NOT NULL,
        account_number varchar(255),
        birthdate date NOT NULL,
        dependent_amount integer,
        email varchar(255) unique NOT NULL,
        first_name varchar(255) NOT NULL,
        gender varchar(255) check (gender in ('MALE','FEMALE','NOTBINARY')),
        last_name varchar(255) NOT NULL,
        marital_status varchar(255) check (marital_status in ('MARRIED','DIVORCED','SINGLE','WIDOW_WIDOWER')),
        middle_name varchar(255),
        employment_employment_uuid uuid unique,
        passport_passport_id_uuid uuid unique,
        CONSTRAINT pk_clients primary key (client_id_uuid),
        CONSTRAINT clients_employment_fk foreign key(employment_employment_uuid) references employments(employment_uuid) ON delete CASCADE ON update CASCADE,
        CONSTRAINT clients_passport_fk foreign key(passport_passport_id_uuid) references passports(passport_id_uuid) ON delete CASCADE ON update CASCADE
    )

GO
