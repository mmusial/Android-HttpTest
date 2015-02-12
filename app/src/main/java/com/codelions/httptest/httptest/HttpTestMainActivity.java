package com.codelions.httptest.httptest;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class HttpTestMainActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http_test_main);

		Button buttonGetData = (Button)findViewById(R.id.buttonGetData);
		if (buttonGetData != null) {
			buttonGetData.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					GetData();

				}
			});
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_http_test_main, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}




	private void GetData()
	{
		EditText inputUrl = (EditText)findViewById(R.id.inputURL);

		if (inputUrl == null)
			return;

		String urlText = inputUrl.getHint().toString();
		if (inputUrl.getText().length() > 0)
			urlText = inputUrl.getText().toString();


		DownloadHttpDataTask task = new DownloadHttpDataTask();
		task.execute(new String[] { urlText });
	}




	private class DownloadHttpDataTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			return GetHttpData(params[0]);
		}


		@Override
		protected void onPostExecute(String result) {
			EditText outputDataText = (EditText)findViewById(R.id.outputDataText);
			if (outputDataText == null)
				return;
			outputDataText.setText(result);
		}


		private String GetHttpData(String url)
		{
			if (!url.startsWith("http://"))
				url = "http://" + url;

			Log.d("HttpTest", "URL: " + url);

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);

			String result = "";
			HttpResponse response;
			try {
				response = httpClient.execute(httpGet);
				result += response.getStatusLine().toString();
				result += "\n";

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = entity.getContent();
					BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));

					String s;
					while ((s = buffer.readLine()) != null) {
						result += s;
					}

					inputStream.close();
				}

			} catch (Exception e) {
				Log.e("HttpTest", e.toString(), e);
			}

			return result;
		}
	}



}
