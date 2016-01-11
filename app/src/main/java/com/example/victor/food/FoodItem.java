package com.example.victor.food;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class FoodItem extends AppCompatActivity {

    private ListView mListView;
    CatalogModel catModel;
    CatalogAdapter adapter;
    CategoryModel categoryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog);
        mListView = (ListView) findViewById(R.id.list_catalog);
        categoryModel = getIntent().getParcelableExtra("food");
        if (checkConnection()) {
            new CatalogAsyncTask().execute("http://ufa.farfor.ru/getyml/?key=ukAXxeJYZN");
            final Intent intent = new Intent(this, FullDescription.class);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CatalogModel catalogModel = adapter.getItem(position);
                    adapter.getItem(position).getMdescription();
                    intent.putExtra("catalog", catalogModel);
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

    private class CatalogHandler extends DefaultHandler {

        ArrayList<CatalogModel> catalog = new ArrayList<>();
        String result = "";
        boolean item, descr, weight;
        //int attrId;
        String attrWeight = "Вес";

        ArrayList<CatalogModel> getCatalog() {
            return catalog;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            item = true;
            result = "";
            weight = descr = false;
            if (localName.equalsIgnoreCase("categories")) {
                return;
            }
            if (localName.equalsIgnoreCase("offer")) {
                //attrId = Integer.parseInt(attributes.getValue("id"));
                catModel = new CatalogModel();
            }
            if (localName.equalsIgnoreCase("description")) {
                descr = true;
            }
            if (localName.equalsIgnoreCase("param")) {
                if (attrWeight.equalsIgnoreCase(attributes.getValue("name"))) {
                    weight = true;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            item = false;
            if (catModel != null) {
                switch (localName) {
                    case "name":
                        catModel.setmName(result);
                        break;
                    case "price":
                        catModel.setmPrice(result);
                        break;
                    case "description":
                        catModel.setMdescription(result);
                    case "picture":
                        catModel.setmIcon(result);
                        break;
                    case "categoryId":
                        catModel.setMcategoryId(Integer.parseInt(result));
                    case "param":
                        if (categoryModel.getId() == 25) {
                            return;
                        }
                        if (weight) {
                            catModel.setmWeight(result);
                        }
                        break;
                    case "offer":
                        if (catModel.getMcategoryId() == categoryModel.getId() && !(categoryModel.getId() == 25)) {
                            catalog.add(new CatalogModel(catModel.getmName(), catModel.getmPrice(), catModel.getMdescription(), catModel.getmIcon(), categoryModel.getId(), catModel.getmWeight()));
                        } else if (catModel.getMcategoryId() == categoryModel.getId()) {
                            catalog.add(new CatalogModel(catModel.getmName(), catModel.getmPrice(), catModel.getMdescription(), catModel.getmIcon(), categoryModel.getId()));
                        }
                        break;
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (item)
                if (descr) {
                    if (length > 1) {
                        result = new String(ch, start, length);
                    }
                } else result = new String(ch, start, length);
        }
    }


    public class CatalogAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater mInflater;
        private ArrayList<CatalogModel> mCatalog = new ArrayList<>();

        public CatalogAdapter(Context context, ArrayList<CatalogModel> mCatalog) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            this.mCatalog = mCatalog;
        }

        @Override
        public int getCount() {
            return mCatalog.size();
        }

        @Override
        public CatalogModel getItem(int position) {
            return mCatalog.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.content_catalog, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ic_catalog);
            Picasso.with(mContext).load(getItem(position).getmIcon()).resize(200, 200).centerInside().into(imageView);
            TextView textView = (TextView) convertView.findViewById(R.id.text_catalog);
            if (!(categoryModel.getId() == 25)) {
                textView.setText(getItem(position).getmName() + '\n' + getItem(position).getmWeight() + '\n' + getItem(position).getmPrice());
            } else
                textView.setText(getItem(position).getmName() + '\n' + getItem(position).getmPrice());
            return convertView;
        }
    }


    private class CatalogAsyncTask extends AsyncTask<String, Void, ArrayList<CatalogModel>> {

        ProgressDialog mDialog;

        CatalogAsyncTask() {
            mDialog = new ProgressDialog(FoodItem.this);
            mDialog.setMessage("Loading");
        }

        private ArrayList<CatalogModel> downloadCatalog(String myUrl) throws IOException {
            InputStreamReader isr = null;
            ArrayList<CatalogModel> models;
            try {
                URL url = new URL(myUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();
                CatalogHandler handler = new CatalogHandler();
                xmlReader.setContentHandler(handler);
                InputSource inputSource = new InputSource();
                inputSource.setEncoding("windows-1251");
                isr = new InputStreamReader(connection.getInputStream(), "windows-1251");
                inputSource.setCharacterStream(isr);
                xmlReader.parse(inputSource);
                models = handler.getCatalog();
                return models;
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
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
        protected ArrayList<CatalogModel> doInBackground(String... params) {
            try {
                return downloadCatalog(params[0]);
            } catch (IOException  | NullPointerException e) {
                Toast.makeText(FoodItem.this, "Ошибка сервера", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<CatalogModel> catalogModels) {
            super.onPostExecute(catalogModels);
            mDialog.dismiss();
            adapter = new CatalogAdapter(getApplicationContext(), catalogModels);
            mListView.setAdapter(adapter);
        }
    }
}