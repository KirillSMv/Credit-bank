create table if not exists credits (
        credit_id_uuid uuid UNIQUE NOT NULL,
        amount numeric(38,2) NOT NULL,
        credit_status varchar(255) check (credit_status in ('CALCULATED','ISSUED')) NOT NULL,
        insurance_enabled boolean NOT NULL,
        monthly_payment numeric(38,2) NOT NULL,
        payment_schedule jsonb NOT NULL,
        psk numeric(38,2) NOT NULL,
        rate numeric(38,2) NOT NULL,
        salary_client boolean NOT NULL,
        term integer NOT NULL,
        primary key (credit_id_uuid)
    )

GO

