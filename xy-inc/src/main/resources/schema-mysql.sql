CREATE TABLE IF NOT EXISTS `xy_inc`.`product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  `price` DECIMAL(10,3) NULL,
  `category` VARCHAR(45) NULL,
  PRIMARY KEY (`id`)
);
