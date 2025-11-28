-- flyway:transactional=false
PRAGMA foreign_keys = ON;
CREATE TABLE Users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role TEXT DEFAULT 'ROLE_USER'
);
CREATE TABLE Groups (
    group_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    created_by INTEGER NOT NULL,
    created_at INTEGER,
    FOREIGN KEY (created_by) REFERENCES Users(user_id)
);
CREATE TABLE Memberships (
     membership_id INTEGER PRIMARY KEY AUTOINCREMENT,
     user_id INTEGER NOT NULL,
     group_id INTEGER NOT NULL,
     role TEXT NOT NULL,
     joined_at INTEGER,
     FOREIGN KEY (user_id) REFERENCES Users(user_id),
     FOREIGN KEY (group_id) REFERENCES Groups(group_id),
     UNIQUE(user_id, group_id)
);
CREATE TABLE Tasks (
    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
    group_id INTEGER NOT NULL,
    created_by INTEGER NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL DEFAULT 'IN_PROGRESS',
    deadline TEXT,
    created_at INTEGER,
    FOREIGN KEY (group_id) REFERENCES Groups(group_id),
    FOREIGN KEY (created_by) REFERENCES Users(user_id)
);
CREATE TABLE Resources (
    resource_id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    uploaded_by INTEGER NOT NULL,
    title TEXT NOT NULL,
    type TEXT NOT NULL,
    format TEXT NOT NULL,
    path_or_url TEXT,
    uploaded_at INTEGER,
    FOREIGN KEY (task_id) REFERENCES Tasks(task_id),
    FOREIGN KEY (uploaded_by) REFERENCES Users(user_id)
);
CREATE TABLE Activity_Log (
    log_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    action TEXT NOT NULL,
    timestamp INTEGER,
    details TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);