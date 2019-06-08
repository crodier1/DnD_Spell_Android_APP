package com.example.dd_spell_search;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<String> spellList = new ArrayList<String>();
    AutoCompleteTextView editText;
    TextView text_view_spells;
    RequestQueue requestQueue2;
    JsonObjectRequest objectRequest2;
    String URL2 = "http://www.dnd5eapi.co/api/spells/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String URL = "http://www.dnd5eapi.co/api/spells/";




        editText = findViewById(R.id.spells);

        text_view_spells = findViewById(R.id.text_view_spells);

        text_view_spells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
            }
        });

        SpellSelected ss = new SpellSelected();

        editText.setOnItemClickListener(ss);

        editText.setOnItemClickListener(ss);



        //adpater elements from array into adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                spellList);

        editText.setAdapter(adapter);

        requestQueue2 = Volley.newRequestQueue(this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray spellNames = response.getJSONArray("results");

                            for (int i=0; i<spellNames.length(); i++){

                                JSONObject spellName = spellNames.getJSONObject(i);

                                spellList.add(spellName.getString("name"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Response", error.toString());
                    }
                }


        );

        requestQueue.add(objectRequest);


    }

    class SpellSelected implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String spellChoice = editText.getAdapter().getItem(position).toString();
            Log.e("I choose you ", spellChoice);

            URL2 = "http://www.dnd5eapi.co/api/spells/";

            int spellIndex = spellList.indexOf(spellChoice) + 1;

            URL2 += spellIndex;

            Log.e("Spell URL is ", URL2);

            editText.setText("");

            text_view_spells.setText("");

            objectRequest2 = new JsonObjectRequest(
                    Request.Method.GET,
                    URL2,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //spell name
                                String sName = response.getString("name");
                                String op = "<p><b>";
                                String cb = "</b>";
                                String c = "</p>";

                                text_view_spells.append(Html.fromHtml(op + "Name"+ cb+ ": " + sName + c));

                                //spell description
                                JSONArray descArr = response.getJSONArray("desc");
                                String desc = "";

                                for (int i=0; i<descArr.length(); i++){
                                    if(i == 0){
                                        desc += descArr.getString(i) + c;
                                    } else {
                                        desc += "<p>" + descArr.getString(i) + c;
                                    }

                                }

                                text_view_spells.append(Html.fromHtml(op + "Spell Description"+cb+": " + desc));


                                //higher level
                                try{
                                    JSONArray higher_level_arr = response.getJSONArray("higher_level");
                                    String higherLevel = "";

                                    for (int i=0; i<higher_level_arr.length(); i++){
                                        if(i == 0){
                                            higherLevel += higher_level_arr.getString(i) + c;
                                        } else {
                                            higherLevel += "<p>" + higher_level_arr.getString(i) + c;
                                        }

                                    }



                                        //String higher_level = higher_level_arr.getString(0);
                                        text_view_spells.append(Html.fromHtml(op+"Higher Level"+cb+": " + higherLevel));

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }


                                //range
                                try {
                                    String range = response.getString("range");
                                    text_view_spells.append(Html.fromHtml(op+"Range"+cb+": " + range + c));

                                }catch (JSONException e) {
                                    e.printStackTrace();

                                }



                                //components
                                String componentsList = "";

                                JSONArray components = response.getJSONArray("components");



                                for(int i=0; i<components.length(); i++){
                                    if(i != components.length()-1){
                                        componentsList += components.getString(i) + ", ";
                                    } else {
                                        componentsList += components.getString(i);

                                    }
                                }

                                text_view_spells.append(Html.fromHtml(op+"Components"+cb+": " + componentsList + c));

                                //duration
                                    String duration = response.getString("duration");
                                    text_view_spells.append(Html.fromHtml(op+"Duration"+cb+": " + duration + c));


                                //concentration
                                    String concentration = response.getString("concentration");
                                    text_view_spells.append(Html.fromHtml(op+"Concentration"+cb+": " + concentration + c));


                                //casting_time
                                String casting_time = response.getString("casting_time");
                                text_view_spells.append(Html.fromHtml(op+"Casting Time"+cb+": " + casting_time + c));


                                //level
                                int level = response.getInt("level");

                                text_view_spells.append(Html.fromHtml(op+"Level"+cb+": " + level + c));

                                //classes
                                JSONArray classes = response.getJSONArray("classes");
                                String classNames = "";

                                for(int i=0; i<classes.length(); i++){
                                    if(i != classes.length()-1){
                                        JSONObject currentName = classes.getJSONObject(i);
                                        classNames += currentName.getString("name") + ", ";

                                    } else {
                                        JSONObject currentName = classes.getJSONObject(i);
                                        classNames += currentName.getString("name");
                                    }


                                }

                                text_view_spells.append(Html.fromHtml(op+"Classes"+cb+": " + classNames + c));



                            //material
                                try{
                                    String material = response.getString("material");
                                    text_view_spells.append(Html.fromHtml(op+"Material"+cb+": " + material + c));
                                }catch (JSONException e) {
                                    e.printStackTrace();

                                }


                                //school
                                JSONObject school = response.getJSONObject("school");
                                String schoolName = school.getString("name");
                                text_view_spells.append(Html.fromHtml(op+"School"+cb+": " + schoolName + c));



                            //page
                                String page = response.getString("page");
                                text_view_spells.append(Html.fromHtml(op+"Page"+cb+": " + page + c));

                                //subclasses
                                JSONArray subclasses = response.getJSONArray("subclasses");
                                String subClassNames = "";


                                if(subclasses.length() != 0 && subclasses.length() != 1){
                                    for (int i=0; i<subclasses.length(); i++){
                                        JSONObject currentSubClass = subclasses.getJSONObject(i);


                                        if(i != classes.length()-1) {
                                            subClassNames += currentSubClass.getString("name") + ", ";
                                        } else {
                                            subClassNames += currentSubClass.getString("name");

                                        }
                                    }
                                } else {
                                    JSONObject currentSubClass = subclasses.getJSONObject(0);
                                    subClassNames = currentSubClass.getString("name");
                                }


                                text_view_spells.append(Html.fromHtml(op+"Subclasses"+cb+": " + subClassNames + c));



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("API Response", error.toString());

                        }
                    }
            );


            requestQueue2.add(objectRequest2);
            closeKeyboard();
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String spellChoice = editText.getAdapter().getItem(position).toString();
            Log.e("I choose you ", spellChoice);



        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();

        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    }
