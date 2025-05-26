-- Sample data for Task table
CREATE TABLE IF NOT EXISTS TASK (
  ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  TITLE VARCHAR(255),
  DESCRIPTION VARCHAR(255),
  COMPLETED BOOLEAN
);

INSERT INTO TASK (TITLE, DESCRIPTION, COMPLETED) VALUES
  ('Buy groceries', 'Milk, Bread, Eggs, and Fruits', 0),
  ('Finish project', 'Complete the Spring Boot to-do app', 0),
  ('Read a book', 'Read at least 30 pages of a novel', 1),
  ('Go for a run', 'Jog for 30 minutes in the park', 0),
  ('Call mom', 'Weekly check-in call', 1),
  ('Plan vacation', 'Research destinations and book flights', 0),
  ('Pay bills', 'Electricity, Internet, and Water bills', 1),
  ('Clean the house', 'Vacuum and dust all rooms', 0),
  ('Write blog post', 'Draft a new article for the tech blog', 0),
  ('Team meeting', 'Discuss project updates with the team', 1),
  ('Doctor appointment', 'Annual health checkup at 10 AM', 0),
  ('Update resume', 'Add recent projects and skills', 0),
  ('Backup files', 'Backup important documents to cloud storage', 1);
