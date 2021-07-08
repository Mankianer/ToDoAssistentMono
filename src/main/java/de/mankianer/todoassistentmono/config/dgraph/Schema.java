package de.mankianer.todoassistentmono.config.dgraph;

public class Schema {

  public static String PREDICATES = """
      name: String @index(exact, fulltext) .
      description: String .
      password: Password .
      category: uid .
      status: uid .
      author: uid .
      assign: uid .
      parent: uid .
      next: [uid] .
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
