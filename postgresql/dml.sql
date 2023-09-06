INSERT INTO app.transaction_types(name) VALUES ('WAGE');
INSERT INTO app.transaction_types(name) VALUES ('MONEY_TRANSFER');
INSERT INTO app.transaction_types(name) VALUES ('DEPOSIT_INTEREST');
INSERT INTO app.transaction_types(name) VALUES ('WITHDRAWALS');
INSERT INTO app.transaction_types(name) VALUES ('PAYMENT_FOR_SERVICES');
INSERT INTO app.transaction_types(name) VALUES ('CASH_REPLENISHMENT');

INSERT INTO app.currencies(name) VALUES ('USD');
INSERT INTO app.currencies(name) VALUES ('BYN');
INSERT INTO app.currencies(name) VALUES ('RUB');
INSERT INTO app.currencies(name) VALUES ('EUR');

INSERT INTO app.banks(id, name)	VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', 'Bank-1');
INSERT INTO app.banks(id, name)	VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', 'Bank-2');
INSERT INTO app.banks(id, name)	VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', 'Bank-3');
INSERT INTO app.banks(id, name)	VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', 'Bank-4');
INSERT INTO app.banks(id, name)	VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', 'Bank-5');
INSERT INTO app.banks(id, name)	VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', 'Clever-Bank');

INSERT INTO app.clients (id, name) VALUES ('7e605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-1');
INSERT INTO app.clients (id, name) VALUES ('8e605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-2');
INSERT INTO app.clients (id, name) VALUES ('9e605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-3');
INSERT INTO app.clients (id, name) VALUES ('10605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-4');
INSERT INTO app.clients (id, name) VALUES ('11605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-5');
INSERT INTO app.clients (id, name) VALUES ('12605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-6');
INSERT INTO app.clients (id, name) VALUES ('13605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-7');
INSERT INTO app.clients (id, name) VALUES ('14605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-8');
INSERT INTO app.clients (id, name) VALUES ('15605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-9');
INSERT INTO app.clients (id, name) VALUES ('16605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-10');
INSERT INTO app.clients (id, name) VALUES ('17605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-11');
INSERT INTO app.clients (id, name) VALUES ('18605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-12');
INSERT INTO app.clients (id, name) VALUES ('19605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-13');
INSERT INTO app.clients (id, name) VALUES ('20605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-14');
INSERT INTO app.clients (id, name) VALUES ('21605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-15');
INSERT INTO app.clients (id, name) VALUES ('22605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-16');
INSERT INTO app.clients (id, name) VALUES ('23605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-17');
INSERT INTO app.clients (id, name) VALUES ('24605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-18');
INSERT INTO app.clients (id, name) VALUES ('25605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-19');
INSERT INTO app.clients (id, name) VALUES ('26605bea-4688-4a8a-b64f-d29e24eb6d81', 'Client-20');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '1e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_DATE, 10000, '7e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', '7e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '1e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_DATE, 10000, '8e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', '8e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '1e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_DATE, 10000, '9e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', '9e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '1e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_DATE, 10000, '10605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', '10605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '1e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_DATE, 10000, '11605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', '11605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '1e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_DATE, 10000, '12605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', '12605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '1e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_DATE, 10000, '13605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', '13605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '1e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_DATE, 10000, '14605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('1e605bea-4688-4a8a-b64f-d29e24eb6d81', '14605bea-4688-4a8a-b64f-d29e24eb6d81');


INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '2e605bea-4688-4a8a-b64f-d29e24eb6d82', CURRENT_DATE, 10000, '7e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', '7e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '2e605bea-4688-4a8a-b64f-d29e24eb6d82', CURRENT_DATE, 10000, '8e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', '8e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '2e605bea-4688-4a8a-b64f-d29e24eb6d82', CURRENT_DATE, 10000, '9e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', '9e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '2e605bea-4688-4a8a-b64f-d29e24eb6d82', CURRENT_DATE, 10000, '26605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', '26605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '2e605bea-4688-4a8a-b64f-d29e24eb6d82', CURRENT_DATE, 10000, '11605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', '11605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '2e605bea-4688-4a8a-b64f-d29e24eb6d82', CURRENT_DATE, 10000, '12605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', '12605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '2e605bea-4688-4a8a-b64f-d29e24eb6d82', CURRENT_DATE, 10000, '13605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', '13605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '2e605bea-4688-4a8a-b64f-d29e24eb6d82', CURRENT_DATE, 10000, '14605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('2e605bea-4688-4a8a-b64f-d29e24eb6d82', '14605bea-4688-4a8a-b64f-d29e24eb6d81');


INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '3e605bea-4688-4a8a-b64f-d29e24eb6d83', CURRENT_DATE, 10000, '7e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', '7e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '3e605bea-4688-4a8a-b64f-d29e24eb6d83', CURRENT_DATE, 10000, '8e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', '8e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '3e605bea-4688-4a8a-b64f-d29e24eb6d83', CURRENT_DATE, 10000, '9e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', '9e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '3e605bea-4688-4a8a-b64f-d29e24eb6d83', CURRENT_DATE, 10000, '25605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', '25605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '3e605bea-4688-4a8a-b64f-d29e24eb6d83', CURRENT_DATE, 10000, '11605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', '11605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '3e605bea-4688-4a8a-b64f-d29e24eb6d83', CURRENT_DATE, 10000, '12605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', '12605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '3e605bea-4688-4a8a-b64f-d29e24eb6d83', CURRENT_DATE, 10000, '13605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', '13605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '3e605bea-4688-4a8a-b64f-d29e24eb6d83', CURRENT_DATE, 10000, '14605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('3e605bea-4688-4a8a-b64f-d29e24eb6d83', '14605bea-4688-4a8a-b64f-d29e24eb6d81');


INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '4e605bea-4688-4a8a-b64f-d29e24eb6d84', CURRENT_DATE, 10000, '7e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', '7e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '4e605bea-4688-4a8a-b64f-d29e24eb6d84', CURRENT_DATE, 10000, '8e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', '8e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '4e605bea-4688-4a8a-b64f-d29e24eb6d84', CURRENT_DATE, 10000, '9e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', '9e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '4e605bea-4688-4a8a-b64f-d29e24eb6d84', CURRENT_DATE, 10000, '10605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', '10605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '4e605bea-4688-4a8a-b64f-d29e24eb6d84', CURRENT_DATE, 10000, '11605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', '11605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '4e605bea-4688-4a8a-b64f-d29e24eb6d84', CURRENT_DATE, 10000, '12605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', '12605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '4e605bea-4688-4a8a-b64f-d29e24eb6d84', CURRENT_DATE, 10000, '13605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', '13605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '4e605bea-4688-4a8a-b64f-d29e24eb6d84', CURRENT_DATE, 10000, '14605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('4e605bea-4688-4a8a-b64f-d29e24eb6d84', '14605bea-4688-4a8a-b64f-d29e24eb6d81');


INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '5e605bea-4688-4a8a-b64f-d29e24eb6d85', CURRENT_DATE, 10000, '7e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', '7e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '5e605bea-4688-4a8a-b64f-d29e24eb6d85', CURRENT_DATE, 10000, '8e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', '8e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '5e605bea-4688-4a8a-b64f-d29e24eb6d85', CURRENT_DATE, 10000, '9e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', '9e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '5e605bea-4688-4a8a-b64f-d29e24eb6d85', CURRENT_DATE, 10000, '10605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', '10605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '5e605bea-4688-4a8a-b64f-d29e24eb6d85', CURRENT_DATE, 10000, '11605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', '11605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '5e605bea-4688-4a8a-b64f-d29e24eb6d85', CURRENT_DATE, 10000, '18605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', '18605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '5e605bea-4688-4a8a-b64f-d29e24eb6d85', CURRENT_DATE, 10000, '13605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', '13605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '5e605bea-4688-4a8a-b64f-d29e24eb6d85', CURRENT_DATE, 10000, '14605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('5e605bea-4688-4a8a-b64f-d29e24eb6d85', '14605bea-4688-4a8a-b64f-d29e24eb6d81');


INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '6e605bea-4688-4a8a-b64f-d29e24eb6d86', CURRENT_DATE, 10000, '7e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', '7e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'USD', '6e605bea-4688-4a8a-b64f-d29e24eb6d86', CURRENT_DATE, 10000, '16605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', '16605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '6e605bea-4688-4a8a-b64f-d29e24eb6d86', CURRENT_DATE, 10000, '9e605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', '9e605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'BYN', '6e605bea-4688-4a8a-b64f-d29e24eb6d86', CURRENT_DATE, 10000, '10605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', '10605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '6e605bea-4688-4a8a-b64f-d29e24eb6d86', CURRENT_DATE, 10000, '11605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', '11605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'RUB', '6e605bea-4688-4a8a-b64f-d29e24eb6d86', CURRENT_DATE, 10000, '12605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', '12605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '6e605bea-4688-4a8a-b64f-d29e24eb6d86', CURRENT_DATE, 10000, '13605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', '13605bea-4688-4a8a-b64f-d29e24eb6d81');

INSERT INTO app.accounts(num, name_currency, id_bank, date_open, balance, id_client, date_last_transaction)	
VALUES (gen_random_uuid(), 'EUR', '6e605bea-4688-4a8a-b64f-d29e24eb6d86', CURRENT_DATE, 10000, '14605bea-4688-4a8a-b64f-d29e24eb6d81', CURRENT_TIMESTAMP);
INSERT INTO app.banks_clients(id_bank, id_clients) VALUES ('6e605bea-4688-4a8a-b64f-d29e24eb6d86', '14605bea-4688-4a8a-b64f-d29e24eb6d81');
