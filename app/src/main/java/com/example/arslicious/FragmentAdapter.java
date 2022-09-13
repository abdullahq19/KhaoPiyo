package com.example.arslicious;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PizzaFragment();
            case 1:
                return new BurgerFragment();
            case 2:
                return new FriesFragment();
            case 3:
                return new ShakesFragment();
            case 4:
                return new PastaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
