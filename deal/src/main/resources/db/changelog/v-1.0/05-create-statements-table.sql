create table if not exists statements
(
    statement_id_uuid
    uuid
    not
    null,
    applied_offer
    jsonb,
    creation_date
    timestamp
(
    6
),
    ses varchar
(
    255
),
    sign_date timestamp
(
    6
),
    status varchar
(
    255
) check
(
    status
    in
(
    'PREAPPROVAL',
    'DENIED',
    'APPROVED',
    'CC_DENIED',
    'CC_APPROVED',
    'PREPARE_DOCUMENTS',
    'DOCUMENT_CREATED',
    'CLIENT_DENIED',
    'DOCUMENT_SIGNED',
    'CREDIT_ISSUED'
)) NOT NULL,
    status_history jsonb,
    client_client_id_uuid uuid,
    credit_credit_id_uuid uuid,
    CONSTRAINT pk_statements primary key
(
    statement_id_uuid
),
    CONSTRAINT statements_clients_fk foreign key
(
    client_client_id_uuid
) references clients
(
    client_id_uuid
) ON delete CASCADE
  ON update CASCADE,
    CONSTRAINT statements_credits_fk foreign key
(
    credit_credit_id_uuid
) references credits
(
    credit_id_uuid
)
  ON delete CASCADE
  ON update CASCADE
    )
    GO
