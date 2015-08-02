package com.monash.survivalguide.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.monash.survivalguide.R;
import com.monash.survivalguide.activities.ComposeActivity;
import com.monash.survivalguide.activities.ReadArticleActivity;
import com.monash.survivalguide.adapters.PackageAdapter;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.List;


public class ItemFragment extends ProgressFragment implements AbsListView.OnItemClickListener, FloatingActionButton.OnFragmentLoadMoreListener {


    public static final String ARG_CATEGORY_ID = "category_id";
    public static final String ARG_UNIT = "unit";
    private int category;
    private boolean hasMore = true;
    private String unit;
    private PackageAdapter adapter;
    SwipeRefreshLayout swipeLayout;
    public int POST_STATUS = 29;
    public static int articlePosition = 0;
    private static final int VIEW_ARTICLE = 3;
    private View mContentView;
    private static boolean mIsLoading = false;
    private ItemFragmentCallbacks mCallbacks;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance(int param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, param1);
        args.putString(ARG_UNIT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            category = getArguments().getInt(ARG_CATEGORY_ID);
            unit = getArguments().getString(ARG_UNIT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        mListView = (ListView) mContentView.findViewById(android.R.id.list);
        mListView.setAdapter(adapter);
        swipeLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // get the new data from you data source
                // our swipeRefreshLayout needs to be notified when the data is returned in order for it to stop the animation
                //TODO:
                adapter.loadObjects();
                hasMore = true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) mContentView.findViewById(R.id.fab);
        fab.attachToListView(mListView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
            }

            @Override
            public void onScrollUp() {
            }
        }, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),
                        ComposeActivity.class);
                startActivityForResult(intent, POST_STATUS);
            }
        });
        fab.setLoadMoreInterface(ItemFragment.this);
//        ListAppTask task = new ListAppTask();
//        task.execute("currentTime", "refresh");
        mListView.setOnItemClickListener(this);


        // Perhaps set a callback to be fired upon successful loading of a new set of ParseObjects.
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {


            @Override
            public void onLoading() {
                // Trigger any "loading" UI
            }

            @Override
            public void onLoaded(List<ParseObject> list, Exception e) {
                Log.e("","finished");
                swipeLayout.setRefreshing(false);
                if(e == null)
                if (list != null && list.size() > 0) {
                    adapter.notifyDataSetChanged();
                    setContentShown(true);
                    setContentEmpty(false);
                } else if(!mIsLoading)
                {
                    setContentShown(false);
                    setContentEmpty(true);
                }else if(mIsLoading && list.size()==0)
                {
                    hasMore = false;
                }
                mIsLoading = false;

            }
        });


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Setup content view
        setContentView(mContentView);
        // Setup text for empty content
        setEmptyText(R.string.empty);
//        if (mEmptyAddPostButton != null) {
//            mEmptyAddPostButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(),
//                            ComposeActivity.class);
//                    startActivityForResult(intent, POST_STATUS);
//                }
//            });
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        data = new ArrayList<>();
        adapter = new PackageAdapter(getActivity());
        try {
            mCallbacks = (ItemFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        articlePosition = position;
        Intent mIntent = new Intent(getActivity(),
                ReadArticleActivity.class);
        mIntent.putExtra("postClicked",adapter.getItem(position).getObjectId());
        startActivityForResult(mIntent, VIEW_ARTICLE);
        getActivity().overridePendingTransition(
                R.anim.pull_in_right,
                R.anim.push_out_left);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onFragmentLoadMore() {
        Log.e("", "call start loading");
        if (hasMore && !mIsLoading) {
            Log.e("", "start loading");
            mIsLoading = true;
            adapter.loadNextPage();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == POST_STATUS) {
            if (resultCode == Activity.RESULT_OK) {
                swipeLayout.setRefreshing(true);
                //TODO:
                adapter.loadObjects();
                mCallbacks.onNewPost();
            }
        }
    }

    public interface ItemFragmentCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNewPost();

    }

}
