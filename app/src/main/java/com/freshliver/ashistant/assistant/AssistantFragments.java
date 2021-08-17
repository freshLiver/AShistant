package com.freshliver.ashistant.assistant;

import androidx.fragment.app.Fragment;

public enum AssistantFragments {

    Home(HomeFragment.newInstance()),
    Editor(EditorFragment.newInstance());

    private final Fragment fragment;


    AssistantFragments(Fragment f) {
        this.fragment = f;
    }


    @Override
    public String toString() {
        return this.fragment.toString();
    }


    public Fragment getFragment() {
        return this.fragment;
    }
}
