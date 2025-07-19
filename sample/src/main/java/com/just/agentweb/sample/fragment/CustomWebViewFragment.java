package com.just.agentweb.sample.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.sample.R;

import us.feras.mdv.MarkdownView;

/**
 * Created by cenxiaozhong on 2017/6/17.
 * Source code: https://github.com/Justson/AgentWeb
 */

public class CustomWebViewFragment extends AgentWebFragment {


	private MarkdownView mMarkdownWebView;
	private EditText markdownEditText;

	public static final CustomWebViewFragment getInstance(Bundle bundle) {

		CustomWebViewFragment mCustomWebViewFragment = new CustomWebViewFragment();
		if (bundle != null) {
			mCustomWebViewFragment.setArguments(bundle);
		}
		return mCustomWebViewFragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.markdown_view, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		// MarkdownView is a subclass of WebView
		mMarkdownWebView = new MarkdownView(getActivity());
		markdownEditText = (EditText) view.findViewById(R.id.markdownText);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
		lp.weight = 1f;
		mAgentWeb = AgentWeb.with(this)//
				.setAgentWebParent((ViewGroup) view, lp)//
				.closeIndicator()//
				.setWebViewClient(mWebViewClient)
				.setWebView(mMarkdownWebView)
				.setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
				.createAgentWeb()//
				.ready()//
				.go(null);


		String text = "## AgentWeb Features\n" +
				"***\n\n" +
				"1. Support progress bar and custom progress bar\n" +
				"2. Support file download\n" +
				"3. Support file download resume\n" +
				"4. Support download notification progress indication\n" +
				"5. Simplify Javascript communication\n" +
				"6. Support Android 4.4 Kitkat and other versions file upload\n" +
				"7. Support injecting Cookies\n" +
				"8. Enhanced Web security\n" +
				"9. Support fullscreen video playback\n" +
				"10. Compatible with low version JS secure communication\n" +
				"11. More power efficient\n" +
				"12. Support calling WeChat Pay\n" +
				"13. Support calling Alipay (please refer to sample)\n" +
				"14. Default support for location";

		markdownEditText.setText(text);

		updateMarkdownView();

		markdownEditText.addTextChangedListener(new TextWatcher() {


			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateMarkdownView();
			}
		});


		initView(view);

	}


	private void updateMarkdownView() {
		mMarkdownWebView.loadMarkdown(markdownEditText.getText().toString());
	}
}