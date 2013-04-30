package contact.space.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LandScreen extends Activity {
	ImageView btnAllContact;
	Button btnLogout;
	TextView displayName;
	EditText edtEmail;
	EditText edtPassword;
	Context contxt = LandScreen.this;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.landscreen);
	}
	
	@Override
    public void onStart(){
    	super.onStart();
    	displayName = (TextView)findViewById(R.id.displayName);
    	displayName.setText(ContactSpaceAPI.DISPLAY_NAME);
    	btnLogout = (Button)findViewById(R.id.logout);
    	btnAllContact = (ImageView)findViewById(R.id.allcontact);
    	btnAllContact.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showAllContact();
			}

			
    	});
    	btnLogout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				try {
					if (!ContactSpaceAPI.SESSION_ID.equals(""))
						ContactSpaceAPI.logoutServer(ContactSpaceAPI
								.convertLogoutInfoToJson(ContactSpaceAPI.EMAIL,
										ContactSpaceAPI.SESSION_ID));
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LandScreen.this.finish();
			}
			
			
    	});
    }
	private void showAllContact(){
		Intent intnt=new Intent(contxt,ContactSpace.class);
		startActivity(intnt);
	}	

}
