package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = {"/edit"})
public class EditServlet extends HttpServlet {
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
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		String id = request.getParameter("id");

		//パラメーターが数値でない場合またはnullの場合エラーメッセージ表示
		if((!id.matches("^[0-9]+$")) || (id == null )) {
			errorMessages.add("不正なパラメーターが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		int messageId = Integer.parseInt(request.getParameter("id"));

		Message message = new MessageService().select(messageId);

		if(message != null) {
			request.setAttribute("message", message);
			request.getRequestDispatcher("edit.jsp").forward(request, response);
		} else {
			//message(bean)がnullであればエラーメッセージを表示
			//存在しないつぶやきId入力したらmessageの中身はnull
			errorMessages.add("不正なパラメーターが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
	}

	//つぶやきの編集内容を更新
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();
		String text = request.getParameter("text");
		int messageId = Integer.parseInt(request.getParameter("id"));

		Message message = new Message();
		message.setId(messageId);
		message.setText(text);

		if(!isValid(text, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("message", message);
			request.getRequestDispatcher("/edit.jsp").forward(request, response);
			return;
		}

		new MessageService().update(message);
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {
		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		if(StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if(140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		if(errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
