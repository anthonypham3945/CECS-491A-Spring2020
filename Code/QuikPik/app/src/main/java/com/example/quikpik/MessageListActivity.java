package com.example.quikpik;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MessageListActivity extends AppCompatActivity {
    private ListView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        mMessageRecycler = (ListView) findViewById(R.id.MessageList);
        //mMessageAdapter = new MessageListAdapter(this, messageList);
    }
}