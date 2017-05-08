package com.progdawn.collectaholic;

import android.support.v4.app.Fragment;

/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class CollectableListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CollectableListFragment();
    }
}
