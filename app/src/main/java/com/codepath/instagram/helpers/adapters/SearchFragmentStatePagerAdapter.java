package com.codepath.instagram.helpers.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;

/**
 * Created by araiff on 11/1/15.
 */
public class SearchFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private CharSequence[] pageTitles = {
            "Users",
            "Tags"
    };
    private String curQuery = " ";
    private SearchUsersResultFragment usersFragment;
    private SearchTagsResultFragment tagsFragment;

    public SearchFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                usersFragment = new SearchUsersResultFragment();
                usersFragment.updateQuery(curQuery);
                return usersFragment;
            case 1:
                tagsFragment = new SearchTagsResultFragment();
                tagsFragment.updateQuery(curQuery);
                return tagsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }

    public void updateQuery(String query) {
        usersFragment.updateQuery(query);
        tagsFragment.updateQuery(query);
    }
}