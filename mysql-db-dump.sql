CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    country VARCHAR(50) NOT NULL,
    level INT NOT NULL,
    coin INT NOT NULL,
    UNIQUE KEY(username)
);

CREATE TABLE IF NOT EXISTS tournament(
    id VARCHAR(36) PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS tournament_group (
    id VARCHAR(36) PRIMARY KEY,
    tournament_id VARCHAR(36) NOT NULL,
    ready BOOLEAN NOT NULL,
    FOREIGN KEY (tournament_id) REFERENCES tournament(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS country_score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tournament_id VARCHAR(36) NOT NULL,
    country VARCHAR(50) NOT NULL,
    total_score INT NOT NULL,
    FOREIGN KEY (tournament_id) REFERENCES tournament(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    score INT NOT NULL,
    country VARCHAR(50) NOT NULL,
    tournament_id VARCHAR(36) NOT NULL,
    group_id VARCHAR(36) NOT NULL,
    has_reward BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES tournament(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES tournament_group(id) ON DELETE CASCADE
);

CREATE INDEX idx_is_active ON tournament(is_active);

CREATE INDEX idx_tournament_id ON tournament_group(tournament_id);

CREATE INDEX idx_user_id ON participant(user_id);
CREATE INDEX idx_tournament_participant ON participant(tournament_id);
CREATE INDEX idx_tournament_country ON country_score(tournament_id);
CREATE INDEX idx_group_id ON participant(group_id);

INSERT INTO user (id, username, country, level, coin) VALUES
('15f20419-f65e-4668-acb7-c740d13c5c87','player6','US',30,5000),
('1649acd0-eba1-48db-8eb9-7b6dfdb4f53e','player3','TR',32,5050),
('3846b00a-324d-4d4b-8025-0268de998035','player9','FR',30,5000),
('6931a176-9558-4700-9d03-626b79f587c7','player7','DE',30,5000),
('7e49bd35-0534-4c03-b4bc-7b3ccfc06973','player2','UK',39,5225),
('b9538a25-453f-4f38-ac9f-187422b146d2','player1','US',35,5125),
('d10fb40f-b5fd-489d-9b68-3a397ee15638','player8','TR',30,5000),
('dda68aa2-ce63-4ee4-970f-9c8c2f225203','player10','UK',30,5000),
('e2c618f4-f1bd-4496-8996-2e3a7b33daaa','player5','DE',31,5025),
('f4b05222-6a57-42ac-a4b3-16620941e87b','player4','TR',30,5000);


INSERT INTO tournament (id, start_time, end_time, is_active) VALUES
('9e919021-e68c-4aa2-8136-033a46fcdc67','2024-10-02 21:00:00','2024-10-03 17:00:00',0),
('e88d92b1-78c0-470a-932c-89b139ec324c','2024-10-02 21:00:00','2024-10-03 17:00:00',1);


INSERT INTO tournament_group (id, tournament_id, ready) VALUES
('4e700331-0957-4d65-a52c-7fe8c45e4df0','9e919021-e68c-4aa2-8136-033a46fcdc67',1),
('a25a125b-9945-45c5-a4e4-3b36cbe83a87','9e919021-e68c-4aa2-8136-033a46fcdc67',0);

INSERT INTO country_score (id, tournament_id, country, total_score) VALUES
(46,'9e919021-e68c-4aa2-8136-033a46fcdc67','UK',9),
(47,'9e919021-e68c-4aa2-8136-033a46fcdc67','TR',2),
(48,'9e919021-e68c-4aa2-8136-033a46fcdc67','US',5),
(49,'9e919021-e68c-4aa2-8136-033a46fcdc67','FR',0),
(50,'9e919021-e68c-4aa2-8136-033a46fcdc67','DE',1),
(51,'e88d92b1-78c0-470a-932c-89b139ec324c','UK',0),
(52,'e88d92b1-78c0-470a-932c-89b139ec324c','TR',0),
(53,'e88d92b1-78c0-470a-932c-89b139ec324c','US',0),
(54,'e88d92b1-78c0-470a-932c-89b139ec324c','FR',0),
(55,'e88d92b1-78c0-470a-932c-89b139ec324c','DE',0);

INSERT INTO participant (id, user_id, score, country, tournament_id, group_id, has_reward) VALUES
(28,'b9538a25-453f-4f38-ac9f-187422b146d2',5,'US','9e919021-e68c-4aa2-8136-033a46fcdc67','4e700331-0957-4d65-a52c-7fe8c45e4df0',1),
(29,'7e49bd35-0534-4c03-b4bc-7b3ccfc06973',9,'UK','9e919021-e68c-4aa2-8136-033a46fcdc67','4e700331-0957-4d65-a52c-7fe8c45e4df0',1),
(30,'1649acd0-eba1-48db-8eb9-7b6dfdb4f53e',2,'TR','9e919021-e68c-4aa2-8136-033a46fcdc67','4e700331-0957-4d65-a52c-7fe8c45e4df0',0),
(31,'e2c618f4-f1bd-4496-8996-2e3a7b33daaa',1,'DE','9e919021-e68c-4aa2-8136-033a46fcdc67','4e700331-0957-4d65-a52c-7fe8c45e4df0',0),
(32,'3846b00a-324d-4d4b-8025-0268de998035',0,'FR','9e919021-e68c-4aa2-8136-033a46fcdc67','4e700331-0957-4d65-a52c-7fe8c45e4df0',0),
(33,'15f20419-f65e-4668-acb7-c740d13c5c87',0,'US','9e919021-e68c-4aa2-8136-033a46fcdc67','a25a125b-9945-45c5-a4e4-3b36cbe83a87',0),
(34,'6931a176-9558-4700-9d03-626b79f587c7',0,'DE','9e919021-e68c-4aa2-8136-033a46fcdc67','a25a125b-9945-45c5-a4e4-3b36cbe83a87',0);