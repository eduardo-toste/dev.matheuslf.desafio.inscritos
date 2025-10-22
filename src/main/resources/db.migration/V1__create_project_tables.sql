CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE
);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    status VARCHAR(20),
    priority VARCHAR(20),
    due_date DATE,
    project_id INT NOT NULL,
    CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects(id)
);