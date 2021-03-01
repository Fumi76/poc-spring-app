package poc.spring.app;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable( tableName = "scorekeep-user" )
public class User {

  private String id;
  private String name;
  
  public User() {
  }

  public User(String id) {
    this.id = id;
  }

  @DynamoDBHashKey(attributeName="id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  @DynamoDBAttribute(attributeName="name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

}