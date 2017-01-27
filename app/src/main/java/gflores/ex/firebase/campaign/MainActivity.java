package gflores.ex.firebase.campaign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build GoogleApiClient with AppInvite API for receiving deep links
        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();

        final TextView deepLinkData = (TextView) findViewById(R.id.deep_link_info_label);
        final Button deepLinkButton = (Button) findViewById(R.id.deep_link_button);

        // Check if this app was launched from a deep link. Setting autoLaunchDeepLink to true
        // would automatically launch the deep link if one is found.
        final boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    // Extract deep link from Intent
                                    final Intent intent = result.getInvitationIntent();
                                    deepLinkData.setText(AppInviteReferral.getDeepLink(intent));
                                    deepLinkButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    Log.d("AppInvite", "getInvitation: no deep link found.");
                                }
                            }
                        });
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }
}
