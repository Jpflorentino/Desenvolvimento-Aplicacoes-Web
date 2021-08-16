create schema dbo;

-- Makes execution independent - Active schema
SET SEARCH_PATH TO dbo;

CREATE TABLE user_account(
    user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(1024) NOT NULL
);

CREATE TABLE project(
    user_id INT,
    project_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(100) NOT NULL UNIQUE,
	description VARCHAR(255) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE ON UPDATE CASCADE -- On UPDATE OR DELETE the parent, the change is cascaded to the child.
);

CREATE TABLE label(
    project_id INT,
    label_name VARCHAR(30) NOT NULL,

    PRIMARY KEY (project_id, label_name),
    FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE state(
    project_id INT,
    state_name VARCHAR(30) NOT NULL,

    PRIMARY KEY (project_id, state_name),
    FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE state_transits_to(
    project_id INT,
    state_name VARCHAR(30),
    transits_to VARCHAR(30),

    PRIMARY KEY (project_id, state_name, transits_to),
    FOREIGN KEY (project_id, state_name) REFERENCES state (project_id, state_name) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (project_id, transits_to) REFERENCES state (project_id, state_name) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE issue(
    user_id INT,
    project_id INT,
    issue_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    opened_on INT NOT NULL,
    closed_on INT,
    state_name VARCHAR(30),

    FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (state_name,project_id) REFERENCES state (state_name,project_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE issue_has_labels(
    project_id INT,
    issue_id INT,
    label_name VARCHAR(30),

    PRIMARY KEY (project_id, issue_id, label_name),
    FOREIGN KEY (issue_id) REFERENCES issue (issue_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (project_id, label_name) REFERENCES label (project_id, label_name) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE comment(
    user_id INT,
    issue_id INT,
    comment_id INT GENERATED ALWAYS AS IDENTITY,
    comment_text VARCHAR(255) NOT NULL,
    creation_date INT NOT NULL,

    PRIMARY KEY (user_id, issue_id, comment_id),
    FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (issue_id) REFERENCES issue (issue_id) ON DELETE CASCADE ON UPDATE CASCADE
);
