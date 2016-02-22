# route-spec-demo

A [live demo](https://jooby-spec.herokuapp.com) for [jooby spec module](http://jooby.org/doc/spec):

## quick preview

```java
{
  use(new Jackson());

  get("/", () -> HOME);

  /**
   *
   * Everything about your Pets.
   */
  use("/api/pets")
      /**
       *
       * List pets ordered by id.
       *
       * @param start Start offset, useful for paging. Default is <code>0</code>.
       * @param max Max page size, useful for paging. Default is <code>50</code>.
       * @return Pets ordered by name.
       */
      .get(req -> {
        int start = req.param("start").intValue(0);
        int max = req.param("max").intValue(50);
        DB db = req.require(DB.class);
        List<Pet> pets = db.findAll(start, max);
        return pets;
      })
      /**
       *
       * Find pet by ID
       *
       * @param id Pet ID.
       * @return Returns <code>200</code> with a single pet or <code>404</code>
       */
      .get("/:id", req -> {
        int id = req.param("id").intValue();
        DB db = req.require(DB.class);
        Pet pet = db.find(id);
        if (pet == null) {
          throw new Err(Status.NOT_FOUND);
        }
        return pet;
      })
      /**
       *
       * Add a new pet to the store.
       *
       * @param body Pet object that needs to be added to the store.
       * @return Returns a saved pet.
       */
      .post(req -> {
        Pet pet = req.body().to(Pet.class);
        DB db = req.require(DB.class);
        db.save(pet);
        return pet;
      })
      /**
       *
       * Update an existing pet.
       *
       * @param body Pet object that needs to be updated.
       * @return Returns a saved pet.
       */
      .put(req -> {
        Pet pet = req.body().to(Pet.class);
        DB db = req.require(DB.class);
        db.save(pet);
        return pet;
      })
      /**
       *
       * Deletes a pet by ID.
       *
       * @param id Pet ID.
       * @return A <code>204</code>
       */
      .delete("/:id", req -> {
        int id = req.param("id").intValue();
        DB db = req.require(DB.class);
        db.delete(id);
        return Results.noContent();
      })
      .produces("json")
      .consumes("json");

  new Raml()
      .install(this);

  new SwaggerUI()
      .install(this);
}

```

Complete source code is available here [App.java](https://github.com/jooby-guides/route-spec/blob/master/src/main/java/org/jooby/spec/App.java).

The demo exports [Jooby Application](http://jooby.org) to:

* [RAML](https://jooby-spec.herokuapp.com/raml)
* [Swagger](https://jooby-spec.herokuapp.com/swagger)
