package com.codi.frame.model;

import com.codi.frame.utils.UploadUtil;

import java.io.Serializable;


/**
 * Title: FormFile
 * Description: 封装的上传文件
 * Company: Deyiff
 * @author Kevin
 * @date 2014年5月21日
 */
public class FormFile implements Serializable {

	private static final long serialVersionUID = -139074155163096826L;
	
	public String filePath;
	public String formName;
	public String contentType;
	
	public FormFile() {
		super();
	}
	
	/**
	 * @param filePath		文件路径
	 * @param formName		表单字段名称
	 * @param contentType	内容类型
	 */
	public FormFile(String filePath, String formName, String contentType) {
		super();
		this.filePath = filePath;
		this.formName = formName;
		if(contentType == null){
			contentType = UploadUtil.FILE_CONTENT_TYPE;
		}else {
			this.contentType = contentType;
		}
	}
	
	/**
	 * @param filePath		文件路径
	 * @return
	 */
	public static FormFile createImgFormFile(String filePath){
		return createImgFormFile(filePath, null);
	}
	
	/**
	 * @param filePath		文件路径
	 * @param contentType	内容类型
	 * @return
	 */
	public static FormFile createImgFormFile(String filePath, String contentType){
		return new FormFile(filePath, UploadUtil.IMG_TYPE, contentType);
	}
	
	/**
	 * @param filePath		文件路径
	 * @return
	 */
	public static FormFile createAudioFormFile(String filePath){
		return createAudioFormFile(filePath, null);
	}
	
	/**
	 * @param filePath		文件路径
	 * @param contentType	内容类型
	 * @return
	 */
	public static FormFile createAudioFormFile(String filePath, String contentType){
		return new FormFile(filePath, UploadUtil.AUDIO_TYPE, contentType);
	}

	@Override
	public String toString() {
		return "FormFile [filePath=" + filePath + ", formName=" + formName
				+ ", contentType=" + contentType + "]";
	}

}
