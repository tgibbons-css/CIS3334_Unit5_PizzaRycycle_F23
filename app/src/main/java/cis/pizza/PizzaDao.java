package cis.pizza;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PizzaDao {
    @Query("SELECT * FROM Pizza")
    LiveData<List<Pizza>> getAll();

    @Query("SELECT * FROM Pizza WHERE id = :Id")
    Pizza getByIds(int Id);

    @Insert
    void insert(Pizza pizza);

    @Delete
    void delete(Pizza pizza);

}
