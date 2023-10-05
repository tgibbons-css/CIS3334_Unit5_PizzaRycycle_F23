package cis.pizza;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    SeekBar seekBarSize;
    Button buttonAddToOrder;
    Button buttonPlaceOrder;
    Chip chipPepperoni, chipChicken, chipGreenPepper;
    TextView textViewSize;
    EditText textOrder;
    Integer pizzaSize = 1;     // Pizza sizes are 0=Small, 1=Medium, 2=Large, 3=X-large
    final String[] PIZZA_SIZES = {"Small","Medium","Large","X-Large"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // May need to add the following to the Module build.gradle file's dependencies section
        // implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        textViewSize = findViewById(R.id.textViewSize);
        textOrder = findViewById(R.id.textOrder);
        chipPepperoni = findViewById(R.id.chipPepperoni);
        chipChicken = findViewById(R.id.chipChicken);
        chipGreenPepper = findViewById(R.id.chipGreenPeppers);

        setupSeekBar();
        setupAddToOrderButton();
        setPlaceOrderButtons();
        setupLiveDataObserver();
    }

    /***
     *   Handle SeekBar changes
     */
    private void setupSeekBar() {
        seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pizzaSize = progress;      // progress will be seekBar position of 0, 1, 2, or 3
                textViewSize.setText(PIZZA_SIZES[pizzaSize]);
            }
            // Required for SeekBar, but not needed in this program
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {    }
            // Required for SeekBar, but not needed in this program
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {     }
        });
    }

    /***
     *   Handle Place Order button clicks
     */
    private void setPlaceOrderButtons() {
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CIS 3334", "Place order button clicked");   // log button click for debugging using "CIS 3334" tag
                mainViewModel.placeOrder();
            }
        });
    }

    private void setupAddToOrderButton () {
        /***
         *   Handle Add To Order button clicks
         */
        buttonAddToOrder = findViewById(R.id.buttonAddToOrder);
        buttonAddToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.addToOrder(getToppings(), pizzaSize);
                //textOrder.setText(mainViewModel.getOrder() );
            }
        });
    }

    private void setupLiveDataObserver () {
        // ===== Order Status ===== Create the observer for the order status which updates the UI.
        final Observer<String> statusObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newStatus) {
                // Update the UI, in this case, a TextView.
                //textOrder.setText(textOrder.getText().toString() + "\n" + newStatus );
                textOrder.setText(newStatus );
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mainViewModel.getOrderStatus().observe(this, statusObserver);

        // ===== Pizza Orders ===== Create the observer for the list of Pizza Orders
        mainViewModel.getAllOrder().observe(this, new Observer<List<Pizza>>() {
            @Override
            public void onChanged(@Nullable List<Pizza> allPizzasInOrder) {
                Log.d("CIS 3334", "MainActivity->LiveData Observer -- List of Pizzas changed");
                Log.d("CIS 3334", "MainActivity -- Number of Pizzas = "+allPizzasInOrder.size());

                // TODO: update the RecycleView Array Adapter
            }
        });
    }

    /***
     *   Check what topping chips have been checked and create a string containing them
     */
    private String getToppings() {
        String toppings = "";
        if (chipChicken.isChecked()) {
            toppings += chipChicken.getText() +" - ";
        }
        if (chipGreenPepper.isChecked()) {
            toppings += chipGreenPepper.getText() +" - ";
        }
        if (chipPepperoni.isChecked()) {
            toppings += chipPepperoni.getText() +" - ";
        }
        return toppings;
    }
}