package de.mankianer.todoassistentmono.config.dgraph;

public class Schema {

  public static String PREDICATES = """
      <year>: int @index(int) .
      """;

  public static String TYPES = """
      type Todo {
        name
        description
        category
        status
        author
        assign
      }
      
      type Category {
        name
        parent
      }
      
      type Status {
        name
        next
      }
      
      type User {
        name
        password
      }
      """;

}
