package starter.apitool;

import java.util.List;

import org.jooby.Err;
import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.apitool.ApiTool;
import org.jooby.json.Jackson;

/**
 * Apitool starter project:
 */
public class App extends Jooby {

  {
    use(new Jackson());

    /**
     *
     * Everything about your Pets.
     */
    path("/api/pets", () -> {
      /**
       *
       * List pets ordered by id.
       *
       * @param start Start offset, useful for paging. Default is <code>0</code>.
       * @param max Max page size, useful for paging. Default is <code>50</code>.
       * @return Pets ordered by name.
       */
      get(req -> {
        int start = req.param("start").intValue(0);
        int max = req.param("max").intValue(50);
        DB db = req.require(DB.class);
        List<Pet> pets = db.findAll(start, max);
        return pets;
      });

      /**
       *
       * Find pet by ID
       *
       * @param id Pet ID.
       * @return Returns <code>200</code> with a single pet or <code>404</code>
       */
      get("/:id", req -> {
        int id = req.param("id").intValue();
        DB db = req.require(DB.class);
        Pet pet = db.find(id);
        if (pet == null) {
          throw new Err(Status.NOT_FOUND);
        }
        return pet;
      });

      /**
       *
       * Add a new pet to the store.
       *
       * @param body Pet object that needs to be added to the store.
       * @return Returns a saved pet.
       */
      post(req -> {
        Pet pet = req.body().to(Pet.class);
        DB db = req.require(DB.class);
        db.save(pet);
        return pet;
      });

      /**
       *
       * Update an existing pet.
       *
       * @param body Pet object that needs to be updated.
       * @return Returns a saved pet.
       */
      put(req -> {
        Pet pet = req.body().to(Pet.class);
        DB db = req.require(DB.class);
        db.save(pet);
        return pet;
      });

      /**
       *
       * Deletes a pet by ID.
       *
       * @param id Pet ID.
       * @return A <code>204</code>
       */
      delete("/:id", req -> {
        int id = req.param("id").intValue();
        DB db = req.require(DB.class);
        db.delete(id);
        return Results.noContent();
      });
    });

    /** Export API to Swagger and RAML: */
    use(new ApiTool()
        .swagger()
        .raml());
  }

  public static void main(final String[] args) {
    run(App::new, args);
  }

}
