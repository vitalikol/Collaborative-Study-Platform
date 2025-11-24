ALTER TABLE Memberships RENAME TO membership_old;

CREATE TABLE Memberships (
    membership_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    role TEXT NOT NULL,
    joined_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (group_id) REFERENCES Groups(group_id),
    UNIQUE(user_id, group_id)
);

INSERT INTO Memberships (membership_id, user_id, group_id, role, joined_at)
SELECT membership_id, user_id, group_id, role, joined_at
FROM membership_old;

DROP TABLE membership_old;
