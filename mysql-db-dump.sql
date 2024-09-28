CREATE TABLE IF NOT EXISTS User (
    id VARCHAR(16) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    country VARCHAR(50) NOT NULL,
    level INT NOT NULL,
    coin INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Tournament (
    id VARCHAR(16) PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS TournamentGroup (
    id VARCHAR(16) PRIMARY KEY,
    tournament_id VARCHAR(16) NOT NULL,
    ready BOOLEAN NOT NULL,
    FOREIGN KEY (tournament_id) REFERENCES Tournament(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS CountryScore (
    id BIGINT PRIMARY KEY,
    tournament_id VARCHAR(16) NOT NULL,
    country VARCHAR(50) NOT NULL,
    total_score INT NOT NULL,
    FOREIGN KEY (tournament_id) REFERENCES Tournament(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Participant (
    id BIGINT PRIMARY KEY,
    user_id VARCHAR(16) NOT NULL,
    score INT NOT NULL,
    country VARCHAR(50) NOT NULL,
    tournament_id VARCHAR(16) NOT NULL,
    group_id VARCHAR(16) NOT NULL,
    reward_claimed BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES Tournament(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES TournamentGroup(id) ON DELETE CASCADE
);

CREATE INDEX idx_is_active ON Tournament(is_active);

CREATE INDEX idx_tournament_id ON TournamentGroup(tournament_id);

CREATE INDEX idx_user_id ON Participant(user_id);
CREATE INDEX idx_tournament_participant ON Participant(tournament_id);
CREATE INDEX idx_tournament_country ON CountryScore(tournament_id);
CREATE INDEX idx_group_id ON Participant(group_id);