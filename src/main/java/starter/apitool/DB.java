package starter.apitool;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * An in-memory database.
 *
 * @author edgar
 */
@Singleton
public class DB {

  private Cache<Integer, Pet> db = CacheBuilder.newBuilder()
      .maximumSize(100)
      .build();

  public List<Pet> findAll(final int start, final int max) {
    return db.asMap().values().stream()
        .sorted((p1, p2) -> p1.getId() - p2.getId())
        .collect(Collectors.toList());
  }

  public Pet find(final int id) {
    return db.getIfPresent(id);
  }

  public void save(final Pet pet) {
    db.put(pet.getId(), pet);
  }

  public void delete(final int id) {
    db.invalidate(id);
  }

}
