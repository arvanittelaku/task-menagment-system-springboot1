ALTER TABLE task DROP CONSTRAINT fk_task_on_assigned_to;
ALTER TABLE task DROP CONSTRAINT fk_task_on_created_by;
DROP TABLE "user";