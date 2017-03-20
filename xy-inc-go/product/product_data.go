package product

import (
	"../utils"
	"log"
	"github.com/gocql/gocql"
)

type Product struct {
	Id          gocql.UUID
	Name        string
	Description string
	Price       float32
	Category    string
}

func FindAll() []Product {
	var product Product

	products := []Product{}
	session := utils.GetSession()

	defer session.Close()

	iter := session.Query(`SELECT id, name, description, price, category FROM product`).Iter()

	for iter.Scan(&product.Id, &product.Name, &product.Description, &product.Price, &product.Category) {
		products = append(products, product)
	}

	if err := iter.Close(); err != nil {
		log.Fatal(err)
	}

	return products
}

func FindById(id gocql.UUID) Product {
	var product Product

	session := utils.GetSession()

	defer session.Close()

	if err := session.Query(`SELECT id, name, description, price, category FROM product WHERE id = ? LIMIT 1`,
		id).Consistency(gocql.One).Scan(&product.Id, &product.Name, &product.Description, &product.Price, &product.Category); err != nil {
		log.Fatal(err)
	}

	return product
}

func Insert(product Product) Product {
	session := utils.GetSession()

	defer session.Close()

	id := gocql.TimeUUID()

	if err := session.Query(`INSERT INTO product (id, name, description, price, category) VALUES (?, ?, ?, ?, ?)`,
		id, product.Name, product.Description, product.Price, product.Category).Exec(); err != nil {
		log.Fatal(err)
	}

	product.Id = id

	return product
}

func Update(product Product) {
	session := utils.GetSession()

	defer session.Close()

	if err := session.Query(`UPDATE product SET Name = ?, Description = ?, Price = ?, Category = ? WHERE id = ? IF EXISTS`,
		product.Name, product.Description, product.Price, product.Category, product.Id).Exec(); err != nil {
		log.Fatal(err)
	}
}

func Delete(product Product) {
	session := utils.GetSession()

	defer session.Close()

	if err := session.Query(`DELETE FROM product WHERE id = ?`, product.Id).Exec(); err != nil {
		log.Fatal(err)
	}
}
