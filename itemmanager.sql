-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 18, 2024 at 04:13 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `itemmanager`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(4) UNSIGNED NOT NULL,
  `username` varchar(20) NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `password` varchar(64) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `username`, `nickname`, `password`, `role`) VALUES
(1, 'admin', 'agus', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'main');

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `id` int(4) UNSIGNED NOT NULL,
  `kode` varchar(5) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `stok` int(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`id`, `kode`, `nama`, `stok`) VALUES
(1, '000A1', '10kg Beras cap Buah Lai', 251),
(2, '000A2', '20kg Beras Cap Buah Lay', 457),
(3, '000A3', '5kg Beras Cap Buah Lai', 141),
(11, '000B1', 'pecel', 56);

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id` int(4) UNSIGNED NOT NULL,
  `nama` varchar(50) NOT NULL,
  `namaToko` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`id`, `nama`, `namaToko`, `alamat`) VALUES
(1, 'Soekarno', 'PerjuanganRakyat', 'JL. Proklamasi'),
(2, 'Hatta', 'SoembakoMoerah', 'JL Rengasdengklok'),
(4, 'santoso', 'tembakau lima', 'jl ayani 2'),
(5, 'dustin', 'sun atk', 'siaga');

-- --------------------------------------------------------

--
-- Table structure for table `pemasok`
--

CREATE TABLE `pemasok` (
  `id` int(10) UNSIGNED NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pemasok`
--

INSERT INTO `pemasok` (`id`, `nama`, `alamat`) VALUES
(1, 'PT besari', 'JL Purnama 2'),
(2, 'PT Rex', 'Sungai Raya'),
(4, 'PT wr', 'purnama 2');

-- --------------------------------------------------------

--
-- Table structure for table `pesanan`
--

CREATE TABLE `pesanan` (
  `id` int(4) UNSIGNED NOT NULL,
  `idAdmin` int(4) UNSIGNED NOT NULL,
  `idPemasok` int(4) UNSIGNED NOT NULL,
  `tanggal` date NOT NULL,
  `metodePembayaran` enum('cash','transfer') NOT NULL,
  `tipe` enum('disetujui','ditolak') NOT NULL,
  `status` enum('berhasil','sedang proses','menunggu') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pesanan`
--

INSERT INTO `pesanan` (`id`, `idAdmin`, `idPemasok`, `tanggal`, `metodePembayaran`, `tipe`, `status`) VALUES
(1, 1, 1, '2024-06-10', 'cash', 'disetujui', 'berhasil'),
(7, 1, 2, '2024-06-12', 'transfer', 'disetujui', 'berhasil'),
(8, 1, 2, '2024-06-28', 'transfer', 'disetujui', 'berhasil'),
(9, 1, 1, '2024-06-06', 'cash', 'disetujui', 'berhasil'),
(12, 1, 4, '2024-06-18', 'transfer', 'disetujui', 'berhasil'),
(13, 1, 4, '2024-06-18', 'cash', 'disetujui', 'berhasil');

-- --------------------------------------------------------

--
-- Table structure for table `pesanandetail`
--

CREATE TABLE `pesanandetail` (
  `id` int(4) UNSIGNED NOT NULL,
  `idPesanan` int(4) UNSIGNED NOT NULL,
  `idBarang` int(4) UNSIGNED NOT NULL,
  `jumlah` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pesanandetail`
--

INSERT INTO `pesanandetail` (`id`, `idPesanan`, `idBarang`, `jumlah`) VALUES
(1, 1, 1, 5),
(2, 1, 2, 3),
(3, 7, 1, 50),
(4, 7, 2, 2),
(5, 8, 3, 50),
(6, 8, 2, 22),
(7, 8, 1, 1),
(8, 9, 1, 5),
(11, 12, 11, 6),
(12, 13, 11, 55);

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id` int(4) UNSIGNED NOT NULL,
  `idAdmin` int(4) UNSIGNED NOT NULL,
  `idPelanggan` int(4) UNSIGNED DEFAULT NULL,
  `tanggal` date NOT NULL,
  `metodePembayaran` enum('cash','transfer') NOT NULL,
  `status` enum('berhasil','menunggu','gagal') NOT NULL,
  `tipe` enum('masuk','keluar') NOT NULL,
  `nomorReferensi` varchar(10) NOT NULL,
  `idPemasok` int(4) UNSIGNED DEFAULT NULL,
  `idPesanan` int(4) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id`, `idAdmin`, `idPelanggan`, `tanggal`, `metodePembayaran`, `status`, `tipe`, `nomorReferensi`, `idPemasok`, `idPesanan`) VALUES
(1, 1, NULL, '2024-06-10', 'cash', 'berhasil', 'masuk', 'asdqerr', 1, 1),
(2, 1, 2, '2024-06-11', 'cash', 'berhasil', 'keluar', 'tty7h', NULL, NULL),
(3, 1, 4, '2024-06-13', 'cash', 'berhasil', 'keluar', 'www12', NULL, NULL),
(4, 1, 2, '2024-06-19', 'cash', 'berhasil', 'keluar', 'www23', NULL, NULL),
(6, 1, NULL, '2024-06-12', 'transfer', 'berhasil', 'masuk', 'ssw21', 2, 7),
(7, 1, NULL, '2024-06-28', 'transfer', 'berhasil', 'masuk', 'dd4w2', 2, 8),
(8, 1, NULL, '2024-06-06', 'cash', 'berhasil', 'masuk', '55w21', 1, 9),
(11, 1, NULL, '2024-06-18', 'transfer', 'berhasil', 'masuk', 'qwerty', 4, 12),
(12, 1, 5, '2024-06-18', 'cash', 'berhasil', 'keluar', 'sssd', NULL, NULL),
(13, 1, 5, '2024-06-18', 'cash', 'berhasil', 'keluar', 'wwww', NULL, NULL),
(14, 1, NULL, '2024-06-18', 'cash', 'berhasil', 'masuk', 'qqqq', 4, 13);

-- --------------------------------------------------------

--
-- Table structure for table `transaksidetail`
--

CREATE TABLE `transaksidetail` (
  `id` int(4) UNSIGNED NOT NULL,
  `idTransaksi` int(4) UNSIGNED NOT NULL,
  `idBarang` int(4) UNSIGNED NOT NULL,
  `jumlah` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksidetail`
--

INSERT INTO `transaksidetail` (`id`, `idTransaksi`, `idBarang`, `jumlah`) VALUES
(1, 1, 2, 5),
(2, 2, 3, 3),
(3, 3, 2, 22),
(4, 3, 3, 3),
(5, 4, 2, 55),
(6, 4, 3, 3),
(7, 6, 1, 50),
(8, 6, 2, 2),
(9, 7, 3, 50),
(10, 7, 2, 22),
(11, 7, 1, 1),
(12, 8, 1, 5),
(14, 11, 11, 6),
(15, 12, 11, 10),
(16, 12, 1, 10),
(17, 13, 11, 50),
(18, 14, 11, 55);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pemasok`
--
ALTER TABLE `pemasok`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pesanan`
--
ALTER TABLE `pesanan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pesanan_ibfk_1` (`idAdmin`),
  ADD KEY `pesanan_ibfk_2` (`idPemasok`);

--
-- Indexes for table `pesanandetail`
--
ALTER TABLE `pesanandetail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pesanandetail_ibfk_2` (`idBarang`),
  ADD KEY `pesanandetail_ibfk_1` (`idPesanan`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `transaksi_ibfk_1` (`idAdmin`),
  ADD KEY `transaksi_ibfk_2` (`idPelanggan`),
  ADD KEY `transaksi_ibfk_3` (`idPemasok`),
  ADD KEY `transaksi_ibfk_4` (`idPesanan`);

--
-- Indexes for table `transaksidetail`
--
ALTER TABLE `transaksidetail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `transaksidetail_ibfk_1` (`idTransaksi`),
  ADD KEY `transaksidetail_ibfk_2` (`idBarang`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `barang`
--
ALTER TABLE `barang`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `pelanggan`
--
ALTER TABLE `pelanggan`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `pemasok`
--
ALTER TABLE `pemasok`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pesanan`
--
ALTER TABLE `pesanan`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `pesanandetail`
--
ALTER TABLE `pesanandetail`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `transaksidetail`
--
ALTER TABLE `transaksidetail`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pesanan`
--
ALTER TABLE `pesanan`
  ADD CONSTRAINT `pesanan_ibfk_1` FOREIGN KEY (`idAdmin`) REFERENCES `admin` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `pesanan_ibfk_2` FOREIGN KEY (`idPemasok`) REFERENCES `pemasok` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `pesanandetail`
--
ALTER TABLE `pesanandetail`
  ADD CONSTRAINT `pesanandetail_ibfk_1` FOREIGN KEY (`idPesanan`) REFERENCES `pesanan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `pesanandetail_ibfk_2` FOREIGN KEY (`idBarang`) REFERENCES `barang` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`idAdmin`) REFERENCES `admin` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`idPelanggan`) REFERENCES `pelanggan` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `transaksi_ibfk_3` FOREIGN KEY (`idPemasok`) REFERENCES `pemasok` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `transaksi_ibfk_4` FOREIGN KEY (`idPesanan`) REFERENCES `pesanan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transaksidetail`
--
ALTER TABLE `transaksidetail`
  ADD CONSTRAINT `transaksidetail_ibfk_1` FOREIGN KEY (`idTransaksi`) REFERENCES `transaksi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `transaksidetail_ibfk_2` FOREIGN KEY (`idBarang`) REFERENCES `barang` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
