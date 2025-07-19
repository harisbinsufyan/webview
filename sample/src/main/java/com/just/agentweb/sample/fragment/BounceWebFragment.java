package com.just.agentweb.sample.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IWebLayout;
import com.just.agentweb.sample.R;
import com.just.agentweb.sample.widget.WebLayout;

/**
 * Created by cenxiaozhong on 2017/7/1.
 * Source code: https://github.com/Justson/AgentWeb
 */

public class BounceWebFragment extends AgentWebFragment {

	public static BounceWebFragment getInstance(Bundle bundle) {
		BounceWebFragment mBounceWebFragment = new BounceWebFragment();
		if (mBounceWebFragment != null){
			mBounceWebFragment.setArguments(bundle);
		}
		return mBounceWebFragment;
	}


	@Override
	public String getUrl() {
		return super.getUrl();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		mAgentWeb = AgentWeb.with(this)
				.setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
				.useDefaultIndicator(-1, 2)
				.setAgentWebWebSettings(getSettings())
				.setWebViewClient(mWebViewClient)
				.setWebChromeClient(mWebChromeClient)
				.setWebLayout(getWebLayout())
				.setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
				.interceptUnkownUrl()
				.setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
				.setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page, -1)
				.createAgentWeb()//
				.ready()//
				.go(getUrl());
		// Get the bottom-most control of AgentWeb
		addBGChild((FrameLayout) mAgentWeb.getWebCreator().getWebParentLayout());
		initView(view);


	}

	protected IWebLayout getWebLayout() {
		return new WebLayout(getActivity());
	}

	protected void addBGChild(FrameLayout frameLayout) {

		TextView mTextView = new TextView(frameLayout.getContext());
		mTextView.setText("Powered by AgentWeb");
		mTextView.setTextSize(16);
		mTextView.setTextColor(Color.parseColor("#727779"));
		frameLayout.setBackgroundColor(Color.parseColor("#272b2d"));
		FrameLayout.LayoutParams mFlp = new FrameLayout.LayoutParams(-2, -2);
		mFlp.gravity = Gravity.CENTER_HORIZONTAL;
		final float scale = frameLayout.getContext().getResources().getDisplayMetrics().density;
		mFlp.topMargin = (int) (15 * scale + 0.5f);
		frameLayout.addView(mTextView, 0, mFlp);
	}


}