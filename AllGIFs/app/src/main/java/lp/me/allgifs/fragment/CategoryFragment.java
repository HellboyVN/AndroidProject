package lp.me.allgifs.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import lp.me.allgifs.R;
import lp.me.allgifs.activity.DetailCategoryActivity;
import lp.me.allgifs.adapter.ImageAdapter;
import lp.me.allgifs.model.CategoryItem;
import lp.me.allgifs.util.AppConstant;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private GridView mGridView;
    private int mCategoryId = -1;
    private List<CategoryItem> listCategoryItem= new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryId = getArguments().getInt(AppConstant.BUNDLE_CATEGORY_ID);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);
        mGridView = (GridView) root.findViewById(R.id.grid_category);

        String[] links = getLinkByCategoryId();
        String[] tags = getTagByCategoryId();

        if (links != null && tags != null) {
            if (mCategoryId != -1) {
                for (int i = 0; i < tags.length; i++) {
                    CategoryItem item = new CategoryItem(links[i], tags[i]);
                    listCategoryItem.add(item);
                }
                mGridView.setAdapter(new ImageAdapter(getActivity(), listCategoryItem));
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), DetailCategoryActivity.class);
                        intent.putExtra("type_key", listCategoryItem.get(i).getTag());
                        startActivity(intent);
                    }
                });
            }
        }

        return root;
    }

    String[] getLinkByCategoryId() {
        String[] data = null;
        switch (mCategoryId) {
            case AppConstant.ID_ACTION_CATEGORY:
                data = getResources().getStringArray(R.array.actions_links);
                break;
            case AppConstant.ID_ANIMAL_CATEGORY:
                data = getResources().getStringArray(R.array.animals_links);
                break;
            case AppConstant.ID_ART_CATEGORY:
                data = getResources().getStringArray(R.array.art_links);
                break;
            case AppConstant.ID_CARTOON_CATEGORY:
                data = getResources().getStringArray(R.array.cartoons_links);
                break;
            case AppConstant.ID_FUNNY_CATEGORY:
                data = getResources().getStringArray(R.array.funny_links);
                break;
            case AppConstant.ID_FILM_CATEGORY:
                data = getResources().getStringArray(R.array.film_links);
                break;
            case AppConstant.ID_GAME_CATEGORY:
                data = getResources().getStringArray(R.array.games_links);
                break;
            case AppConstant.ID_ISLAMIC_CATEGORY:
                data = getResources().getStringArray(R.array.islamic_links);
                break;
            case AppConstant.ID_NATURE_CATEGORY:
                data = getResources().getStringArray(R.array.nature_links);
                break;
            case AppConstant.ID_NEWS_CATEGORY:
                data = getResources().getStringArray(R.array.news_links);
                break;
            case AppConstant.ID_SCIENCE_CATEGORY:
                data = getResources().getStringArray(R.array.science_links);
                break;
            case AppConstant.ID_SPORT_CATEGORY:
                data = getResources().getStringArray(R.array.sport_links);
                break;
            default:
                break;
        }

        return data;
    }

    String[] getTagByCategoryId() {
        String[] data = null;
        switch (mCategoryId) {
            case AppConstant.ID_ACTION_CATEGORY:
                data = getResources().getStringArray(R.array.actions_tags);
                break;
            case AppConstant.ID_ANIMAL_CATEGORY:
                data = getResources().getStringArray(R.array.animals_tags);
                break;
            case AppConstant.ID_ART_CATEGORY:
                data = getResources().getStringArray(R.array.art_tags);
                break;
            case AppConstant.ID_CARTOON_CATEGORY:
                data = getResources().getStringArray(R.array.cartoons_tags);
                break;
            case AppConstant.ID_FUNNY_CATEGORY:
                data = getResources().getStringArray(R.array.funny_tags);
                break;
            case AppConstant.ID_FILM_CATEGORY:
                data = getResources().getStringArray(R.array.film_tags);
                break;
            case AppConstant.ID_GAME_CATEGORY:
                data = getResources().getStringArray(R.array.games_tags);
                break;
            case AppConstant.ID_ISLAMIC_CATEGORY:
                data = getResources().getStringArray(R.array.islamic_tags);
                break;
            case AppConstant.ID_NATURE_CATEGORY:
                data = getResources().getStringArray(R.array.nature_tags);
                break;
            case AppConstant.ID_NEWS_CATEGORY:
                data = getResources().getStringArray(R.array.news_tags);
                break;
            case AppConstant.ID_SCIENCE_CATEGORY:
                data = getResources().getStringArray(R.array.science_tags);
                break;
            case AppConstant.ID_SPORT_CATEGORY:
                data = getResources().getStringArray(R.array.sport_tags);
                break;
            default:
                break;
        }

        return data;
    }

}
