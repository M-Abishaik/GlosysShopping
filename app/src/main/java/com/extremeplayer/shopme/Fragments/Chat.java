package com.extremeplayer.shopme.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.extremeplayer.shopme.Interface.MailInterface;
import com.extremeplayer.shopme.Interface.MyInterface;
import com.extremeplayer.shopme.R;
import com.extremeplayer.shopme.UserActivities.VoiceActivity;

import java.sql.Timestamp;


import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class Chat extends Fragment implements MyInterface, AIListener{

    private static AIService aiService;
    private ChatView chatView;
    private MailInterface mailInterface;
    private Button voiceBot;

    public Chat() {
        // Required empty public consructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mailInterface = (MailInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onFragmentChangeListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Debug", "TESTING");

        final AIConfiguration aiConfig = new AIConfiguration("e9e2cc162ca644e0ab341b4549f3a493",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(getContext(), aiConfig);
        aiService.setListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatView = (ChatView)view.findViewById(R.id.chat_view);
        voiceBot = (Button)view.findViewById(R.id.voice);

        voiceBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),VoiceActivity.class);
                startActivity(intent);
            }
        });

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                Log.d("TEST", String.valueOf(chatMessage.getTimestamp()));
                new Fetch().execute(chatMessage.getMessage());
                return true;
            }
        });

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        chatView.addMessage(new ChatMessage("Hello! I am a S9.123.", timestamp.getTime(), ChatMessage.Type.RECEIVED));
        chatView.addMessage(new ChatMessage("How can I help you?", timestamp.getTime(), ChatMessage.Type.RECEIVED));

        Log.d("TEST", "fragment chat");

        return view;
    }

    @Override
    public void fragmentNowVisible() {
        Log.d("Debug", "Chat Screen visible");
    }


    private class Fetch extends AsyncTask<String, Void, AIResponse> {

        private AIError aiError;

        @Override
        protected AIResponse doInBackground(final String... params) {
            final AIRequest request = new AIRequest();
            String query = params[0];
            request.setQuery(query);

            try {
                return Chat.aiService.textRequest(request);
            } catch (final AIServiceException e) {
                aiError = new AIError(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(final AIResponse response) {
            if (response != null) {
                onResult(response);
            } else {
                onError(aiError);
            }
        }
    }

        @Override
        public void onResult(final AIResponse response) {
            Intent intent;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Result result = response.getResult();

            chatView.addMessage(new ChatMessage(result.getFulfillment().getSpeech(), timestamp.getTime(), ChatMessage.Type.RECEIVED));

            Log.d("debug", "" + result.getAction());
            Log.d("debug", "" + result.getStringParameter("event"));

        }

        @Override
        public void onError(AIError error) {
            Log.d("Error", error.toString());
        }

        @Override
        public void onAudioLevel(float level) {
        }

        @Override
        public void onListeningStarted() {
        }

        @Override
        public void onListeningCanceled() {
        }

        @Override
        public void onListeningFinished() {
        }
}
