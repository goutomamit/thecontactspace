package contact.space.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
				
				//ContactSpaceAPI.decrypt(ContactSpaceAPI.encrypt(ContactSpaceAPI.convertLoginInfoToJson("vagabondlab@gmail.com", "12345").toString(), ContactSpaceAPI.Encryption_key), ContactSpaceAPI.Encryption_key);
				ContactSpaceAPI.convertLoginInfoToJson(edtEmail.getText().toString(), edtPassword.getText().toString());
				try {
					ContactSpaceAPI.SendListToServer();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(ContactSpaceAPI.isLoginSuccessFull(ContactSpaceAPI.jsnLoginResponseData,edtEmail.getText().toString()))
					login();
				else Toast.makeText(HomeScreen.this, "Can't login!! verify your email or password", 5000 ).show();
				//loginScreen();
			}

			
    	});
    }
	private void login() {
		Intent intnt = new Intent(this,LandScreen.class);
    	startActivity(intnt);
	}
	

}
