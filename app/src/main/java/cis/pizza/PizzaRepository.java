package cis.pizza;


import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class PizzaRepository {
    public static final Double DELIVERY_PRICE = 2.50;
    private PizzaDao heartrateDao;
    private LiveData<List<Pizza>> pizzasInOrder;          // list of pizzas ordered so far

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    PizzaRepository(Application application) {
        PizzaDatabase db = PizzaDatabase.getDatabase(application);
        heartrateDao = db.pizzaDao();
        pizzasInOrder = heartrateDao.getAll();
    }

    /***
     * Add an pizza to the order
     * @return the description of the pizza is returned if needed
     */
    public void OrderPizza(Pizza newPizza){
        // ROOM -- Add insert into database
        PizzaDatabase.databaseWriteExecutor.execute(() -> {
            heartrateDao.insert(newPizza);
            //Log.d ("CIS 3334", newPizza.toString());
        });
        //pizzasInOrder.add(newPizza);
        //return newPizza.toString();             // return a description of what was ordered
        return;
    }

    /***
     * Retrieve the full order
     * @return a list of pizzas, each one describe by a single string
     */
    public LiveData<List<Pizza>> getAllOrder() {
        pizzasInOrder = heartrateDao.getAll();
        return pizzasInOrder;
    }






}
