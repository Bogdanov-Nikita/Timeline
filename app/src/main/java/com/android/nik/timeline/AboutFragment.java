package com.android.nik.timeline;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AboutFragment extends Fragment {
    public static final String Event1="event_1";
    public static final String Event2="event_2";
    public static final String E_Mail="Timeline.FeedBack@mail.com";
    public AboutFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_about, container, false);
        WebView About = (WebView)RootView.findViewById(R.id.AboutWeb);
        About.getSettings().setDefaultTextEncodingName("UTF-8");
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">"+
                "<head>"+"<title>"+getResources().getString(R.string.app_name)+"</title>"+"</head>"+"<body>"+
                "<h2 align=\"center\">"+getResources().getString(R.string.app_name)+"</h2>"+
                "<p>"+getResources().getString(R.string.about)+"	"+
                "<a href=\""+Event1+"\">"+getResources().getString(R.string.about_android_market)+"</a></br>"+
                getResources().getString(R.string.about_or)+": "+
                "<a href=\""+Event2+"\">"+getResources().getString(R.string.e_mail)+"</a></p>"+
                "</body>"+"</html>";
        About.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.equals(Event1)){
                    AndroidMarket();
                }
                if(url.equals(Event2)){
                    composeEmail(new String[]{E_Mail},"About Timeline");
                }
                return true;
            }
        });

        About.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        return RootView;
    }
    //вызов активити для написания письма
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    //вызов андроид маркет
    public void AndroidMarket(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }
}
