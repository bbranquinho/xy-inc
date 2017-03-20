package product

import (
	"net/http"
	"encoding/json"
	"io/ioutil"
	"io"
	"github.com/gorilla/mux"
	"github.com/gocql/gocql"
)

func FindAllResource(w http.ResponseWriter, r *http.Request) {
	products := FindAll()

	json.NewEncoder(w).Encode(products)
}

func FindByIdResource(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	id, _ := gocql.ParseUUID(vars["id"])

	product := FindById(id)

	json.NewEncoder(w).Encode(product)
}

func InsertResource(w http.ResponseWriter, r *http.Request) {
	product := extractProduct(w, r)

	product = Insert(product)

	json.NewEncoder(w).Encode(product)
}

func UpdateResource(w http.ResponseWriter, r *http.Request) {
	product := extractProduct(w, r)

	Update(product)
}

func DeleteResource(w http.ResponseWriter, r *http.Request) {
	product := extractProduct(w, r)

	Delete(product)
}

func extractProduct(w http.ResponseWriter, r *http.Request) Product {
	var product Product

	body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))

	if err != nil {
		panic(err)
	}

	if err := r.Body.Close(); err != nil {
		panic(err)
	}

	if err := json.Unmarshal(body, &product); err != nil {
		w.Header().Set("Content-Type", "application/json; charset=UTF-8")
		w.WriteHeader(422)

		if err := json.NewEncoder(w).Encode(err); err != nil {
			panic(err)
		}
	}

	return product
}