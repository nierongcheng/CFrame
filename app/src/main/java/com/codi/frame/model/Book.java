package com.codi.frame.model;

import java.io.Serializable;

import com.codi.frame.model.BaseModel;
import com.j256.ormlite.field.DatabaseField;

/**
 * 图书表 
 */
public class Book implements BaseModel, Serializable {
	
	private static final long serialVersionUID = 67702857465137108L;
	
	@DatabaseField(id=true)
	public String	bookID;				// Book ID
	@DatabaseField
	public String	title;				// Book 标题
	@DatabaseField
	public int		brand;				// 品牌
	@DatabaseField
	public int		category1;			// 分类1 KeyLinks：0 SharedReading：1
	@DatabaseField
	public int		category2;			// 分类2
	@DatabaseField
	public int		category3;			// 分类3
	@DatabaseField
	public int		bookOrder;			// 书本顺序
	@DatabaseField
	public String   publisher;			// 出版机构
	@DatabaseField
	public String   type;				// 书本类型
	@DatabaseField
	public String	isbn;				// isbn
	@DatabaseField
	public String	isbnDigital;		// isbnDigital
	@DatabaseField
	public int		wordCount;			// 字数
	@DatabaseField
	 public String	author;				// 作者
	@DatabaseField
	public String	illustratedBy;		// 出版社
	@DatabaseField
	public String	ageGroup;			// 年龄段
	@DatabaseField
	 public int   	ageMax;				// 最大年龄
	@DatabaseField
	public int  	ageMin;				// 最小年龄
	@DatabaseField
	public String	keyWords;			// 关键词
	@DatabaseField
	public String	keySentences;		// 关键句子
	@DatabaseField
	public String	description;		// 书本描述
	@DatabaseField	
	public int		version;			// 在线版本
	@DatabaseField
	public int		curVersion = 1;			// 当前版本
	@DatabaseField
	public String	md5Check;			// md5检查码
	@DatabaseField
	public String	createAt;			// 书本创建时间
	@DatabaseField
	public String	updateAt;			// 书本更新时间
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public String toString() {
		return "Book [bookID=" + bookID + ", title=" + title + ", brand="
				+ brand + ", category1=" + category1 + ", category2="
				+ category2 + ", category3=" + category3 + ", bookOrder="
				+ bookOrder + ", isbn=" + isbn + ", wordCount=" + wordCount
				+ ", author=" + author + ", illustrationBy=" + illustratedBy
				+ ", ageGroup=" + ageGroup + ", ageMax=" + ageMax + ", ageMin="
				+ ageMin + ", keywords=" + keyWords + ", keySentences="
				+ keySentences + ", description=" + description + ", version="
				+ version + ", curVersion=" + curVersion + ", md5Check="
				+ md5Check + ", createAt=" + createAt + ", updateAt="
				+ updateAt + "]";
	}
	
}
