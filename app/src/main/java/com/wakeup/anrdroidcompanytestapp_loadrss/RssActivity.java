package com.wakeup.anrdroidcompanytestapp_loadrss;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssActivity extends ListActivity {

    List headlines;
    List links;
    ListView list;

    private RssActivity local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        list = (ListView) findViewById(android.R.id.list);

        headlines = new ArrayList();
        links = new ArrayList();

        String stringURL = getIntent().getStringExtra(MainActivity.URL_CODE);

        GetRSSDataTask task = new GetRSSDataTask();
        task.execute(stringURL);
        //task.execute("http://www.itcuties.com/feed/");
        //task.execute("http://www.pcworld.com/index.rss");

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri = Uri.parse(links.get(position).toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private class GetRSSDataTask extends AsyncTask<String, Void, List<Object> > {
        @Override
        protected List<Object> doInBackground(String... urls) {
            try {
                //URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
                //URL url = new URL("http://www.pcworld.com/index.rss");
                URL url = new URL(urls[0]);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;

                // Returns the type of current event: START_TAG, END_TAG, etc..
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem)
                                headlines.add(xpp.nextText()); //extract the headline
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem)
                                links.add(xpp.nextText()); //extract the link of article
                        }
                    }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem=false;
                    }

                    eventType = xpp.next(); //move to next element
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                /*Toast.makeText(RssActivity.this, "Can't connect to the URL", Toast.LENGTH_SHORT).show();
                try {
                    wait(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }*/
                Intent intent = new Intent(RssActivity.this,MainActivity.class);
                startActivity(intent);
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<Object> result) {

            // Create a list adapter
            ArrayAdapter adapter = new ArrayAdapter(RssActivity.this, android.R.layout.simple_list_item_1, headlines);

            // Set list adapter for the ListView
            list.setAdapter(adapter);

        }

        public InputStream getInputStream(URL url) {
            try {
                return url.openConnection().getInputStream();
            } catch (IOException e) {
                return null;
            }
        }
    }







}
