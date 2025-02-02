package tw.brad.stest5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.brad.stest5.model.T1;

/*
 * CREATE TABLE `t1` (
  `id` int(10) UNSIGNED NOT NULL,
  `f1` varchar(100) DEFAULT NULL,
  `f2` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `t2`
--

CREATE TABLE `t2` (
  `id` int(10) UNSIGNED NOT NULL,
  `t1_id` int(10) UNSIGNED NOT NULL,
  `f3` varchar(100) NOT NULL,
  `f4` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `t1`
--
ALTER TABLE `t1`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `t2`
--
ALTER TABLE `t2`
  ADD PRIMARY KEY (`id`),
  ADD KEY `t1t2` (`t1_id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `t1`
--
ALTER TABLE `t1`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `t2`
--
ALTER TABLE `t2`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `t2`
--
ALTER TABLE `t2`
  ADD CONSTRAINT `t1t2` FOREIGN KEY (`t1_id`) REFERENCES `t1` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

 */

@Repository
public interface T1Repository extends JpaRepository<T1, Long>{
	
}