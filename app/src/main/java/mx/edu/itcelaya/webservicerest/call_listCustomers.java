package mx.edu.itcelaya.webservicerest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class call_listCustomers extends AppCompatActivity {

    ListView lv;
    String jsonResult;
    String url = "http://gen-42-shop.000webhostapp.com/api/customers?output_format=JSON&display=full";
    String prestashop_key = "IA8WQDU181IIFLN1INFKAK9V8R3F2NYD"; //"RM6FQ7HBHV654CC82SEFUI18ZHX5FAPU";
    TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_customer);
        lv = (ListView) findViewById(R.id.listCustomer);
        lv.setOnItemClickListener(listenerList);
        cargarEmpleados();
        titulo = (TextView) findViewById(R.id.lol2);
        titulo.setText("List of clients");

    }

    private AdapterView.OnItemClickListener listenerList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getApplicationContext(), (String) view.getTag() + "", Toast.LENGTH_LONG).show();
            Intent intentEdit = new Intent(getApplicationContext(), MainActivity.class);;
            intentEdit.putExtra("id", (String) view.getTag() + "");
            startActivity(intentEdit);
        }
    };

    private void cargarEmpleados() {
        loadListTask tarea = new loadListTask(this, prestashop_key);
        try {
            jsonResult = tarea.execute(new String[] { url }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Log.e("a veeeeeeeeeeeeeer",jsonResult);
        llenarClientes();
    }

    public void llenarClientes(){
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> lastNames = new ArrayList<>();
        ArrayList<String> IDs = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        String[] tags = {"FirstName","LastName","ID","email"};

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("customers");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                names.add(jsonChildNode.optString("firstname"));
                lastNames.add(jsonChildNode.optString("lastname"));
                IDs.add(jsonChildNode.optString("id"));
                emails.add(jsonChildNode.optString("email"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        row_list_adapter adapter = new row_list_adapter(this, names, lastNames,IDs,emails,tags);
        lv.setAdapter(adapter);
    }

// build hash set for list view
    public void LLenaList() {
        List<Map<String, String>> clientList = new ArrayList<Map<String, String>>();
        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("customers");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String firstname = jsonChildNode.optString("firstname");
                String lastname = jsonChildNode.optString("lastname");
                String outPut = firstname + " " + lastname;
                clientList.add(createClient("clientes", outPut));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, clientList,
                android.R.layout.simple_list_item_1,
                new String[] { "clientes" }, new int[] { android.R.id.text1 });
        lv.setAdapter(simpleAdapter);
    }

    private HashMap<String, String> createClient(String name, String number) {
        HashMap<String, String> employeeName = new HashMap<String, String>();
        employeeName.put(name, number);
        return employeeName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
