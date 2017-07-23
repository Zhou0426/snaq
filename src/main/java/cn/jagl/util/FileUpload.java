package cn.jagl.util;

import cn.jagl.util.FileModel;

public interface FileUpload {

	//3.实现文件上传功能，返回上传后新的文件名称
	String uploadFile(FileModel fileModel);

}