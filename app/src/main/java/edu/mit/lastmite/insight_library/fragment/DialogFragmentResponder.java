package edu.mit.lastmite.insight_library.fragment;

import edu.mit.lastmite.insight_library.communication.TargetListener;
import edu.mit.lastmite.insight_library.communication.TargetResponder;
import edu.mit.lastmite.insight_library.util.ApplicationComponent;

public class DialogFragmentResponder extends DaggerDialogFragment implements TargetResponder {

    protected int mRequestCode;
    protected  TargetListener mTargetListener;

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
