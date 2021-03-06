package com.lll.supportotherdemos.leanback.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.DividerRow;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SectionRow;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lll.supportotherdemos.R;
import com.lll.supportotherdemos.leanback.helper.BackgroundHelper;
import com.lll.supportotherdemos.leanback.BrowseActivity;
import com.lll.supportotherdemos.leanback.DetailsActivity;
import com.lll.supportotherdemos.leanback.GuidedStepActivity;
import com.lll.supportotherdemos.leanback.GuidedStepHalfScreenActivity;
import com.lll.supportotherdemos.leanback.RowsActivity;
import com.lll.supportotherdemos.leanback.SearchActivity;
import com.lll.supportotherdemos.leanback.pojo.PhotoItem;
import com.lll.supportotherdemos.leanback.presenter.CardPresenter;

/**
 * Version 1.0
 * Created by lll on 17/7/21.
 * Description
 * copyright generalray4239@gmail.com
 */
public class BrowseFragment extends android.support.v17.leanback.app.BrowseFragment {

    private static final String TAG = "leanback.BrowseFragment";

    private static final boolean TEST_ENTRANCE_TRANSITION = true;
    private static final int NUM_ROWS = 8;
    private static final long HEADER_ID1 = 1001;
    private static final long HEADER_ID2 = 1002;
    private static final long HEADER_ID3 = 1003;

    private ArrayObjectAdapter mRowsAdapter;
    private BackgroundHelper mBackgroundHelper = new BackgroundHelper();

    // For good performance, it's important to use a single instance of
    // a card presenter for all rows using that presenter.
    final CardPresenter mCardPresenter = new CardPresenter();
    final CardPresenter mCardPresenter2 = new CardPresenter(R.style.MyImageCardViewTheme);

    public BrowseFragment() {
        getMainFragmentRegistry().registerFragment(PageRow.class, new PageRowFragmentFactory());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBadgeDrawable(getActivity().getDrawable(R.drawable.ic_title));
        setTitle("Leanback Sample App");
        setHeadersState(HEADERS_ENABLED);
        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (isShowingHeaders()) {
                    mBackgroundHelper.setBackground(getActivity(), null);
                } else if (item instanceof PhotoItem) {
                    mBackgroundHelper.setBackground(
                            getActivity(), ((PhotoItem) item).getImageResourceId());
                }
            }
        });
        if (TEST_ENTRANCE_TRANSITION) {
            // don't run entrance transition if fragment is restored.
            if (savedInstanceState == null) {
                prepareEntranceTransition();
            }
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setupRows();
                loadData();
                startEntranceTransition();
            }
        }, 2000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupRows() {
        ListRowPresenter lPresenter = new ListRowPresenter();
        lPresenter.setNumRows(2);
        mRowsAdapter = new ArrayObjectAdapter(lPresenter);
        setAdapter(mRowsAdapter);
    }

    private void loadData() {
        int i = 0;
        mRowsAdapter.add(new PageRow(new HeaderItem(HEADER_ID1, "Page Row 0")));
        mRowsAdapter.add(new DividerRow());

        mRowsAdapter.add(new SectionRow(new HeaderItem("section 0")));
        for (; i < NUM_ROWS; ++i) {
            mRowsAdapter.add(new ListRow(new HeaderItem(i, "Row " + i), createListRowAdapter(i)));
        }

        mRowsAdapter.add(new DividerRow());
        mRowsAdapter.add(new PageRow(new HeaderItem(HEADER_ID2, "Page Row 1")));

        mRowsAdapter.add(new PageRow(new HeaderItem(HEADER_ID3, "Page Row 2")));
    }

    private ArrayObjectAdapter createListRowAdapter(int i) {
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter((i & 1) == 0 ? mCardPresenter : mCardPresenter2);
        listRowAdapter.add(new PhotoItem(
                "Hello world",
                R.drawable.gallery_photo_1));
        listRowAdapter.add(new PhotoItem(
                "This is a test",
                "Only a test",
                R.drawable.gallery_photo_2));
        listRowAdapter.add(new PhotoItem(
                "Android TV",
                "by Google",
                R.drawable.gallery_photo_3));
        listRowAdapter.add(new PhotoItem(
                "Leanback",
                R.drawable.gallery_photo_4));
        listRowAdapter.add(new PhotoItem(
                "GuidedStep (Slide left/right)",
                R.drawable.gallery_photo_5));
        listRowAdapter.add(new PhotoItem(
                "GuidedStep (Slide bottom up)",
                "Open GuidedStepFragment",
                R.drawable.gallery_photo_6));
        listRowAdapter.add(new PhotoItem(
                "Android TV",
                "open RowsActivity",
                R.drawable.gallery_photo_7));
        listRowAdapter.add(new PhotoItem(
                "Leanback",
                "open BrowseActivity",
                R.drawable.gallery_photo_8));
        listRowAdapter.add(new PhotoItem(
                "Hello world",
                R.drawable.gallery_photo_1));
        listRowAdapter.add(new PhotoItem(
                "This is a test",
                "Only a test",
                R.drawable.gallery_photo_2));
        listRowAdapter.add(new PhotoItem(
                "Android TV",
                "by Google",
                R.drawable.gallery_photo_3));
        listRowAdapter.add(new PhotoItem(
                "Leanback",
                R.drawable.gallery_photo_4));
        return listRowAdapter;
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            Intent intent = null;
            Bundle bundle = null;
            if (((PhotoItem) item).getImageResourceId() == R.drawable.gallery_photo_6) {
                GuidedStepFragment.add(getFragmentManager(), new GuidedStepHalfScreenActivity.FirstStepFragment(),
                        R.id.lb_guidedstep_host);
            } else if (((PhotoItem) item).getImageResourceId() == R.drawable.gallery_photo_5) {
                GuidedStepFragment.add(getFragmentManager(),
                        new GuidedStepActivity.FirstStepFragment(), R.id.lb_guidedstep_host);
                return;
            } else if (((PhotoItem) item).getImageResourceId() == R.drawable.gallery_photo_8) {
                intent = new Intent(getActivity(), BrowseActivity.class);
                bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle();
            } else if (((PhotoItem) item).getImageResourceId() == R.drawable.gallery_photo_7) {
                intent = new Intent(getActivity(), RowsActivity.class);
                bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                        .toBundle();
            } else {
                intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_ITEM, (PhotoItem) item);
                bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
            }
            getActivity().startActivity(intent, bundle);
        }
    }

    public static class PageRowFragmentFactory extends FragmentFactory {

        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row) rowObj;
            if (row.getHeaderItem().getId() == HEADER_ID1) {
                return new SampleRowsFragment();
            } else if (row.getHeaderItem().getId() == HEADER_ID2) {
                return new SampleRowsFragment();
            } else if (row.getHeaderItem().getId() == HEADER_ID3) {
                return new SampleFragment();
            }
            return null;
        }
    }

    public static class SampleRowsFragment extends RowsFragment {
        final CardPresenter mCardPresenter = new CardPresenter();
        final CardPresenter mCardPresenter2 = new CardPresenter(R.style.MyImageCardViewTheme);

        void loadFragmentData() {
            ArrayObjectAdapter adapter = (ArrayObjectAdapter) getAdapter();
            for (int i = 0; i < 4; i++) {
                ListRow row = new ListRow(new HeaderItem("Row " + i), createListRowAdapter(i));
                adapter.add(row);
            }
            if (getMainFragmentAdapter() != null) {
                getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
            }
        }

        public SampleRowsFragment() {
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(new ListRowPresenter());
            setAdapter(adapter);
            // simulates late data loading:
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    loadFragmentData();
                }
            }, 500);

            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                    Intent intent;
                    Bundle bundle;
                    if (((PhotoItem) item).getImageResourceId() == R.drawable.gallery_photo_6) {
                        GuidedStepFragment.add(getActivity().getFragmentManager(),
                                new GuidedStepHalfScreenActivity.FirstStepFragment(),
                                R.id.lb_guidedstep_host);
                        return;
                    } else if (((PhotoItem) item).getImageResourceId() == R.drawable.gallery_photo_5) {
                        GuidedStepFragment.add(getActivity().getFragmentManager(),
                                new GuidedStepActivity.FirstStepFragment(), R.id.lb_guidedstep_host);
                        return;
                    } else if (((PhotoItem) item).getImageResourceId() == R.drawable.gallery_photo_8) {
                        intent = new Intent(getActivity(), BrowseActivity.class);
                        bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                                .toBundle();
                    } else if (((PhotoItem) item).getImageResourceId() == R.drawable.gallery_photo_7) {
                        intent = new Intent(getActivity(), RowsActivity.class);
                        bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                                .toBundle();
                    } else {
                        intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra(DetailsActivity.EXTRA_ITEM, (PhotoItem) item);
                        bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                ((ImageCardView) itemViewHolder.view).getMainImageView(),
                                DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                    }
                    getActivity().startActivity(intent, bundle);
                }
            });
        }

        private ArrayObjectAdapter createListRowAdapter(int i) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter((i & 1) == 0 ?
                    mCardPresenter : mCardPresenter2);
            listRowAdapter.add(new PhotoItem(
                    "Hello world",
                    R.drawable.gallery_photo_1));
            listRowAdapter.add(new PhotoItem(
                    "This is a test",
                    "Only a test",
                    R.drawable.gallery_photo_2));
            listRowAdapter.add(new PhotoItem(
                    "Android TV",
                    "by Google",
                    R.drawable.gallery_photo_3));
            listRowAdapter.add(new PhotoItem(
                    "Leanback",
                    R.drawable.gallery_photo_4));
            listRowAdapter.add(new PhotoItem(
                    "GuidedStep (Slide left/right)",
                    R.drawable.gallery_photo_5));
            listRowAdapter.add(new PhotoItem(
                    "GuidedStep (Slide bottom up)",
                    "Open GuidedStepFragment",
                    R.drawable.gallery_photo_6));
            listRowAdapter.add(new PhotoItem(
                    "Android TV",
                    "open RowsActivity",
                    R.drawable.gallery_photo_7));
            listRowAdapter.add(new PhotoItem(
                    "Leanback",
                    "open BrowseActivity",
                    R.drawable.gallery_photo_8));
            return listRowAdapter;
        }
    }

    public static class PageFragmentAdapterImpl extends MainFragmentAdapter<SampleFragment> {

        public PageFragmentAdapterImpl(SampleFragment fragment) {
            super(fragment);
            setScalingEnabled(true);
        }

        @Override
        public void setEntranceTransitionState(boolean state) {
            getFragment().setEntranceTransitionState(state);
        }
    }

    public static class SampleFragment extends Fragment implements MainFragmentAdapterProvider {

        final PageFragmentAdapterImpl mMainFragmentAdapter = new PageFragmentAdapterImpl(this);

        public void setEntranceTransitionState(boolean state) {
            final View view = getView();
            int visibility = state ? View.VISIBLE : View.INVISIBLE;
            view.findViewById(R.id.tv1).setVisibility(visibility);
            view.findViewById(R.id.tv2).setVisibility(visibility);
            view.findViewById(R.id.tv3).setVisibility(visibility);
        }

        @Nullable
        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page_fragment, container, false);
            view.findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(inflater.getContext(), GuidedStepActivity.class);
                    startActivity(intent);
                }
            });

            return view;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            // static layout has view and data ready immediately
            mMainFragmentAdapter.getFragmentHost().notifyViewCreated(mMainFragmentAdapter);
            mMainFragmentAdapter.getFragmentHost().notifyDataReady(mMainFragmentAdapter);
        }

        @Override
        public MainFragmentAdapter getMainFragmentAdapter() {
            return mMainFragmentAdapter;
        }
    }


}
