package contact.space.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LandScreen extends Activity {
	ImageView btnAllContact;
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
    	btnAllContact = (ImageView)findViewById(R.id.allcontact);
    	btnAllContact.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showAllContact();
			}

			
    	});
    }
	private void showAllContact(){
		Intent intnt=new Intent(contxt,ContactSpace.class);
		startActivity(intnt);
	}	

}
