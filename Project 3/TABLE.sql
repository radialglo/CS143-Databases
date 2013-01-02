
use cs143vky;

DROP TABLE IF EXISTS Conference, User, Author, Chair, Reviews, Paper, Status, Coauthors, Reviewer;

CREATE TABLE Conference(
	cid INT(5),  
	cname VARCHAR(60), 
	submission_start_time TIMESTAMP,
	submission_end_time TIMESTAMP,
	num_reviews_per_paper INT(5), 
	max_reviews_per_reviewer INT(5),
	PRIMARY KEY (cid));

CREATE TABLE User(
	uid INT(5), 
	email VARCHAR(60),
	last_name VARCHAR(60),
	middle_name VARCHAR(60),
	first_name VARCHAR(60),
	affiliation VARCHAR(60),
	role VARCHAR(20),
	PRIMARY KEY (uid));

CREATE TABLE Author(
	uid INT(5),
	paper_id INT(5),
	PRIMARY KEY (uid, paper_id));

CREATE TABLE Chair(
	uid INT(5), 
	cid INT(5),
	has_ended INT(1),
	PRIMARY KEY (uid, cid));
	
-- instantiate initial Chairman Joe Bruin
INSERT INTO User (uid,email,last_name, middle_name, first_name,affiliation,role) VALUES (95030,"joe@ucla.edu","bruin","smo", "joe","UCLA","CHAIR");
INSERT INTO User (uid,email,last_name,first_name,affiliation,role) VALUES (95031,"bob@ucla.edu","bruin","bob","UCLA","CHAIR");


CREATE TABLE Reviews(
	uid INT(5), 
	paper_id INT(5),
	comments text,
	rating INT(5) DEFAULT NULL,
	PRIMARY KEY (uid, paper_id));

CREATE TABLE Paper(
	paper_id INT(5),
	title VARCHAR(200), 
	abstract text,
	content text,
	num_reviews INT(5),
	PRIMARY KEY(paper_id));

CREATE TABLE Status(
	cid INT(5), 
	paper_id INT(5),
	decision INT(5) DEFAULT 0,
	PRIMARY KEY (paper_id));

CREATE TABLE Reviewer(
	uid INT(5), 
	cid INT(5),
	email VARCHAR(60),
	num_of_reviews INT(5),
	PRIMARY KEY (uid, cid));


