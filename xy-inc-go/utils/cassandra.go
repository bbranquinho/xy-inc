package utils

import "github.com/gocql/gocql"

var cluster = GetCluster()

func GetCluster() *gocql.ClusterConfig {
	cluster := gocql.NewCluster("127.0.0.1")
	cluster.Keyspace = "xy_inc"
	cluster.Consistency = gocql.Quorum

	return cluster
}

func GetSession() *gocql.Session {
	session, _ := cluster.CreateSession()

	return session
}

