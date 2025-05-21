CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       status VARCHAR(50) NOT NULL,   -- TaskStatus enum as string
                       priority VARCHAR(50) NOT NULL, -- TaskPriority enum as string
                       deadline DATE NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       assigned_to_id BIGINT NOT NULL,
                       created_by_id BIGINT NOT NULL,

                       CONSTRAINT fk_assigned_to FOREIGN KEY (assigned_to_id) REFERENCES users(id) ON DELETE CASCADE,
                       CONSTRAINT fk_created_by FOREIGN KEY (created_by_id) REFERENCES users(id) ON DELETE CASCADE,

    -- Optional enum enforcement (PostgreSQL-specific)
                       CONSTRAINT status_check CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELED')),
                       CONSTRAINT priority_check CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH'))
);
