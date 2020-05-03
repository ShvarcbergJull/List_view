package com.example.pc.listview_pract1;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    UserListAdapter adapter;
    ListView listView;
    ArrayList<User> susers = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();

    protected ArrayList<User> getMans(ArrayList<User> u){
        ArrayList<User> su = new ArrayList<>();
        for (int i = 0;i < u.size();i++){
            if (u.get(i).sex == Sex.MAN){
                su.add(u.get(i));
            }
        }

        return su;
    }

    protected ArrayList<User> getWomans(ArrayList<User> u){
        ArrayList<User> su = new ArrayList<>();
        for (int i = 0;i < u.size();i++){
            if (u.get(i).sex == Sex.WOMAN){
                su.add(u.get(i));
            }
        }

        return su;
    }

    protected ArrayList<User> getUnkn(ArrayList<User> u) {
        ArrayList<User> su = new ArrayList<>();
        for (int i = 0;i < u.size();i++){
            if (u.get(i).sex == Sex.UNKNOWN){
                su.add(u.get(i));
            }
        }

        return su;
    }

    protected ArrayList<User> sortingByBig(ArrayList<User> u) {
        ArrayList<User> su = new ArrayList<>();
        HashMap<String, Integer> forSort = new HashMap<>();

        for (int i = 0;i < u.size();i++) {
            forSort.put(u.get(i).name, i);
        }

        TreeMap<String, Integer> sorted = new TreeMap<>();

        sorted.putAll(forSort);
         for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
             su.add(u.get(entry.getValue()));
         }

         return su;
    }

    protected ArrayList<User> sortingBySmall (ArrayList<User> u) {
        ArrayList<User> su = new ArrayList<>();
        ArrayList<User> tu = new ArrayList<>();

        tu = sortingByBig(u);

        for (int i = (tu.size() - 1); i >= 0; i--) {
            su.add(tu.get(i));
        }

        return su;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);


        AssetManager textik = getResources().getAssets();
        try (InputStreamReader istream = new InputStreamReader(textik.open("number_book.json"))) {
            BufferedReader buf = new BufferedReader(istream);
            String text = "", line = "";

            while ((line = buf.readLine()) != null) {
                text = text + line;
            }

//            Log.d("probkaaaa", text);

            String data[] = text.split("\"user\":");
//            Gson test = new Gson();
//            Contact con = test.fromJson(data[1], Contact.class);
            for (int i = 1;i < data.length;i++) {
                Gson test = new Gson();
                data[i] = data[i].trim();
                String str = data[i].substring(0, data[i].length() - 1);;
                Contact contact = test.fromJson(str, Contact.class);

                Sex temp;
                switch (contact.sex) {
                    case "man":
                        temp = Sex.MAN;
                        break;
                    case "woman":
                        temp = Sex.WOMAN;
                        break;
                    default:
                        temp = Sex.UNKNOWN;
                }

                users.add(new User(contact.name, contact.phone, temp));
                susers.add(new User(contact.name, contact.phone, temp));
            }

            // TODO: реализовать загрузку данных из JSON-файла
            // который загрузить в папку assets

//            for (int i = 0; i < 10; i++) {
//                users.add(new User("Petya", "123", Sex.MAN));
//                users.add(new User("Vasya", "234", Sex.MAN));
//                users.add(new User("Valya", "456", Sex.WOMAN));
//                users.add(new User("UFO", "@@@", Sex.UNKNOWN));
//            }


            adapter = new UserListAdapter(this, users);

            listView.setAdapter(adapter);
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.sex_sp) {
            Spinner spinner = findViewById(R.id.spin1);
            String selected = spinner.getSelectedItem().toString();
            Log.d("spink", selected);
            switch (selected) {
                case "man":
                    users.clear();
                    users.addAll(getMans(susers));
                    Log.d("spink", String.valueOf(users.size()));
                    break;

                case "woman":
                    users.clear();
                    users.addAll(getWomans(susers));
                    break;

                case "unknown":
                    users.clear();
                    users.addAll(getUnkn(susers));
                    break;

                default:
                    users.clear();
                    users.addAll(susers);
            }
        }

        if (v.getId() == R.id.sort_big) {
            ArrayList<User> tus = new ArrayList<>();
            tus.addAll(users);
            users.clear();
            users.addAll(sortingByBig(tus));
        }

        if (v.getId() == R.id.sort_small) {
            ArrayList<User> tus = new ArrayList<>();
            tus.addAll(users);
            users.clear();
            users.addAll(sortingBySmall(tus));
        }

        adapter.notifyDataSetChanged();
    }
}
