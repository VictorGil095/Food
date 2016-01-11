package com.example.victor.food;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends DrawerActivity1 {

    private ListView mListView;
    CategoryModel model;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = (ListView) findViewById(R.id.list_category);
        if (checkConnection()) {
            new CategoryAsyncTask().execute("http://ufa.farfor.ru/getyml/?key=ukAXxeJYZN");
            final Intent intent = new Intent(this, FoodItem.class);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CategoryModel categoryModel = adapter.getItem(position);
                    adapter.getItem(position).getId();
                    intent.putExtra("food", categoryModel);
                    startActivity(intent);
                }
            });
        }
    }

    private boolean checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }


    private class CategoryHandler extends DefaultHandler {

        ArrayList<CategoryModel> category = new ArrayList<>();
        String result = "";
        boolean item = false;

        ArrayList<CategoryModel> getCategory() {
            return category;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (localName.equals("category")) {
                item = true;
                model = new CategoryModel();
                model.setId(Integer.parseInt(attributes.getValue("id")));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            item = false;
            if (localName.equals("category")) {
                model.setNamecategory(result);
                category.add(model);
            }
            result = "";
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            result = new String(ch, start, length);
        }
    }


    public class CategoryAdapter extends BaseAdapter {

        LayoutInflater mInflater;
        private ArrayList<CategoryModel> mCategory = new ArrayList<>();

        public CategoryAdapter(Context context, ArrayList<CategoryModel> mCategory) {
            this.mCategory = mCategory;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mCategory.size();
        }

        @Override
        public CategoryModel getItem(int position) {
            return mCategory.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.content_main, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.text_category);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ic_category);
            textView.setText(getItem(position).getNamecategory());
            if (getItem(position).getId() == 5) {
                imageView.setImageResource(R.drawable.sushi);
            } else if (getItem(position).getId() == 8) {
                imageView.setImageResource(R.drawable.warms);
            } else if (getItem(position).getId() == 9) {
                imageView.setImageResource(R.drawable.potables);
            } else if (getItem(position).getId() == 24) {
                imageView.setImageResource(R.drawable.barbecue);
            } else if (getItem(position).getId() == 23) {
                imageView.setImageResource(R.drawable.additives);
            } else if (getItem(position).getId() == 20) {
                imageView.setImageResource(R.drawable.appetizers);
            } else if (getItem(position).getId() == 1) {
                imageView.setImageResource(R.drawable.pizza);
            } else if (getItem(position).getId() == 18) {
                imageView.setImageResource(R.drawable.rolls);
            } else if (getItem(position).getId() == 3) {
                imageView.setImageResource(R.drawable.noodles);
            } else if (getItem(position).getId() == 10) {
                imageView.setImageResource(R.drawable.dessert);
            } else if (getItem(position).getId() == 2) {
                imageView.setImageResource(R.drawable.sets);
            } else if (getItem(position).getId() == 6) {
                imageView.setImageResource(R.drawable.soups);
            } else if (getItem(position).getId() == 7) {
                imageView.setImageResource(R.drawable.salads);
            } else if (getItem(position).getId() == 30) {
                imageView.setImageResource(R.drawable.snacks);
            } else if (getItem(position).getId() == 25) {
                imageView.setImageResource(R.drawable.build);
            } else imageView.setVisibility(View.GONE);
            return convertView;
        }
    }


    private class CategoryAsyncTask extends AsyncTask<String, Void, ArrayList<CategoryModel>> {

        ProgressDialog mDialog;

        CategoryAsyncTask() {
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Loading");
        }


        private ArrayList<CategoryModel> downloadCategory(String myUrl) throws IOException {
            InputStreamReader isr = null;
            ArrayList<CategoryModel> models;
            try {
                URL url = new URL(myUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();
                CategoryHandler handler = new CategoryHandler();
                xmlReader.setContentHandler(handler);
                InputSource inputSource = new InputSource();
                inputSource.setEncoding("windows-1251");
                isr = new InputStreamReader(connection.getInputStream(), "windows-1251");
                inputSource.setCharacterStream(isr);
                xmlReader.parse(inputSource);
                models = handler.getCategory();
                return models;
            } catch (IOException | ParserConfigurationException | SAXException | NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Ошибка сервера", Toast.LENGTH_SHORT).show();
            } finally {
                if (isr != null)
                    isr.close();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected ArrayList<CategoryModel> doInBackground(String... params) {
            try {
                return downloadCategory(params[0]);
            } catch (IOException | NullPointerException e) {
                Toast.makeText(MainActivity.this, "Ошибка сервера", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<CategoryModel> categoryModels) {
            super.onPostExecute(categoryModels);
            mDialog.dismiss();
            adapter = new CategoryAdapter(getApplicationContext(), categoryModels);
            mListView.setAdapter(adapter);
        }
    }
}