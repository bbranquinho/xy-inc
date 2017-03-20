package product

import "../utils"

const rootPath = "/api/product"

var routes = []utils.Route {
	utils.Route { "FindAll", "GET", rootPath, FindAllResource, },
	utils.Route { "FindById", "GET", rootPath + "/{id}", FindByIdResource, },
	utils.Route { "Insert", "POST", rootPath, InsertResource, },
	utils.Route { "Update", "PUT", rootPath, UpdateResource, },
	utils.Route { "Delete", "DELETE", rootPath, DeleteResource, },
}

func ProductRouters() ([]utils.Route) {
	return routes
}
