/*
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Created by Pablo Cárdenas on 25/10/15.
 */

package edu.mit.lastmite.insight_library.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import edu.mit.lastmite.insight_library.R;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;
import edu.mit.lastmite.insight_library.util.Helper;
import edu.mit.lastmite.insight_library.util.ViewUtils;


public abstract class SingleFragmentActivity extends DaggerActivity {

    private static final String TAG = "SingleFragmentActivity";



    /**
     * Should inflate view during the {AppCompatActivity#onCreate}
     */
    protected boolean inflateOnCreate = true;

    /**
     * Override default Android transitions with Slidr
     */
    protected boolean overrideTransitions = true;

    @Inject
    protected Helper mHelper;

    public void injectActivity(ApplicationComponent component) {
        component.inject(this);
    }

    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    protected int getContainerResId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        if (inflateOnCreate) inflateFragment();

        if (overrideTransitions) {
            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        }
    }

    protected void inflateFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(getContainerResId());

        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(getContainerResId(), fragment)
                    .commit();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void inflateFragment(int resourceId, Helper.FragmentCreator creator, int inAnimation, int outAnimation) {
        mHelper.inflateFragment(getSupportFragmentManager(), resourceId, creator, inAnimation, outAnimation);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void inflateFragment(int resourceId, Helper.FragmentCreator creator, int inAnimation, int outAnimation, boolean backStack) {
        mHelper.inflateFragment(getSupportFragmentManager(), resourceId, creator, inAnimation, outAnimation, backStack);
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void removeFragment(int resourceId) {
        mHelper.removeFragment(getSupportFragmentManager(), resourceId);
    }

    protected void onHomePressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (upIntent != null && NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder
                    .create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            finish();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public View getActionBarView() {
        return ViewUtils.getActionBarView(this);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setDarkenedStatusBarColor(int color) {
        setStatusBarColor(ViewUtils.getDarkenedColor(color));
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (overrideTransitions) {
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onHomePressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
