package om.koli.inout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter  {
    public TabsAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {

        return 1;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:

                return "Groups";
            default:
                return null;
        }

    }
}


