package contact.space.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class HomeScreen extends Activity {
	Button btnLogin;
	EditText edtEmail;
	EditText edtPassword;
	private ProgressDialog dialog;
	Handler backHandler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 1:
				if (dialog != null) {
					dialog.setTitle("Finished");
					dialog.dismiss();
				}
				if(ContactSpaceAPI.isLoginSuccessFull(ContactSpaceAPI.jsnLoginResponseData,edtEmail.getText().toString()))
					login();
				else Toast.makeText(HomeScreen.this, "Can't login!! verify your email or password", 5000 ).show();

			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.home);
	}
	
	@Override
    public void onStart(){
    	super.onStart();
    	edtEmail = (EditText)findViewById(R.id.txtEmail);
    	edtPassword = (EditText)findViewById(R.id.txtPassword); 
    	btnLogin = (Button)findViewById(R.id.btnLogin);
    	btnLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
		    	showProgress();
				// ContactSpaceAPI.decrypt(ContactSpaceAPI.encrypt(ContactSpaceAPI.convertLoginInfoToJson("vagabondlab@gmail.com",
				// "12345").toString(), ContactSpaceAPI.Encryption_key),
				// ContactSpaceAPI.Encryption_key);
				Thread th = new Thread() {
					public void run() {
						ContactSpaceAPI.convertLoginInfoToJson(edtEmail
								.getText().toString(), edtPassword.getText()
								.toString());
						try {
							ContactSpaceAPI.SendListToServer();
							NotifyHandler(1, 100);
						} catch (ClientProtocolException e) {
							NotifyHandler(1, 100);
							e.printStackTrace();
						} catch (IOException e) {
							NotifyHandler(1, 100);
							e.printStackTrace();
						}
						catch (Exception e) {
							NotifyHandler(1, 100);
							e.printStackTrace();
						}
						// loginScreen();
					}
				};
				th.start();
			}

			
    	});
    }
	private void login() {
		Intent intnt = new Intent(this,LandScreen.class);
    	startActivity(intnt);
	}
	public void showProgress() {
		dialog = new ProgressDialog(this);
		dialog = ProgressDialog.show(this, "Please wait..",
				"Login...", true, true,
				new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dilog) {

					}
				});

		// handler.removeCallbacks(sendUpdatesToUI);
		// handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (dialog != null)
					dialog.dismiss();
			}
		});
	}
   public void NotifyHandler(int mssg, long delay) {
		Message msg = new Message();
		msg.what = mssg;
		backHandler.sendMessageDelayed(msg, delay);

	}

}
