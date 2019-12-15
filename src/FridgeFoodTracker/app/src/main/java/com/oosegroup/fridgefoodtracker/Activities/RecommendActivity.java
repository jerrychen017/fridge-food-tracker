package com.oosegroup.fridgefoodtracker.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.Fridge;
import com.oosegroup.fridgefoodtracker.models.Item;

import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    MainActivity mainActivity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        this.sharedPreferences = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavDrawer(toolbar);

        Fridge fridge = mainActivity.getFridge();
        List<Item> eatenItems = fridge.recommend();
        List<String> eatenString = new ArrayList<>();
        for (Item i: eatenItems) {
            eatenString.add(i.getDescription());
        }
        adapter = new ArrayAdapter<String>(RecommendActivity.this, R.layout.recommend_listview, eatenString);
        ListView listView = (ListView) findViewById(R.id.recommendListView);
        listView.setAdapter(adapter);
    }

    private void setupNavDrawer(Toolbar toolbar) {

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Fridge 1");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Fridge 2");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Recommendations");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Logout");

        Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                System.out.println(drawerItem);
                if (drawerItem.getIdentifier() == 3) {
                    System.out.println("logging out");
                    logout();
                } else if (drawerItem.getIdentifier() == 1) {
                    goToMainActivity();
                }
                return false;
            }
        };


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(item1, new DividerDrawerItem(), item2, new DividerDrawerItem(), item4, new DividerDrawerItem(), item3)
                .withOnDrawerItemClickListener(onDrawerItemClickListener)
                .build();
    }

    public void logout() {
        this.editor.clear();
        this.editor.commit();
        goToLoginActivity();
    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent (this, MainActivity.class);
        mainActivityIntent.putExtra("fridgeDataTag", SplashActivity.jsonString);
        startActivity(mainActivityIntent);
    }

    private void goToLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
    }
}

