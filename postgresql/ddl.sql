CREATE SCHEMA IF NOT EXISTS app
    AUTHORIZATION postgres;
	
CREATE TABLE IF NOT EXISTS app.transaction_types
(
    name text NOT NULL,
    CONSTRAINT transaction_type_pkey PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS app.currencies
(
    name text NOT NULL,
    CONSTRAINT currency_pkey PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS app.clients
(
    id uuid NOT NULL,
    name text NOT NULL,
    CONSTRAINT clients_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS app.banks
(
    id uuid NOT NULL,
    name text NOT NULL,
    CONSTRAINT bank_pkey PRIMARY KEY (id),
    CONSTRAINT banks_name_key UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS app.banks_clients
(
    id_bank uuid NOT NULL,
    id_clients uuid NOT NULL,
    CONSTRAINT banks_clients_id_bank_id_clients_key UNIQUE (id_bank, id_clients),
    CONSTRAINT banks_clients_id_bank_fkey FOREIGN KEY (id_bank)
        REFERENCES app.banks (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT banks_clients_id_clients_fkey FOREIGN KEY (id_clients)
        REFERENCES app.clients (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS app.accounts
(
    num uuid NOT NULL,
    name_currency text NOT NULL,
    id_bank uuid NOT NULL,
    date_open date NOT NULL,
    balance double precision NOT NULL,
    id_client uuid NOT NULL,
    date_last_transaction timestamp without time zone NOT NULL,
    CONSTRAINT accounts_pkey PRIMARY KEY (num),
    CONSTRAINT accounts_id_bank_fkey FOREIGN KEY (id_bank)
        REFERENCES app.banks (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT accounts_id_client_fkey FOREIGN KEY (id_client)
        REFERENCES app.clients (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT accounts_name_currency_fkey FOREIGN KEY (name_currency)
        REFERENCES app.currencies (name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS app.transactions
(
    id uuid NOT NULL,
    name_transaction_type text NOT NULL,
    name_currency text NOT NULL,
    date timestamp without time zone NOT NULL,
    CONSTRAINT transactions_pkey PRIMARY KEY (id),
    CONSTRAINT transactions_name_currency_fkey FOREIGN KEY (name_currency)
        REFERENCES app.currencies (name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT transactions_name_transaction_type_fkey FOREIGN KEY (name_transaction_type)
        REFERENCES app.transaction_types (name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS app.account_transaction
(
    num_account uuid NOT NULL,
    id_transaction uuid NOT NULL,
    sum double precision NOT NULL,
    CONSTRAINT account_transaction_num_account_id_transaction_key UNIQUE (num_account, id_transaction),
    CONSTRAINT account_transaction_id_transaction_fkey FOREIGN KEY (id_transaction)
        REFERENCES app.transactions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT account_transaction_num_account_fkey FOREIGN KEY (num_account)
        REFERENCES app.accounts (num) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


