CREATE TABLE IF NOT EXISTS account (
                         id INT IDENTITY PRIMARY KEY,
                         username VARCHAR NOT NULL,
                         email VARCHAR NOT NULL UNIQUE,
                         phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS ticket (
                        id INT IDENTITY PRIMARY KEY,
                        session_id INT NOT NULL,
                        rowplace INT NOT NULL,
                        cell INT NOT NULL,
                        account_id INT NOT NULL REFERENCES account(id),
                        CONSTRAINT constraint_ticket UNIQUE (session_id, rowplace, cell)
);

CREATE TABLE IF NOT EXISTS hall (
                         id INT IDENTITY PRIMARY KEY,
                         rowplace INT NOT NULL,
                         cell INT NOT NULL,
                         session_id INT not null,
                         available BOOLEAN
);