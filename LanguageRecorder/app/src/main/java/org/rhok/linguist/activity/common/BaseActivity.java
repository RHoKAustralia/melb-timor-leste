package org.rhok.linguist.activity.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.rhok.linguist.R;

/**Base activity class with some helpers for managing fragments (activity does not need to use fragments)
 * Created by bramleyt on 5/12/2015.
 */
public class BaseActivity extends AppCompatActivity {


    private String mFirstFragmentTag;

    protected Fragment getMainFragment() {
        Fragment fragment =  getSupportFragmentManager().findFragmentById(getMainFragmentContainerId());
        if(fragment!=null && fragment.isAdded()) return fragment;
        return null;
    }

    /**
     * Show a new fragment, add it to the backStack if required}
     * @param containerId id of the container you want to put the Fragment in. Defaults to {@link #getMainFragmentContainerId()}
     * @param fragment the new Fragment
     * @param tag tag for the Fragment transaction, can be used later with {@link FragmentManager#findFragmentByTag(String)}
     * @return Returns the identifier of this transaction's back stack entry
     */
    public int transactTo(int containerId, Fragment fragment, String tag){
        return transactTo(containerId, fragment, tag, true, 0, 0, 0, 0);
    }
    /**
     * Show a new fragment, add it to the backStack if required}
     * @param containerId id of the container you want to put the Fragment in. Defaults to {@link #getMainFragmentContainerId()}
     * @param fragment the new Fragment
     * @param tag tag for the Fragment transaction, can be used later with {@link FragmentManager#findFragmentByTag(String)}
     * @param addToBackStack
     * @param animEnter
     * @param animExit
     * @param animPopEnter
     * @param animPopExit
     * @return Returns the identifier of this transaction's back stack entry, if <code>addToBackStack<code> is true. Otherwise, returns a negative number.
     */
    public int transactTo(int containerId, Fragment fragment, String tag, boolean addToBackStack, int animEnter, int animExit, int animPopEnter, int animPopExit){
        if (containerId==0) containerId = getMainFragmentContainerId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if(animEnter!=0 && animExit!=0 && animPopEnter!=0 && animPopExit!=0){
            fragmentTransaction.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
        }
        else if(animEnter!=0 || animExit!=0){
            fragmentTransaction.setCustomAnimations(animEnter, animExit);
        }
        else if(containerId==getMainFragmentContainerId()) fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(containerId, fragment, tag);



        if(mFirstFragmentTag==null){
            //first transaction
            mFirstFragmentTag = tag;
            Log.d(getClass().getName(), "not adding " + tag + " to backStack, setting mFirstFragmentTag");
        }
        else if (addToBackStack){
            Log.d(getClass().getName(), "adding "+tag+" to backStack");
            fragmentTransaction.addToBackStack(tag);
        }
        else{
            Log.d(getClass().getName(), "not adding "+tag+" to backStack, mFirstFragmentTag is "+mFirstFragmentTag);
        }

        int backStackID = fragmentTransaction.commit();

        return backStackID;
    }

    public int getMainFragmentContainerId() {
        return R.id.fragment_container_main;
    }
}
