package com.vivekkaushik.resumify;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vivekkaushik.resumify.fragments.AboutFragment;
import com.vivekkaushik.resumify.fragments.EducationFragment;
import com.vivekkaushik.resumify.fragments.ExperienceFragment;
import com.vivekkaushik.resumify.fragments.ProjectsFragment;
import com.vivekkaushik.resumify.fragments.ReferenceFragment;
import com.vivekkaushik.resumify.fragments.SkillsFragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {


    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AboutFragment();
            case 1:
                return new EducationFragment();
            case 2:
                return new ExperienceFragment();
            case 3:
                return new SkillsFragment();
            case 4:
                return new ReferenceFragment();
            case 5:
                return new ProjectsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
