package chapter6.controller;

import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import chapter6.logging.InitApplication;

@WebServlet(urlPatterns = {"/edit"})
public class EditServlet {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施
	 */
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	//編集画面を表示
}
