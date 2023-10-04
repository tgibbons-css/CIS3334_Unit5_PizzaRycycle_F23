package cis.pizza;


import android.app.Application;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PizzaRepository {
    public static final Double DELIVERY_PRICE = 2.50;
    private PizzaDao heartrateDao;
    private List<Pizza> pizzasInOrder;          // list of pizzas ordered so far
    private boolean delivery=false;             // true if customer wants order delivered

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    PizzaRepository(Application application) {
        PizzaDatabase db = PizzaDatabase.getDatabase(application);
        heartrateDao = db.pizzaDao();
        pizzasInOrder = new ArrayList<Pizza>();
    }

    /***
     * Add an pizza to the order
     * @return the description of the pizza is returned if needed
     */
    public String OrderPizza(Pizza newPizza){
        // ROOM -- Add insert into database
        PizzaDatabase.databaseWriteExecutor.execute(() -> {
            heartrateDao.insert(newPizza);
            //Log.d ("CIS 3334", newPizza.toString());
        });
        pizzasInOrder.add(newPizza);
        return newPizza.toString();             // return a description of what was ordered
    }

    /***
     * Retrieve the full order
     * @return a list of pizzas, each one describe by a single string
     */
    public List<String> getOrder() {
        PizzaDatabase.databaseWriteExecutor.execute(() -> {
            pizzasInOrder = heartrateDao.getAll();              // ROOM - add this
        });
        ArrayList<String> strOrder = new ArrayList<String>();
        for (Pizza p:pizzasInOrder ){
            strOrder.add(p.toString());
        }
        return strOrder;
    }

    /***
     * Retrieve one item from the order
     * @return a list of pizzas, each one describe by a single string
     */
    public String getOrderItem(Integer position) {
        Pizza currentPizza = pizzasInOrder.get(position);
        return currentPizza.toString();
    }

    public Integer getOrderSize() {
        return pizzasInOrder.size();
    }

    /***
     * Calculate the total bill for this order
     * @return total bill as Double
     */
    public Double getTotalBill() {
        Double total = 0.0;
        for (Pizza p:pizzasInOrder ){
            total += p.getPrice();
        }
        if (delivery) {
            total += DELIVERY_PRICE;
        }
        return total;
    }

    public void setDelivery(boolean deliver) {
        this.delivery = deliver;
    }

    public boolean getDelivery() {
        return delivery;
    }
}
