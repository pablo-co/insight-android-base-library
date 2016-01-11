/*
 * [2015] - [2015] Grupo Raido SAPI de CV.
 * All Rights Reserved.
 *
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

package edu.mit.lastmite.insight_library.fragment;


import edu.mit.lastmite.insight_library.communication.TargetListener;
import edu.mit.lastmite.insight_library.communication.TargetResponder;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;

public class ListFragmentResponder extends DaggerListFragment implements TargetResponder {
    protected int mRequestCode;
    protected TargetListener mTargetListener;

    @Override
    public int getRequestCode() {
        return mRequestCode;
    }

    @Override
    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }

    @Override
    public TargetListener getTargetListener() {
        return mTargetListener;
    }

    @Override
    public void setTargetListener(TargetListener targetListener, int requestCode) {
        mTargetListener = targetListener;
        mRequestCode = requestCode;
    }

    @Override
    public void injectFragment(ApplicationComponent component) {
    }
}
