package org.acme;

import org.bson.codecs.pojo.annotations.BsonId;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Book extends PanacheMongoEntity {
	public String title;
	public String author;
	public int stock;
}
