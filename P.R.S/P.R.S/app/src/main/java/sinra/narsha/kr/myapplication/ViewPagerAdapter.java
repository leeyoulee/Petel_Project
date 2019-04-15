package sinra.narsha.kr.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sinra.narsha.kr.myapplication.frag_view.FirstFragment;
import sinra.narsha.kr.myapplication.frag_view.FourthFragment;
import sinra.narsha.kr.myapplication.frag_view.SecondFragment;
import sinra.narsha.kr.myapplication.frag_view.ThirdFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_FIRST = 0;
    private static final int PAGE_SECOND = 1;
    private static final int PAGE_THIRD = 2;
    private static final int PAGE_FOURTH = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) { // 표시할 Fragment
        switch (position) {
            case PAGE_FIRST:
                return FirstFragment.newInstance(); // 전체
            case PAGE_SECOND:
                return SecondFragment.newInstance(); // 호텔
            case PAGE_THIRD:
                return ThirdFragment.newInstance(); // 분양
            case PAGE_FOURTH:
                return FourthFragment.newInstance(); // 미용
            default:
                return null;
        }
    }

    @Override
    public int getCount() { // Tab 의 갯수
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case PAGE_FIRST:
                return "전체"; // 페이지(position)에 따른 tab 의 타이틀 지정
            case PAGE_SECOND:
                return "호텔"; // 페이지(position)에 따른 tab 의 타이틀 지정
            case PAGE_THIRD:
                return "분양"; // 페이지(position)에 따른 tab 의 타이틀 지정
            case PAGE_FOURTH:
                return "미용"; // 페이지(position)에 따른 tab 의 타이틀 지정
            default:
                return null;
        }
    }
}
