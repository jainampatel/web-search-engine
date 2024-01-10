package GUI;

import io.javalin.http.staticfiles.Location;
import io.javalin.Javalin;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;

import SearchEngine.NotStaticSearchEngine;
import SearchEngine.SearchEngine;
import console.Console;

public class index {
  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
      config.addStaticFiles("/", Location.CLASSPATH);
      Console.startSearchEngine();
    }).start(7003);

    final String RegularExprWord = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[�]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+";
    // Run the Javalin Instance on the port 7003
    app.get("/main", ctx -> {
      ctx.render("/GUI/index.html");
    });
    // Run the Javalin Instance to build an API for Search
    app.post("/search", ctx -> {
      // ctx.result(Main.search(ctx.formParam("Search_Query")));
      System.out.println(ctx.formParam("Search_Query"));
      LinkedHashMap<String, Integer> results = Console.search(ctx.formParam("Search_Query"));
      System.out.println(results);
      if (results != null) {

        ctx.json(results);
      } else
        ctx.json("{}");

    });

    app.post("/suggest", ctx -> {
      String suggestion = Console.searchEngineMain.suggestWords(ctx.formParam("Suggest"));

      ObjectMapper mapper = new ObjectMapper();
      ObjectNode json = mapper.createObjectNode();
      json.put("suggestion", suggestion);

      ctx.json(json);
    });
  }
}
