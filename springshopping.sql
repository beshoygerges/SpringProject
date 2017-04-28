-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema springshopping
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema springshopping
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `springshopping` DEFAULT CHARACTER SET utf8 ;
USE `springshopping` ;

-- -----------------------------------------------------
-- Table `springshopping`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `springshopping`.`user` (
  `email` VARCHAR(45) NOT NULL,
  `gender` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `birthDate` DATE NULL DEFAULT NULL,
  `password` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NULL DEFAULT NULL,
  `imageurl` VARCHAR(1024) NULL DEFAULT NULL,
  `type` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  `creditCard_number` BIGINT(20) NULL DEFAULT NULL,
  `balance` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `springshopping`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `springshopping`.`orders` (
  `date` DATE NULL DEFAULT NULL,
  `status` INT(11) NULL DEFAULT '0',
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `User_email` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id`, `User_email`),
  INDEX `fk_order_User1_idx` (`User_email` ASC),
  CONSTRAINT `fk_order_User1`
    FOREIGN KEY (`User_email`)
    REFERENCES `springshopping`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `springshopping`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `springshopping`.`products` (
  `product_id` INT(11) NOT NULL AUTO_INCREMENT,
  `productName` VARCHAR(45) NOT NULL,
  `price` DOUBLE NOT NULL,
  `quantity` INT(11) NOT NULL,
  `imageUrl` VARCHAR(1024) NOT NULL,
  `description` VARCHAR(500) NOT NULL,
  `discount` DOUBLE NULL DEFAULT NULL,
  `categoryName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE INDEX `productName_UNIQUE` (`productName` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `springshopping`.`orderdetails`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `springshopping`.`orderdetails` (
  `products_product_id` INT(11) NOT NULL,
  `order_id` INT(11) NOT NULL,
  `price` DOUBLE NOT NULL,
  `quantity` INT(11) NOT NULL,
  PRIMARY KEY (`products_product_id`, `order_id`),
  INDEX `fk_products_has_order_order1_idx` (`order_id` ASC),
  INDEX `fk_products_has_order_products1_idx` (`products_product_id` ASC),
  CONSTRAINT `fk_products_has_order_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `springshopping`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_has_order_products1`
    FOREIGN KEY (`products_product_id`)
    REFERENCES `springshopping`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `springshopping`.`rechargecards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `springshopping`.`rechargecards` (
  `number` INT(11) NOT NULL,
  `value` INT(11) NULL DEFAULT NULL,
  `status` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`number`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
