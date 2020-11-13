package com.example.booksapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FetchBook extends AsyncTask<String,Void,String> {
    private ArrayList<ItemData> values;
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;
    Context context;

    //Constrcutor
    public FetchBook(Context context, ArrayList<ItemData> values,
                     ItemAdapter itemAdapter, RecyclerView recyclerView){
        this.values=values;
        this.itemAdapter=itemAdapter;
        this.context=context;
        this.recyclerView=recyclerView;
    }

    //Method untuk menjalankan perintah di background
    @Override
    protected String doInBackground(String... strings) {
        //Diambil di awal karena queryString merupakan array of string
        String queryString=strings[0];

        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String bookJSONString=null;
        //URL dasar API
        String BOOK_BASE_URL="https://www.googleapis.com/books/v1/volumes?";
        //Parameter lanjutan dari URL API utk menentukan isi API
        String QUERY_PARAM="q";
        //Penggabungan param dan base url
        Uri builtURI=Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM,queryString).build();

        try {
            //Ubah Uri jadi URL
            URL requestURL=new URL(builtURI.toString());
            urlConnection=(HttpURLConnection) requestURL.openConnection();
            //Method untuk mengelola API
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder=new StringBuilder();
            reader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line=reader.readLine())!=null){
                builder.append(line+"\n");
            }
            if(builder.length()==0){
                return null;
            }
            bookJSONString=builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookJSONString;
        //Outputnya file JSON
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        values=new ArrayList<>();

        try {
            //Arahkan ke object di API
            JSONObject jsonObject=new JSONObject(s);
            //Di file JSON diarahkan untuk ke array items, krn title, author,dll ada di object
            //items
            JSONArray itemsArray=jsonObject.getJSONArray("items");
            //Definisi data yg diambil
            String title=null;
            String author=null;
            String image=null;
            String desc=null;
            //Pengulangan agar setiap buku bisa ditampilkan
            int i=0;
            while (i<itemsArray.length()){
                //dari Array items diambil berdasarkan i
                JSONObject book=itemsArray.getJSONObject(i);
                //Arahkan kembali ke object volumeInfo yg berada dalam array items
                JSONObject volumeInfo=book.getJSONObject("volumeInfo");
                //try catch jika tidak ada data buku
                try {
                    //Mengambil title di dalam object volumeInfo
                    title=volumeInfo.getString("title");
                    //Mengambil author di dalam object volumeInfo
                    if(volumeInfo.has("authors")){
                        author=volumeInfo.getString("authors");
                    }else {
                        author="";
                    }
                    //Mengambil deskripsi di dalam object volumeInfo
                    if(volumeInfo.has("description")){
                        desc=volumeInfo.getString("description");
                    }else {
                        desc="";
                    }
                    //Mengambil link gambar di dalam object volumeInfo
                    if(volumeInfo.has("imageLinks")){
                        //gambar punya 2 isi oleh karena itu harus masuk ke objek imageLinks
                        //baru bisa mengakses isi dari gambar
                        image=volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                    }else {
                        image="";
                    }
                    //Memasukkan data dari API ke dalam itemData
                    ItemData itemData=new ItemData();
                    itemData.itemTitle=title;
                    itemData.itemAuthor=author;
                    itemData.itemDescription=desc;
                    itemData.itemImage=image;
                    values.add(itemData);

                }catch (Exception e){
                    e.printStackTrace();
                }
                //Perulangan
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //Kirim itemData ke adapter agar bisa ditampilkan di UI thread (Main Activity)
        this.itemAdapter=new ItemAdapter(context,values);
        this.recyclerView.setAdapter(this.itemAdapter);
    }
}
