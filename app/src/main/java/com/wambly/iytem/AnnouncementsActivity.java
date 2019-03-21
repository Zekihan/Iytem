package com.wambly.iytem;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        final RecyclerView rv = findViewById(R.id.RC);


        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Announcement> announcements = getAnnouncements("http://iyte.edu.tr/haber/");
                rv.setAdapter(new AnnouncementsCustomAdapter(announcements));
            }
        }).start();

    }
    private List<Announcement> getAnnouncements(String src) {
        List<Announcement> announcements = new ArrayList<>();
        try {

            String[] x = getHtml(src)
                    .split("<div class=\"elementor-posts-container elementor-posts elementor-grid elementor-posts--skin-classic\">")[1]
                    .split("</nav></div></div></div></div></div></div>")[0]
                    .split("<article class");

            for (int i = 1 ; i < x.length ; i++) {

                x[i] = "<article class"+x[i];

                String[] split = x[i].split("<span class=\"elementor-post-date\">");
                String date = split[1].split("</span>")[0];

                split = x[i].split("<span class=\"elementor-post-time\">");
                String time = split[1].split("</span>")[0];

                split = x[i].split("<h2 class=\"elementor-post__title\">");
                String title = split[1].split("</a></h2><div class=\"elementor-post__meta-data\">")[0];

                split = x[i].split("<div class=\"elementor-post__excerpt\">");
                String desc = split[1].split("</p></div>")[0];

                split = x[i].split("<a class=\"elementor-post__read-more\" href=\"");
                String link = split[1].split("\"> Devamı » </a></div></article>")[0];
                split = getHtml(link).split("<meta property=\"og:type\" content=\"article\" />");
                String body = split[1].split("<meta property=\"og:url\" content=\"")[0]
                        .split("<meta property=\"og:description\" content=")[1];
                announcements.add(new Announcement(getText(title), getText(date + time), getText(desc), getText(body), getText(link)));

            }
            announcements.addAll(getAnnouncements(x[x.length-1]
                    .split("<a class=\"page-numbers next\" href=\"")[1]
                    .split("\">Next &raquo;</a>")[0]));

        }catch (IOException e) {
            Log.e("AnnouncementsActivity",e.toString());
            e.printStackTrace();
            return announcements;
        }catch (Exception e){
            Log.e("AnnouncementsActivity",e.toString());
            e.printStackTrace();
            return announcements;
        }
        return  announcements;
    }
    public static String getHtml(String fileURL)
            throws IOException {
        GetMethod get = new GetMethod(fileURL);
        HttpClient client = new HttpClient();
        HttpClientParams params = client.getParams();
        params.setSoTimeout(2000);
        params.setParameter(HttpMethodParams.USER_AGENT,
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
        );
        client.setParams(params);
        try {
            client.executeMethod(get);
        } catch(ConnectException e){
            // Add some context to the exception and rethrow
            throw new IOException("ConnectionException trying to GET " +
                    fileURL,e);
        }

        if(get.getStatusCode()!=200){
            throw new FileNotFoundException(
                    "Server returned " + get.getStatusCode());
        }
        return  IOUtils.toString(get.getResponseBodyAsStream());
    }
    public static String getText(String bodyHtml) {

        // get pretty printed html with preserved br and p tags
        String prettyPrintedBodyFragment = Jsoup.clean(bodyHtml, "", Whitelist.none().addTags("br", "p"), new Document.OutputSettings().prettyPrint(true));
        // get plain text with preserved line breaks by disabled prettyPrint
        return Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(true))
                .replaceAll("/&gt;","").replaceAll("&nbsp;","");
    }
}
