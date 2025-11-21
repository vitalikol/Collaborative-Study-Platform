CREATE TABLE Groups_new (
    group_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    created_by INTEGER NOT NULL,
    created_at INTEGER NOT NULL,
    FOREIGN KEY (created_by) REFERENCES Users(user_id)
);

DROP TABLE Groups;
ALTER TABLE Groups_new RENAME TO Groups;

CREATE TABLE Memberships_new (
    membership_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    role TEXT NOT NULL,
    joined_at INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (group_id) REFERENCES Groups(group_id)
);

DROP TABLE Memberships;
ALTER TABLE Memberships_new RENAME TO Memberships;

CREATE TABLE Tasks_new (
    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
    group_id INTEGER NOT NULL,
    created_by INTEGER NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL DEFAULT 'IN_PROGRESS',
    deadline TEXT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES Groups(group_id),
    FOREIGN KEY (created_by) REFERENCES Users(user_id)
);

DROP TABLE Tasks;
ALTER TABLE Tasks_new RENAME TO Tasks;
CREATE TABLE Resources_new (
    resource_id INTEGER PRIMARY KEY AUTOINCREMENT,
    group_id INTEGER NOT NULL,
    uploaded_by INTEGER NOT NULL,
    title TEXT NOT NULL,
    type TEXT NOT NULL,
    format TEXT NOT NULL,
    path_or_url TEXT NOT NULL,
    uploaded_at INTEGER NOT NULL,
    FOREIGN KEY (group_id) REFERENCES Groups(group_id),
    FOREIGN KEY (uploaded_by) REFERENCES Users(user_id)
);

DROP TABLE Resources;
ALTER TABLE Resources_new RENAME TO Resources;
CREATE TABLE Activity_Log_new (
    log_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    action TEXT NOT NULL,
    timestamp INTEGER NOT NULL,
    details TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

DROP TABLE Activity_Log;
ALTER TABLE Activity_Log_new RENAME TO Activity_Log;
