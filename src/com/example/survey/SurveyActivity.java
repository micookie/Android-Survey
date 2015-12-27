package com.example.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.example.survey.model.QuestionModel;
import com.example.survey.uitils.ToastUtils;
import com.example.survey.uitils.UiUtils;
import com.example.survey.weight.FlowRadioGroup;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

//@SuppressLint("UseSparseArrays")
public class SurveyActivity extends Activity implements OnClickListener {
	private ImageButton btnImgBack;
	private ArrayList<QuestionModel> questionList;
	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> result = new HashMap<Integer, String>();
	private TextView tvSubmit, tvBack;

	private LinearLayout llSurveyMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_survey);
		initLayout();

		/**
		 * 
		 * 1.网络请求表单数据 2.解析JSON 3.根据表单数据生成Layout样式 4.数据提交
		 */
		// 测试数据
		// final String jsonChinese =
		// "[{\"questionName\":\"您对本次展厅的参观很满意吗？\",\"questionNo\":1,\"questionOption\":[\"非常同意\",\"同意\",\"不确定\",\"不同意\",\"完全不同意\"]},{\"questionName\":\"这次展厅的参观满足了您在技术层面的需求吗？\",\"questionNo\":2,\"questionOption\":[\"非常同意\",\"同意\",\"不确定\",\"不同意\",\"完全不同意\"]},{\"questionName\":\"中兴展厅的布局和风格很吸引您吗？\",\"questionNo\":3,\"questionOption\":[\"非常同意\",\"同意\",\"不确定\",\"不同意\",\"完全不同意\"]},{\"questionName\":\"您觉得我们展厅的哪部分还需要改进？\",\"questionNo\":4,\"questionOption\":[\"形象区部分（PPT）\",\"无线产品（Pre5G,基站）\",\"终端\",\"智慧城市\",\"IPTV/家庭应用\",\"有线产品(C300, T8000)\"]},{\"questionName\":\"您会购买中兴的产品吗？\",\"questionNo\":5,\"questionOption\":[\"会\",\"可能会\",\"不会\"]},{\"questionName\":\"本次宣讲在技术层面上生动形象、易于理解吗？\",\"questionNo\":6,\"questionOption\":[\"非常同意\",\"同意\",\"不确定\",\"不同意\",\"完全不同意\"]},{\"questionName\":\"您对本次宣讲人员很满意吗？\",\"questionNo\":7,\"questionOption\":[\"非常同意\",\"同意\",\"不确定\",\"不同意\",\"完全不同意\"]},{\"questionName\":\"您对我们展厅的哪部分展示最满意？\",\"questionNo\":8,\"questionOption\":[\"形象区部分（PPT）\",\"无线产品（Pre5G,基站）\",\"终端\",\"智慧城市\",\"IPTV/家庭应用\",\"有线产品(C300, T8000)\"]},{\"questionName\":\"宣讲人员态度友善、彬彬有礼吗？\",\"questionNo\":9,\"questionOption\":[\"非常同意\",\"同意\",\"不确定\",\"不同意\",\"完全不同意\"]},{\"questionName\":\"我的问题得到了满意的回答吗？\",\"questionNo\":10,\"questionOption\":[\"非常同意\",\"同意\",\"不确定\",\"不同意\",\"完全不同意\"]}]";
		final String json = "[{\"questionName\":\"您对我们的产品满意吗？\",\"questionNo\":1,\"questionType\":0,\"questionOption\":[]},{\"questionName\":\"我们的布局和风格很吸引您吗？\",\"questionNo\":2,\"questionType\":0,\"questionOption\":[]},{\"questionName\":\"您觉得我们的哪部分还需要改进？\",\"questionNo\":3,\"questionType\":1,\"questionOption\":[\"A部分xxxxx\",\"B部分oooo\",\"C部分www\",\"D部分mmm\",\"E部分\",\"F部分\",]},{\"questionName\":\"您会购买我们的产品吗？\",\"questionNo\":4,\"questionType\":1,\"questionOption\":[\"会\",\"可能会\",\"不会\"]},{\"questionName\":\"您对我们的服务人员很满意吗？\",\"questionNo\":5,\"questionType\":0,\"questionOption\":[]},{\"questionName\":\"我的问题得到了满意的回答吗？\",\"questionNo\":6,\"questionType\":0,\"questionOption\":[]}]";

		// json请求URL
		String url = "";

		HttpUtils http = new HttpUtils();
		// 1.网络请求表单数据
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ToastUtils.showLong(SurveyActivity.this, "网络加载失败，使用本地数据加载...");

				// 测试代码，当网络请求失败的时候，使用变量中的json
				// 2.解析JSON
				analysisJson(json);
				// 3.生成样式
				createLayout(questionList);
				// 4.数据提交
				submitResultBind();

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				// 2.解析JSON
				analysisJson(arg0.result);
				// 3.生成样式
				createLayout(questionList);
				// 4.数据提交
				submitResultBind();
			}

		});

	}

	/**
	 * 
	 * <p>
	 * Title: initLayout
	 * </p>
	 * <p>
	 * Description:资源初始化
	 * </p>
	 */
	private void initLayout() {
		llSurveyMain = (LinearLayout) findViewById(R.id.ll_survey_main);
		btnImgBack = (ImageButton) findViewById(R.id.btnImg_back_survey);
		tvBack = (TextView) findViewById(R.id.tv_back_survey);
		btnImgBack.setOnClickListener(this);
		tvBack.setOnClickListener(this);
	}

	/**
	 * 
	 * <p>
	 * Title: analysisJson
	 * </p>
	 * <p>
	 * Description: 网络返回问题JSON解析
	 * </p>
	 * 
	 * @param result
	 */
	private void analysisJson(String result) {
		questionList = (ArrayList<QuestionModel>) JSONArray.parseArray(result, QuestionModel.class);
	}

	/**
	 * <p>
	 * Title: submitResult
	 * </p>
	 * <p>
	 * Description: 数据提交
	 * </p>
	 */
	private void submitResultBind() {

		// 提交按钮点击事件绑定
		tvSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				int checkResult = checkSelectedAll(questionList.size(), result);
				// 问题是否回答完整检查
				if (checkResult < 0) {
					ToastUtils.show(SurveyActivity.this, "正在提交");

				} else {
					// 提示未选择的问题
					ToastUtils.show(SurveyActivity.this, "请填写问题" + (checkResult + 1));
				}
			}
		});

	}

	/**
	 * <p>
	 * Title: createLayout
	 * </p>
	 * <p>
	 * Description: 样式生成
	 * </p>
	 */
	/**
	 * <p>
	 * Title: createLayout
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param questionList
	 */
	private void createLayout(ArrayList<QuestionModel> questionList) {
		for (int i = 0; i < questionList.size(); i++) {

			// ####生成item框架####
			LinearLayout itemSurveyLayout = new LinearLayout(this);
			itemSurveyLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
			// 设置item宽度、高度
			// LinearLayout.LayoutParams paramsItem = new
			// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
			// UiUtils.dip2px(this, 100));
			LinearLayout.LayoutParams paramsItem = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			// 设置item margin
			paramsItem.setMargins(0, UiUtils.dip2px(this, 10), 0, 0);
			itemSurveyLayout.setPadding(UiUtils.dip2px(this, 5), 0, UiUtils.dip2px(this, 5), 0);
			itemSurveyLayout.setLayoutParams(paramsItem);
			itemSurveyLayout.setBackgroundColor(Color.parseColor("#00000000"));
			itemSurveyLayout.setOrientation(LinearLayout.VERTICAL);

			// ####生成问题TextView####
			TextView question = new TextView(this);
			LinearLayout.LayoutParams paramsTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsTextView.setMargins(0, UiUtils.dip2px(this, 10), 0, UiUtils.dip2px(this, 10));
			question.setLayoutParams(paramsTextView);
			question.setTextSize(UiUtils.sp2px(this, 5.2f));
			question.setText((questionList.get(i).getQuestionNo()) + "." + questionList.get(i).getQuestionName());
			// 渲染问题到item框架
			itemSurveyLayout.addView(question);

			// 问题类型0，没有具体选择答案，星级评价
			if (questionList.get(i).getQuestionType() == 0) {
				// *********************************************************************
				RatingBar ratingBar = new RatingBar(this, null, android.R.attr.ratingBarStyle);
				LinearLayout.LayoutParams paramsRastingBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsRastingBar.setMargins(UiUtils.dip2px(this, 10), 0, 0, 0);
				ratingBar.setId(i);// 设置ID
				ratingBar.setLayoutParams(paramsRastingBar);
				ratingBar.setNumStars(5);// 设置最大星星数量
				ratingBar.setStepSize(1.0f);// 设置星星步长
				ratingBar.setRating(0);// 设置默认星星得分
				// ratingBar.setProgress(progress);
				// 设置监听事件
				ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
						ToastUtils.show(SurveyActivity.this, "星星：" + ratingBar.getId() + "分数" + rating);
						result.put(ratingBar.getId(), String.valueOf(rating));
					}
				});

				itemSurveyLayout.addView(ratingBar);
				// 问题类型1，有具体选择项，使用Radio处理
			} else if (questionList.get(i).getQuestionType() == 1) {
				// ###################################################################
				// ####生成答案选项RadioGroup###
				final FlowRadioGroup group = new FlowRadioGroup(this); // 实例化单选按钮组
				LinearLayout.LayoutParams paramsGroup = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsGroup.setMargins(UiUtils.dip2px(this, 7), 0, 0, 0);
				group.setLayoutParams(paramsGroup);
				group.setId(i);
				group.setOrientation(LinearLayout.HORIZONTAL);
				// 添加单选按钮
				for (int j = 0; j < questionList.get(i).getQuestionOption().size(); j++) {
					final RadioButton radio = new RadioButton(this);
					// 设置字体大小
					radio.setTextSize(UiUtils.dip2px(SurveyActivity.this, 5));
					// 设置radio文字
					radio.setText(questionList.get(i).getQuestionOption().get(j));
					// 设置radio监听事件
					radio.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ToastUtils.show(SurveyActivity.this, "Group" + String.valueOf(group.getId()) + "&Radio：" + radio.getText().toString());
							result.put(group.getId(), radio.getText().toString());
						}
					});
					group.addView(radio);
				}
				// 渲染RadioGroup到item框架
				itemSurveyLayout.addView(group);
			}

			// 附加到item
			llSurveyMain.addView(itemSurveyLayout);
		}

		// ####添加提交按钮
		tvSubmit = new TextView(this);
		LinearLayout.LayoutParams paramsTvSubmit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.dip2px(this, 50));
		paramsTvSubmit.setMargins(0, UiUtils.dip2px(this, 10), 0, 0);
		tvSubmit.setText("提交");
		tvSubmit.setTextColor(Color.parseColor("#02A0F2"));
		tvSubmit.setTextSize(UiUtils.sp2px(this, 6.2f));
		tvSubmit.setGravity(Gravity.CENTER);
		tvSubmit.setBackgroundColor(Color.parseColor("#AAFFFFFF"));
		tvSubmit.setLayoutParams(paramsTvSubmit);
		llSurveyMain.addView(tvSubmit);

	}

	/**
	 * 
	 * <p>
	 * Title: checkSelectAll
	 * </p>
	 * <p>
	 * Description: 检查是否全部选择了
	 * </p>
	 * 
	 * @param questionNum
	 *            调查问卷问题数量,从0开始
	 * @param result
	 *            选择结果的map
	 * @return 如果全部填写完成返回-1 ，否则返回问题所在序号
	 */
	protected int checkSelectedAll(int questionNum, Map<Integer, String> resultMap) {
		for (int i = 0; i < questionNum; i++) {
			if (!resultMap.containsKey(i)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back_survey:
			SurveyActivity.this.finish();
			break;
		case R.id.btnImg_back_survey:
			SurveyActivity.this.finish();
			break;

		default:
			break;
		}

	}

}
