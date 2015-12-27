package com.example.survey.model;
/**
 * <p>Title: FeedbackModel.java</p>
 * <p>Description: </p>
 * @author miCookie
 * @date 2015-12-2
 */


import java.util.ArrayList;

/**
 * @author 10192537
 * 
 */
public class QuestionModel {

	private int questionNo;
	private int questionType;// 定义问题类型0 为没有具体答案（星级评价），类型1 为有具体答案选择使用Radio
	private String questionName;
	private ArrayList<String> questionOption;

	/**
	 * @param questionNo
	 * @param questionName
	 * @param questionOption
	 */
	public QuestionModel(int questionNo, int questionType, String questionName, ArrayList<String> questionOption) {
		super();
		this.questionNo = questionNo;
		this.questionType = questionType;
		this.questionName = questionName;
		this.questionOption = questionOption;
	}

	/**
	 * 
	 */
	public QuestionModel() {
		super();
	}

	/**
	 * @return the questionNo
	 */
	public int getQuestionNo() {
		return questionNo;
	}

	/**
	 * @param questionNo
	 *            the questionNo to set
	 */
	public void setQuestionNo(int questionNo) {
		this.questionNo = questionNo;
	}

	/**
	 * @return the questionName
	 */
	public String getQuestionName() {
		return questionName;
	}

	/**
	 * @param questionName
	 *            the questionName to set
	 */
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	/**
	 * @return the questionOption
	 */
	public ArrayList<String> getQuestionOption() {
		return questionOption;
	}

	/**
	 * @param questionOption
	 *            the questionOption to set
	 */
	public void setQuestionOption(ArrayList<String> questionOption) {
		this.questionOption = questionOption;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

}
