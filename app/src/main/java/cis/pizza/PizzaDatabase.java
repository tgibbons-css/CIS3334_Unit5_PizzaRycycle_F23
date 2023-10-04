package cis.pizza;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Pizza.class}, version = 1)
public abstract class PizzaDatabase extends RoomDatabase {
    public abstract PizzaDao pizzaDao();
    private static PizzaDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PizzaDatabase getDatabase(final Context context) {
        Log.d("CIS 3334", "PizzaDatabase in getDatabase()");
        if (INSTANCE == null) {
            synchronized (PizzaDatabase.class) {
                if (INSTANCE == null) {
                    Log.d("CIS 3334", "PizzaDatabase in getDatabase() calling databaseBuilder");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PizzaDatabase.class, "pizza_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
