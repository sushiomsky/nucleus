/*
 * *
 *  ${PROJECT_NAME}
 *  Copyright (c) ${YEAR} Dennis Suchomsky <dennis.suchomsky@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * */
 *
 * *
 *  @todo Keep it simple!
 *
 */

package nucleus.example.main;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nucleus.example.R;

public class AdmobActivity extends Activity {
	// Remove the below line after defining your own ad unit ID.
	private static final String TOAST_TEXT = "Test ads are being shown. "
			+ "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";

	private static final int START_LEVEL = 1;
	private int mLevel;
	private Button mNextLevelButton;
	private InterstitialAd mInterstitialAd;
	private TextView mLevelTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admob);

		// Create the next level button, which tries to show an interstitial when clicked.
		mNextLevelButton = ((Button) findViewById(R.id.next_level_button));
		mNextLevelButton.setEnabled(false);
		mNextLevelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showInterstitial();
			}
		});

		// Create the text view to show the level number.
		mLevelTextView = (TextView) findViewById(R.id.level);
		mLevel = START_LEVEL;

		// Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
		mInterstitialAd = newInterstitialAd();
		loadInterstitial();

		// Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
		Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_admob, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private InterstitialAd newInterstitialAd() {
		InterstitialAd interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				mNextLevelButton.setEnabled(true);
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				mNextLevelButton.setEnabled(true);
			}

			@Override
			public void onAdClosed() {
				// Proceed to the next level.
				goToNextLevel();
			}
		});
		return interstitialAd;
	}

	private void showInterstitial() {
		// Show the ad if it's ready. Otherwise toast and reload the ad.
		if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		} else {
			Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
			goToNextLevel();
		}
	}

	private void loadInterstitial() {
		// Disable the next level button and load the ad.
		mNextLevelButton.setEnabled(false);
		AdRequest adRequest = new AdRequest.Builder()
				.setRequestAgent("android_studio:ad_template").build();
		mInterstitialAd.loadAd(adRequest);
	}

	private void goToNextLevel() {
		// Show the next level and reload the ad to prepare for the level after.
		mLevelTextView.setText("Level " + (++mLevel));
		mInterstitialAd = newInterstitialAd();
		loadInterstitial();
	}
}
