package com.progdawn.collectaholic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.List;
import java.util.UUID;


/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class CollectablePagerActivity extends AppCompatActivity{

    private static final String EXTRA_COLLECTABLE_ID = "com.progdawn.android.collectaholic.collectable_id";

    private ViewPager mViewPager;
    private List<Collectable> mCollectables;

    public static Intent newIntent(Context packageContext, UUID collectableId){
        Intent intent = new Intent(packageContext, CollectablePagerActivity.class);
        intent.putExtra(EXTRA_COLLECTABLE_ID, collectableId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectable_pager);

        UUID collectableId = (UUID)getIntent().getSerializableExtra(EXTRA_COLLECTABLE_ID);

        mViewPager = (ViewPager)findViewById(R.id.collectable_view_pager);

        mCollectables = Collection.get(this).getCollectables();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Collectable collectable = mCollectables.get(position);
                return CollectableFragment.newInstance(collectable.getId());
            }

            @Override
            public int getCount() {
                return mCollectables.size();
            }
        });

        for(int i = 0; i < mCollectables.size(); i++){
            if(mCollectables.get(i).getId().equals(collectableId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
