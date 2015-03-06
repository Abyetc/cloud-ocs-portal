package com.cloud.ocs.portal.core.infrastructure.dto;

/**
 * 用于返回模板信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-4 上午11:28:20
 * 
 */
public class TemplateDto {

	private String templateId;
	private String templateName;
	private String displayText;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

}
