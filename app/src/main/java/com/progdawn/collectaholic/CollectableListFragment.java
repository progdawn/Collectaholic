package com.progdawn.collectaholic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class CollectableListFragment extends Fragment{

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mCollectableRecyclerView;
    private CollectableAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collectable_list, container, false);

        mCollectableRecyclerView = (RecyclerView) view.findViewById(R.id.collectable_recycler_view);
        mCollectableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        Collection collection = Collection.get(getActivity());
        List<Collectable> collectables = collection.getCollectables();

        if(mAdapter == null){
            mAdapter = new CollectableAdapter(collectables);
            mCollectableRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setCollectables(collectables);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class CollectableHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNameTextView;
        private TextView mDateTextView;
        private Collectable mCollectable;

        public CollectableHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_collectable, parent, false));
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.collectable_name);
            mDateTextView = (TextView) itemView.findViewById(R.id.collectable_date);
        }

        public void bind(Collectable collectable){
            mCollectable = collectable;
            mNameTextView.setText(mCollectable.getName());
            mDateTextView.setText(mCollectable.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CollectablePagerActivity.newIntent(getActivity(), mCollectable.getId());
            startActivity(intent);
        }
    }

    private class CollectableAdapter extends RecyclerView.Adapter<CollectableHolder>{
        private List<Collectable> mCollectables;

        public CollectableAdapter(List<Collectable> collectables){
            mCollectables = collectables;
        }

        @Override
        public CollectableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CollectableHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CollectableHolder holder, int position) {
            Collectable collectable = mCollectables.get(position);
            holder.bind(collectable);

        }

        @Override
        public int getItemCount() {
            return mCollectables.size();
        }

        public void setCollectables(List<Collectable> collectables){
            mCollectables = collectables;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_collectable_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_collectable:
                Collectable collectable = new Collectable();
                Collection.get(getActivity()).addCollectable(collectable);
                Intent intent = CollectablePagerActivity.newIntent(getActivity(), collectable.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        Collection collection = Collection.get(getActivity());
        int collectionCount = collection.getCollectables().size();
        String subtitle = getString(R.string.subtitle_format, collectionCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
}
