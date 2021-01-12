package com.example.stufa.app_utilities;

import android.widget.Filter;

import com.example.stufa.data_models.Announcement;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper extends Filter
{

    static List<Announcement> announcementList;
    static AnnouncementAdapter announcementAdapter;

    public static FilterHelper newInstance(List<Announcement> currentList, AnnouncementAdapter adapter) {
        FilterHelper.announcementAdapter= adapter;
        FilterHelper.announcementList = currentList;
        return new FilterHelper();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        //ArrayList<Announcement> foundFilters = new ArrayList<>();
        filterResults.count = announcementList.size();
        filterResults.values = announcementList;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        announcementAdapter.announcements= (ArrayList<Announcement>) filterResults.values;
        announcementAdapter.notifyDataSetChanged();
    }
}
