CREATE DATABASE  IF NOT EXISTS `farmacia` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `farmacia`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: farmacia
-- ------------------------------------------------------
-- Server version	8.0.38

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `farmaci`
--

DROP TABLE IF EXISTS `farmaci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaci` (
  `id` int NOT NULL AUTO_INCREMENT,
  `prezzo` float NOT NULL,
  `prescrizione` tinyint NOT NULL,
  `nome` varchar(45) NOT NULL,
  `scorte` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome_UNIQUE` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmaci`
--

LOCK TABLES `farmaci` WRITE;
/*!40000 ALTER TABLE `farmaci` DISABLE KEYS */;
INSERT INTO `farmaci` VALUES (110,20,1,'Vicodin',7),(111,5,0,'Tachipirina',50),(113,20,1,'Xanax',10),(114,20,1,'Voltaren',10),(115,20,1,'Clavulin',10),(116,20,1,'Augmentin',10),(117,20,1,'Cortisone',10),(118,20,1,'Bentelan',10),(119,20,1,'PizzaMargherita',10),(120,20,1,'Rinazina',10),(121,20,1,'Viks',10),(122,20,1,'Neoduplamox',10),(123,20,1,'Ayrinal',10),(124,20,1,'Fluifort',10),(125,20,1,'Levotuss',10),(126,20,1,'MedicinaMagica',10),(127,20,1,'PizzaFritta',10),(128,20,1,'Unguento',10),(129,20,1,'FagioliBalzar',10),(130,20,1,'PastaSugo',10);
/*!40000 ALTER TABLE `farmaci` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordini`
--

DROP TABLE IF EXISTS `ordini`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordini` (
  `id` varchar(45) NOT NULL,
  `dataCreazione` date NOT NULL,
  `ritirato` tinyint NOT NULL,
  `cliente` int NOT NULL,
  `totale` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cliente_idx` (`cliente`),
  CONSTRAINT `cliente` FOREIGN KEY (`cliente`) REFERENCES `utenti` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini`
--

LOCK TABLES `ordini` WRITE;
/*!40000 ALTER TABLE `ordini` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordini` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordini_acquisto`
--

DROP TABLE IF EXISTS `ordini_acquisto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordini_acquisto` (
  `id` varchar(45) NOT NULL,
  `dataCreazione` date NOT NULL,
  `ricevuto` tinyint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini_acquisto`
--

LOCK TABLES `ordini_acquisto` WRITE;
/*!40000 ALTER TABLE `ordini_acquisto` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordini_acquisto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordini_acquisto_farmaci`
--

DROP TABLE IF EXISTS `ordini_acquisto_farmaci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordini_acquisto_farmaci` (
  `ordineAcquisto` varchar(45) NOT NULL,
  `farmacoAcquisto` int NOT NULL,
  `quantita` int NOT NULL,
  PRIMARY KEY (`ordineAcquisto`,`farmacoAcquisto`),
  KEY `farmaco_idx` (`farmacoAcquisto`),
  CONSTRAINT `farmacoAcquisto` FOREIGN KEY (`farmacoAcquisto`) REFERENCES `farmaci` (`id`),
  CONSTRAINT `ordineAcquisto` FOREIGN KEY (`ordineAcquisto`) REFERENCES `ordini_acquisto` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini_acquisto_farmaci`
--

LOCK TABLES `ordini_acquisto_farmaci` WRITE;
/*!40000 ALTER TABLE `ordini_acquisto_farmaci` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordini_acquisto_farmaci` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordini_farmaci`
--

DROP TABLE IF EXISTS `ordini_farmaci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordini_farmaci` (
  `ordine` varchar(45) NOT NULL,
  `farmaco` int NOT NULL,
  `quantita` int NOT NULL,
  PRIMARY KEY (`ordine`,`farmaco`),
  KEY `farmaco_idx` (`farmaco`),
  CONSTRAINT `farmaco` FOREIGN KEY (`farmaco`) REFERENCES `farmaci` (`id`),
  CONSTRAINT `ordine` FOREIGN KEY (`ordine`) REFERENCES `ordini` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini_farmaci`
--

LOCK TABLES `ordini_farmaci` WRITE;
/*!40000 ALTER TABLE `ordini_farmaci` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordini_farmaci` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `dataNascita` date NOT NULL,
  `tipo` int NOT NULL,
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (17,'carmine','pass','carmine','colucci','2003-03-22',0,'miamail@gmail.com');
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-08 11:35:54
