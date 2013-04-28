package contact.space.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class HomeScreen extends Activity {
	Button btnLogin;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
	}
	
	@Override
    public void onStart(){
    	super.onStart();
    	
    	btnLogin = (Button)findViewById(R.id.btnLogin);
    	btnLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				//ContactSpaceAPI.decrypt(ContactSpaceAPI.encrypt(ContactSpaceAPI.convertLoginInfoToJson("vagabondlab@gmail.com", "12345").toString(), ContactSpaceAPI.Encryption_key), ContactSpaceAPI.Encryption_key);
				ContactSpaceAPI.convertLoginInfoToJson("vagabondlab@gmail.com", "12345");
				try {
					ContactSpaceAPI.SendListToServer();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				login();
				//loginScreen();
			}

			
    	});
    	
    	
    }
	private void login() {
		Intent intnt = new Intent(this,ContactSpace.class);
    	startActivity(intnt);
	}
	

}
