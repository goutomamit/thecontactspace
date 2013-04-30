package contact.space.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class ContactSpaceAPI {
	static JSONObject jsnContactList;
	static JSONObject jsnLoginInfo;
	static JSONObject jsnLoginResponseData = null;
	public static final String Encryption_key = "1234567890123456";
	public static final String Initial_vector = "1234567890123456";
	public static final String SUCCESS_TAG = "sucess";
	public static final String EMAIL_TAG = "email";
	public static final String DATA_TAG = "data";
	public static final String DISPLAY_TAG = "display_name";
	public static final String SESSION_TAG = "session_id";
	public static String DISPLAY_NAME;
	public static String SESSION_ID;
	//public static final String RESPONSE = {"sucess":"1","message":"Login successful","data":{"email":"vagabondlab@gmail.com","display_name":"Green Mile"}}
	public static JSONObject convertContactListTosJSON(String[] nameList,
			String[] numberList) {
		jsnContactList = new JSONObject();
		try {
			jsnContactList.put(nameList[0], numberList[0]);
			jsnContactList.getJSONArray("contactlist");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsnContactList;
	}

	public static JSONObject convertLoginInfoToJson(String username,
			String password) {
		jsnLoginInfo = new JSONObject();
		JSONObject jsnTime = new JSONObject();
		JSONObject jsnUser = new JSONObject();
		Date dat = new Date();
		final Calendar c = Calendar.getInstance();
		try {
			jsnTime.put("year", c.get(Calendar.YEAR));
			jsnTime.put("month", c.get(Calendar.MONTH));
			jsnTime.put("day", dat.getDay());
			jsnTime.put("hour", dat.getHours());
			jsnTime.put("minute", dat.getMinutes());
			jsnTime.put("second", dat.getSeconds());
			jsnUser.put(EMAIL_TAG, username);
			jsnUser.put("password", password);
			jsnUser.put("time", jsnTime);
			jsnLoginInfo.put("auth", jsnUser);
			Log.w("JSON:", "" + jsnLoginInfo.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsnLoginInfo;

	}

	public static JSONObject SendListToServer() throws ClientProtocolException,
			IOException {
		String url = "http://www.marinerjob.com/thecontactspace/api/login";
		// Map<String, String> kvPairs = new HashMap<String, String>();
		// kvPairs.put("contactlist", jsnContactList.toString());
		// Normally I would pass two more JSONObjects.....
        String token = "{\"auth\": {\"email\": \"vagabondlab@gmail.com\", \"password\":\"12345\", \"time\": {\"year\":\"2013\", \"month\":\"04\", \"day\":\"22\", \"hour\":\"17\", \"minute\":\"10\", \"second\":\"20\" }}}";
		HttpResponse re = doPostSecond(url, encrypt(jsnLoginInfo.toString(), Encryption_key));// doPost(url,
																			// kvPairs);
		String temp = EntityUtils.toString(re.getEntity());
		Log.w("Response,", "" + temp);
		// if (temp.compareTo("SUCCESS")==0)
		// {
		// Toast.makeText(null, "Sending complete!", Toast.LENGTH_LONG).show();
		// }
		
		try {
			jsnLoginResponseData = new JSONObject(temp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsnLoginResponseData;
	}

	public HttpResponse doPost(String url, Map<String, String> kvPairs)
			throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		if (kvPairs != null && kvPairs.isEmpty() == false) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					kvPairs.size());
			String k, v;
			Iterator<String> itKeys = kvPairs.keySet().iterator();
			while (itKeys.hasNext()) {
				k = itKeys.next();
				v = kvPairs.get(k);
				nameValuePairs.add(new BasicNameValuePair(k, v));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		HttpResponse response;
		response = httpclient.execute(httppost);
		return response;
	}

	public static HttpResponse doPostSecond(String url, String message)
			throws ClientProtocolException, IOException {
//		if(message.equals("RNhG8vbkSEPAKCk+1uzMy9Qk7t4v/33lTLcWygHsONRBwIgvWu8IXDlOpyCJkkG9+a29OAG3BJmi4mMO8vBJLo3THoPeuKhV1/CvjV9kNl7L9EF0hthILeSE3JSM6YfyH9jg5fTVeg5+JmctExSZ1u7JtMNhwf7GGfwo5tGD+DfsRzO2r8fsrr6KKH9AgpnLEn3XN7AS8K6y/rb1rquypMkalzuf7DDCCQUkLFy6Ybk="))
//			Log.w("same:",message);
//		else Log.w("not same: " + message.length(), "RNhG8vbkSEPAKCk+1uzMy9Qk7t4v/33lTLcWygHsONRBwIgvWu8IXDlOpyCJkkG9+a29OAG3BJmi4mMO8vBJLo3THoPeuKhV1/CvjV9kNl7L9EF0hthILeSE3JSM6YfyH9jg5fTVeg5+JmctExSZ1u7JtMNhwf7GGfwo5tGD+DfsRzO2r8fsrr6KKH9AgpnLEn3XN7AS8K6y/rb1rquypMkalzuf7DDCCQUkLFy6Ybk=".length()+": "+message);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url + "?token="
				+ URLEncoder.encode(message, "UTF-8"));
		Log.w("url:", request.getURI().toString());
//		HttpEntity entity;
//		StringEntity s = new StringEntity("token="
//				+ URLEncoder.encode(message, "UTF-8"));
//		s.setContentEncoding("UTF-8");
//		s.setContentType("application/x-www-form-urlencoded");
//
//		entity = s;
		// request.setEntity(entity);
		// request.
		HttpResponse response;
		response = httpclient.execute(request);
		return response;
	}

	public static String encrypt(final String plainMessage,
			final String symKeyHex) {
				
		try {
			final byte[] symKeyData = symKeyHex.getBytes("UTF-8");
			byte[] encodedMessage = plainMessage.getBytes("UTF-8");
			final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			final int blockSize = cipher.getBlockSize();

			// create the key
			final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

			// generate random IV using block size (possibly create a method for
			// this)
			final byte[] ivData = Initial_vector.getBytes("UTF-8");// new
				Log.w("Size:",""+ivData.length);											// byte[blockSize];
			// final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
			// rnd.nextBytes(ivData);
			final IvParameterSpec iv = new IvParameterSpec(ivData);
			
			cipher.init(Cipher.ENCRYPT_MODE, symKey, iv);

			final byte[] encryptedMessage = cipher.doFinal(encodedMessage);

//			// concatenate IV and encrypted message
//			final byte[] ivAndEncryptedMessage = new byte[ivData.length
//					+ encryptedMessage.length];
//			System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage,
//					0, encryptedMessage.length);
//			System.arraycopy(ivData, 0, ivAndEncryptedMessage, encryptedMessage.length, ivData.length);
			

			final String ivAndEncryptedMessageBase64 = new String(Base64
					.encode(encryptedMessage, Base64.DEFAULT));
			decrypt(new String(ivAndEncryptedMessageBase64), symKeyHex);
			return new String(ivAndEncryptedMessageBase64);

		}catch (InvalidKeyException e) {
			Log.w("say", "esdf: " + e.toString());
			throw new IllegalArgumentException(
					"key argument does not contain a valid AES key");
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException(
					"Unexpected exception during encryption", e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException(
			"key argument does not contain a valid AES key");
		}
	}
	
	public static boolean isLoginSuccessFull(JSONObject responsedata, String email){
		boolean success = false;
//		JSONObject responsedata = new JSONObject();
//		try {
//			responsedata.put(SUCCESS_TAG, "1");
//			responsedata.put(DATA_TAG,new JSONObject("{\"email\":\"vagabondlab@gmail.com\",\"display_name\":\"Green Mile\"}"));
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		JSONObject data;
		try {
			
			if(responsedata.getString(SUCCESS_TAG).equals("1")){
				data = responsedata.getJSONObject(DATA_TAG);
				if(data.getString(EMAIL_TAG).equals(email))
				{
					success = true;
					DISPLAY_NAME = data.getString(DISPLAY_TAG);
					SESSION_ID = data.getString(SESSION_TAG);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	public static String decrypt(final String ivAndEncryptedMessageBase64,
			final String symKeyHex) {
		

		try {
			final byte[] symKeyData = symKeyHex.getBytes("UTF-8");

			final byte[] ivAndEncryptedMessage = Base64.decode(
					ivAndEncryptedMessageBase64.getBytes(), Base64.DEFAULT);
			final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			final int blockSize = cipher.getBlockSize();

			// create the key
			final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

			// retrieve random IV from start of the received message
			final byte[] ivData = Initial_vector.getBytes("UTF-8");//new byte[blockSize];
			//System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
			final IvParameterSpec iv = new IvParameterSpec(ivData);

			// retrieve the encrypted message itself
//			final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length
//					- blockSize];
//			System.arraycopy(ivAndEncryptedMessage, blockSize,
//					encryptedMessage, 0, encryptedMessage.length);

			cipher.init(Cipher.DECRYPT_MODE, symKey, iv);

			final byte[] encodedMessage = cipher.doFinal(ivAndEncryptedMessage);

			// concatenate IV and encrypted message
			String message = "";
			try {
				message = new String(encodedMessage, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.w("Dycrypt", "message: " + message);
			return message;

		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException(
					"key argument does not contain a valid AES key");
		} catch (BadPaddingException e) {
			// you'd better know about padding oracle attacks
			return null;
		} catch (GeneralSecurityException e) {
			Log.w("dycrypt", "msg: " + e.toString());
			throw new IllegalStateException(
					"Unexpected exception during decryption", e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException(
			"key argument does not contain a valid AES key");
		}
	}

}
