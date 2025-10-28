-- Add created_by_manager_id column to users table
ALTER TABLE users 
ADD COLUMN created_by_manager_id BIGINT,
ADD CONSTRAINT fk_created_by_manager 
    FOREIGN KEY (created_by_manager_id) 
    REFERENCES users(id) 
    ON DELETE SET NULL;

-- Create index for better query performance
CREATE INDEX idx_users_created_by_manager ON users(created_by_manager_id);

