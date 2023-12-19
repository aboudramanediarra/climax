
CREATE DATABASE testdb;
USE testdb;

CREATE TABLE client (id INT NOT NULL AUTO_INCREMENT ,nom VARCHAR(100) NOT NULL ,prenom VARCHAR(100) NOT NULL,
                     age INT NOT NULL ,profession VARCHAR(100) NOT NULL ,salaire INT NOT NULL ,PRIMARY KEY (id));


CREATE USER 'sa'@'localhost' IDENTIFIED BY '7229';

GRANT ALL PRIVILEGES ON testdb.* TO 'sa'@'localhost';

INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (1, 'Sanou','Jean',25, 'chauffeur', 79);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (2, 'Traoré', 'Issouf',28, 'comptable', 112);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (3, 'Kabré', 'Jeanne',35, 'informaticienne', 75);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (4, 'San', 'Aboubacar',40, 'commenrcante', 10);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (5, 'Bazié', 'Mark',27, 'ménuisier', 10);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (6, 'Derra', 'Alioune',70, 'commenrcante', 80);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (7, 'Traoré', 'Jennifer',28, 'comptable', 185);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (8, 'DIARRA', 'Ahmed',55, 'comptable', 91);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (9, 'SAN', 'Omah',32, 'comptable', 150);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (10, 'Ky', 'Aziz',42, 'chauffeur', 50);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (11, 'Sanou', 'Wilfrid',25, 'chauffeur', 90);
INSERT INTO client (id, nom,prenom, age, profession, salaire) VALUES (12, 'Traoré', 'Bertrand',28, 'informaticienne', 99);