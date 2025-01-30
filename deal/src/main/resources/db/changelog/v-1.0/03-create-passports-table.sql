create table if not exists passports
(
    passport_id_uuid
    uuid
    unique
    NOT
    NULL,
    issue_branch
    varchar
(
    255
),
    issue_date date,
    number varchar
(
    255
) NOT NULL,
    series varchar
(
    255
) NOT NULL,
    primary key
(
    passport_id_uuid
)
    )
    GO

