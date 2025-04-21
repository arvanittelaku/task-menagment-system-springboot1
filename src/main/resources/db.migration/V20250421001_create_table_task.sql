CREATE TABLE tasks (
                       id BIGINT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       status VARCHAR(255) NOT NULL,  -- Assuming TaskStatus is a string enum
                       priority VARCHAR(255) NOT NULL, -- Assuming TaskPriority is a string enum
                       deadline DATE NOT NULL,
                       created_at DATE NOT NULL,
                       assigned_to_id BIGINT NOT NULL,  -- Foreign key to the user
                       created_by_id BIGINT NOT NULL,   -- Foreign key to the user
                       CONSTRAINT fk_assigned_to FOREIGN KEY (assigned_to_id) REFERENCES users(id),
                       CONSTRAINT fk_created_by FOREIGN KEY (created_by_id) REFERENCES users(id)
);