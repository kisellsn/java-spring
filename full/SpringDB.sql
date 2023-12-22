DROP DATABASE IF EXISTS SpringDB;

CREATE DATABASE SpringDB;

USE SpringDB;

CREATE TABLE [User] (
	userID INT PRIMARY KEY IDENTITY,
	login VARCHAR(255),
	password VARCHAR(30),
);

CREATE SEQUENCE Seq_Queue AS INT START WITH 1 INCREMENT BY 1;

CREATE TABLE Queue (
	queueID INT DEFAULT NEXT VALUE FOR Seq_Queue PRIMARY KEY,
	name VARCHAR(255),
	code VARCHAR(30),
	isLocked BIT,
	ownerID INT FOREIGN KEY REFERENCES [User](userID),
);

CREATE TABLE PlaceInQueue (
	recordID INT PRIMARY KEY IDENTITY,
	queueID INT FOREIGN KEY REFERENCES Queue(queueID),
	userID INT FOREIGN KEY REFERENCES [User](userID),
);




-- Дальше тесты--

SELECT * FROM [User]
SELECT * FROM Queue
INSERT INTO [User] (login, password) VALUES ('F','G')
DELETE FROM [User] WHERE userID=1

INSERT INTO Queue (isLocked, name, code, ownerID) VALUES ('false', 'QueueName', 'QueueCode', 1);
SELECT * FROM Queue

--Транзакция--
BEGIN TRANSACTION;
DECLARE @UserId INT;
SET @UserId = 4;
DELETE FROM PlaceInQueue WHERE userID = @UserId;
DELETE FROM PlaceInQueue WHERE queueID IN (SELECT queueID FROM Queue WHERE ownerID = @UserId);
DELETE FROM Queue WHERE ownerID = @UserId;
DELETE FROM [User] WHERE userID = @UserId;
IF @@ERROR = 0
    COMMIT TRANSACTION;
ELSE
    ROLLBACK TRANSACTION;

	
--DELETE FROM PlaceInQueue WHERE userID = @UserId;
--DELETE FROM Queue WHERE ownerID = @UserId;
--DELETE FROM [User] WHERE userID = @UserId;
--DELETE FROM PlaceInQueue WHERE queueID in (SELECT queueID from [User], Queue Where userID = @UserId);

INSERT INTO PlaceInQueue (queueID, userID) VALUES (14,7);
INSERT INTO PlaceInQueue (queueID, userID) VALUES (14,9);
INSERT INTO PlaceInQueue (queueID, userID) VALUES (14,7);
INSERT INTO PlaceInQueue (queueID, userID) VALUES (14,7);
INSERT INTO PlaceInQueue (queueID, userID) VALUES (14,7);
INSERT INTO PlaceInQueue (queueID, userID) VALUES (14,9);

SELECT * FROM [User], PlaceInQueue, Queue WHERE [User].userID=PlaceInQueue.userID AND PlaceInQueue.queueID=Queue.queueID AND Queue.queueID=14
SELECT DISTINCT [User].* FROM Queue, [User] WHERE userID=ownerID
SELECT COUNT(*) FROM PlaceInQueue WHERE queueID = 7 AND recordID <= (SELECT recordID FROM PlaceInQueue WHERE queueID = 7 AND userID = 8)


