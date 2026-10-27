CREATE TABLE redirect_configurations
(
    uuid            UUID         NOT NULL,
    destination_url VARCHAR(255) NOT NULL,
    redirect_type   INTEGER      NOT NULL,
    enabled         BOOLEAN      NOT NULL,
    description     VARCHAR(500),
    created_at      TIMESTAMP(6) WITHOUT TIME ZONE,
    updated_at      TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_redirect_configurations PRIMARY KEY (uuid)
);

INSERT INTO redirect_configurations VALUES ('126147c0-05a9-4a83-8b59-f3c8763a85c0','https://temp.example.com',307,TRUE,'test1','2024-10-21 10:00:00','2024-10-21 10:00:00');
INSERT INTO redirect_configurations VALUES ('6f372f07-f8c3-47f7-90f4-f5c3e980f23f','https://perm.example.com',308,TRUE,'test2','2024-10-21 10:00:00','2024-10-21 10:00:00');