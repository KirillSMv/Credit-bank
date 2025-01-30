create table if not exists employments
(
    employment_uuid
    uuid
    unique
    NOT
    NULL,
    employerinn
    varchar
(
    255
),
    employment_status varchar
(
    255
) check
(
    employment_status
    in
(
    'UNEMPLOYED',
    'SELF_EMPLOYED',
    'BUSINESS_OWNER',
    'EMPLOYED'
)) NOT NULL,
    position varchar
(
    255
) check
(
    position
    in
(
    'MID_MANAGER',
    'TOP_MANAGER',
    'OTHER',
    'WORKER',
    'OWNER'
)) NOT NULL,
    salary numeric
(
    38,
    2
) NOT NULL,
    work_experience_current integer NOT NULL,
    work_experience_total integer NOT NULL,
    primary key
(
    employment_uuid
)
    )
    GO