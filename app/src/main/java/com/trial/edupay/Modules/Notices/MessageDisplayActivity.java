package com.trial.edupay.Modules.Notices;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trial.edupay.Controller.NotificationApiController;
import com.trial.edupay.Model.BaseEntity;
import com.trial.edupay.Model.Message;
import com.trial.edupay.Modules.Base.BaseActionBarActivity;
import com.trial.edupay.R;
import com.trial.edupay.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageDisplayActivity extends BaseActionBarActivity {

    @BindView(R.id.schoolLogo) ImageView schoolLogo;
    @BindView(R.id.profileText) TextView profileText;
    @BindView(R.id.schoolName) TextView schoolName;
    @BindView(R.id.messageTime) TextView messageTime;
    @BindView(R.id.messageBody) TextView messageBody;

    private String messageId, studentId;
    private Message _message;

    public MessageDisplayActivity() {
        _message = new Message();
    }


    @Override protected boolean setupActionBar() {
        super.setupActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_display);
        ButterKnife.bind(this);
        setupActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String messageJson = bundle.getString("message");
            studentId = bundle.getString("studentId");
            messageId = bundle.getString("messageId");

            if (messageJson != null) {
                _message = BaseEntity.fromJson(messageJson, Message.class);
                if (_message != null) {
                    messageId = _message._id;
                    studentId = _message.studentId._id;
                }
            }

            if (studentId != null && messageId != null) getMessageById();
        }
    }

    public void getMessageById() {
        NotificationApiController.getNotificationById(studentId, messageId, new Callback<com.trial.edupay.Model.Message>() {
            @Override
            public void onResponse(Call<com.trial.edupay.Model.Message> call, Response<com.trial.edupay.Model.Message> response) {
                _message = response.body();
                if (_message != null) {
                    inflateUI();
                    if (!_message.isRead)
                        informNotificationSeen();
                }
            }

            @Override
            public void onFailure(Call<com.trial.edupay.Model.Message> call, Throwable t) {

            }
        });
    }

    public void inflateUI() {
        if (_message.organisationId.logo != null) {
            schoolLogo.setVisibility(View.VISIBLE);
            profileText.setVisibility(View.GONE);

//            Glide.with(this)
//                    .load(_message.organisationId.logo)
//                    .centerCrop()
//                    .fitCenter()
//                    .into(schoolLogo);
        } else {
            schoolLogo.setVisibility(View.GONE);
            profileText.setVisibility(View.VISIBLE);

            profileText.setText(Utils.getInitials(_message.organisationId.name));
        }
        schoolName.setText(_message.organisationId.name);
        messageTime.setText(Utils.getDisplayDateTime(_message.createdAt));
        messageBody.setText(_message.content);
    }

    public void informNotificationSeen() {
        NotificationApiController.informNotificationSeen(_message.studentId._id, _message._id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //TODO - tell previous activity that message has been read
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //todo - retry mechanism?
            }
        });
    }
}
