INSERT INTO users (id, name, email, phone_number)
VALUES (1, 'admin', 'admin@gmail.com', '751117575');


INSERT INTO role (role_id, role_name)
VALUES (3, 'ADMIN');

INSERT INTO user_info (id, user_name, password, role_id, user_id)
VALUES (2, 'Slava', '$2a$12$i6NkMo61D7uzZfaTAvRasulqLjavoqDsVgl3gDeEcV2rqSntxI4P2', 3, 1);

INSERT INTO event (id, description, recipient, title, type)
VALUES (4, 'Event description example', 'Recipient name', 'Event title', 'Event type');