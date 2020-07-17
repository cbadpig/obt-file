package com.hyt.obt.file.config.fastdfs;

import java.io.IOException;
import java.io.InputStream;


import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
/**
 * 
 * @author qiaozj
 * @Date 2020.04.07
 * @Description 实现DownloadCallback接口 下载返回一个输入流
 *
 */
public class DownloadCallbankStream implements DownloadCallback<InputStream>{
	@Override
	public InputStream recv(InputStream ins) throws IOException {
		return ins;
	}
}
