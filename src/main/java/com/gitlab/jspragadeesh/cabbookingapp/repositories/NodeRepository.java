package com.gitlab.jspragadeesh.cabbookingapp.repositories;

import com.gitlab.jspragadeesh.cabbookingapp.models.Node;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NodeRepository extends MongoRepository<Node, String> {
}
