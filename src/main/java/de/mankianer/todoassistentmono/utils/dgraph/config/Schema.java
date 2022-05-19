package de.mankianer.todoassistentmono.utils.dgraph.config;

/**
 * Schema for DGraph: Types and Predicates
 *
 * should be loaded after connecting to DGraph
 */
public class Schema {

  public static String PREDICATES = """
      <year>: int @index(int) .
      <_ConfigName>: string @index(hash) .
      <propertyName>: string @index(hash) .
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
