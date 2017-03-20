package main

import (
	"./product"
	"./utils"
	"log"
	"net/http"
	"github.com/gorilla/mux"
)

type Route struct {
	Name        string
	Method      string
	Pattern     string
	HandlerFunc http.HandlerFunc
}

func main() {
	router := mux.NewRouter().StrictSlash(true)

	loadRouters(router, product.ProductRouters())

	log.Fatal(http.ListenAndServe(":8080", router))
}

func loadRouters(router *mux.Router, mapRouters []utils.Route) {
	for _, route := range mapRouters {
		handler := route.HandlerFunc
		router.Methods(route.Method).Path(route.Pattern).Name(route.Name).Handler(handler)
	}
}
