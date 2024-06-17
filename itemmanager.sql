-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 17, 2024 at 12:22 PM
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
(1, '000A1', '10kg Beras cap Buah Lai', 206),
(2, '000A2', '20kg Beras Cap Buah Lai', 455),
(3, '000A3', '5kg Beras Cap Buah Lai', 141);

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
(4, 'santoso', 'tembakau lima', 'jl ayani 2');

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
(2, 'PT Rex', 'Sungai Raya');

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
(7, 1, 2, '2024-06-12', 'transfer', 'disetujui', 'sedang proses'),
(8, 1, 2, '2024-06-28', 'transfer', 'disetujui', 'berhasil'),
(9, 1, 1, '2024-06-06', 'cash', 'disetujui', 'berhasil');

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
(8, 9, 1, 5);

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
(6, 1, NULL, '2024-06-12', 'transfer', 'menunggu', 'masuk', 'ssw21', 2, 7),
(7, 1, NULL, '2024-06-28', 'transfer', 'berhasil', 'masuk', 'dd4w2', 2, 8),
(8, 1, NULL, '2024-06-06', 'cash', 'berhasil', 'masuk', '55w21', 1, 9);

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
(12, 8, 1, 5);

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
  ADD KEY `idAdmin` (`idAdmin`),
  ADD KEY `idPemasok` (`idPemasok`);

--
-- Indexes for table `pesanandetail`
--
ALTER TABLE `pesanandetail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idPesanan` (`idPesanan`),
  ADD KEY `idBarang` (`idBarang`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idAdmin` (`idAdmin`),
  ADD KEY `idPelanggan` (`idPelanggan`),
  ADD KEY `idPemasok` (`idPemasok`),
  ADD KEY `idPesanan` (`idPesanan`);

--
-- Indexes for table `transaksidetail`
--
ALTER TABLE `transaksidetail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idTransaksi` (`idTransaksi`),
  ADD KEY `idBarang` (`idBarang`);

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
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `pelanggan`
--
ALTER TABLE `pelanggan`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pemasok`
--
ALTER TABLE `pemasok`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `pesanan`
--
ALTER TABLE `pesanan`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `pesanandetail`
--
ALTER TABLE `pesanandetail`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `transaksidetail`
--
ALTER TABLE `transaksidetail`
  MODIFY `id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pesanan`
--
ALTER TABLE `pesanan`
  ADD CONSTRAINT `pesanan_ibfk_1` FOREIGN KEY (`idAdmin`) REFERENCES `admin` (`id`),
  ADD CONSTRAINT `pesanan_ibfk_2` FOREIGN KEY (`idPemasok`) REFERENCES `pemasok` (`id`);

--
-- Constraints for table `pesanandetail`
--
ALTER TABLE `pesanandetail`
  ADD CONSTRAINT `pesanandetail_ibfk_1` FOREIGN KEY (`idPesanan`) REFERENCES `pesanan` (`id`),
  ADD CONSTRAINT `pesanandetail_ibfk_2` FOREIGN KEY (`idBarang`) REFERENCES `barang` (`id`);

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`idAdmin`) REFERENCES `admin` (`id`),
  ADD CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`idPelanggan`) REFERENCES `pelanggan` (`id`),
  ADD CONSTRAINT `transaksi_ibfk_3` FOREIGN KEY (`idPemasok`) REFERENCES `pemasok` (`id`),
  ADD CONSTRAINT `transaksi_ibfk_4` FOREIGN KEY (`idPesanan`) REFERENCES `pesanan` (`id`);

--
-- Constraints for table `transaksidetail`
--
ALTER TABLE `transaksidetail`
  ADD CONSTRAINT `transaksidetail_ibfk_1` FOREIGN KEY (`idTransaksi`) REFERENCES `transaksi` (`id`),
  ADD CONSTRAINT `transaksidetail_ibfk_2` FOREIGN KEY (`idBarang`) REFERENCES `barang` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
