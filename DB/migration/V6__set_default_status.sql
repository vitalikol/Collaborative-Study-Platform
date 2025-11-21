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

INSERT INTO Tasks_new (task_id, group_id, created_by, title, description, status, deadline, created_at)
SELECT task_id, group_id, created_by, title, description, status, deadline, created_at
FROM Tasks;

DROP TABLE Tasks;

ALTER TABLE Tasks_new RENAME TO Tasks;