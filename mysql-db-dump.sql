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
    reward_claimed BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES tournament(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES tournament_group(id) ON DELETE CASCADE
);

CREATE INDEX idx_is_active ON tournament(is_active);

CREATE INDEX idx_tournament_id ON tournament_group(tournament_id);

CREATE INDEX idx_user_id ON Participant(user_id);
CREATE INDEX idx_tournament_participant ON participant(tournament_id);
CREATE INDEX idx_tournament_country ON country_score(tournament_id);
CREATE INDEX idx_group_id ON participant(group_id);