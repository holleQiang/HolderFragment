package com.zhangqiang.holderfragment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HolderFragment extends Fragment {

    private static final HolderFragmentManager mHolderFragmentManager = new HolderFragmentManager();

    private static final String TAG_HOLDER_FRAGMENT = HolderFragment.class.getCanonicalName() + "_Singleton";
    private final Map<String, Object> mTags = new HashMap<>();
    private final List<LifecycleCallback> lifecycleCallbacks = new ArrayList<>();
    private final List<OnActivityResultCallback> activityResultCallbacks = new ArrayList<>();
    private final List<OnPermissionsResultCallback> permissionsResultCallbacks = new ArrayList<>();

    public interface OnActivityResultCallback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public interface OnPermissionsResultCallback {
        void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    public HolderFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onAttach(context);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHolderFragmentManager.holderFragmentCreated(this);
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onCreate(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onViewCreated(view, savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onDestroy();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        for (int i = lifecycleCallbacks.size() - 1; i >= 0; i--) {
            lifecycleCallbacks.get(i).onDetach();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (int i = activityResultCallbacks.size() - 1; i >= 0; i--) {
            activityResultCallbacks.get(i).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = permissionsResultCallbacks.size() - 1; i >= 0; i--) {
            permissionsResultCallbacks.get(i).onPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void registerActivityResultCallback(OnActivityResultCallback activityResultCallback) {
        if (activityResultCallbacks.contains(activityResultCallback)) {
            return;
        }
        activityResultCallbacks.add(activityResultCallback);
    }

    public void unregisterActivityResultCallback(OnActivityResultCallback activityResultCallback) {
        activityResultCallbacks.remove(activityResultCallback);
    }

    public void registerPermissionResultCallback(OnPermissionsResultCallback permissionsResultCallback) {
        if (permissionsResultCallbacks.contains(permissionsResultCallback)) {
            return;
        }
        permissionsResultCallbacks.add(permissionsResultCallback);
    }

    public void unregisterPermissionResultCallback(OnPermissionsResultCallback permissionsResultCallback) {
        permissionsResultCallbacks.remove(permissionsResultCallback);
    }

    public void registerLifecycleCallback(LifecycleCallback lifecycleCallback) {
        if (lifecycleCallbacks.contains(lifecycleCallback)) {
            return;
        }
        lifecycleCallbacks.add(lifecycleCallback);
    }

    public void unregisterLifecycleCallback(LifecycleCallback lifecycleCallback) {
        lifecycleCallbacks.remove(lifecycleCallback);
    }

    public void setTag(String key, Object object) {
        mTags.put(key, object);
    }

    @Nullable
    public Object getTag(String key) {
        return mTags.get(key);
    }

    public static HolderFragment forActivity(FragmentActivity fragmentActivity) {
        return mHolderFragmentManager.get(fragmentActivity);
    }

    public static HolderFragment forFragment(Fragment fragment) {
        return mHolderFragmentManager.get(fragment);
    }

    private static final class HolderFragmentManager {
        private final Map<Activity, HolderFragment> mNotCommittedActivityHolders = new HashMap<>();
        private final Map<Fragment, HolderFragment> mNotCommittedFragmentHolders = new HashMap<>();
        private boolean mActivityCallbackAdded = false;

        private final Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                mNotCommittedActivityHolders.remove(activity);
            }
        };

        private final FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDestroyed(fm, f);
                mNotCommittedFragmentHolders.remove(f);
            }
        };

        void holderFragmentCreated(Fragment fragment) {
            Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment != null) {
                mNotCommittedFragmentHolders.remove(parentFragment);
                parentFragment.getChildFragmentManager().unregisterFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks);
            } else {
                mNotCommittedActivityHolders.remove(fragment.getActivity());
            }
        }

        HolderFragment get(FragmentActivity activity) {

            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            HolderFragment holderFragment = findHolderFragment(fragmentManager);

            if (holderFragment != null) {
                return holderFragment;
            }

            holderFragment = mNotCommittedActivityHolders.get(activity);
            if (holderFragment != null) {
                return holderFragment;
            }

            if (!mActivityCallbackAdded) {
                mActivityCallbackAdded = true;
                activity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            }

            holderFragment = createHolderFragment(fragmentManager);
            mNotCommittedActivityHolders.put(activity, holderFragment);
            return holderFragment;
        }

        HolderFragment get(Fragment parentFragment) {

            FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
            HolderFragment holderFragment = findHolderFragment(fragmentManager);

            if (holderFragment != null) {
                return holderFragment;
            }

            holderFragment = mNotCommittedFragmentHolders.get(parentFragment);
            if (holderFragment != null) {
                return holderFragment;
            }

            fragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, false);

            holderFragment = createHolderFragment(fragmentManager);
            mNotCommittedFragmentHolders.put(parentFragment, holderFragment);
            return holderFragment;
        }


        private static HolderFragment createHolderFragment(FragmentManager fragmentManager) {

            HolderFragment holderFragment = new HolderFragment();
            fragmentManager.beginTransaction()
                    .add(holderFragment, TAG_HOLDER_FRAGMENT)
                    .commitAllowingStateLoss();
            return holderFragment;
        }


        private static HolderFragment findHolderFragment(FragmentManager fragmentManager) {

            Fragment fragment = fragmentManager.findFragmentByTag(TAG_HOLDER_FRAGMENT);
            if (fragment != null && !(fragment instanceof HolderFragment)) {
                throw new RuntimeException("fragment must be instance of " + HolderFragment.class);
            }
            return (HolderFragment) fragment;
        }
    }

    public interface LifecycleCallback {

        void onCreate(Bundle savedInstanceState);

        void onAttach(Context context);

        void onStart();

        void onResume();

        void onStop();

        void onDestroyView();

        void onDetach();

        void onDestroy();

        void onViewCreated(View view, Bundle savedInstanceState);

        void onActivityCreated(Bundle savedInstanceState);

        void onPause();
    }
}