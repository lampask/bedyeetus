-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 22, 2019 at 11:16 PM
-- Server version: 10.4.8-MariaDB
-- PHP Version: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bedyeetus`
--

-- --------------------------------------------------------

--
-- Table structure for table `games`
--

CREATE TABLE `games` (
  `id` int(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `world` varchar(50) NOT NULL,
  `x` decimal(10,0) NOT NULL,
  `y` decimal(10,0) NOT NULL,
  `z` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `spawners`
--

CREATE TABLE `spawners` (
  `id` int(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `game_id` int(50) NOT NULL,
  `x` int(10) NOT NULL,
  `y` int(10) NOT NULL,
  `z` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `teams`
--

CREATE TABLE `teams` (
  `id` int(50) NOT NULL,
  `name` int(50) NOT NULL,
  `game_id` int(50) NOT NULL,
  `spawn_x` decimal(10,0) NOT NULL,
  `spawn_y` decimal(10,0) NOT NULL,
  `spawn_z` decimal(10,0) NOT NULL,
  `nexus_x` decimal(10,0) NOT NULL,
  `nexus_y` decimal(10,0) NOT NULL,
  `nexus_z` decimal(10,0) NOT NULL,
  `d_shop_x` decimal(10,0) NOT NULL,
  `d_shop_y` decimal(10,0) NOT NULL,
  `d_shop_z` decimal(10,0) NOT NULL,
  `e_shop_x` decimal(10,0) NOT NULL,
  `e_shop_y` decimal(10,0) NOT NULL,
  `e_shop_z` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `spawners`
--
ALTER TABLE `spawners`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `games`
--
ALTER TABLE `games`
  MODIFY `id` int(50) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `teams`
--
ALTER TABLE `teams`
  MODIFY `id` int(50) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
