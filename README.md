[![Build Status](https://travis-ci.org/jooby-project/apitool-starter.svg?branch=master)](https://travis-ci.org/jooby-project/apitool-starter)
# apitool

Starter project for [apitool](http://jooby.org/doc/apitool/) module.

## quick preview

This project contains a simple application that:

- Defines a Pet API using script routes
- Export API to [Swagger](https://swagger.io) and [RAML](https://raml.org)

```java
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
```

## run

    mvn jooby:run

Try:

- http://localhost:8080/swagger
- http://localhost:8080/raml

## build

    mvn clean package

The `apitool` plugin generates an `App.json` file to keep documentation available at deploy time.

```
[INFO] --- jooby-maven-plugin:apitool (default) @ apitool-starter ---
[INFO] API file: apitool-starter/target/classes/starter/apitool/App.json
``` 

## help

* Read the [module documentation](http://jooby.org/doc/apitool)
* Join the [channel](https://gitter.im/jooby-project/jooby)
* Join the [group](https://groups.google.com/forum/#!forum/jooby-project)
