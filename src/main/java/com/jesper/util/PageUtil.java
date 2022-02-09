package com.jesper.util;

public class PageUtil {
	
	public static String getPageContent(String url,int pageCurrent,int pageSize,int pageCount){
		if (pageCount == 0) {
			return "";
		}
		String urlNew = url.replace("{pageSize}", pageSize+"").replace("{pageCount}", pageCount+"");
		
		String first = urlNew.replace("{pageCurrent}", 1+"");
		String prev = urlNew.replace("{pageCurrent}", (pageCurrent - 1)+"");
		String next = urlNew.replace("{pageCurrent}", (pageCurrent + 1)+"");
		String last = urlNew.replace("{pageCurrent}", pageCount+"");
		
		StringBuilder html = new StringBuilder();
		html.append("<li class=\"footable-page-arrow").append(pageCurrent <= 1 ? " disabled" : "").append("\"><a href=\"").append(pageCurrent <= 1 ? "#" : first).append("\">«</a></li>");
		html.append("<li class=\"footable-page-arrow").append(pageCurrent <= 1 ? " disabled" : "").append("\"><a href=\"").append(pageCurrent <= 1 ? "#" : prev).append("\">‹</a></li>");
		for(int i = 0 ;i < pageCount; i++){
			String urlItem = urlNew.replace("{pageCurrent}", (i+1)+"");
			html.append("<li class=\"footable-page").append(((i + 1) == pageCurrent) ? " active" : "").append("\"><a href=\"").append(urlItem).append("\">").append(i + 1).append("</a></li>");
		}
		html.append("<li class=\"footable-page-arrow").append(pageCurrent == pageCount ? " disabled" : "").append("\"><a href=\"").append(pageCurrent == pageCount ? "#" : next).append("\">›</a></li>");
		html.append("<li class=\"footable-page-arrow").append(pageCurrent == pageCount ? " disabled" : "").append("\"><a href=\"").append(pageCurrent == pageCount ? "#" : last).append("\">»</a></li>");
		
		return html.toString().replaceAll("null", "");
	}
	
}